package com.xz.helpful.dao;

import com.xz.helpful.pojo.Task;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskMapper {
    Task findById(Integer id);

    List<Task> findAll();

    /*随机取出一条数据*/
    Task getOne();

    /*随机取出n条数据*/
    List<Task> getMore(Integer size);

    /*随机取出n条数据，并且通过filter.task_id过滤已执行的任务，只返回n条未执行的任务*/
    List<Task> getNotInFilterTask(Integer userId,Integer size);

    void updateTaskEnable(Integer id, Integer enable);
}
