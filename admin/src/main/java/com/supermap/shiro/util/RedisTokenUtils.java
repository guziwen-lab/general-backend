package com.supermap.shiro.util;

import com.supermap.common.util.JSON;
import com.supermap.common.constant.AuthenticationConstant;
import com.supermap.common.util.StringUtils;
import com.supermap.common.util.UUIDUtils;
import com.supermap.shiro.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author gzw
 */
@Component
public class RedisTokenUtils {

    private static RedisTemplate<String, String> redisTemplate;

    @Autowired
    public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
        RedisTokenUtils.redisTemplate = redisTemplate;
    }

    /**
     * 生成token
     */
    public static String createToken(LoginUser user) {
        String uuid = UUIDUtils.get();
        user.setToken(uuid);
        BoundValueOperations<String, String> ops = redisTemplate.boundValueOps(RedisTokenUtils.getKey(uuid));
        ops.set(JSON.toJSONString(user), AuthenticationConstant.DEFAULT_EXPIRE_SECONDS, TimeUnit.SECONDS);
        return uuid;
    }

    /**
     * 更新token
     */
    public static void refreshToken(LoginUser user) {
        BoundValueOperations<String, String> ops = redisTemplate.boundValueOps(RedisTokenUtils.getKey(user.getToken()));
        ops.set(JSON.toJSONString(user), AuthenticationConstant.DEFAULT_EXPIRE_SECONDS, TimeUnit.SECONDS);
    }

    /**
     * 根据token获取用户信息
     */
    public static LoginUser getLoginUser(String token) {
        BoundValueOperations<String, String> ops = redisTemplate.boundValueOps(getKey(token));
        String userStr = ops.get();

        if (StringUtils.isEmpty(userStr))
            throw new RuntimeException("token无效");

        // token续期
        Long expire = ops.getExpire();
        if (expire != null && expire < AuthenticationConstant.DEFAULT_EXPIRE_SECONDS / 2) {
            ops.expire(AuthenticationConstant.DEFAULT_EXPIRE_SECONDS, TimeUnit.SECONDS);
        }

        return JSON.parseObject(userStr, LoginUser.class);
    }

    public static String getKey(String token) {
        return AuthenticationConstant.USER_KEY_PREFIX + token;
    }

}
