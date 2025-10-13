package com.supermap.shiro.realm;

import com.supermap.shiro.LoginUser;
import com.supermap.shiro.token.RedisToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * @author gzw
 */
@Slf4j
public class RedisRealm extends AuthorizingRealm {

    public RedisRealm(CredentialsMatcher matcher) {
        super(matcher);
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
            String password = (String) authenticationToken.getCredentials();
            return new SimpleAuthenticationInfo(loginUser, password, getName());
        } catch (Exception e) {
            if (log.isDebugEnabled())
                log.debug("认证失败", e);
            return null;
        }
    }

    /**
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        try {
            LoginUser loginUser = (LoginUser) principalCollection.getPrimaryPrincipal();
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            info.setRoles(loginUser.getRoles());
            info.setStringPermissions(loginUser.getPermissions());
            return info;
        } catch (Exception e) {
            return null;
        }
    }

}
