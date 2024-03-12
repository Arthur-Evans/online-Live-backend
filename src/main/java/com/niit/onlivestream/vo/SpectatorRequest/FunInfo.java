package com.niit.onlivestream.vo.SpectatorRequest;

import lombok.Data;

import java.io.Serializable;




@Data
public class FunInfo implements Serializable {

    private String uuid;

    private String userName;

    private byte[] userAvatar;

    private String userSignature;
}
