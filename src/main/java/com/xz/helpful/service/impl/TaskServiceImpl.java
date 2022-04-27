package com.xz.helpful.service.impl;

import com.xz.helpful.dao.TaskMapper;
import com.xz.helpful.pojo.Task;
import com.xz.helpful.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    private TaskMapper taskMapper;

    @Override
    public Task findById(Integer id) {
        return taskMapper.findById(id);
    }

    @Override
    public void updateTaskEnable(Integer id, Boolean on) {
        taskMapper.updateTaskEnable(id, on ? 1 : 0);
    }
}
