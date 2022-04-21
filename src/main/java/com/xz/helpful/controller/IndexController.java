package com.xz.helpful.controller;

import com.xz.helpful.pojo.User;
import com.xz.helpful.service.UserServer;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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


    @PostMapping("/login")
    public String login(@RequestBody User user) {

        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(
                user.getEmail(),
                user.getPasswd()
        );
        try {
            //进行验证，这里可以捕获异常，然后返回对应信息
            subject.login(usernamePasswordToken);
        } catch (UnknownAccountException e) {
            //return "用户名不存在！";
            return "index";
        } catch (AuthenticationException e) {
            //return "账号或密码错误！";
            return "index";
        } catch (AuthorizationException e) {
            //return "没有权限";
            return "index";
        }
        return "home";
    }

    @GetMapping("/home")
    public String home(){
        return "/home";
    }

}
