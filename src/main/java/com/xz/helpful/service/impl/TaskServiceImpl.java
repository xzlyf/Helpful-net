package com.xz.helpful.service.impl;

import com.xz.helpful.dao.TaskFilterMapper;
import com.xz.helpful.dao.TaskMapper;
import com.xz.helpful.global.RedisKey;
import com.xz.helpful.pojo.BiliMovie;
import com.xz.helpful.pojo.Task;
import com.xz.helpful.service.OrderService;
import com.xz.helpful.service.TaskService;
import com.xz.helpful.service.WalletServer;
import com.xz.helpful.utils.ConvertUtil;
import com.xz.helpful.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Autowired
    private OrderService orderService;

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
            redisUtil.set(userKey, tasks, 300);
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
     */
    @Override
    @Transactional
    public void finishOne(String email, Integer userId, Integer taskId) throws RuntimeException {
        Task task = findById(taskId);
        if (task == null) {
            //抛出异常，事务回滚
            throw new RuntimeException("任务不存在");
        }
        //写入filter任务过滤表
        filterMapper.insert(email, task.getId());
        //写入订单
        orderService.addOrder(userId, task.getId());
        //更新用户余额
        walletServer.updateMoneyByUserId(userId, +task.getTaskPay());
        //更新任务创建者的余额
        walletServer.updateMoneyByUserId(task.getTaskFrom(), -task.getTaskPay());
        Integer taskFromWallet = walletServer.queryMoneyByUserId(task.getTaskFrom());
        if (taskFromWallet <= 0) {
            //创建者余额耗尽，任务关闭
            updateTaskEnable(task.getId(), false);
        }

    }

    @Override
    public void updateTaskEnable(Integer id, Boolean on) {
        taskMapper.updateTaskEnable(id, on ? 1 : 0);
    }

    @Override
    public void createOne(Integer userId, BiliMovie biliMovie) throws Exception {
        Task task = new Task();
        task.setTaskType(2);
        task.setTaskPay(1);
        task.setTaskFrom(userId);
        task.setIsEnable(1);
        task.setIsHidden(0);
        task.setTaskDesc(biliMovie.getTitle());
        //https://href.li/?https://www.bilibili.com/video/BV1fr4y1W79k
        String url = "https://href.li/?https://www.bilibili.com/video/" + biliMovie.getBvid();
        task.setTaskUrl(url);
        task.setTaskMid(biliMovie.getBvid());
        task.setTaskImg(biliMovie.getPic());
        taskMapper.insert(task);
    }
}
