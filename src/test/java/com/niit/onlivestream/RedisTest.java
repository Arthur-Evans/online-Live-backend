package com.niit.onlivestream;

import com.niit.onlivestream.domain.UserInfo;
import jakarta.annotation.Resource;
import org.apache.catalina.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import static com.niit.onlivestream.contant.RedisDataUse.LiveRedisTemplate;
import static com.niit.onlivestream.contant.RedisDataUse.TokenRedisTemplate;


@SpringBootTest
public class RedisTest {

    // 这里跟 name的值 跟在  RedisConfig的 @Bean("redisTemplate")
    @Resource(name = TokenRedisTemplate )
    private RedisTemplate<String, Object> redisTemplate0;
    @Resource(name = LiveRedisTemplate)
    private RedisTemplate<String, Object> redisTemplate1;

    @Test
    public void test1(){
        UserInfo userInfo =new UserInfo();
        userInfo.setUuid("evans");
        String token = "12345";
        String name="张三";
        redisTemplate0.opsForValue().set(userInfo.getUuid(), token);
    }

    @Test
    public void test2(){
        UserInfo userInfo =new UserInfo();
        userInfo.setUuid("arthur");
        String token = "12345";
        String name="张三";
        redisTemplate1.opsForValue().set(userInfo.getUuid(), name);
    }

}
