package com.xz.helpful.controller;

import com.xz.helpful.global.RedisKey;
import com.xz.helpful.pojo.Task;
import com.xz.helpful.service.TaskService;
import com.xz.helpful.service.UserServer;
import com.xz.helpful.utils.RedisUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 任务大厅接口
 */
@Controller
@RequestMapping("/taskhall")
public class TaskHallController {
    @Autowired
    private TaskService taskService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private UserServer userServer;

    @RequestMapping("/get")
    public ModelAndView getRandom() {
        ModelAndView modelAndView = new ModelAndView();
        Subject subject = SecurityUtils.getSubject();
        String email = subject.getPrincipal().toString();
        if (email == null) {
            //todo重定向login页面
            modelAndView.setViewName("view/task-error");
            modelAndView.addObject("msg","未登录");
            return modelAndView;
        }
        //1、从redis根据email取出userID
        Integer userId = (Integer) redisUtil.hget(RedisKey.REDIS_USER_ID, email);
        //2、没有取到让用户重新登录(或取数据库)
        if (userId == null) {
            userId = userServer.findUserIdByEmail(email);
            if (userId == null) {
                modelAndView.setViewName("view/task-error");
                modelAndView.addObject("msg","非法请求");
                return modelAndView;
            }
        }

        //3.开始取任务
        Task one = taskService.getOne(userId);
        //4.使用view页面返回html
        modelAndView.setViewName("view/task");
        modelAndView.addObject("task",one);
        return modelAndView;
    }

}
