package com.xz.helpful.service;

import com.xz.helpful.pojo.vo.OrderVo;

import java.util.List;

public interface OrderService {
    List<OrderVo> findAll(Integer userId);

    /**
     * @param payType 支付类型  +收入 -支出
     */
    Integer addOrder(int userId,int taskId,String payType);
}
