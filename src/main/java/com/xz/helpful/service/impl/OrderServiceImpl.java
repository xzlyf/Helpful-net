package com.xz.helpful.service.impl;

import com.xz.helpful.dao.OrderMapper;
import com.xz.helpful.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Override
    public Integer addOrder(int userId, int taskId) {
        return orderMapper.addOrder(userId,taskId);
    }
}
