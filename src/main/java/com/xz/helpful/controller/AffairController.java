package com.xz.helpful.controller;

import com.xz.helpful.pojo.Notice;
import com.xz.helpful.pojo.vo.BaseVo;
import com.xz.helpful.pojo.vo.RegisterVo;
import com.xz.helpful.service.CaptchaService;
import com.xz.helpful.service.NoticeServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @Author: xz
 * @Date: 2022/4/21
 */
@RestController
@RequestMapping("/event")
public class AffairController {
    @Autowired
    private NoticeServer noticeServer;
    @Autowired
    private CaptchaService captchaService;

    /**
     * 查询公告
     */
    @GetMapping("/notice")
    public Object getNotice() {
        List<Notice> notices = noticeServer.getNotices();
        if (notices == null || notices.size() == 0) {
            return BaseVo.failed(null, "无公告", -1);
        }
        return BaseVo.success(noticeServer.getNotices());
    }


    /**
     * 刷新验证码
     * @return
     * @throws IOException
     */
    @GetMapping("/captcha")
    public Map<String, Object> captcha() throws IOException {
        return captchaService.captchaCreator();
    }

    /**
     * 校验验证码
     */
    @PostMapping("/verify")
    public Object login(String token,String code) {
        boolean isOk = captchaService.versifyCaptcha(token, code);
        if (!isOk) {
            return BaseVo.failed("验证码校验失败请重试");
        }
        return BaseVo.success(null);
    }
}