package com.bigsys.shiro.realm;

import com.bigsys.shiro.model.User;
import com.bigsys.shiro.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import javax.annotation.Resource;

/**
 * Created by fangzhipeng on 2017/8/15.
 */
public class UserRealm extends AuthorizingRealm {

    @Resource
    private UserService userService;

    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String username = (String) principalCollection.fromRealm(getName()).iterator().next();
        User user = userService.getByUsername(username);
        if (user != null) {
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            info.addRole("admin");
            return info;
        } else {
            return null;
        }
    }

    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        User user = userService.getByUsername(token.getUsername());
        if (user != null) {
            return new SimpleAuthenticationInfo(user.getUsername(), user.getPassword(), getName());
        } else {
            return null;
        }
    }
}
