package com.niit.onlivestream.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.niit.onlivestream.common.BaseResponse;
import com.niit.onlivestream.common.ErrorCode;
import com.niit.onlivestream.domain.*;
import com.niit.onlivestream.service.CommentLogService;
import com.niit.onlivestream.service.PresentLogService;
import com.niit.onlivestream.util.ResultUtils;
import com.niit.onlivestream.exception.BusinessException;
import com.niit.onlivestream.service.RoomInfoService;
import com.niit.onlivestream.service.RoomLogService;
import com.niit.onlivestream.util.ThreadLocalUtil;
import com.niit.onlivestream.vo.OnliveRequest.StreamRequest;
import com.niit.onlivestream.vo.OnliveRequest.UpdateLiveRequest;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.niit.onlivestream.contant.RedisDataUse.*;

@RestController
@RequestMapping("/influencer")
public class InfluencerController {

    @Resource
    private RoomInfoService roomInfoService;
    @Resource
    private RoomLogService roomLogService;

    @Resource(name = LiveRedisTemplate)
    private RedisTemplate<String, Object> liveTemplate;
    /**
     *
     * @param uuid 用户名
     * @return 直播序列号及相关信息
     */
    @CrossOrigin
    @GetMapping  ("/getRoomInfo")
    public BaseResponse<RoomInfo> getRoomInfoByUserId(@RequestParam String uuid){
        if(uuid==null)
            throw new BusinessException(ErrorCode.NULL_ERROR);
        RoomInfo roomInfo = roomInfoService.getRoomById(uuid);
        return ResultUtils.success(roomInfo);
    }



