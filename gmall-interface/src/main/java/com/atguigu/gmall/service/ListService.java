package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.SkuLsInfo;
import com.atguigu.gmall.bean.SkuLsParams;
import com.atguigu.gmall.bean.SkuLsResult;

public interface ListService {

    // SkuLsInfo -- 对es 数据进行封装的对象
    void saveSkuInfo(SkuLsInfo skuLsInfo);
    // 根据用户输入的参数，返回封装好的结果集
    SkuLsResult search(SkuLsParams skuLsParams);
    //为当前商品增加热度
    void incrHotScore(String skuId);
}
