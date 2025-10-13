package com.supermap.modules.security.service.impl;

import com.supermap.modules.security.service.LoginService;
import com.supermap.modules.sys.dto.UserLoginDTO;
import com.supermap.modules.sys.service.PermissionService;
import com.supermap.modules.sys.service.impl.RoleServiceImpl;
import com.supermap.shiro.LoginUser;
import com.supermap.shiro.LoginUserContextHandler;
import com.supermap.shiro.token.RedisToken;
import com.supermap.shiro.token.TokenUsernamePassword;
import com.supermap.shiro.util.RedisTokenUtils;
import lombok.AllArgsConstructor;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author gzw
 */
@Service
@AllArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final PermissionService permissionService;

    private final RoleServiceImpl roleService;

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public String login(UserLoginDTO user) {
        TokenUsernamePassword tokenUsernamePassword = new TokenUsernamePassword()
                .setUsername(user.getUsername())
                .setPassword(user.getPassword());

        LoginUser principal = doLogin(tokenUsernamePassword);

        Set<String> roleNames = roleService.getRoleNamesByUserId(principal.getUserId());
        Set<String> permissions = permissionService.getPermsKeysByUserId(principal.getUserId());
        principal.setPermissions(permissions);
        principal.setRoles(roleNames);

        return RedisTokenUtils.createToken(principal);
    }

    @Override
    public void logout(String token) {
        redisTemplate.delete(RedisTokenUtils.getKey(token));
        LoginUserContextHandler.logout();
    }

    private LoginUser doLogin(TokenUsernamePassword tokenUsernamePassword) {
        LoginUser principal;
        try {
            RedisToken token = new RedisToken(tokenUsernamePassword);
            SecurityUtils.getSubject().login(token);
            principal = (LoginUser) token.getPrincipal();
        } catch (IncorrectCredentialsException e) {
            throw new IncorrectCredentialsException("密码错误", e);
        }
        return principal;
    }

}
