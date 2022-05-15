package com.xz.helpful.dao;

import com.xz.helpful.pojo.BiliMovie;
import com.xz.helpful.pojo.Task;
import org.apache.ibatis.annotations.MapKey;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface TaskMapper {
    Task findById(Integer id);

    List<Task> findAll();

    /*随机取出n条数据，并且通过filter.task_id过滤已执行的任务，只返回n条未执行的任务*/
    List<Task> getNotInFilterTask(Integer userId,String email,Integer size);

    void updateTaskEnable(Integer id, Integer enable);

    Integer insert(Task task) throws Exception;
}
