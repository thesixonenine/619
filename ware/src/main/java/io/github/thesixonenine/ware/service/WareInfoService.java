package io.github.thesixonenine.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.thesixonenine.common.utils.PageUtils;
import io.github.thesixonenine.ware.entity.WareInfoEntity;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 仓库信息
 *
 * @author thesixonenine
 * @date 2020-06-06 01:52:04
 */
public interface WareInfoService extends IService<WareInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    BigDecimal getFare(Long addrId);
}

