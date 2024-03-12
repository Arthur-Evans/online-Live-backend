package com.niit.onlivestream.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.niit.onlivestream.domain.CommentLog;
import com.niit.onlivestream.mapper.CommentLogMapper;
import com.niit.onlivestream.service.CommentLogService;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

import static com.niit.onlivestream.contant.RedisDataUse.CommentRedisTemplate;

/**
* @author 29756
* @description 针对表【comment_log】的数据库操作Service实现
* @createDate 2024-01-15 19:09:48
*/
@Service
public class CommentLogServiceImpl extends ServiceImpl<CommentLogMapper, CommentLog>
    implements CommentLogService {


    @Resource(name = CommentRedisTemplate)
    private RedisTemplate<String, Set<CommentLog>> commentTemplate;

    @Override
    public void saveCommentLog() {

    }
}




