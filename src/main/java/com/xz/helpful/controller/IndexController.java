package com.xz.helpful.controller;

import com.xz.helpful.global.RedisKey;
import com.xz.helpful.service.UserServer;
import com.xz.helpful.service.WalletServer;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;

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
    @Autowired
    private WalletServer walletServer;

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
    public ModelAndView home(@SessionAttribute(RedisKey.SESSION_USER_EMAIL) String email,
                                    @SessionAttribute(RedisKey.SESSION_USER_ID) Integer id) {
        ModelAndView modelAndView = new ModelAndView();
        String name = userServer.findUserNameByEmail(email);
        Integer wallet = walletServer.queryMoneyByUserId(id);
        modelAndView.setViewName("home");
        modelAndView.addObject("name", name);
        modelAndView.addObject("wallet", wallet);
        return modelAndView;
    }


    /**
     * 跳转任务大厅
     */
    @GetMapping("/receive")
    public ModelAndView receiveTask(@SessionAttribute(RedisKey.SESSION_USER_EMAIL) String email,
                                    @SessionAttribute(RedisKey.SESSION_USER_ID) Integer id) {
        ModelAndView modelAndView = new ModelAndView();
        String name = userServer.findUserNameByEmail(email);
        Integer wallet = walletServer.queryMoneyByUserId(id);
        modelAndView.setViewName("receive");
        modelAndView.addObject("name", name);
        modelAndView.addObject("wallet", wallet);
        return modelAndView;
    }


    /**
     * 发布任务
     */
    @GetMapping("/publish")
    public ModelAndView publishTask(@SessionAttribute(RedisKey.SESSION_USER_EMAIL) String email,
                                    @SessionAttribute(RedisKey.SESSION_USER_ID) Integer id) {
        ModelAndView modelAndView = new ModelAndView();
        String name = userServer.findUserNameByEmail(email);
        Integer wallet = walletServer.queryMoneyByUserId(id);
        modelAndView.setViewName("publish");
        modelAndView.addObject("name", name);
        modelAndView.addObject("wallet", wallet);
        return modelAndView;
    }

    @GetMapping("/history")
    public ModelAndView history(@SessionAttribute(RedisKey.SESSION_USER_EMAIL) String email) {
        ModelAndView modelAndView = new ModelAndView();
        String name = userServer.findUserNameByEmail(email);
        modelAndView.setViewName("history");
        modelAndView.addObject("name", name);
        return modelAndView;
    }

    @GetMapping("/manager")
    public ModelAndView manager(@SessionAttribute(RedisKey.SESSION_USER_EMAIL) String email) {
        ModelAndView modelAndView = new ModelAndView();
        String name = userServer.findUserNameByEmail(email);
        modelAndView.setViewName("taskManager");
        modelAndView.addObject("name", name);
        return modelAndView;
    }

    @GetMapping("/userinfo")
    public ModelAndView userinfo(@SessionAttribute(RedisKey.SESSION_USER_EMAIL) String email) {
        ModelAndView modelAndView = new ModelAndView();
        String name = userServer.findUserNameByEmail(email);
        modelAndView.setViewName("userInfo");
        modelAndView.addObject("name", name);
        return modelAndView;
    }

    @GetMapping("/helpful")
    public ModelAndView helpful(@SessionAttribute(RedisKey.SESSION_USER_EMAIL) String email) {
        ModelAndView modelAndView = new ModelAndView();
        String name = userServer.findUserNameByEmail(email);
        modelAndView.setViewName("helpful");
        modelAndView.addObject("name", name);
        return modelAndView;
    }
}
