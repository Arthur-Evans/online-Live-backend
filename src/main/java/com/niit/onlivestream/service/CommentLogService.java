package com.niit.onlivestream.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.niit.onlivestream.domain.CommentLog;

public interface CommentLogService extends IService<CommentLog> {


    void saveCommentLog();

}
