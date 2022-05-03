package com.xz.helpful.controller;

import com.xz.helpful.global.RedisKey;
import com.xz.helpful.pojo.Task;
import com.xz.helpful.pojo.vo.BaseVo;
import com.xz.helpful.service.TaskService;
import com.xz.helpful.service.UserServer;
import com.xz.helpful.utils.RedisUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 任务大厅接口
 */
@RestController
@RequestMapping("/taskhall")
public class TaskHallController {
    @Autowired
    private TaskService taskService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private UserServer userServer;

    @RequestMapping("/get")
    public Object getRandom() {
        Subject subject = SecurityUtils.getSubject();
        String email = subject.getPrincipal().toString();
        if (email == null) {
            return BaseVo.failed("未登录", 5);
        }
        //1、从redis根据email取出userID
        Integer userId = (Integer) redisUtil.hget(RedisKey.REDIS_USER_ID, email);
        //2、没有取到让用户重新登录(或取数据库)
        if (userId == null) {
            userId = userServer.findUserIdByEmail(email);
            if (userId == null) {
                return BaseVo.failed("非法请求");
            }
        }

        //3.开始取任务
        Task one = taskService.getOne(userId);
        return BaseVo.success(one);
    }

}
