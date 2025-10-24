package com.supermap.modules.security.service.impl;

import com.supermap.common.constant.MenuType;
import com.supermap.modules.security.service.LoginService;
import com.supermap.modules.sys.dto.UserLoginDTO;
import com.supermap.modules.sys.entity.PermissionEntity;
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

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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
        principal.setRoles(roleNames);

        Set<PermissionEntity> permissionEntities = permissionService.getByUserId(principal.getUserId());
        principal.setPermissions(permissionEntities.stream()
                .map(PermissionEntity::getPermsKey)
                .collect(Collectors.toSet()));
        principal.setRoutes(permissionEntities.stream()
                .filter(permissionEntity -> Objects.equals(permissionEntity.getType(), MenuType.PAGE))
                .map(PermissionEntity::getPath)
                .collect(Collectors.toSet()));
        principal.setButtons(permissionEntities.stream()
                .filter(permissionEntity -> Objects.equals(permissionEntity.getType(), MenuType.BUTTON))
                .map(PermissionEntity::getPermsKey)
                .collect(Collectors.toSet()));

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
