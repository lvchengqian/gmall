package com.atguigu.gmall.item.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ItemController {

@RequestMapping("{skuId}.html")
public String skuInfoPage(@PathVariable(value = "skuId")String skuId){



    return "item";
}


}
