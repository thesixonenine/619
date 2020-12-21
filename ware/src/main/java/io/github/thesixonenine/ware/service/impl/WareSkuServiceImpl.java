package io.github.thesixonenine.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import io.github.thesixonenine.common.utils.PageUtils;
import io.github.thesixonenine.common.utils.Query;
import io.github.thesixonenine.ware.dao.WareSkuDao;
import io.github.thesixonenine.ware.entity.WareSkuEntity;
import io.github.thesixonenine.ware.service.WareSkuService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
        for (Map.Entry<Long, Integer> entry : map.entrySet()) {
            boolean lock = false;
            Long skuId = entry.getKey();
            Integer num = entry.getValue();
            List<Long> wareIdList = wareSkuDao.selectWareId(skuId);
            if (CollectionUtils.isEmpty(wareIdList)) {
                throw new RuntimeException("商品" + skuId + "库存不足01");
            }
            for (Long wareId : wareIdList) {
                if (SqlHelper.retBool(wareSkuDao.lockStock(skuId, wareId, num))) {
                    lock = true;
                    // 发送MQ消息以便解锁
                    break;
                }
            }
            if (!lock) {
                throw new RuntimeException("商品" + skuId + "库存不足02");
            }
        }
    }

}