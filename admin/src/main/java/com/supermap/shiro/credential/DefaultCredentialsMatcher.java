package com.supermap.shiro.credential;

import com.supermap.common.util.BeanUtils;
import com.supermap.modules.sys.entity.UserEntity;
import com.supermap.modules.sys.service.UserService;
import com.supermap.shiro.LoginUser;
import com.supermap.shiro.encoder.PasswordEncoder;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;

/**
 * @author gzw
 */
public class DefaultCredentialsMatcher extends SimpleCredentialsMatcher {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    public DefaultCredentialsMatcher(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        // 获取用户输入的密码
        Object tokenCredentials = getCredentials(token);
        if (tokenCredentials == null)
            return true;
        String plainPassword = (String) tokenCredentials;

        // 获取数据库中的加密密码
        LoginUser principal = (LoginUser) token.getPrincipal();
        UserEntity userEntity = userService.getByUsername(principal.getUsername());
        if (userEntity == null) {
            throw new UnknownAccountException("用户不存在");
        }
        BeanUtils.copyProperties(userEntity, principal);

        return passwordEncoder.matches(plainPassword, userEntity.getPassword());
    }

}
