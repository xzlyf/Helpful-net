package com.xz.helpful.service;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

public interface CaptchaService {

    //生成captcha验证码
    Map<String, Object> captchaCreator(HttpSession session) throws IOException;

    //验证输入的验证码是否正确
    boolean versifyCaptcha(HttpSession session, String inputCode);

    //发送邮箱验证码
    void sendEmailCaptcha(HttpSession session, String email) throws RuntimeException;

    //邮箱验证码验证,没有异常就验证成功
    void verifyEmailCaptcha(HttpSession session,String email,String inputCode) throws RuntimeException;
}