package com.niit.onlivestream.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.niit.onlivestream.domain.RoomLog;




public interface RoomLogService extends IService<RoomLog> {

    void findRoomLodAll();

}
