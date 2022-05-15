package com.xz.helpful.dao;

import com.xz.helpful.pojo.vo.OrderVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderMapper {
    List<OrderVo> findAll(Integer userId);

    Integer addOrder(Integer userId, Integer taskId,String payType);
}
