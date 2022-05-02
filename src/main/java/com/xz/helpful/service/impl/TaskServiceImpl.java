package com.xz.helpful.service.impl;

import com.xz.helpful.dao.TaskMapper;
import com.xz.helpful.pojo.Task;
import com.xz.helpful.service.TaskService;
import com.xz.helpful.utils.ConvertUtil;
import com.xz.helpful.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private RedisUtil redisUtil;

    public static final String REDIS_TASK_KEY = "task_list";

    @Override
    public List<Task> findAll() {
        return taskMapper.findAll();
    }

    @Override
    public Task findById(Integer id) {
        return taskMapper.findById(id);
    }

    @Override
    public Task getOne(Integer userId) {
        //以用户email的hashcode查询redis，是否存缓存。没有重新拉去数据库，并缓存到redis供下次使用
        String userKey = String.valueOf(userId);
        //List<Task> tasks = ConvertUtil.castList(redisUtil.get(userKey), Task.class);
        //if (tasks == null) {
        //    //redis没有查询到数据，重新去sql拉取n条未执行的任务
        //    tasks = taskMapper.getNotInFilterTask(25, 10);
        //    //并存入redis
        //    redisUtil.set(userKey, tasks);
        //}
        ////刷新存活时间5分钟
        //redisUtil.expire(userKey, 300);

        List<Task> tasks = ConvertUtil.castList(redisUtil.hget(REDIS_TASK_KEY, userKey), Task.class);
        if (tasks == null) {
            //缓存空，查询数据库，保存至缓存，重新去sql拉取n条未执行的任务
            tasks = taskMapper.getNotInFilterTask(userId, 2);
        }
        //取出一条数据,并更新redis
        Task target = null;
        if (tasks.size() > 0) {
            target = tasks.remove(0);
            redisUtil.hset(REDIS_TASK_KEY, userKey, tasks);
            //刷新存活时间5分钟
            redisUtil.expire(REDIS_TASK_KEY, 300);
        }
        //清理redis
        if (tasks.size() == 0) {
            redisUtil.hdel(REDIS_TASK_KEY, userKey);
        }
        return target;
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
