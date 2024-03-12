package com.niit.onlivestream.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.niit.onlivestream.domain.FollowerInfo;
import com.niit.onlivestream.domain.UserInfo;
import com.niit.onlivestream.mapper.FollowerInfoMapper;
import com.niit.onlivestream.mapper.UserInfoMapper;
import com.niit.onlivestream.service.FollowerInfoService;
import com.niit.onlivestream.vo.SpectatorRequest.FunInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* @author 29756
* @description 针对表【follower_info】的数据库操作Service实现
* @createDate 2024-01-15 19:09:48
*/
@Service
public class FollowerInfoServiceImpl extends ServiceImpl<FollowerInfoMapper, FollowerInfo>
    implements FollowerInfoService {

    @Resource
    private FollowerInfoMapper followMapper;
    @Resource
    private UserInfoMapper userMapper;

    @Override
    public int delete(String spectator, String influencer) {
        QueryWrapper<FollowerInfo> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("followed",influencer);
        queryWrapper.eq("following",spectator);
        return followMapper.delete(queryWrapper);

    }

    @Override
    public ArrayList<FunInfo> getFollowingInfo(String uuid) {
        ArrayList<FunInfo> funInfos = new ArrayList<>();
        QueryWrapper<FollowerInfo> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("following",uuid);
        List<FollowerInfo> followerInfos = followMapper.selectList(queryWrapper);
        for (FollowerInfo flo: followerInfos) {
            UserInfo user = new UserInfo();
            QueryWrapper<UserInfo> userQuery =new QueryWrapper<>();
            userQuery.eq("uuid",flo.getFollowed());
            user = userMapper.selectOne(userQuery);
            FunInfo funInfo = new FunInfo();
            funInfo=  getFunByUser(user);
            funInfos.add(funInfo);
        }
        return funInfos;
    }


    @Override
    public ArrayList<FunInfo> getFollowedInfo(String uuid) {
        ArrayList<FunInfo> funInfos = new ArrayList<>();
        QueryWrapper<FollowerInfo> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("followed",uuid);
        List<FollowerInfo> followerInfos = followMapper.selectList(queryWrapper);
        for (FollowerInfo flo: followerInfos) {
            UserInfo user = new UserInfo();
            QueryWrapper<UserInfo> userQuery =new QueryWrapper<>();
            userQuery.eq("uuid",flo.getFollowing());
            user = userMapper.selectOne(userQuery);
            FunInfo funInfo = new FunInfo();
            funInfo=  getFunByUser(user);
            funInfos.add(funInfo);
        }
        return funInfos;
    }


    public FunInfo getFunByUser(UserInfo userInfo){
        FunInfo funInfo =new FunInfo();
        if(userInfo!=null){
            funInfo.setUuid(userInfo.getUuid());
            funInfo.setUserAvatar(userInfo.getUseravatar());
            funInfo.setUserName(userInfo.getUsername());
            funInfo.setUserSignature(userInfo.getUserSignature());
        }
        return funInfo;
    }
}




