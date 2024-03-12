package com.niit.onlivestream.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.niit.onlivestream.common.ErrorCode;
import com.niit.onlivestream.domain.RoomInfo;
import com.niit.onlivestream.exception.BusinessException;
import com.niit.onlivestream.mapper.RoomInfoMapper;
import com.niit.onlivestream.service.RoomInfoService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
* @author 29756
* @description 针对表【room_info】的数据库操作Service实现
* @createDate 2024-01-15 19:09:48
*/
@Service
public class RoomInfoServiceImpl extends ServiceImpl<RoomInfoMapper, RoomInfo>
    implements RoomInfoService {

    @Resource
    private RoomInfoMapper roomInfoMapper;

    private static final String roomName="直播间";

    private static final  Integer defaultPid = 999;

    @Override
    public void InsertLiveId(String uuid, String userAccount) {
        RoomInfo roomInfo =new RoomInfo();
        roomInfo.setUuid(uuid);
        roomInfo.setRoomname(roomName+userAccount);
        roomInfo.setPartitionid(defaultPid);// 默认分区未设置
        boolean result = this.save(roomInfo);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"插入失败");
        }
    }

    @Override
    public RoomInfo getRoomById(String uuid) {
        QueryWrapper<RoomInfo> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("uuid",uuid);
        RoomInfo roomInfo= roomInfoMapper.selectOne(queryWrapper);
        if(roomInfo==null)
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"找不到对应ID的直播序列号");
        return roomInfo;
    }
}




