package com.niit.onlivestream.vo.SpectatorRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowRequest implements Serializable {

    private String spectator;

    private String influencer;

}
