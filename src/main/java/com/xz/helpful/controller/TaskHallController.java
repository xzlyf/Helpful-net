package com.xz.helpful.controller;

import com.xz.helpful.pojo.vo.BaseVo;
import com.xz.helpful.service.TaskService;
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


}
