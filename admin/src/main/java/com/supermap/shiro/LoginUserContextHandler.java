package com.supermap.shiro;

import org.apache.shiro.SecurityUtils;

/**
 * @author gzw
 */
public class LoginUserContextHandler {

    public static LoginUser getLoginUser() {
        return (LoginUser) SecurityUtils.getSubject().getPrincipal();
    }

    public static void logout() {
        SecurityUtils.getSubject().logout();
    }

}
