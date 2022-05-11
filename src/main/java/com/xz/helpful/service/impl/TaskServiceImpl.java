package com.xz.helpful.service.impl;

import com.xz.helpful.dao.TaskFilterMapper;
import com.xz.helpful.dao.TaskMapper;
import com.xz.helpful.global.RedisKey;
import com.xz.helpful.pojo.Task;
import com.xz.helpful.service.TaskService;
import com.xz.helpful.service.WalletServer;
import com.xz.helpful.utils.ConvertUtil;
import com.xz.helpful.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private TaskFilterMapper filterMapper;
    @Autowired
    private WalletServer walletServer;
    //事务管理器
    @Autowired
    private DataSourceTransactionManager dataSourceTransactionManager;

    @Override
    public List<Task> findAll() {
        return taskMapper.findAll();
    }

    @Override
    public Task findById(Integer id) {
        return taskMapper.findById(id);
    }

    @Override
    public Task getOne(String email) {
        //以用户email的hashcode查询redis，是否存缓存。没有重新拉去数据库，并缓存到redis供下次使用
        String userKey = RedisKey.REDIS_TASK_KEY + email;


        /*方案一*/
        List<Task> tasks = ConvertUtil.castList(redisUtil.get(userKey), Task.class);
        if (tasks == null) {
            //redis没有查询到数据，重新去sql拉取n条未执行的任务
            tasks = taskMapper.getNotInFilterTask(email, 10);

        }
        Task target = null;
        if (tasks.size() > 0) {
            //移出第一个任务，其余的保存在redis
            target = tasks.remove(0);
            redisUtil.set(userKey, tasks,300);
        }
        //如果等于tasks等于0直接删除redis
        if (tasks.size() == 0) {
            redisUtil.del(userKey);
        }

       /* 方案二
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
        */

        return target;
    }

    /**
     * 完成一个任务并获取奖励
     * 开启事务管理，失败回滚
     *
     */
    @Override
    @Transactional
    public void finishOne(String email,Integer userId, Integer taskId) throws RuntimeException{
            //写入filter任务过滤表
            filterMapper.insert(email,taskId);
            //用户积分增加
            walletServer.updateMoney(userId,taskId);
            //扣去发布者的积分

    }

    @Override
    public void updateTaskEnable(Integer id, Boolean on) {
        taskMapper.updateTaskEnable(id, on ? 1 : 0);
    }
}
