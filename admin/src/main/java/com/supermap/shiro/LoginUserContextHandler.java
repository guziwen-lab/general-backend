package com.supermap.shiro;

import com.supermap.shiro.util.RedisTokenUtils;
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

    public static void refreshLoginUser(LoginUser user) {
        RedisTokenUtils.refreshToken(user);
    }
}
