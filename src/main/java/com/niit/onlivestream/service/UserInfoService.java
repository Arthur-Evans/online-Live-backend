package com.niit.onlivestream.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.niit.onlivestream.domain.UserInfo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface UserInfoService extends IService<UserInfo> {
    String userRegister(String userAccount, String userPassword, String checkPassword);
    UserInfo userLogin(String userAccount, String userPassword, HttpServletRequest request);
    Integer userUpdatePassword(String oldPassword, String newPassword, String checkPassword);
    UserInfo getSafetyUser(UserInfo originUser);
    String encodePws(String password);



}
