package com.xz.helpful.service.impl;

import com.xz.helpful.dao.NoticeMapper;
import com.xz.helpful.pojo.Notice;
import com.xz.helpful.service.NoticeServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: xz
 * @Date: 2022/4/21
 */
@Service
public class NoticeServerImpl implements NoticeServer {
    @Autowired
    private NoticeMapper noticeMapper;
    @Override
    public List<Notice> getNotices() {
        return noticeMapper.findAll();
    }
}
