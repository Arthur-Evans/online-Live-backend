package com.niit.onlivestream.vo.OnliveRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateLiveRequest implements Serializable {

    private String uuid;

    private Integer liveId;

    private String roomName;

    private Integer partitionId;

    private String profile;

    private byte[] roomAvatar;

}
