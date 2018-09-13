package com.atguigu.gmall.usermanage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.bean.UserAddress;
import com.atguigu.gmall.bean.UserInfo;
import com.atguigu.gmall.config.RedisUtil;
import com.atguigu.gmall.service.UserInfoService;
import com.atguigu.gmall.usermanage.mapper.UserAddressMapper;
import com.atguigu.gmall.usermanage.mapper.UserInfoMapper;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import redis.clients.jedis.Jedis;
import tk.mybatis.mapper.entity.Example;

import java.util.List;


@Service
public class UserInfoServiceImpl implements UserInfoService {


    // 定义用户信息
    public String userKey_prefix="user:";
    public String userinfoKey_suffix=":info";
    public int userKey_timeOut=60*60;


   @Autowired
    private RedisUtil redisUtil;




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

    @Override
    public UserInfo login(UserInfo userInfo) {
        // 页面传递 admin  -- 123
        // db   admin -- 202cb962ac59075b964b07152d234b70
        // 将 123 加密
        String passwd = userInfo.getPasswd();
        // 123 -- 202cb962ac59075b964b07152d234b70
        String newPwd = DigestUtils.md5DigestAsHex(passwd.getBytes());
        userInfo.setPasswd(newPwd);
        UserInfo info = userInfoMapper.selectOne(userInfo);
        // 如果用户登录成功则将用户信息存放到redis
        Jedis jedis = redisUtil.getJedis();
        System.err.println("jedis==================>"+jedis);
        if (info!=null){
            // 定义key：user:1:info
            String userKey = userKey_prefix+info.getId()+userinfoKey_suffix;
            // 做存储
            jedis.setex(userKey,userKey_timeOut, JSON.toJSONString(info));

            jedis.close();

            return info;
        }
        return null;
    }
    @Override
    public UserInfo verify(String userId) {
        //根据UserInfo redis中查询数据
        Jedis jedis = redisUtil.getJedis();

        //定义key
        String key = userKey_prefix+userId+userinfoKey_suffix;
    //通过key获取数据
        String userJson = jedis.get(key);
        //因为认证操作相当于其他模块，在登陆时，就需要延长用户的过期时间
        jedis.expire(key,userKey_timeOut);
        if (userJson!=null && userJson.length()>0){
            //将字符串转换为对象
            UserInfo userInfo = JSON.parseObject(userJson, UserInfo.class);
            return  userInfo;

        }

        return null;
    }


}
