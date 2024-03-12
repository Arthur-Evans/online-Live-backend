package com.niit.onlivestream.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 
 * @TableName room_info
 */
@TableName(value ="room_info")
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RoomInfo implements Serializable {
    /**
     * 直播间号
     */
    @TableId(type = IdType.AUTO)
    private Integer liveid;

    /**
     * 名字
     */
    private String roomname;

    /**
     * 拥有者的uuid
     */
    private String uuid;

    /**
     * 分区
     */
    private Integer partitionid;

    /**
     * 简介
     */
    private String profile;

    /***
     * 封面
     */
    private byte[] roomAvatar;


    @TableField(exist = false)
    private static final long serialVersionUID = 1755162995496909499L;


}
