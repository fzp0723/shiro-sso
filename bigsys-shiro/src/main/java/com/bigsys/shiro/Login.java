package com.bigsys.shiro;

import com.bigsys.shiro.authcache.RedisCacheManager;
import org.apache.shiro.cache.Cache;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by fangzhipeng on 2017/8/15.
 */
@Controller
public class Login {

    @Resource
    private RedisCacheManager cacheManager;

    @RequestMapping(value = "/test")
    @ResponseBody
    public String login() {
        //com.bigsys.shiro.realm.UserRealm.authenticationCache
//      com.bigsys.shiro.realm.UserRealm.authorizationCache
//      shiro-activeSessionCache
        Cache cache = cacheManager.getCache("com.bigsys.shiro.realm.UserRealm.authenticationCache");
        Cache cache1 = cacheManager.getCache("com.bigsys.shiro.realm.UserRealm.authorizationCache");
        Cache cache2 = cacheManager.getCache("shiro-activeSessionCache");
        cache.keys().forEach(System.out::println);
        cache1.keys().forEach(System.out::println);
        cache2.keys().forEach(System.out::println);
        return "hello shiro";
    }

}
