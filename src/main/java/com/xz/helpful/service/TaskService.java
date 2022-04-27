package com.xz.helpful.service;

import com.xz.helpful.pojo.Task;

public interface TaskService {
    Task findById(Integer id);

    /**
     * 设置任务启用状态
     * @param id 任务id
     * @param on true 开启， false 关闭
     */
    void updateTaskEnable(Integer id, Boolean on);
}
