package com.supermap.shiro.config;

import com.supermap.common.util.StringUtils;
import com.supermap.shiro.interceptor.RedisTokenInterceptor;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Map;

@Configuration
@AllArgsConstructor
public class InterceptorConfig implements WebMvcConfigurer {

    private final RedisTokenInterceptor redisTokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        Map<String, String> filterChainDefinitionMap = ShiroConfig.filterChainDefinitionMap;
        InterceptorRegistration reg = registry.addInterceptor(redisTokenInterceptor);

        filterChainDefinitionMap.forEach((k, v) -> {
            if (v.contains("anon") || v.contains("logout")) {
                reg.excludePathPatterns(k);
            } else {
                reg.addPathPatterns(k);
            }
        });
    }

}