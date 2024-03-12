package com.niit.onlivestream.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.niit.onlivestream.domain.UserViewLog;
import com.niit.onlivestream.mapper.UserViewLogMapper;
import com.niit.onlivestream.service.UserViewLogService;
import org.springframework.stereotype.Service;

/**
* @author 29756
* @description 针对表【user_view_log(用户浏览日志)】的数据库操作Service实现
* @createDate 2024-01-15 19:09:48
*/
@Service
public class UserViewLogServiceImpl extends ServiceImpl<UserViewLogMapper, UserViewLog>
    implements UserViewLogService {

}




