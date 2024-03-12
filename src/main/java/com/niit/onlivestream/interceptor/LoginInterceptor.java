package com.niit.onlivestream.interceptor;

import com.niit.onlivestream.common.ErrorCode;
import com.niit.onlivestream.config.RedisConfig;
import com.niit.onlivestream.domain.UserInfo;
import com.niit.onlivestream.exception.BusinessException;
import com.niit.onlivestream.util.JwtUtil;
import com.niit.onlivestream.util.ThreadLocalUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

import static com.niit.onlivestream.contant.RedisDataUse.TokenStringRedisTemplate;
import static com.niit.onlivestream.util.OMUtils.MapToObject;


@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Resource(name = TokenStringRedisTemplate)
    private StringRedisTemplate redisTemplate;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)throws Exception{
        String token = request.getHeader("Authorization");
        UserInfo user = new UserInfo();
        try {
            Map<String,Object> objectMap = JwtUtil.parseToken(token);
            //把业务数据存储到ThreadLocal中
            user= (UserInfo) MapToObject(objectMap,user);
        }catch (Exception e){
            //不放行
            throw new BusinessException(ErrorCode.NOT_LOGIN,"token为空");
        }
        //从redis中获取相同的token
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        String redisToken = operations.get(user.getUuid());
        //token失效
        if(redisToken==null)
            throw new BusinessException(ErrorCode.TOKEN_OUTTIME,"请重新登录");
        if(!redisToken.equals(token))
            throw new BusinessException(ErrorCode.TOKEN_OUTTIME,"账号已在别处登录");
        ThreadLocalUtil.set(user);
        return true;
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //清空ThreadLocal
        ThreadLocalUtil.remove();
    }
}