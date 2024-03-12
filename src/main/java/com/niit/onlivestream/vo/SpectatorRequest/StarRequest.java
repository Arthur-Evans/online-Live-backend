package com.niit.onlivestream.vo.SpectatorRequest;

import lombok.Data;

import java.io.Serializable;



@Data
public class StarRequest implements Serializable {

    /**
     * 拥有者的uuid
     */
    private String uuid;

    /**
     * 直播间号
     */
    private Integer starliveid;

}
