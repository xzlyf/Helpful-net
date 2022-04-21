package com.xz.helpful.controller;

import com.xz.helpful.pojo.User;
import com.xz.helpful.pojo.vo.BaseVo;
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
