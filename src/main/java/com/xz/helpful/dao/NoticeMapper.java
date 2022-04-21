package com.xz.helpful.dao;

import com.xz.helpful.pojo.Notice;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: xz
 * @Date: 2022/4/21
 */
@Repository
public interface NoticeMapper {
    List<Notice> findAll();
}
