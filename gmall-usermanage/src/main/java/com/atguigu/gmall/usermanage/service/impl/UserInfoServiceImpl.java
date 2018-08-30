package com.atguigu.gmall.usermanage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.bean.UserAddress;
import com.atguigu.gmall.bean.UserInfo;
import com.atguigu.gmall.service.UserInfoService;
import com.atguigu.gmall.usermanage.mapper.UserAddressMapper;
import com.atguigu.gmall.usermanage.mapper.UserInfoMapper;


import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.List;


@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private UserAddressMapper userAddressMapper;
    @Override
    public List<UserInfo> findAll() {
        return userInfoMapper.selectAll();
    }


    public List<UserAddress> getUserAddressList(String userId) {
        UserAddress userAddress = new UserAddress();
        userAddress.setUserId(userId);
        // ctrl+alt+v 自动补全
        List<UserAddress> select = userAddressMapper.select(userAddress);
        return select;
    }






    public List<UserInfo> getUserInfoList() {
        Example example = new Example(UserInfo.class);
        example.createCriteria().andLike("loginName","%a%");
        List<UserInfo> userInfos = userInfoMapper.selectByExample(example);
        return userInfos;
    }

    //    添加：



    public void addUserInfo(UserInfo userInfo) {
        userInfoMapper.insertSelective(userInfo);
    }
    //    修改：

    @Override
    public void updUserInfo(UserInfo userInfo) {
        userInfoMapper.updateByPrimaryKey(userInfo);
    }

    //    当修改条件不是id的时候可以使用updateByExampleSelective(userInfo,example);

    @Override
    public void updUser1(UserInfo userInfo) {
        Example example = new Example(UserInfo.class);
        example.createCriteria().andEqualTo("loginName",userInfo.getLoginName());
        userInfoMapper.updateByExampleSelective(userInfo,example);
    }

    //    删除：


    @Override
    public void delUserInfo(UserInfo userInfo) {
        userInfoMapper.deleteByPrimaryKey(userInfo);
    }




}
