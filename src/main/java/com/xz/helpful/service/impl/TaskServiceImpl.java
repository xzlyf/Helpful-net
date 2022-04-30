package com.xz.helpful.service.impl;

import com.xz.helpful.dao.TaskMapper;
import com.xz.helpful.pojo.Task;
import com.xz.helpful.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    private TaskMapper taskMapper;

    @Override
    public List<Task> findAll() {
        return taskMapper.findAll();
    }

    @Override
    public Task findById(Integer id) {
        return taskMapper.findById(id);
    }

    @Override
    public Task getOne() {
        //todo 随机获取一任务，任务完成后写入filter表
        return taskMapper.getOne();
    }

    @Override
    public void finishOne(Integer userId, Integer taskId) {
        //写入filter任务过滤表
        //用户积分增加
        //失败回滚
    }

    @Override
    public void updateTaskEnable(Integer id, Boolean on) {
        taskMapper.updateTaskEnable(id, on ? 1 : 0);
    }
}
