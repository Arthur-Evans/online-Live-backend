package com.niit.onlivestream.vo.UserRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserChangeInfoRequest implements Serializable {

    private String uuid;

    private String username;

    private Integer userage;

    private String useremail;

    private Integer usersex;

    private byte[] useravatar;

    private String userSignature;


}
