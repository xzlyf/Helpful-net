package com.xz.helpful.dao;

import com.xz.helpful.pojo.Task;
import com.xz.helpful.pojo.vo.OrderVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderMapper {
    List<OrderVo> findAll(Integer userId,String mode);

    Integer addOrder(int doneUser, int fromUser, String tUser, String fUser, Task task);
}
