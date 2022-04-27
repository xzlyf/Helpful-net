package com.xz.helpful.dao;

import org.springframework.stereotype.Repository;

@Repository
public interface OrderMapper {
    Integer addOrder(Integer userId, Integer taskId);
}
