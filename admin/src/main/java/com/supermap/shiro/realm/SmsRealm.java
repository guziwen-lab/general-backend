package com.supermap.shiro.realm;

import com.supermap.modules.sys.entity.UserEntity;
import com.supermap.shiro.credential.AllowAllCredentialsMatcher;
import com.supermap.shiro.token.SmsCodeToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.springframework.stereotype.Component;

/**
 * @author gzw
 */
@Slf4j
@Component
public class SmsRealm extends AbstractAuthenticationRealm<SmsCodeToken> {

    public SmsRealm(AllowAllCredentialsMatcher credentialsMatcher) {
        setCachingEnabled(false);
        setAuthorizationCachingEnabled(false);
        setCredentialsMatcher(credentialsMatcher);
    }

    @Override
    protected Class<SmsCodeToken> getTokenClass() {
        return SmsCodeToken.class;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        try {
            String phone = (String) authenticationToken.getPrincipal();
            String inputCode = (String) authenticationToken.getCredentials();

            UserEntity userEntity = getUserService().getByUsername(phone);
            if (userEntity == null)
                throw new UnknownAccountException("用户不存在: " + authenticationToken.getPrincipal());

            // TODO 验证短信验证码 from redis

            return new SimpleAuthenticationInfo(buildLoginUser(userEntity), null, getName());
        } catch (AuthenticationException e) {
            throw e;
        } catch (Exception e) {
            log.debug("认证过程出现异常", e);
            throw new AuthenticationException("认证过程出现异常", e);
        }
    }

}
