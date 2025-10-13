package com.supermap.shiro.encoder;

/**
 * 密码加密器
 * 默认使用 BCrypt 加密
 *
 * @author gzw
 */
public interface PasswordEncoder {

    String encode(CharSequence rawPassword);

    boolean matches(CharSequence rawPassword, String encodedPassword);

}
