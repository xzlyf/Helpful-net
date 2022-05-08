package com.xz.helpful.controller;

import com.xz.helpful.global.RedisKey;
import com.xz.helpful.pojo.Task;
import com.xz.helpful.pojo.vo.BaseVo;
import com.xz.helpful.service.TaskService;
import com.xz.helpful.service.UserServer;
import com.xz.helpful.utils.RedisUtil;
import com.xz.helpful.utils.UUIDUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

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
    @Autowired
    private UUIDUtil uuidUtil;

    @RequestMapping("/get")
    public ModelAndView getRandom() {
        ModelAndView modelAndView = new ModelAndView();
        Subject subject = SecurityUtils.getSubject();
        String email = subject.getPrincipal().toString();
        if (email == null) {
            modelAndView.setViewName("view/task-error");
            modelAndView.addObject("msg", "未登录");
            return modelAndView;
        }
        //开始取任务
        Task one = taskService.getOne(email);
        //使用view页面返回html
        modelAndView.setViewName("view/task");
        modelAndView.addObject("task", one);
        return modelAndView;
    }

    /**
     * 记录任务开始，任务结束返回随机码，通过该随机码来判断完成任务
     */
    @ResponseBody
    @GetMapping("/startTask")
    public Object startTask(HttpSession session,String taskId) throws InterruptedException {
        Thread.sleep(15000);
        String r = uuidUtil.getUUID32();
        //todo 校验任务
        //做校验用
        //session.setAttribute("taskId",taskId);
        //session.setAttribute("r",r);
        return BaseVo.success(r);
    }

    /**
     * 校验任务
     * @param r startTask返回的随机值
     */
    @ResponseBody
    @GetMapping("/doneTask")
    public Object doneTask(String r){
        return null;
    }

}
