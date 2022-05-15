package com.xz.helpful.service.impl;

import com.xz.helpful.dao.OrderMapper;
import com.xz.helpful.pojo.Order;
import com.xz.helpful.pojo.vo.OrderVo;
import com.xz.helpful.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;

    @Override
    public List<OrderVo> findAll(Integer userId) {
        return orderMapper.findAll(userId);
    }

    @Override
    public Integer addOrder(int userId, int taskId) {
        return orderMapper.addOrder(userId,taskId);
    }
}
