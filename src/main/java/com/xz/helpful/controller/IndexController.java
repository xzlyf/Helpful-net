package com.xz.helpful.controller;

import com.xz.helpful.pojo.vo.UserVo;
import com.xz.helpful.service.UserServer;
import com.xz.helpful.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @Author: xz
 * @Date: 2022/4/20
 */
@Slf4j
@Controller
@RequestMapping("/")
public class IndexController {
    @Autowired
    private UserServer userServer;

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
    public ModelAndView publishTask() {
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
    public ModelAndView history() {
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
    public ModelAndView manager() {
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
    public ModelAndView userinfo() {
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
    public ModelAndView helpful() {
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
