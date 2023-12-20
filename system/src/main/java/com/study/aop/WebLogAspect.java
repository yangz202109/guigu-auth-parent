package com.study.aop;

import com.study.domain.ResultData;
import com.study.domain.system.WebLog;
import com.study.exception.BusinessException;
import com.study.service.WebLogService;
import com.study.utils.IdSnowflakeUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 拦截所有web请求,记录操作
 *
 * @author yangz
 * @createTime 2023/12/7 - 11:26
 */
@Aspect
@Component
public class WebLogAspect {

    @Resource
    private WebLogService webLogService;

    @Pointcut("execution(public * com.study.controller.*.*(..))")
    public void webLog() {
    }

    //@Before("webLog()")
    public void doBefore(JoinPoint joinPoint) {
    }

    @AfterReturning(value = "webLog()", returning = "ret")
    public void doAfterReturning(JoinPoint joinPoint, Object ret) {
        if(ret == null || ret instanceof ResultData){
            throw new BusinessException(500,"接口返回格式错误");
        }

        WebLog webLog = new WebLog();
        webLog.setId(IdSnowflakeUtil.getId());
        webLog.setCreateTime(LocalDateTime.now());

        //获取请求
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = Objects.requireNonNull(attributes).getRequest();
        webLog.setRequestMethod(request.getMethod());
        webLog.setRequestUrl(request.getRequestURI());
        webLog.setIpAddress(getIp(request));

        ResultData resultData = (ResultData) ret;
        webLog.setStatusCode(resultData.getCode());

        webLogService.save(webLog);
    }

    private String getIp(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");

        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress == null ? "" : ipAddress;
    }
}
