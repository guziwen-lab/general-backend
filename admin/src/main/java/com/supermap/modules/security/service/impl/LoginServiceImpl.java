package com.supermap.modules.security.service.impl;

import com.supermap.common.util.IpUtils;
import com.supermap.modules.security.service.LoginService;
import com.supermap.modules.security.vo.RouteVO;
import com.supermap.modules.sys.dto.UserLoginDTO;
import com.supermap.modules.sys.entity.LoginLogEntity;
import com.supermap.modules.sys.entity.PermissionEntity;
import com.supermap.modules.sys.service.LoginLogService;
import com.supermap.modules.sys.service.impl.PermissionServiceImpl;
import com.supermap.shiro.LoginUser;
import com.supermap.shiro.LoginUserContextHandler;
import com.supermap.shiro.token.RedisToken;
import com.supermap.shiro.token.TokenUsernamePassword;
import com.supermap.shiro.util.RedisTokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
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

    private final LoginLogService loginLogService;

    @Override
    public String login(UserLoginDTO user, HttpServletRequest request) {
        TokenUsernamePassword tokenUsernamePassword = new TokenUsernamePassword()
                .setUsername(user.getUsername())
                .setPassword(user.getPassword());

        LoginUser principal = doLogin(tokenUsernamePassword);

        String token = RedisTokenUtils.createToken(principal);

        // 保存登录日志
        LoginLogEntity loginLogEntity = new LoginLogEntity();
        loginLogEntity.setToken(token);
        loginLogEntity.setUserId(principal.getUserId());
        loginLogEntity.setLoginTime(new Timestamp(System.currentTimeMillis()));
        loginLogEntity.setIsForceLogout(false);
        String ip = IpUtils.getClientIp(request);
        loginLogEntity.setIp(ip);
        loginLogService.save(loginLogEntity);

        return token;
    }

    @Override
    public void logout(String token) {
        redisTemplate.delete(RedisTokenUtils.getKey(token));
        LoginUserContextHandler.logout();

        LoginLogEntity loginLogEntity = loginLogService.getByToken(token);
        if (loginLogEntity == null)
            return;
        LoginLogEntity update = new LoginLogEntity();
        update.setId(loginLogEntity.getId());
        update.setIsForceLogout(true);
        loginLogService.updateById(update);
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
