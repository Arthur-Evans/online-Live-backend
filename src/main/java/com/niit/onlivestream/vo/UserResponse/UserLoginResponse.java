package com.niit.onlivestream.vo.UserResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginResponse implements Serializable {


    private String uuid;

    private String useraccount;

    private String userpassword;

    private String username;

    private Integer userage;

    private String useremail;

    private Integer userprivilege;

    private Date usercreatetime;

    private Integer usersex;

    private byte[] useravatar;

    private String token;



}
