package com.xz.helpful.service;

import com.xz.helpful.pojo.User;
import com.xz.helpful.pojo.vo.BaseVo;
import com.xz.helpful.pojo.vo.RegisterVo;
import com.xz.helpful.pojo.vo.UserVo;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @Author: xz
 * @Date: 2022/4/20
 */
public interface UserServer {
    List<User> findAll();

    //校验账号密码是否符合规定
    boolean norm(User user);

    String getUserPassword(String email);

    UserVo findInfoByEmail(String email);

    //根据邮箱查询用户id
    Integer findUserIdByEmail(String email);

    String findUserNameByEmail(String email);

    //验证人机，并验证邮箱是否注册，完成验证后发送验证码邮件，并把用户数据存入redis
    BaseVo verify(HttpSession session, RegisterVo registerVo);

    //再次发送邮箱验证码
    void sendEmailAgain(HttpSession session, String email) throws Exception;

    //验证邮箱验证码并注册
    User register(String email, String code, HttpSession session) throws Exception;
}
