package com.xz.helpful.service;

import com.xz.helpful.pojo.Order;
import com.xz.helpful.pojo.vo.OrderVo;

import java.util.List;

public interface OrderService {
    List<OrderVo> findAll(Integer userId);

    Integer addOrder(int userId,int taskId);
}
