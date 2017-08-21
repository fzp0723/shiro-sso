package com.bigsys.webdemo;

import org.apache.shiro.web.servlet.ShiroFilter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by fangzhipeng on 2017/8/15.
 */
@Controller
public class TestController {

    @RequestMapping(value = "/hello")
    @ResponseBody
    public String hello() {

        System.out.println("hit");

        return "hello world";
    }

}
