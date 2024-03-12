package com.niit.onlivestream.domain;


import lombok.Data;
import lombok.ToString;

import java.util.Date;


@Data
@ToString
public class ChatMessage {
    /**
     * 消息类型
     */
    private MessageType type;
    /**
     * 房间号
     */
    private String liveRoom;
    /**
     * 发送者
     */
    private String sender;
    /**
     * 消息内容
     */
    private String content;
    /**
     * 发送时间
     */
    private Date createTime;


    public enum MessageType {
        CHAT,
        GIFT,
        JOIN
    }

}
