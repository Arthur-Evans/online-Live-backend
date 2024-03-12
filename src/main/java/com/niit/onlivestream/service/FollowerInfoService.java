package com.niit.onlivestream.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.niit.onlivestream.domain.FollowerInfo;
import com.niit.onlivestream.vo.SpectatorRequest.FunInfo;

import java.util.ArrayList;

public interface FollowerInfoService extends IService<FollowerInfo> {

    /**
     * 删除
     * @param spectator 观众ID
     * @param influencer 主播ID
     */
    int delete(String spectator, String influencer);
    ArrayList<FunInfo> getFollowingInfo(String uuid);
    ArrayList<FunInfo> getFollowedInfo(String uuid);


}
