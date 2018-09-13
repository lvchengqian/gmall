package com.atguigu.gmall.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.bean.CartInfo;
import com.atguigu.gmall.bean.SkuInfo;
import com.atguigu.gmall.config.LoginRequire;
import com.atguigu.gmall.service.CartService;
import com.atguigu.gmall.service.ManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class CartController {

    @Reference
    private CartService cartService;
    //建立一个操作cookie的类
    @Autowired
    private CartCookieHandler cartCookieHandler;

    @Reference
    private ManageService manageService;

    @RequestMapping("addToCart")
    @LoginRequire(autoRedirect = false)
    public String addToCart(HttpServletRequest request, HttpServletResponse response) {
        //获取skuNum
        String skuNum = request.getParameter("skuNum");
        String skuId = request.getParameter("skuId");
        //userId从哪儿获取   在单点登录中
        String userId = (String) request.getAttribute("userId");
        System.out.println(userId);
        //判断当前用户是否登录
        if (userId != null) {
            //添玩数据放入redis。addToCart 该方法实在登陆之后使用的！
            cartService.addToCart(skuId, userId, Integer.parseInt(skuNum));
        } else {
            //未登录--放入cookie中放入购物车
            cartCookieHandler.addToCart(request, response, skuId, userId, Integer.parseInt(skuNum));
        }
        //添加成功页面需要skuInfo对象
        SkuInfo skuInfo = manageService.getSkuInfo(skuId);
        request.setAttribute("skuInfo", skuInfo);
        request.setAttribute("skuNum", skuNum);
        return "success";
    }

    @RequestMapping("cartList")
    @LoginRequire(autoRedirect = false)
    public String cartList(HttpServletRequest request, HttpServletResponse response) {
        //展示; 分为登录，未登录
        String userId = (String) request.getAttribute("userid");
        if (userId != null) {
            //从redis --db查看当前是谁的购物车
            List<CartInfo> cartInfoList = null;
            //会有联合操作，cookie 中的数据没有，应该删除！ cookie跟redis合并
            //先获取cookie中的数据
            List<CartInfo> cartListCK = cartCookieHandler.getCartList(request);
            if (cartListCK != null && cartListCK.size() > 0) {
                //定义一个合并的方法
                cartInfoList = cartService.mergeToCartList(cartListCK, userId);
                //cookie 中要删除商品数据
                cartCookieHandler.deleteCartCookie(request, response);

            } else {
                //cookie zhong 没有数据 直接从redis中取得数据
                cartInfoList = cartService.getCartList(userId);

            }
            request.setAttribute("cartInfoList", cartInfoList);
        } else {
            //cookie 中查看 --单纯不做任何操作
            List<CartInfo> cartList = cartCookieHandler.getCartList(request);
            request.setAttribute("cartInfoList", cartList);
        }
        return "cartList";
    }

    //商品选中状态控制器
    @RequestMapping(value = "checkCart", method = RequestMethod.POST)
    @ResponseBody
    @LoginRequire(autoRedirect = false)
    public void checkCart(HttpServletRequest request, HttpServletResponse response) {
        //  var param="isChecked="+isCheckedFlag+"&"+"skuId="+skuId;
        String isChecked = request.getParameter("isChecked");
        String skuId = request.getParameter("skuId");
        //得到skuId
        String userId = (String) request.getAttribute("userId");

        if (userId != null) {
            //redis登录，存值的时候需要userId
            cartService.checkCart(skuId, isChecked, userId);
        } else {
            //操作cookie不需要userId
            cartCookieHandler.checkCart(request, response, skuId, isChecked);
        }
    }

    @RequestMapping("toTrade")
    @LoginRequire
    public String toTrade(HttpServletRequest request,HttpServletResponse response) {
        String userId = (String) request.getAttribute("userId");
        //选中的商品列表，reids，cookie中是不是有可能同样被选中
        List<CartInfo> cookieHandlerCartList = cartCookieHandler.getCartList(request);
        if (cookieHandlerCartList != null && cookieHandlerCartList.size() > 0) {
            //调用合并方法
            cartService.mergeToCartList(cookieHandlerCartList, userId);
            //删除cookie中的数据
            cartCookieHandler.deleteCartCookie(request,response);
        }


        return "redirect://order.gmall.com/trade";
    }
    }
