package io.github.thesixonenine.ware.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import io.github.thesixonenine.common.utils.R;
import io.github.thesixonenine.member.controller.MemberReceiveAddressController;
import io.github.thesixonenine.member.entity.MemberReceiveAddressEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.thesixonenine.common.utils.PageUtils;
import io.github.thesixonenine.common.utils.Query;

import io.github.thesixonenine.ware.dao.WareInfoDao;
import io.github.thesixonenine.ware.entity.WareInfoEntity;
import io.github.thesixonenine.ware.service.WareInfoService;


@Service("wareInfoService")
public class WareInfoServiceImpl extends ServiceImpl<WareInfoDao, WareInfoEntity> implements WareInfoService {

    @Autowired
    private MemberReceiveAddressController memberReceiveAddressController;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WareInfoEntity> page = this.page(
                new Query<WareInfoEntity>().getPage(params),
                new QueryWrapper<WareInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public BigDecimal getFare(Long addrId) {
        // 根据收货地址计算运费
        MemberReceiveAddressEntity memberReceiveAddressEntity = memberReceiveAddressController.getById(addrId);
        String phone = memberReceiveAddressEntity.getPhone();
        String substring = phone.substring(phone.length() - 1);
        return new BigDecimal(substring);
    }

}