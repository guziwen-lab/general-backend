package com.supermap.shiro.credential;

import com.supermap.modules.sys.service.UserService;
import com.supermap.shiro.LoginUser;
import com.supermap.shiro.encoder.PasswordEncoder;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RetryLimitCredentialsMatcher extends DefaultCredentialsMatcher {

    private final RedisTemplate<String, String> redisTemplate;

    private static final int MAX_RETRY_COUNT = 5;

    private static final long RETRY_EXPIRE_SECONDS = 1800;

    public RetryLimitCredentialsMatcher(UserService userService,
                                        PasswordEncoder passwordEncoder,
                                        RedisTemplate<String, String> redisTemplate) {
        super(userService, passwordEncoder);
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        LoginUser principal = (LoginUser) token.getPrincipal();
        String key = "login:retry:" + principal.getUsername();

        String retryStr = redisTemplate.opsForValue().get(key);
        int retryCount = retryStr == null ? 0 : Integer.parseInt(retryStr);
        if (retryCount >= MAX_RETRY_COUNT)
            throw new ExcessiveAttemptsException("账号已锁定，请30分钟后再试");

        boolean matches = super.doCredentialsMatch(token, info);
        if (matches) {
            redisTemplate.delete(key);
        } else {
            Long val = redisTemplate.opsForValue().increment(key);
            if (val != null && val == 1) {
                redisTemplate.expire(key, RETRY_EXPIRE_SECONDS, TimeUnit.SECONDS);
            }
        }
        return matches;
    }

}