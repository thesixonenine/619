package io.github.thesixonenine.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import io.github.thesixonenine.common.utils.PageUtils;
import io.github.thesixonenine.common.utils.Query;
import io.github.thesixonenine.ware.config.RabbitConfig;
import io.github.thesixonenine.ware.dao.WareSkuDao;
import io.github.thesixonenine.ware.dto.mq.StockLockedDTO;
import io.github.thesixonenine.ware.entity.WareOrderTaskDetailEntity;
import io.github.thesixonenine.ware.entity.WareOrderTaskEntity;
import io.github.thesixonenine.ware.entity.WareSkuEntity;
import io.github.thesixonenine.ware.service.WareOrderTaskDetailService;
import io.github.thesixonenine.ware.service.WareOrderTaskService;
import io.github.thesixonenine.ware.service.WareSkuService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {

    @Resource
    private WareSkuDao wareSkuDao;
    @Autowired
    private WareOrderTaskService wareOrderTaskService;
    @Autowired
    private WareOrderTaskDetailService wareOrderTaskDetailService;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),
                new QueryWrapper<WareSkuEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 锁定库存
     *
     * 库存解锁的场景:
     * 1. 下单成功但未支付, 被顾客手动/系统自动取消
     * 2. 下单成功, 库存锁定成功, 接下来的其他业务失败导致订单回滚, 需回滚库存
     *
     * @param orderSn 订单号
     * @param map 锁定信息
     */
    @Override
    public void lockStock(String orderSn, Map<Long/* skuId */, Integer/* lockNum */> map) {
        // 保存库存工作单
        WareOrderTaskEntity task = new WareOrderTaskEntity();
        task.setOrderSn(orderSn);
        wareOrderTaskService.save(task);
        Long taskId = task.getId();

        for (Map.Entry<Long, Integer> entry : map.entrySet()) {
            boolean lock = false;
            Long skuId = entry.getKey();
            Integer num = entry.getValue();
            List<Long> wareIdList = wareSkuDao.selectWareId(skuId);
            if (CollectionUtils.isEmpty(wareIdList)) {
                throw new RuntimeException("商品" + skuId + "库存不足01");
            }
            // 如果循环中某个锁定失败, 则数据库回滚, 之前发出去的mq消息找不到对应的工作单, 不会回滚
            for (Long wareId : wareIdList) {
                if (SqlHelper.retBool(wareSkuDao.lockStock(skuId, wareId, num))) {
                    lock = true;


                    // 保存库存工作单详情
                    WareOrderTaskDetailEntity detail = new WareOrderTaskDetailEntity();
                    detail.setSkuId(skuId);
                    detail.setSkuNum(num);
                    detail.setTaskId(taskId);
                    detail.setWareId(wareId);
                    detail.setLockStatus(WareOrderTaskDetailEntity.LockStatusEnum.LOCKED.getCode());
                    wareOrderTaskDetailService.save(detail);
                    // 发送MQ消息以便解锁
                    StockLockedDTO stockLockedDTO = new StockLockedDTO();
                    BeanUtils.copyProperties(detail, stockLockedDTO);
                    stockLockedDTO.setDetailId(detail.getId());
                    rabbitTemplate.convertAndSend(RabbitConfig.STOCK_EVENT_EXCHANGE, RabbitConfig.STOCK_LOCKED_ROUTING_KEY, stockLockedDTO);


                    break;
                }
            }
            if (!lock) {
                throw new RuntimeException("商品" + skuId + "库存不足02");
            }
        }
    }

    @Override
    public int unLockStock(StockLockedDTO dto) {
        wareOrderTaskDetailService.updateById(WareOrderTaskDetailEntity.builder().id(dto.getDetailId()).lockStatus(WareOrderTaskDetailEntity.LockStatusEnum.UNLOCK.getCode()).build());
        return wareSkuDao.unLockStock(dto);
    }

}