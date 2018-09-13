package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.*;

import java.util.List;

public interface ManageService {

    // 查询所有一级分类
    List<BaseCatalog1> getCatalog1();
    // 根据一级分类Id查询二级分类
    List<BaseCatalog2> getCatalog2(String catalog1Id);
    // 根据二级分类Id查询三级分类
    List<BaseCatalog3> getCatalog3(String catalog2Id);
    // 根据三级分类Id查询平台属性列表
    List<BaseAttrInfo> getAttrList(String catalog3Id);
    // 保存平台属性，平台属性值
    void saveAttrInfo(BaseAttrInfo baseAttrInfo);
    // 根据平台属性名称Id取得平台属性对象
    BaseAttrInfo getAttrInfo(String attrId);


    //获取spu平台属性集合
    List<SpuInfo> getSpuInfoList(SpuInfo spuInfo);


    // 查询基本销售属性表
    List<BaseSaleAttr> getBaseSaleAttrList();

//保存spu图片属性
    void saveSpuInfo(SpuInfo spuInfo);

    // 查询所有图片列表
    List<SpuImage> getSpuImageList(String spuId);


    //查询sku列表
    List<SkuInfo> getSkuInfoListBySpu(String spuId);
    // 根据spuId 查询SpuSaleAttr 集合
    List<SpuSaleAttr> getSpuSaleAttrList(String spuId);
    //保存sku
    void saveSku(SkuInfo skuInfo);
    // 根据skuId 查询数据
    SkuInfo getSkuInfo(String skuId);
    // 根据skuInfo 查询 List<SpuSaleAttr>
    List<SpuSaleAttr> selectSpuSaleAttrListCheckBySku(SkuInfo skuInfo);
    // 根据spuId查询销售属性值集合
    List<SkuSaleAttrValue> getSkuSaleAttrValueListBySpu(String spuId);


    List<BaseAttrInfo> getAttrList(List<String> attrValueIdList);
}
