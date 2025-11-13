package com.supermap.modules.security.service.impl;

import com.supermap.modules.security.service.LoginService;
import com.supermap.modules.security.vo.RouteVO;
import com.supermap.modules.sys.dto.UserLoginDTO;
import com.supermap.modules.sys.entity.PermissionEntity;
import com.supermap.modules.sys.service.impl.PermissionServiceImpl;
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

import java.util.List;
import java.util.Objects;

/**
 * @author gzw
 */
@Service
@AllArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final RedisTemplate<String, String> redisTemplate;

    private final PermissionServiceImpl permissionService;

    @Override
    public String login(UserLoginDTO user) {
        TokenUsernamePassword tokenUsernamePassword = new TokenUsernamePassword()
                .setUsername(user.getUsername())
                .setPassword(user.getPassword());

        LoginUser principal = doLogin(tokenUsernamePassword);

        return RedisTokenUtils.createToken(principal);
    }

    @Override
    public void logout(String token) {
        redisTemplate.delete(RedisTokenUtils.getKey(token));
        LoginUserContextHandler.logout();
    }

    @Override
    public List<RouteVO> getLoginUserRoute(Long userId) {
        List<PermissionEntity> perms = permissionService.getBaseMapper().getLoginUserRoute(userId);
        List<RouteVO> list = perms.stream().map(item -> {
            RouteVO route = new RouteVO();
            route.setPermissionId(item.getPermissionId());
            route.setParentId(item.getParentId());
            route.setPath(item.getUrl());
            route.setName(item.getName());

            route.setTitle(item.getName());
            route.setHidden(item.getHidden() != null && item.getHidden() == 1);
            route.setIcon(item.getIcon());
            route.setOpenStyle(item.getOpenStyle());

            return route;
        }).toList();

        List<RouteVO> root = list.stream()
                .filter(item -> item.getParentId() == null)
                .toList();

        root.forEach(routeVO -> buildRoute(routeVO, list));

        return root;
    }

    private void buildRoute(RouteVO root, List<RouteVO> all) {
        List<RouteVO> children = all.stream()
                .filter(item -> Objects.equals(item.getParentId(), root.getPermissionId()))
                .toList();
        root.setChildren(children);

        children.forEach(item -> buildRoute(item, all));
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
