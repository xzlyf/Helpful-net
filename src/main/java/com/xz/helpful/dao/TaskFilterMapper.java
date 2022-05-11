package com.xz.helpful.dao;

import org.springframework.stereotype.Repository;

/**
 * @author XiaoZe
 * @email czr2001@outlook.com
 * @date 2022/5/11 15:00
 */
@Repository
public interface TaskFilterMapper {

    int insert(String email, Integer taskId);
}
