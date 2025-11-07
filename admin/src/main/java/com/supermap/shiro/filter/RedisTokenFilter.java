package com.supermap.shiro.filter;

import com.supermap.common.enumeration.BizCodeEnum;
import com.supermap.common.pojo.R;
import com.supermap.common.util.ServletUtils;
import com.supermap.common.util.StringUtils;
import com.supermap.shiro.token.RedisToken;
import com.supermap.shiro.token.TokenUsernamePassword;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.WebUtils;

@Slf4j
public class RedisTokenFilter extends BasicHttpAuthenticationFilter {

    private static final String AUTH_MISSING = "MISSING";

    private static final String AUTH_INVALID = "INVALID";

    /**
     * 过滤器拦截请求的入口方法
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        // 从请求中获取 token
        String token = getTokenFromRequest(httpServletRequest);

        if (StringUtils.isEmpty(token)) {
            request.setAttribute("authFailReason", AUTH_MISSING);
            return false;
        }

        try {
            // 交给 myRealm
            SecurityUtils.getSubject().login(new RedisToken(new TokenUsernamePassword().setToken(token)));
            return true;
        } catch (Exception e) {
            log.debug("认证失败", e);
            request.setAttribute("authFailReason", AUTH_INVALID);
            return false;
        }
    }

    /**
     * 从请求中提取 token
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        String token = null;

        String authHeader = request.getHeader("Authorization");
        if (StringUtils.isNotEmpty(authHeader) && authHeader.startsWith("Bearer "))
            token = authHeader.substring(7); // 去掉 "Bearer "

        if (StringUtils.isEmpty(token))
            token = request.getHeader("token");

        if (StringUtils.isEmpty(token))
            token = request.getParameter("token");

        if (StringUtils.isEmpty(token)) {
            Cookie cookie = WebUtils.getCookie(request, "token");
            if (cookie != null)
                token = cookie.getValue();
        }

        return token;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        String reason = (String) request.getAttribute("authFailReason");

        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        if (AUTH_MISSING.equals(reason)) {
            ServletUtils.renderJson(httpServletResponse, R.error(BizCodeEnum.UNAUTHORIZED));
        } else {
            ServletUtils.renderJson(httpServletResponse, R.error(BizCodeEnum.AUTHENTICATION_FAILED));
        }

        return false;
    }

    /**
     * 对跨域访问提供支持
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域发送一个option请求
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }

}
