package com.xz.helpful.service;

import com.xz.helpful.pojo.User;
import com.xz.helpful.pojo.vo.BaseVo;
import com.xz.helpful.pojo.vo.RegisterVo;
import com.xz.helpful.pojo.vo.UserVo;

import javax.servlet.http.HttpSession;
import java.io.IOException;
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

    //根据邮箱查询用户id
    Integer findUserIdByEmail(String email);

    //验证人机，并验证邮箱是否注册，完成验证后发送验证码邮件，并把用户数据存入redis
    BaseVo verify(HttpSession session,RegisterVo registerVo);

    //验证邮箱验证码并注册
    User register(String email,String code,String session) throws IOException;
}
