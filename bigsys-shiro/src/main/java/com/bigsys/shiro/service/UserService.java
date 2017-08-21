package com.bigsys.shiro.service;

import com.bigsys.shiro.model.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by fangzhipeng on 2017/8/15.
 */
@Service("userService")
public class UserService {


    public User getByUsername(String username) {
        User user = new User();
        user.setUsername(username);;
        user.setPassword("yuanguang2017");
        return user;
    }
}
