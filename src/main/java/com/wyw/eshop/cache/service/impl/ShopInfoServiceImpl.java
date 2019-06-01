package com.wyw.eshop.cache.service.impl;

import com.wyw.eshop.cache.mapper.ShopInfoMapper;
import com.wyw.eshop.cache.model.ShopInfo;
import com.wyw.eshop.cache.service.ShopInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("shopInfoService")
public class ShopInfoServiceImpl implements ShopInfoService {
    @Resource
    private ShopInfoMapper shopInfoMapper;

    @Override
    public ShopInfo getShopInfo(Integer shopInfoId) {
        return shopInfoMapper.selectByPrimaryKey(shopInfoId);
    }
}
