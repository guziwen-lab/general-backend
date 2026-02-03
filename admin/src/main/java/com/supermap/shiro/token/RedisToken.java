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

    public RedisToken(TokenUsernamePasswordDTO tokenUsernamePasswordDTO) {
        if (tokenUsernamePasswordDTO.getToken() != null)
            this.loginUser = RedisTokenUtils.getLoginUser(tokenUsernamePasswordDTO.getToken());

        if (tokenUsernamePasswordDTO.getPassword() != null)
            this.password = tokenUsernamePasswordDTO.getPassword();

        if (tokenUsernamePasswordDTO.getPassword() != null && loginUser == null) {
            loginUser = new LoginUser();
            loginUser.setUsername(tokenUsernamePasswordDTO.getUsername());
            loginUser.setPassword(tokenUsernamePasswordDTO.getPassword());
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
