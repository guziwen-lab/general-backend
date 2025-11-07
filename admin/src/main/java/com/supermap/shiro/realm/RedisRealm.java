package com.supermap.shiro.realm;

import com.supermap.common.util.BeanUtils;
import com.supermap.common.util.StringUtils;
import com.supermap.modules.sys.entity.UserEntity;
import com.supermap.modules.sys.service.UserService;
import com.supermap.shiro.LoginUser;
import com.supermap.shiro.token.RedisToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

/**
 * @author gzw
 */
@Slf4j
@Component
public class RedisRealm extends AuthorizingRealm {

    private final ObjectProvider<UserService> userProvider;

    /**
     * 构造函数
     *
     * @param userProvider       UserServiceProvider 防止过早注入一个没有被动态代理的UserService
     * @param credentialsMatcher 密码匹配器
     */
    public RedisRealm(ObjectProvider<UserService> userProvider, CredentialsMatcher credentialsMatcher) {
        setCredentialsMatcher(credentialsMatcher);
        this.userProvider = userProvider;
    }

    private UserService getUserService() {
        UserService svc = userProvider.getIfAvailable();
        if (svc == null) throw new IllegalStateException("UserService 未就绪");
        return svc;
    }


    @Override
    public String getName() {
        return "redisRealm";
    }

    /**
     * 限定这个realm只能处理RedisToken
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof RedisToken;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        try {
            LoginUser loginUser = (LoginUser) authenticationToken.getPrincipal();
            if (StringUtils.isEmpty(loginUser.getPassword())) {
                // 密码为空说明是通过token在请求其他接口。不是请求登录。
                return new SimpleAuthenticationInfo(loginUser, authenticationToken.getCredentials(), getName());
            }

            UserEntity userEntity = getUserService().getByUsername(loginUser.getUsername());
            if (userEntity == null)
                throw new UnknownAccountException("用户不存在: " + loginUser.getUsername());

            if (StringUtils.isEmpty(userEntity.getPassword()))
                throw new AuthenticationException("用户未设置密码: " + loginUser.getUsername());

            BeanUtils.copyProperties(userEntity, loginUser);

            // 设置权限信息
            getUserService().setLoginUserPermsInfo(loginUser);

            return new SimpleAuthenticationInfo(loginUser, userEntity.getPassword(), getName());
        } catch (AuthenticationException e) {
            throw e;
        } catch (Exception e) {
            log.debug("认证失败", e);
            throw new AuthenticationException("认证过程出现异常", e);
        }
    }

    /**
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        try {
            return (LoginUser) principalCollection.getPrimaryPrincipal();
        } catch (Exception e) {
            return null;
        }
    }

}
