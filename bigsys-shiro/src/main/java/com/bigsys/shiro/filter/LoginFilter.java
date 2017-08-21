package com.bigsys.shiro.filter;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by fangzhipeng on 2017/8/15.
 */
public class LoginFilter  extends FormAuthenticationFilter {

    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        return new UsernamePasswordToken(username, password);
    }
}
