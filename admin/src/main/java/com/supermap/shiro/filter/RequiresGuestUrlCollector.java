package com.supermap.shiro.filter;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresGuest;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.*;
import java.util.regex.Pattern;

@Slf4j
@Component
@AllArgsConstructor
public class RequiresGuestUrlCollector {

    private final RequestMappingHandlerMapping handlerMapping;

    // 存储免认证 URL
    private static final Set<String> requiresGuestUrls = new HashSet<>();

    private static final Set<String> exactMatchPaths = new HashSet<>();

    private static final List<Pattern> patternPaths = new ArrayList<>();

    @PostConstruct
    public void init() {
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
            HandlerMethod handlerMethod = entry.getValue();
            Class<?> beanType = handlerMethod.getBeanType();

            boolean hasGuest = beanType.isAnnotationPresent(RequiresGuest.class)
                    || handlerMethod.getMethod().isAnnotationPresent(RequiresGuest.class);
            if (hasGuest) {
                // 收集所有 URL pattern
                requiresGuestUrls.addAll(entry.getKey().getPatternValues());
            }
        }

        for (String path : requiresGuestUrls) {
            if (path.contains("*")) {
                String regex = path.replace("**", ".*").replace("*", "[^/]*");
                Pattern pattern = Pattern.compile(regex);
                patternPaths.add(pattern);
            } else {
                exactMatchPaths.add(path);
            }
        }
    }

    public static boolean isExcluded(String uri) {
        // 精确匹配
        if (exactMatchPaths.contains(uri)) {
            return true;
        }
        // 通配符匹配（正则）
        for (Pattern pattern : patternPaths) {
            if (pattern.matcher(uri).matches()) {
                return true;
            }
        }
        return false;
    }

}