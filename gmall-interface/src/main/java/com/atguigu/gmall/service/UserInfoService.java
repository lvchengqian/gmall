package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.UserAddress;
import com.atguigu.gmall.bean.UserInfo;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

public interface UserInfoService {
  // alt+enter
  List<UserInfo> findAll();
  // 根据userId 查询用户地址列表
 List<UserAddress> getUserAddressList(String userId);







   List<UserInfo> getUserInfoList() ;


  //    添加：
   void  addUserInfo(UserInfo userInfo);

  //    修改：
    void  updUserInfo(UserInfo userInfo);



  //    当修改条件不是id的时候可以使用updateByExampleSelective(userInfo,example);
  void updUser1(UserInfo userInfo);






   void delUserInfo(UserInfo userInfo) ;




}
