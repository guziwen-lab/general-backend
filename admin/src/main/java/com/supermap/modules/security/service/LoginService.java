package com.supermap.modules.security.service;

import com.supermap.modules.sys.dto.UserLoginDTO;

/**
 * @author gzw
 */
public interface LoginService {

    String login(UserLoginDTO user);

    void logout(String token);

}
