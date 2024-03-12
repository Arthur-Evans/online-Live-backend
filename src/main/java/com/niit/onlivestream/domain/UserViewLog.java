package com.niit.onlivestream.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户浏览日志
 * @TableName user_view_log
 */
@TableName(value ="user_view_log")
@Data
public class UserViewLog implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    private String uuid;

    /**
     * 
     */
    private Date startviewtime;

    /**
     * 
     */
    private Date endviewtime;

    /**
     * 单位：秒
     */
    private Integer staytime;

    /**
     * 访问的哪场直播
     */
    private Integer roomLogId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        UserViewLog other = (UserViewLog) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUuid() == null ? other.getUuid() == null : this.getUuid().equals(other.getUuid()))
            && (this.getStartviewtime() == null ? other.getStartviewtime() == null : this.getStartviewtime().equals(other.getStartviewtime()))
            && (this.getEndviewtime() == null ? other.getEndviewtime() == null : this.getEndviewtime().equals(other.getEndviewtime()))
            && (this.getStaytime() == null ? other.getStaytime() == null : this.getStaytime().equals(other.getStaytime()))
            && (this.getRoomLogId() == null ? other.getRoomLogId() == null : this.getRoomLogId().equals(other.getRoomLogId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUuid() == null) ? 0 : getUuid().hashCode());
        result = prime * result + ((getStartviewtime() == null) ? 0 : getStartviewtime().hashCode());
        result = prime * result + ((getEndviewtime() == null) ? 0 : getEndviewtime().hashCode());
        result = prime * result + ((getStaytime() == null) ? 0 : getStaytime().hashCode());
        result = prime * result + ((getRoomLogId() == null) ? 0 : getRoomLogId().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", uuid=").append(uuid);
        sb.append(", startviewtime=").append(startviewtime);
        sb.append(", endviewtime=").append(endviewtime);
        sb.append(", staytime=").append(staytime);
        sb.append(", roomLogId=").append(roomLogId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}