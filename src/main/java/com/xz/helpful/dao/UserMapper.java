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

    User findByEmail(String email);

    Integer findUserIdByEmail(String email);

    /**
     * 新建用户
     *
     * @param user 用户信息
     * @return 主键id
     */
    Integer save(User user);

}
