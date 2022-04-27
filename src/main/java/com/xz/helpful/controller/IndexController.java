package com.xz.helpful.controller;

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
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @Author: xz
 * @Date: 2022/4/20
 */
@Controller
@RequestMapping("/")
public class IndexController {
    @Autowired
    private UserServer userServer;

    @GetMapping("/")
    public ModelAndView root() {
        ModelAndView modelAndView = new ModelAndView();
        Subject subject = SecurityUtils.getSubject();
        //已登录的用户直接重定向至home页面
        if (subject.getPrincipal() == null) {
            modelAndView.setViewName("index");
        } else {
            modelAndView.setViewName("redirect:/home");
        }
        return modelAndView;
    }


    @ResponseBody
    @PostMapping("/login")
    public Object login(@RequestBody User user) {
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
        } catch (UnknownAccountException e) {
            return BaseVo.failed("用户名不存在");
        } catch (AuthenticationException e) {
            return BaseVo.failed("账号或密码错误");
        } catch (AuthorizationException e) {
            return BaseVo.failed("没有权限");
        }
        return BaseVo.success(null);
    }

    @ResponseBody
    @PostMapping("/verify")
    public Object verify(HttpSession session, @RequestBody RegisterVo registerVo) {
        //存入session，校验邮件验证码时会用到
        registerVo.setSession(session.getId());
        return userServer.verify(registerVo);
    }

    @ResponseBody
    @PostMapping("/register")
    @Transactional(rollbackFor = Exception.class)
    public Object register(HttpSession session,
                           @RequestParam String email,
                           @RequestParam String code) {
        User user = null;
        try {
            user = userServer.register(email, code, session.getId());
        } catch (IOException e) {
            return BaseVo.failed(e.getMessage());
        }
        //注册成功后完成登录操作
        if (user != null) {
            Subject subject = SecurityUtils.getSubject();
            UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(email, user.getPasswd());
            try {
                //进行验证，这里可以捕获异常，然后返回对应信息
                subject.login(usernamePasswordToken);
            } catch (UnknownAccountException e) {
                return BaseVo.success("自动登录失败", 3);
            }
            return BaseVo.success("注册成功");
        } else {
            return BaseVo.failed("注册失败，请稍后重试");
        }
    }

    @GetMapping("/logout")
    public String logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "index";
    }

    @GetMapping("/home")
    public ModelAndView home() {
        ModelAndView modelAndView = new ModelAndView();
        Subject subject = SecurityUtils.getSubject();
        String email = subject.getPrincipal().toString();
        UserVo userInfo = userServer.findByEmailInfo(email);
        if (userInfo == null) {
            subject.logout();
            modelAndView.setViewName("index");
            return modelAndView;
        }
        modelAndView.setViewName("home");
        modelAndView.addObject("info", userInfo);
        return modelAndView;
    }

}
