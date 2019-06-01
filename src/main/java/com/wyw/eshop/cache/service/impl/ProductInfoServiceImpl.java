package com.wyw.eshop.cache.service.impl;

import com.wyw.eshop.cache.mapper.ProductInfoMapper;
import com.wyw.eshop.cache.model.ProductInfo;
import com.wyw.eshop.cache.service.ProductInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("productInfoService")
public class ProductInfoServiceImpl implements ProductInfoService {

    @Resource
    private ProductInfoMapper productInfoMapper;

    @Override
    public ProductInfo getProductInfo(Integer productInfoId) {
        return productInfoMapper.selectByPrimaryKey(productInfoId);
    }
}
