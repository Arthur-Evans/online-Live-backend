package com.niit.onlivestream.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 
 * @TableName room_log
 */
@TableName(value ="room_log")
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RoomLog implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    private Date createtime;

    /**
     * 外键，关联Room_info的那个房间推流号
     */
    private Integer roomforeignid;

    /**
     * 简介
     */
    private String profile;

    /**
     * 房间名
     */
    private String name;

    /**
     * 
     */
    private Date stoptime;

    /**
     * 
     */
    private Integer totalstar;

    /**
     * 
     */
    private Integer totalpresentvalues;

    /**
     * 分区
     */
    private Integer partitionid;


    /***
     * 封面
     */
    private byte[] roomAvatar;


    @TableField(exist = false)
    private static final long serialVersionUID = 2755162995496950499L;
}