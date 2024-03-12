package com.niit.onlivestream.config;


import com.niit.onlivestream.domain.CommentLog;
import com.niit.onlivestream.domain.PresentLog;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.Set;

import static com.niit.onlivestream.contant.RedisDataUse.*;

/**
 * Redis多数据库配置
 * @author  arthur
 */
@Configuration
public class RedisConfig {

    private final int db0 = 0;

    private final int db1 = 1;
    private final int db2 = 2;


    private final int db_comment = 3;


    @Bean
    public GenericObjectPoolConfig getPoolConfig(){
        // 配置redis连接池
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        int maxActive = 100;
        poolConfig.setMaxTotal(maxActive);
        int maxIdle = 100;
        poolConfig.setMaxIdle(maxIdle);
        int minIdle = 10;
        poolConfig.setMinIdle(minIdle);
        return poolConfig;
    }

    private RedisConnectionFactory getFactory(int database) {
        // 构建工厂对象
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        String host = "8.140.143.119";
        config.setHostName(host);
        int port = 6379;
        config.setPort(port);
        String password = "arthurevans777";
        config.setPassword(RedisPassword.of(password));
        int timeout = 3;
        LettucePoolingClientConfiguration clientConfig = LettucePoolingClientConfiguration.builder()
                .commandTimeout(Duration.ofSeconds(timeout))
                .poolConfig(getPoolConfig())
                .build();
        LettuceConnectionFactory factory = new LettuceConnectionFactory(config, clientConfig);
        // 设置使用的redis数据库
        factory.setDatabase(database);
        // 重新初始化工厂
        factory.afterPropertiesSet();
        return factory;
    }

    @NotNull
    private RedisTemplate<String, Object> getObjectRedisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        return redisTemplate;
    }



    @NotNull
    private RedisTemplate<String, CommentLog> getObjectRedisTemplate2() {
        RedisTemplate<String, CommentLog> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        return redisTemplate;
    }


    @NotNull
    private RedisTemplate<String, Set<CommentLog>> getSetRedisTemplate() {
        RedisTemplate<String, Set<CommentLog>> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        return redisTemplate;
    }



    /**
     * userToken数据库
     * @return
     */

    @Bean(name = TokenRedisTemplate)
    //加这个注解表示默认使用 也就是 主库的意思 我这里默认使用0号
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = getObjectRedisTemplate();
        redisTemplate.setConnectionFactory(this.getFactory(db0));
        return redisTemplate;
    }
    @Bean(name = TokenStringRedisTemplate)
    public StringRedisTemplate stringRedisTemplate() {
        RedisConnectionFactory factory = getFactory(db0);
        return new StringRedisTemplate(factory);
    }


    /**
     * 直播信息数据库
     * @return
     */
    @Bean(name = LiveRedisTemplate)
    public RedisTemplate<String, Object> redisTemplate2() {
        RedisTemplate<String, Object> redisTemplate = getObjectRedisTemplate();
        redisTemplate.setConnectionFactory(this.getFactory(db1));
        return redisTemplate;
    }

    @Bean(name = LiveStringRedisTemplate)
    public StringRedisTemplate getRedisTemplate2(){
        RedisConnectionFactory factory = getFactory(db1);
        return new StringRedisTemplate(factory);
    }

    /**
     *  2号数据库 存储 sessionID 和 房间名
     */
    /**
     * 直播信息数据库
     * @return
     */
    @Bean(name = SocketRedisTemplate)
    public RedisTemplate<String, Object> redisTemplate3() {
        RedisTemplate<String, Object> redisTemplate = getObjectRedisTemplate();
        redisTemplate.setConnectionFactory(this.getFactory(db2));
        return redisTemplate;
    }
    @Bean(name = SocketStringRedisTemplate)
    public StringRedisTemplate getRedisTemplate3(){
        RedisConnectionFactory factory = getFactory(db2);
        return new StringRedisTemplate(factory);
    }


    /**
     * 评论数据库
     */
    @Bean(name = CommentRedisTemplate)
    public RedisTemplate<String, Set<CommentLog>> setRedisTemplate() {
        RedisTemplate<String, Set<CommentLog>> redisTemplate = getSetRedisTemplate();
        redisTemplate.setConnectionFactory(getFactory(db_comment));
        return redisTemplate;
    }


    @Bean(name = "CommentRedis")
    public RedisTemplate<String, CommentLog> setRedisTemplate3() {
        RedisTemplate<String, CommentLog> redisTemplate = getObjectRedisTemplate2();
        redisTemplate.setConnectionFactory(getFactory(db_comment));
        return redisTemplate;
    }


    @NotNull
    private RedisTemplate<String, PresentLog> getObjectRedisTemplate3() {
        RedisTemplate<String, PresentLog> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        return redisTemplate;
    }


    private final static int db_gift=4;
    @Bean(name = "PresentRedisTemplate")
    public RedisTemplate<String, PresentLog> redisTemplate4() {
        RedisTemplate<String, PresentLog> redisTemplate = getObjectRedisTemplate3();
        redisTemplate.setConnectionFactory(this.getFactory(db_gift));
        return redisTemplate;
    }


}