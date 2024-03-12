package com.niit.onlivestream.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.Data;
import lombok.ToString;

/**
 * 
 * @TableName comment_log
 */
@TableName(value ="comment_log")
@Data
@ToString
public class CommentLog implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer commentid;

    /**
     * 
     */
    private String userName;

    /**
     * 
     */
    private String comment;

    /**
     * 
     */
    private Integer roomlogid;

    /**
     * 
     */
    private Date createtime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}