package com.xz.helpful.dao;

import com.xz.helpful.pojo.Task;
import com.xz.helpful.pojo.vo.TaskVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskMapper {
    Task findById(Integer id);

    List<TaskVo> findAll(Integer userId);

    /*随机取出n条数据，并且通过filter.task_id过滤已执行的任务，只返回n条未执行的任务*/
    List<TaskVo> getNotInFilterTask(Integer userId, String email, Integer size);

    void updateTaskEnable(Integer id, Integer enable);

    Integer insert(Task task) throws Exception;

    Integer delete(Integer userId, Integer taskId);
}
