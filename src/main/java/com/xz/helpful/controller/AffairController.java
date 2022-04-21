package com.xz.helpful.controller;

import com.xz.helpful.pojo.Notice;
import com.xz.helpful.pojo.vo.BaseVo;
import com.xz.helpful.service.NoticeServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: xz
 * @Date: 2022/4/21
 */
@RestController
@RequestMapping("/event")
public class AffairController {
    @Autowired
    private NoticeServer noticeServer;

    @GetMapping("/notice")
    public Object getNotice() {
        List<Notice> notices = noticeServer.getNotices();
        if (notices == null || notices.size() == 0) {
            return BaseVo.failed(null, "无公告", -1);
        }
        return BaseVo.success(noticeServer.getNotices());
    }
}
