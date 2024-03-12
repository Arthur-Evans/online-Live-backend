package com.niit.onlivestream.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName present_log
 */
@TableName(value ="present_log")
@Data
public class PresentLog implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 谁送的
     */
    private String uuid;

    /**
     * 送给哪场直播
     */
    private Integer roomlogId;

    /**
     * 送的是啥
     */
    private Integer presentid;

    /**
     * 啥时候送的
     */
    private Date sendtime;

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
        PresentLog other = (PresentLog) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUuid() == null ? other.getUuid() == null : this.getUuid().equals(other.getUuid()))
            && (this.getRoomlogId() == null ? other.getRoomlogId() == null : this.getRoomlogId().equals(other.getRoomlogId()))
            && (this.getPresentid() == null ? other.getPresentid() == null : this.getPresentid().equals(other.getPresentid()))
            && (this.getSendtime() == null ? other.getSendtime() == null : this.getSendtime().equals(other.getSendtime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUuid() == null) ? 0 : getUuid().hashCode());
        result = prime * result + ((getRoomlogId() == null) ? 0 : getRoomlogId().hashCode());
        result = prime * result + ((getPresentid() == null) ? 0 : getPresentid().hashCode());
        result = prime * result + ((getSendtime() == null) ? 0 : getSendtime().hashCode());
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
        sb.append(", roomlogId=").append(roomlogId);
        sb.append(", presentid=").append(presentid);
        sb.append(", sendtime=").append(sendtime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}