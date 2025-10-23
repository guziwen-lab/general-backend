package com.supermap.modules.security.controller;

import com.supermap.common.enumeration.BizCodeEnum;
import com.supermap.common.pojo.R;
import com.supermap.common.util.StringUtils;
import com.supermap.modules.security.service.CaptchaService;
import com.supermap.modules.security.service.LoginService;
import com.supermap.modules.sys.dto.UserLoginDTO;
import com.supermap.shiro.LoginUser;
import com.supermap.shiro.LoginUserContextHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author gzw
 */
@RestController
@Tag(name = "登录管理")
@AllArgsConstructor
public class LoginController {

    private final LoginService loginService;

    private final CaptchaService captchaService;

    @GetMapping("captcha")
    @Operation(summary = "验证码")
    public void captcha(@RequestParam String uuid, HttpServletResponse response) throws IOException {
        //生成验证码
        captchaService.create(uuid, response);
    }

    @Operation(summary = "登录")
    @PostMapping(value = "/login")
    public R<String> login(UserLoginDTO user, HttpServletResponse response) {
        if (StringUtils.isEmpty(user.getUsername()) ||
                StringUtils.isEmpty(user.getPassword()) ||
                StringUtils.isEmpty(user.getCaptcha()) ||
                StringUtils.isEmpty(user.getUuid())) {
            return R.error(BizCodeEnum.VALID_PARAM_EXCEPTION);
        }

        boolean flag = captchaService.validate(user.getUuid(), user.getCaptcha());
        if (!flag)
            return R.error(BizCodeEnum.CAPTCHA_ERROR);

        String token = loginService.login(user);

        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60);
        response.addCookie(cookie);

        return R.ok(token);
    }

    @Operation(summary = "退出登录")
    @GetMapping("/logout")
    public R<Void> logout() {
        LoginUser loginUser = LoginUserContextHandler.getLoginUser();
        loginService.logout(loginUser.getToken());
        return R.ok();
    }

}
