package com.xz.helpful.controller;

import com.xz.helpful.pojo.User;
import com.xz.helpful.pojo.vo.BaseVo;
import com.xz.helpful.pojo.vo.UserVo;
import com.xz.helpful.service.UserServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

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
    public ModelAndView root(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        return modelAndView;
    }


    @ResponseBody
    @PostMapping("/login")
    public Object login(@RequestBody User user) {
        UserVo login = userServer.login(user);
        if (login == null) {
            //登录失败
            return BaseVo.failed(null);
        }
        return BaseVo.success(null);
    }

    @GetMapping("/test")
    public ModelAndView test() {
        return new ModelAndView("home");
    }
}
