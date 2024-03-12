package com.niit.onlivestream.vo.OnliveRequest;

import lombok.Data;

import java.io.Serializable;


@Data
public class StreamRequest implements Serializable {
    /**
     * 直播间号
     */
    private Integer liveid;
    /**
     * 拥有者的uuid
     */
    private String uuid;

}
