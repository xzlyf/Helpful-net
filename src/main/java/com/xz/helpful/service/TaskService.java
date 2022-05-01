package com.xz.helpful.service;

import com.xz.helpful.pojo.Task;

import java.util.List;

public interface TaskService {
    List<Task> findAll();

    /**
     * 根据任务id查询任务
     */
    Task findById(Integer id);

    /**
     * 随机获取一个任务
     * @param userId 用户id
     */
    Task getOne(Integer userId);

    /**
     * 完成一个任务
     */
    void finishOne(Integer userId,Integer taskId);

    /**
     * 设置任务启用状态
     * @param id 任务id
     * @param on true 开启， false 关闭
     */
    void updateTaskEnable(Integer id, Boolean on);
}
