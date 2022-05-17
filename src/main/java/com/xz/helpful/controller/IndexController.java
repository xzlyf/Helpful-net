package com.xz.helpful.controller;

import com.xz.helpful.global.RedisKey;
import com.xz.helpful.pojo.User;
import com.xz.helpful.pojo.vo.BaseVo;
import com.xz.helpful.pojo.vo.RegisterVo;
import com.xz.helpful.pojo.vo.UserVo;
import com.xz.helpful.service.UserServer;
import com.xz.helpful.utils.RedisUtil;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 根目录跳转
     * 已登录：跳转/home
     * 未登录：跳转/index
     */
    @GetMapping("/")
    public ModelAndView root(HttpSession session) {
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

    /**
     * home目录，要求已登录用户才能访问
     */
    @GetMapping("/home")
    public ModelAndView home(HttpSession session,
                             HttpServletRequest request,
                             HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();
        Subject subject = SecurityUtils.getSubject();
        String email = subject.getPrincipal().toString();
        if (email == null) {
            subject.logout();
            modelAndView.setViewName("index");
            return modelAndView;
        }
        UserVo userInfo = userServer.findInfoByEmail(email);
        modelAndView.setViewName("home");
        modelAndView.addObject("info", userInfo);
        return modelAndView;
    }

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
    public String logout(HttpSession session) {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "index";
    }


    /**
     * 跳转任务大厅
     */
    @GetMapping("/receive")
    public ModelAndView receiveTask() {
        ModelAndView modelAndView = new ModelAndView();
        Subject subject = SecurityUtils.getSubject();
        String email = subject.getPrincipal().toString();
        UserVo userInfo = userServer.findInfoByEmail(email);
        if (userInfo == null) {
            subject.logout();
            modelAndView.setViewName("index");
            return modelAndView;
        }
        modelAndView.setViewName("receive");
        modelAndView.addObject("info", userInfo);
        return modelAndView;
    }

    /**
     * 发布任务
     */
    @GetMapping("/publish")
    public ModelAndView publishTask(){
        ModelAndView modelAndView = new ModelAndView();
        Subject subject = SecurityUtils.getSubject();
        String email = subject.getPrincipal().toString();
        UserVo userInfo = userServer.findInfoByEmail(email);
        if (userInfo == null) {
            subject.logout();
            modelAndView.setViewName("index");
            return modelAndView;
        }
        modelAndView.setViewName("publish");
        modelAndView.addObject("info", userInfo);
        return modelAndView;
    }

    @GetMapping("/history")
    public ModelAndView history(){
        ModelAndView modelAndView = new ModelAndView();
        Subject subject = SecurityUtils.getSubject();
        String email = subject.getPrincipal().toString();
        UserVo userInfo = userServer.findInfoByEmail(email);
        if (userInfo == null) {
            subject.logout();
            modelAndView.setViewName("index");
            return modelAndView;
        }
        modelAndView.setViewName("history");
        modelAndView.addObject("info", userInfo);
        return modelAndView;
    }

    @GetMapping("/manager")
    public ModelAndView manager(){
        ModelAndView modelAndView = new ModelAndView();
        Subject subject = SecurityUtils.getSubject();
        String email = subject.getPrincipal().toString();
        UserVo userInfo = userServer.findInfoByEmail(email);
        if (userInfo == null) {
            subject.logout();
            modelAndView.setViewName("index");
            return modelAndView;
        }
        modelAndView.setViewName("taskManager");
        modelAndView.addObject("info", userInfo);
        return modelAndView;
    }

    @GetMapping("/userinfo")
    public ModelAndView userinfo(){
        ModelAndView modelAndView = new ModelAndView();
        Subject subject = SecurityUtils.getSubject();
        String email = subject.getPrincipal().toString();
        UserVo userInfo = userServer.findInfoByEmail(email);
        if (userInfo == null) {
            subject.logout();
            modelAndView.setViewName("index");
            return modelAndView;
        }
        modelAndView.setViewName("userInfo");
        modelAndView.addObject("info", userInfo);
        return modelAndView;
    }

    @GetMapping("/helpful")
    public ModelAndView helpful(){
        ModelAndView modelAndView = new ModelAndView();
        Subject subject = SecurityUtils.getSubject();
        String email = subject.getPrincipal().toString();
        UserVo userInfo = userServer.findInfoByEmail(email);
        if (userInfo == null) {
            subject.logout();
            modelAndView.setViewName("index");
            return modelAndView;
        }
        modelAndView.setViewName("helpful");
        modelAndView.addObject("info", userInfo);
        return modelAndView;
    }
}
