package com.atguigu.gmall.passport.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.bean.UserInfo;
import com.atguigu.gmall.passport.util.JwtUtil;
import com.atguigu.gmall.service.UserInfoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class PassportController {
    @Value("${token.key}")
    private String key;
    // 需要用户模块的Service
    @Reference
    private UserInfoService userInfoService;

    @RequestMapping("index")
    public String index(HttpServletRequest request){
        String originUrl = request.getParameter("originUrl");
        // 保存上
        request.setAttribute("originUrl",originUrl);
        return "index";
    }
    @RequestMapping(value ="login")
    @ResponseBody
    public  String login(UserInfo userInfo,HttpServletRequest request){
        //获取Linux服务器的IP地址
        String ip = request.getHeader("X-forwarded-for");
        //用户名+密码进行验证 select * from user_info where username = ？， andpwd=？
        UserInfo loginUser =  userInfoService.login(userInfo);
        if (loginUser!=null){
            //做token--JWT
            HashMap<String, Object> map = new HashMap<>();
            map.put("userId",loginUser.getId());
            map.put("nickName",loginUser.getNickName());

            String token = JwtUtil.encode(key, map, ip);
            return token;

        }else {
           return "fail";
        }

    }
    // 登录认证方法
    @RequestMapping("verify")
    @ResponseBody
    public String verify(HttpServletRequest request){
        // 取得token
        String  token = request.getParameter("token");
        //  salt = ip
        String salt = request.getHeader("X-forwarded-for");
        // 对token进行解密 {userId=1001, nickName=admin}
        Map<String, Object> map = JwtUtil.decode(token, key, salt);

        // map 中的userId 。跟redis 中进行匹配。
        if (map!=null && map.size()>0){
            String userId = (String) map.get("userId");
            // 调用认证方法将userId 传入进去
            UserInfo userInfo =  userInfoService.verify(userId);
            if (userInfo!=null){
                return "success";
            }else {
                return "fail";
            }
        }
        return "fail";

    }
}
