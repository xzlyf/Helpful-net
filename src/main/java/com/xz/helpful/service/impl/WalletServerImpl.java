package com.xz.helpful.service.impl;

import com.xz.helpful.dao.WalletMapper;
import com.xz.helpful.pojo.Task;
import com.xz.helpful.service.OrderService;
import com.xz.helpful.service.TaskService;
import com.xz.helpful.service.WalletServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: xz
 * @Date: 2022/4/22
 */
@Service
public class WalletServerImpl implements WalletServer {
    @Autowired
    private WalletMapper walletMapper;
    @Autowired
    private OrderService orderService;
    @Autowired
    private TaskService taskService;

    @Override
    @Transactional
    public void initWallet(Integer userId) {
        walletMapper.insertWallet(userId, 20);
    }

    @Override
    public Integer queryMoneyByUserId(Integer userId) {
        return walletMapper.queryMoneyByUserId(userId);
    }

    @Override
    public Integer queryMoneyById(Integer id) {
        return walletMapper.queryMoneyById(id);
    }

    /**
     * 更新余额
     *
     * @param userId 用户id
     * @param taskId 任务id
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateMoney(int userId, int taskId) {
        Task task = taskService.findById(taskId);
        if (task == null) {
            //抛出异常，事务回滚
            throw new RuntimeException("任务不存在");
        }
        if (task.getIsEnable() == 0) {
            throw new RuntimeException("任务已关闭");
        }

        //写入订单
        orderService.addOrder(userId, taskId);
        //更新用户余额
        int money = walletMapper.queryMoneyByUserId(userId);
        walletMapper.updateMoney(userId, money + task.getTaskPay());
        //查询用户创建者余额
        int moneyByAuthor = walletMapper.queryMoneyByUserId(task.getTaskFrom());
        //更新任务创建者的余额
        walletMapper.updateMoney(task.getTaskFrom(), moneyByAuthor - task.getTaskPay());
        if (moneyByAuthor - task.getTaskPay() < 0) {
            //创建者余额耗尽，任务关闭
            taskService.updateTaskEnable(task.getId(), false);
        }
    }

}
