package com.xz.helpful.service.impl;

import com.xz.helpful.dao.OrderMapper;
import com.xz.helpful.pojo.Task;
import com.xz.helpful.pojo.vo.OrderVo;
import com.xz.helpful.service.OrderService;
import com.xz.helpful.service.UserServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserServer userServer;

    @Override
    public List<OrderVo> findAll(Integer userId) {
        return findAll(userId, null);
    }

    @Override
    public List<OrderVo> findAll(Integer userId, String mode) {
        //使用mybatis if标签判断任务完成还是别人完成
        return orderMapper.findAll(userId, mode);
    }

    @Override
    public void addOrder(int doneUser, int fromUser, Task task) {
        //查询用户名称
        String tUser = userServer.findUserNameById(doneUser);
        String fUser = userServer.findUserNameById(fromUser);
        //任务精简描述
        task.setTaskDesc(simpleDesc(task.getTaskDesc()));
        orderMapper.addOrder(doneUser, fromUser, tUser, fUser, task);
    }

    /**
     * 截取desc描述文本
     */
    private String simpleDesc(String st) {
        return st.length() <= 8 ? st : st.substring(0, 8);
    }
}
