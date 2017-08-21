package com.bigsys.redis.service;

import com.bigsys.redis.api.TestService;

/**
 * Created by fangzhipeng on 2017/8/15.
 */
public class TestServiceImpl implements TestService {


    public String sayHello(String name) {
        return "hello " + name;
    }


}
