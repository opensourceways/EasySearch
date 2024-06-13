

package com.search.docsearch.aop;

import com.search.docsearch.entity.software.SoftwareSysResult;
import com.search.docsearch.utils.ClientIPUtil;
import com.search.docsearch.utils.LogUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.concurrent.TimeUnit;

@Aspect
@Component

public class RequestLimitRedisAspect {
    /**
     * Logger for logging messages in RequestLimitRedisAspect class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestLimitRedisAspect.class);

    /**
     * Autowired RedisTemplate for interacting with Redis.
     */
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * Value of the rejection period configured globally.
     */
    @Value("${dos-global.rejectPeriod}")
    private long rejectPeriod;

    /**
     * Value of the rejection count configured globally.
     */
    @Value("${dos-global.rejectCount}")
    private long rejectCount;

    /**
     * Pointcut method to define where the aspect applies based on the
     * RequestLimitRedis annotation.
     *
     * @param requestLimit The RequestLimitRedis annotation.
     */
    @Pointcut("@annotation(requestLimit)")
    public void controllerAspect(final RequestLimitRedis requestLimit) {
    }

    /**
     * Advice method that intercepts the method calls annotated with
     * RequestLimitRedis and enforces request limiting.
     *
     * @param joinPoint    The ProceedingJoinPoint representing the intercepted
     *                     method.
     * @param requestLimit The RequestLimitRedis annotation containing request
     *                     limiting criteria.
     * @return The result of the intercepted method execution.
     * @throws Throwable if an error occurs during method execution.
     */
    @Around("controllerAspect(requestLimit)")
    public Object before(final ProceedingJoinPoint joinPoint, final RequestLimitRedis requestLimit) throws Throwable {

        long period = rejectPeriod;
        long limitCount = rejectCount;

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = (attributes != null) ? attributes.getRequest() : null;
        if (request == null) {
            LOGGER.error("Failed to obtain HttpServletRequest in the current context.");
            return SoftwareSysResult.fail(HttpStatus.BAD_REQUEST.getReasonPhrase(), null);
        }

        // 获取url
        String ip = ClientIPUtil.getClientIpAdd(request);
        String uri = request.getRequestURI();
        String key = "req_limit:".concat(uri).concat(ip);

        ZSetOperations zSetOperations = redisTemplate.opsForZSet();

        long currentMs = System.currentTimeMillis();
        zSetOperations.add(key, currentMs, currentMs);

        redisTemplate.expire(key, period, TimeUnit.SECONDS);

        // remove the value that out of current window
        zSetOperations.removeRangeByScore(key, 0, currentMs - period * 1000);

        // 检查访问次数
        Long count = zSetOperations.zCard(key);

        if (count != null && count > limitCount) {
            // 审计日志
            LOGGER.error("Resource uri: {}，the request frequency of uri exceeds the limited frequency: "
                            + "{} times/{}s ,Client IP：{},type: GET, Result: query failed.", LogUtil.formatCodeString(uri),
                    limitCount, period, ip);
            return SoftwareSysResult.fail(HttpStatus.BAD_REQUEST.getReasonPhrase(), null);
        }

        return joinPoint.proceed();
    }

}
