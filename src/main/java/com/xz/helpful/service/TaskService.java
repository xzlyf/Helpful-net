package com.xz.helpful.service;

import com.xz.helpful.pojo.BiliMovie;
import com.xz.helpful.pojo.Task;
import com.xz.helpful.pojo.vo.TaskVo;

import java.util.List;

public interface TaskService {
    List<TaskVo> findAll(Integer userId);

    /**
     * 根据任务id查询任务
     */
    Task findById(Integer id);

    /**
     * 随机获取一个任务
     * @param email 用户email
     */
    TaskVo getOne(Integer userId, String email);

    /**
     * 完成一个任务
     */
    void finishOne(String email,Integer userId,Integer taskId) throws RuntimeException;

    /**
     * 设置任务启用状态
     * @param id 任务id
     * @param on true 开启， false 关闭
     */
    void updateTaskEnable(Integer id, Boolean on);

    /**
     * 创建一个任务
     */
    void createOne(Integer userId,BiliMovie biliMovie) throws Exception;
}
