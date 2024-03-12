package com.niit.onlivestream.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.niit.onlivestream.domain.RoomLog;
import com.niit.onlivestream.mapper.RoomLogMapper;
import com.niit.onlivestream.service.RoomLogService;
import org.springframework.stereotype.Service;

/**
* @author 29756
* @description 针对表【room_log】的数据库操作Service实现
* @createDate 2024-01-15 19:09:48
*/
@Service
public class RoomLogServiceImpl extends ServiceImpl<RoomLogMapper, RoomLog>
    implements RoomLogService {

    @Override
    public void findRoomLodAll() {

    }
}




