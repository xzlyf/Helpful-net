package com.xz.helpful.controller;

import com.xz.helpful.global.RedisKey;
import com.xz.helpful.pojo.User;
import com.xz.helpful.pojo.vo.BaseVo;
import com.xz.helpful.pojo.vo.RegisterVo;
import com.xz.helpful.pojo.vo.UserVo;
import com.xz.helpful.service.UserServer;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author XiaoZe
 * @email czr2001@outlook.com
 * @date 2022/5/18 16:04
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserServer userServer;

    /**
     * 登录接口
     */
    @ResponseBody
    @PostMapping("/login")
    public Object login(HttpSession session,
                        @RequestBody User user) {
        boolean verify = userServer.verify(user);
        if (!verify) {
            return BaseVo.failed("账号或密码格式不正确");
        }

        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(
                user.getEmail(),
                user.getPasswd()
        );
        try {
            //进行验证，这里可以捕获异常，然后返回对应信息
            subject.login(usernamePasswordToken);
            //session绑定用户的email和id
            Integer userId = userServer.findUserIdByEmail(user.getEmail());
            session.setAttribute(RedisKey.SESSION_USER_ID, userId);
            session.setAttribute(RedisKey.SESSION_USER_EMAIL, user.getEmail());

        } catch (UnknownAccountException e) {
            return BaseVo.failed("用户名不存在");
        } catch (AuthenticationException e) {
            return BaseVo.failed("账号或密码错误");
        } catch (AuthorizationException e) {
            return BaseVo.failed("没有权限");
        }
        return BaseVo.success(null);
    }

    /**
     * 人机验证码验证接口（携带注册信息）
     */
    @ResponseBody
    @PostMapping("/verify")
    public Object verify(HttpSession session, @RequestBody RegisterVo registerVo) {
        return userServer.verify(session, registerVo);
    }

    /**
     * 邮件验证接口
     */
    @ResponseBody
    @PostMapping("/register")
    @Transactional(rollbackFor = Exception.class)
    public Object register(HttpSession session,
                           @RequestParam String email,
                           @RequestParam String code) {
        User user = null;
        try {
            user = userServer.register(email, code, session);
        } catch (Exception e) {
            return BaseVo.failed(e.getMessage());
        }
        //注册成功后完成登录操作
        if (user != null) {
            Subject subject = SecurityUtils.getSubject();
            UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(email, user.getPasswd());
            try {
                //进行验证，这里可以捕获异常，然后返回对应信息
                subject.login(usernamePasswordToken);
                //session绑定用户的email和id
                session.setAttribute(RedisKey.SESSION_USER_ID, user.getId());
                session.setAttribute(RedisKey.SESSION_USER_EMAIL, user.getEmail());
            } catch (UnknownAccountException e) {
                return BaseVo.success("自动登录失败", 3);
            }
            return BaseVo.success("注册成功");
        } else {
            return BaseVo.failed("注册失败，请稍后重试");
        }
    }

    /**
     * 退出登录接口
     */
    @GetMapping("/logout")
    public String logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "index";
    }

    @GetMapping("/info")
    public Object getUserInfo(@SessionAttribute(RedisKey.SESSION_USER_EMAIL)String email){
        ModelAndView modelAndView = new ModelAndView();
        UserVo userInfo = userServer.findInfoByEmail(email);
        modelAndView.setViewName("view/user-info");
        modelAndView.addObject("info", userInfo);
        return modelAndView;
    }

    @GetMapping("/reset")
    public Object getUserReset(@SessionAttribute(RedisKey.SESSION_USER_EMAIL)String email){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("view/user-reset");
        modelAndView.addObject("email",email);
        return modelAndView;
    }

    @ResponseBody
    @PostMapping("/reset/passwd")
    public Object resetPasswd(@RequestParam String passwd,
                              @RequestParam String code){

        return BaseVo.success(passwd+code);
    }
}
