package com.xz.helpful.service;

import com.xz.helpful.pojo.User;
import com.xz.helpful.pojo.vo.UserVo;

import java.util.List;

/**
 * @Author: xz
 * @Date: 2022/4/20
 */
public interface UserServer {
    List<User> findAll();

    //校验账号密码是否符合规定
    boolean verify(User user);

    User findByEmail(String email);

    UserVo findByEmailInfo(String email);
}
