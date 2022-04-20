package com.xz.helpful.dao;

import com.xz.helpful.pojo.User;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: xz
 * @Date: 2022/4/20
 */
@Repository
public interface UserMapper {
    List<User> findAll();

    User login(User user);

}
