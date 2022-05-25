package com.xz.helpful.service;

import com.xz.helpful.pojo.Task;
import com.xz.helpful.pojo.vo.OrderVo;

import java.util.List;

public interface OrderService {
    List<OrderVo> findAll(Integer userId);

    List<OrderVo> findAll(Integer userId, String mode);


    void addOrder(int doneUser, int fromUser, Task task);
}