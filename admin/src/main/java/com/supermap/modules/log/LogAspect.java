package com.supermap.modules.log;

import com.supermap.common.util.JSON;
import com.supermap.common.pojo.R;
import com.supermap.modules.log.entity.GlobalEntity;
import com.supermap.modules.log.service.GlobalService;
import com.supermap.shiro.LoginUser;
import com.supermap.shiro.LoginUserContextHandler;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * 日志切面
 *
 * @author gzw
 */
@Aspect
@Order(510)
@Component
@Slf4j
public class LogAspect {

    private final GlobalService globalService;

    public LogAspect(GlobalService globalService) {
        this.globalService = globalService;
    }

    @Pointcut("@annotation(com.supermap.modules.log.Log) || execution(* com.supermap.modules.biz.controller..*(..))")
    public void pointCut() {
    }

    @SuppressWarnings("rawtypes")
    @Around("pointCut()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        final String methodName = signature.getName();
        String className = signature.getDeclaringTypeName();
        Object param = proceedingJoinPoint.getArgs()[0];

        // 打印参数
        if (log.isDebugEnabled()) {
            String request = String.format("请求方法: %s.%s ---------------- 参数: %s",
                    className,
                    methodName,
                    JSON.toJSONString(param));
            log.debug(request);
        }
        long begin = System.currentTimeMillis();
        R result = null;
        try {
            result = (R) proceedingJoinPoint.proceed();
            long take = System.currentTimeMillis() - begin;
            log.debug("执行结束 ---------------- 返回值: {}, 耗时：{}", JSON.toJSONString(result), take);
            persistLog(className, methodName, param, result, take);
            return result;
        } catch (Throwable e) {
            long take = System.currentTimeMillis() - begin;
            persistLog(className, methodName, param, result, take, e.getLocalizedMessage(), JSON.toJSONString(e));
            throw e;
        }
    }

    private void persistLog(String className, String methodName, Object param, Object result, long take) {
        persistLog(className, methodName, param, result, take, null, null);
    }

    private void persistLog(String className, String methodName, Object param, Object result, long take,
                            String errorMsg, String exception) {
        LoginUser loginUser = LoginUserContextHandler.getLoginUser();

        GlobalEntity globalEntity = new GlobalEntity();
        globalEntity.setClassName(className);
        globalEntity.setMethodName(methodName);
        globalEntity.setParam(JSON.toJSONString(param));
        globalEntity.setResult(JSON.toJSONString(result));
        globalEntity.setTake(take);
        globalEntity.setErrorMsg(errorMsg);
        globalEntity.setException(exception);
        globalEntity.setRequestUser(loginUser.getUserId());
        globalEntity.setCreateTime(new Timestamp(System.currentTimeMillis()));
        globalService.save(globalEntity);
    }

}
