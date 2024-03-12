package com.niit.onlivestream.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.niit.onlivestream.common.BaseResponse;
import com.niit.onlivestream.common.ErrorCode;
import com.niit.onlivestream.domain.FollowerInfo;
import com.niit.onlivestream.domain.RoomLog;
import com.niit.onlivestream.domain.UserInfo;
import com.niit.onlivestream.exception.BusinessException;
import com.niit.onlivestream.service.FollowerInfoService;
import com.niit.onlivestream.util.ResultUtils;
import com.niit.onlivestream.util.ThreadLocalUtil;
import com.niit.onlivestream.vo.SpectatorRequest.*;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static com.niit.onlivestream.contant.RedisDataUse.LiveRedisTemplate;
import static com.niit.onlivestream.contant.funType.TYPE_FOLLOWED;
import static com.niit.onlivestream.contant.funType.TYPE_FOLLOWING;

@RestController
@RequestMapping("/spectator")
public class SpectatorController {

    @Resource(name = LiveRedisTemplate)
    private RedisTemplate<String, Object> liveTemplate;



    @Resource
    private FollowerInfoService followerInfoService;



    /**
     * 关注主播功能
     * @return 是否关注成功
     */
    @CrossOrigin
    @PostMapping("/follow")
    public BaseResponse<String> followLive(@RequestBody FollowRequest request){
        if(request==null)
            throw new BusinessException(ErrorCode.NULL_ERROR,"无request");
        String spectator = request.getSpectator();
        String influencer = request.getInfluencer();
        if(spectator==null|| influencer==null|| StringUtils.isAnyBlank(spectator,influencer))
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请输入正确的请求信息");
        if(spectator.equals(influencer))
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"不能关注自己");
        UserInfo user= ThreadLocalUtil.get();
        if(!user.getUuid().equals(spectator))
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"非法入侵用户ID");
        QueryWrapper<FollowerInfo> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("followed",influencer);
        queryWrapper.eq("following",spectator);
        FollowerInfo info = followerInfoService.getOne(queryWrapper);
        if(info!=null)
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"您已关注，无须再次点击");
        FollowerInfo followerInfo =new FollowerInfo();
        followerInfo.setFollowed(influencer);
        followerInfo.setFollowing(spectator);
        try {
            followerInfoService.save(followerInfo);
        }catch (Exception e){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"观众ID和主播ID必须正确");
        }
        return ResultUtils.success("关注成功");
    }

    /**
     * 取消关注
     * @param frequest 请求
     * @return 信息
     */
    @CrossOrigin
    @PostMapping("/unfollow")
    public BaseResponse<String> unfollowLive(@RequestBody FollowRequest frequest){
        if(frequest==null)
            throw new BusinessException(ErrorCode.NULL_ERROR,"无request");
        String spectator = frequest.getSpectator();
        String influencer = frequest.getInfluencer();
        if(spectator==null|| influencer==null|| StringUtils.isAnyBlank(spectator,influencer))
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请输入正确的请求信息");
        UserInfo user= ThreadLocalUtil.get();
        if(!user.getUuid().equals(spectator))
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"非法入侵用户ID");
        int result  = followerInfoService.delete(spectator,influencer);
        if(result>0)
            return ResultUtils.success("取消关注成功");
        else
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"删除关注失败");
    }

    /**
     *
     * @param funRequest  type 类型 uuid 用户ID
     * @return 关注的主播信息  或者 被关注的粉丝信息
     */
    @CrossOrigin
    @PostMapping("getFunInfo")
    public BaseResponse<FunResponse>  getFollowMessage(@RequestBody FunRequest funRequest){
        if(funRequest==null)
            throw new BusinessException(ErrorCode.NULL_ERROR,"无request");
        if(StringUtils.isAnyBlank(funRequest.getUuid(),funRequest.getType()))
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请输入正确的请求信息");
        String uuid = funRequest.getUuid();
        FunResponse funResponse =new FunResponse();
        ArrayList<FunInfo> arrayList = new ArrayList<>();
        funResponse.setType(funRequest.getType());
        if(!funRequest.getType().equals(TYPE_FOLLOWED) &&! funRequest.getType().equals(TYPE_FOLLOWING))
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"type类型只要关注和粉丝两种类型");
        if(funRequest.getType().equals(TYPE_FOLLOWED)){
            arrayList = followerInfoService.getFollowedInfo(uuid);
        }
        if(funRequest.getType().equals(TYPE_FOLLOWING)){
            arrayList = followerInfoService.getFollowingInfo(uuid);
        }
        funResponse.setNumber(arrayList.size());
        funResponse.setFunInfoList(arrayList);
        return ResultUtils.success(funResponse);
    }





    @CrossOrigin
    @PostMapping("/star")
    public BaseResponse<String> starLive(@RequestBody StarRequest request){
        // 校验
        if(request==null)
            throw new BusinessException(ErrorCode.NULL_ERROR);
        String liveId = String.valueOf(request.getStarliveid());
        if(StringUtils.isAnyBlank(request.getUuid(),liveId))
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        UserInfo userInfo = ThreadLocalUtil.get();
        if(!userInfo.getUuid().equals(request.getUuid()))
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"非法入侵ID");
        //  执行操作
        ValueOperations<String,Object> liveDB = liveTemplate.opsForValue();
        RoomLog roomLog = (RoomLog) liveDB.get(liveId);
        if(roomLog==null)
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"直播间不存在");
        roomLog.setTotalstar(roomLog.getTotalstar()+1);
        liveDB.set(liveId,roomLog);
        return ResultUtils.success("点赞成功");
    }



}
