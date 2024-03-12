package com.niit.onlivestream;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.niit.onlivestream.domain.RoomInfo;
import com.niit.onlivestream.util.SerializeUtil;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class OnliveStreamApplicationTests {


    @Resource
    private StringRedisTemplate stringRedisTemplate;


    @Test
    void genToken(){
        Map<String,Object> claim = new HashMap<>();
        claim.put("id","1");
        claim.put("username","张三");

        String token = JWT.create().
                withClaim("user",claim)
                .withExpiresAt(new Date(System.currentTimeMillis()+1000*60*60*12))
                .sign(Algorithm.HMAC256("arthur"));
        System.out.println(token);
    }


    @Test
    void parseToken(){
        String token="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9" +
                ".eyJ1c2VyIjp7ImlkIjoiMSIsInVzZXJuYW" +
                "1lIjoi5byg5LiJIn0sImV4cCI6MTcwNjI5NjcxMn0" +
                ".yTFzYpbeZ5BN2YahQGBrBAQoJdkE58QITVFeUtEQ3cA";
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256("arthur")).build();
        DecodedJWT decodedJWT = jwtVerifier.verify(token);
        Map<String, Claim> claimMap = decodedJWT.getClaims();
        System.out.println(claimMap.get("user"));
    }


    @Test
    void testStringSet(){
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        operations.set("username","李四");
        operations.set("id","1",15, TimeUnit.SECONDS);
    }

    @Test
    void testStringGet(){
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();

        String username = operations.get("username");
        String id = operations.get("id");
        System.out.println(username);
        System.out.println(id);
    }



    @Test
    void testSerialize() throws Exception {
        RoomInfo roomInfo =new RoomInfo();
        roomInfo.setRoomname("李三的直播间");
        roomInfo.setUuid("123456");
        roomInfo.setLiveid(123);
        roomInfo.setProfile("这是我的直播间");
        System.out.println(roomInfo);
        //转成json字符串
        String json = JSON.toJSON(roomInfo).toString();
        System.out.println(json);
        //json字符串转成对象
        RoomInfo user1 = JSON.parseObject(json,RoomInfo.class);
        System.out.println(user1);
    }


}
