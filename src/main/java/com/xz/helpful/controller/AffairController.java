package com.xz.helpful.controller;

import com.xz.helpful.annotation.LimitRequest;
import com.xz.helpful.pojo.Notice;
import com.xz.helpful.pojo.vo.BaseVo;
import com.xz.helpful.service.CaptchaService;
import com.xz.helpful.service.NoticeServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.io.IOException;
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
     */
    @GetMapping("/captcha")
    @LimitRequest(count = 25)
    public Object captcha(HttpSession session) throws IOException {
        return BaseVo.success(captchaService.captchaCreator(session));
    }

    ///**
    // * 校验验证码
    // */
    //@PostMapping("/verify")
    //public Object verify(HttpSession session,String code) {
    //    boolean isOk = captchaService.versifyCaptcha(session, code);
    //    if (!isOk) {
    //        return BaseVo.failed("验证码校验失败请重试");
    //    }
    //    return BaseVo.success(null);
    //}
}