package com.supermap.shiro.token;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author gzw
 */
public class SmsCodeToken implements AuthenticationToken {

    private final String phoneNumber;

    private final String verificationCode;

    public SmsCodeToken(String phoneNumber, String verificationCode) {
        this.phoneNumber = phoneNumber;
        this.verificationCode = verificationCode;
    }

    @Override
    public Object getPrincipal() {
        return phoneNumber;
    }

    @Override
    public Object getCredentials() {
        return verificationCode;
    }

}