    /**
     * 开始直播
     * @param request  用户ID 直播序列号
     * @return  直播成功
     */
    @CrossOrigin
    @PostMapping("/startlive")
    public BaseResponse<String> startLive(@RequestBody StreamRequest request){
        if(request==null)
            throw new BusinessException(ErrorCode.NULL_ERROR);
        if (StringUtils.isAnyBlank(request.getUuid()))
            throw new BusinessException(ErrorCode.NULL_ERROR,"部分数据为空");
        int liveId = request.getLiveid();
        String userId = request.getUuid();
        UserInfo user= ThreadLocalUtil.get();
        if(!user.getUuid().equals(userId))
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"非法入侵用户ID");
        //查一下用户ID和直播序列号 是不是 一一对应
        QueryWrapper<RoomInfo> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("liveid",liveId);
        queryWrapper.eq("uuid",userId);
        RoomInfo roomInfo = roomInfoService.getOne(queryWrapper);
        if(roomInfo==null)
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"序列号与用户ID不匹配");

        //先持久化部分 roomLog
        RoomLog roomLog =new RoomLog();
        roomLog.setId(roomLog.getId());
        roomLog.setCreatetime(new Date());
        roomLog.setRoomforeignid(roomInfo.getLiveid());
        roomLog.setName(roomInfo.getRoomname());
        roomLog.setProfile(roomInfo.getProfile());
        roomLog.setRoomAvatar(roomInfo.getRoomAvatar());
        roomLog.setPartitionid(roomInfo.getPartitionid());
        roomLog.setTotalstar(0);
        roomLog.setTotalpresentvalues(0);
        boolean saveRoomLog = roomLogService.save(roomLog);
        // 存redis 一部分roomlog
        // Redis 加入到开播队列
        ValueOperations<String,Object> liveDB = liveTemplate.opsForValue();
        String live = String.valueOf(liveId);
        liveDB.set(live,roomLog,10, TimeUnit.DAYS);
        return ResultUtils.success("开始直播成功");
    }


    /**
     * 结束直播
     * @param request 用户ID 直播序列号
     * @return 结束成功
     */


    @CrossOrigin
    @PostMapping("endlive")
    public BaseResponse<String> endLive(@RequestBody StreamRequest request){
        if(request==null)
            throw new BusinessException(ErrorCode.NULL_ERROR);
        if (StringUtils.isAnyBlank(request.getUuid()))
            throw new BusinessException(ErrorCode.NULL_ERROR,"部分数据为空");
        int liveId = request.getLiveid();
        String userId = request.getUuid();
        UserInfo user= ThreadLocalUtil.get();
        if(!user.getUuid().equals(userId))
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"非法入侵用户ID");
        //查一下用户ID和直播序列号 是不是 一一对应
        QueryWrapper<RoomInfo> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("liveid",liveId);
        queryWrapper.eq("uuid",userId);
        RoomInfo roomInfo = roomInfoService.getOne(queryWrapper);
        if(roomInfo==null)
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"序列号与用户ID不匹配");

        //从RedIS里面拿取数据
        String liveID = String.valueOf(request.getLiveid());

        ValueOperations<String,Object> liveDB = liveTemplate.opsForValue();
        RoomLog roomLog = (RoomLog) liveDB.get(liveID);

        if(roomLog==null)
            throw new BusinessException(ErrorCode.TOKEN_OUTTIME,"直播间信息未及时关闭十天自动关闭");
        roomLog.setStoptime(new Date());

        // 礼物数
        roomLog.setTotalpresentvalues(SaveGift(liveID));
        // 更新房间LOG数据
        QueryWrapper<RoomLog> queryWrapper2 =new QueryWrapper<>();
        queryWrapper2.eq("id",roomLog.getId());
        boolean logResult = roomLogService.update(roomLog,queryWrapper2);
        if(!logResult)
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"保存失败");
        //从Redis删除数据
        liveTemplate.delete(liveID);
        // 从这里开始一个异步操作 评论保存数据库
        try {
            performAsyncOperation(liveID);
        } catch (InterruptedException e) {
            System.out.println("评论保存有可能出现问题");
            throw new RuntimeException(e);
        }
        // 从Redis删除礼物记录
        presentLogRedisTemplate.opsForSet().getOperations().delete(liveID);
        return ResultUtils.success("结束直播成功");
    }

    @Resource(name = "PresentRedisTemplate")
    private RedisTemplate<String, PresentLog> presentLogRedisTemplate;
    @Resource
    private PresentLogService presentLogService;

    private static final  Map<Integer,Integer> valueMap = new HashMap<>();

    static {
        valueMap.put(1,1);
        valueMap.put(2,1);
        valueMap.put(3,10);
        valueMap.put(4,52);
        valueMap.put(5,520);
        valueMap.put(6,1000);
        valueMap.put(7,2333);
        valueMap.put(8,6666);
        valueMap.put(9,12450);
    }


    public synchronized Integer SaveGift(String live){
        Set<PresentLog> presentLogs =presentLogRedisTemplate.opsForSet().members(live);
        assert presentLogs != null;
        for (PresentLog presentLog:presentLogs) {
            presentLogService.save(presentLog);
        }
        System.out.println("礼物记录保存成功");
        //
        int res=0;
        // 礼物总量
        for (PresentLog presentLog:presentLogs) {
            res+=valueMap.get(presentLog.getPresentid());
        }
        return  res;
    }


    @Resource(name = "CommentRedis")
    private RedisTemplate<String,CommentLog> commentLogRedisTemplate;
    @Resource
    private CommentLogService commentLogService;


    @Async
    public void performAsyncOperation(String live) throws InterruptedException {
        Set<CommentLog> commentLogs = commentLogRedisTemplate.opsForSet().members(live);
        assert commentLogs != null;
        for (CommentLog comment:commentLogs) {
            commentLogService.save(comment);
        }
        System.out.println("数据库异步保存成功");

        // 保存MySQL之后删除redis
        commentLogRedisTemplate.opsForValue().getOperations().delete(live);
        System.out.println("删除成功");
    }

    /**
     * 根据主键liveID查询是否直播
     * 是 返回房间信息
     * 否 返回null
     */
    @CrossOrigin
    @GetMapping("/findIslive")
    public BaseResponse<RoomLog> getRoomByLiveId(@RequestParam Integer liveID){
        if(liveID==null|| liveID==0)
            throw new BusinessException(ErrorCode.NULL_ERROR,"没有房间号");
        // 得到liveID
        String live = String.valueOf(liveID);
        ValueOperations<String,Object> liveDB = liveTemplate.opsForValue();
        RoomLog roomLog = (RoomLog) liveDB.get(live);
        if(roomLog==null)
            throw  new BusinessException(ErrorCode.SUCCESS,"没有直播");
        return ResultUtils.success(roomLog,"正在直播");
    }


    @CrossOrigin
    @PostMapping("/updateLive")
    public BaseResponse<String> updateRoomInfo(@RequestBody UpdateLiveRequest updateLiveRequest){
        if(updateLiveRequest==null)
            throw new BusinessException(ErrorCode.NULL_ERROR);
        if (StringUtils.isAnyBlank(updateLiveRequest.getUuid()))
            throw new BusinessException(ErrorCode.NULL_ERROR,"部分数据为空");
        String liveId = String.valueOf(updateLiveRequest.getLiveId());
        String userId = updateLiveRequest.getUuid();
        UserInfo user= ThreadLocalUtil.get();
        if(!user.getUuid().equals(userId))
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"非法入侵用户ID");
        //查一下用户ID和直播序列号 是不是 一一对应
        QueryWrapper<RoomInfo> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("liveid",updateLiveRequest.getLiveId());
        queryWrapper.eq("uuid",userId);
        RoomInfo roomInfo = roomInfoService.getOne(queryWrapper);

        if(roomInfo==null)
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        // 开始操作 w分别往MySQL和Redis里面去写
        ValueOperations<String,Object> liveDB = liveTemplate.opsForValue();
        RoomLog roomLog = (RoomLog) liveDB.get(liveId);
        int flag =1; // 未开播
        if(roomLog==null)
            flag=0;
        if(updateLiveRequest.getRoomName()!=null && updateLiveRequest.getRoomName().length()>0){
            roomInfo.setRoomname(updateLiveRequest.getRoomName());
            if(flag ==1)
                roomLog.setName(updateLiveRequest.getRoomName());
        }

        if(updateLiveRequest.getPartitionId()!=null &&updateLiveRequest.getPartitionId()>0&&updateLiveRequest.getPartitionId()<=10){
            roomInfo.setPartitionid(updateLiveRequest.getPartitionId());
            if(flag ==1)
                roomLog.setPartitionid(updateLiveRequest.getPartitionId());

        }

        if(updateLiveRequest.getProfile()!=null&& updateLiveRequest.getProfile().length()>0){
            roomInfo.setProfile(updateLiveRequest.getProfile());
            if(flag ==1)
                roomLog.setProfile(updateLiveRequest.getProfile());

        }

        if(updateLiveRequest.getRoomAvatar()!=null && updateLiveRequest.getRoomAvatar().length>0){
            roomInfo.setRoomAvatar(updateLiveRequest.getRoomAvatar());
            if(flag ==1)
                roomLog.setRoomAvatar(updateLiveRequest.getRoomAvatar());

        }
        // 校验完成 REDIS
        if(flag==1)
            liveDB.set(liveId,roomLog);
        // MYSQL
        roomInfoService.update(roomInfo,queryWrapper);
        return ResultUtils.success("修改成功");
    }




}
