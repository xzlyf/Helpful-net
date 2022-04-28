package com.xz.helpful.dao;

import com.xz.helpful.pojo.Task;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskMapper {
    Task findById(Integer id);

    void updateTaskEnable(Integer id, Integer enable);

    List<Task> findAll();
}
