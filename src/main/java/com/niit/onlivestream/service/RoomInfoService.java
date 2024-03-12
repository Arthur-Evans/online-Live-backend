package com.niit.onlivestream.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.niit.onlivestream.domain.RoomInfo;
import com.niit.onlivestream.domain.UserInfo;

public interface RoomInfoService extends IService<RoomInfo> {

    /**
     * 注册的时候拿到唯一直播序列号
     *
     * @param uuid 用户ID
     */
    void InsertLiveId(String uuid, String userAccount);

    RoomInfo getRoomById(String uuid);

}
