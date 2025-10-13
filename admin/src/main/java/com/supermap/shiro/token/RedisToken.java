package com.supermap.shiro.token;

import com.supermap.shiro.LoginUser;
import com.supermap.shiro.util.RedisTokenUtils;
import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author gzw
 */
public class RedisToken implements AuthenticationToken {

    private LoginUser loginUser;

    private String password;

    public RedisToken(TokenUsernamePassword tokenUsernamePassword) {
        if (tokenUsernamePassword.getToken() != null)
            this.loginUser = RedisTokenUtils.getLoginUser(tokenUsernamePassword.getToken());

        if (tokenUsernamePassword.getPassword() != null)
            this.password = tokenUsernamePassword.getPassword();

        if (tokenUsernamePassword.getPassword() != null && loginUser == null) {
            loginUser = new LoginUser();
            loginUser.setUsername(tokenUsernamePassword.getUsername());
            loginUser.setPassword(tokenUsernamePassword.getPassword());
        }
    }

    @Override
    public Object getPrincipal() {
        return loginUser;
    }

    @Override
    public Object getCredentials() {
        return password;
    }

}
