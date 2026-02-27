package com.supermap.shiro.token;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author gzw
 */
@AllArgsConstructor
public class PasswordToken implements AuthenticationToken {

    private String username;

    private String password;

    @Override
    public Object getPrincipal() {
        return username;
    }

    @Override
    public Object getCredentials() {
        return password;
    }

}
