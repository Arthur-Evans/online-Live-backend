package com.niit.onlivestream.vo.UserRequest;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserChangePswRequest implements Serializable {


    private String oldPassword;

    private String newPassword;

    private String checkPassword;


}
