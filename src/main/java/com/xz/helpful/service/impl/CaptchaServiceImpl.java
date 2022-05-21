package com.xz.helpful.service.impl;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.xz.helpful.global.RedisKey;
import com.xz.helpful.service.CaptchaService;
import com.xz.helpful.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: xz
 * @Date: 2022/4/22
 */
@Slf4j
@Service
public class CaptchaServiceImpl implements CaptchaService {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private DefaultKaptcha producer;
    //session过期时间 60秒
    private final Integer TIMEOUT = 60;


    //生成captcha验证码
    @Override
    public Map<String, Object> captchaCreator(HttpSession session) throws IOException {
        //生成文字验证码
        String text = producer.createText();
        //生成文字对应的图片验证码
        BufferedImage image = producer.createImage(text);
        //将图片写出
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", outputStream);
        //对写出的字节数组进行Base64编码 ==> 用于传递8比特字节码
        BASE64Encoder encoder = new BASE64Encoder();
        //返回数据
        Map<String, Object> data = new HashMap<>();
        data.put("img", encoder.encode(outputStream.toByteArray()));
        data.put("expire", TIMEOUT);
        //存入redis
        if (redisUtil.hasKey(getRobotKey(session.getId()))) {
            redisUtil.del(getRobotKey(session.getId()));
        }
        redisUtil.set(getRobotKey(session.getId()), text, TIMEOUT);
        return data;
    }

    //验证输入的验证码是否正确
    @Override
    public boolean versifyCaptcha(HttpSession session, String inputCode) {
        //根据前端传回的token在redis中找对应的value
        if (redisUtil.hasKey(getRobotKey(session.getId()))) {
            //验证通过, 删除对应的key
            if (redisUtil.get(getRobotKey(session.getId())).equals(inputCode)) {
                redisUtil.del(getRobotKey(session.getId()));
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public void sendEmailCaptcha(HttpSession session, String email) throws RuntimeException {
        //查询是否近期发送过验证码
        boolean has = redisUtil.hasKey(getEmailKey(email));
        if (has) {
            throw new RuntimeException("近期发送过邮件，请稍后再来！");
        }
        //todo 发送邮件
        String text = producer.createText();
        redisUtil.hset(getEmailKey(email),"code",text);
        redisUtil.hset(getEmailKey(email),"session",session.getId());//session绑定请求
        redisUtil.expire(getEmailKey(email),300);//5分钟过期时间
        log.info("邮箱：" + email + "\t验证码：" + text);//todo debug

    }

    @Override
    public void verifyEmailCaptcha(HttpSession session, String email,String inputCode) throws RuntimeException{
        //校验邮件验证码
        if (!redisUtil.hasKey(getEmailKey(email))) {
            throw new RuntimeException("邮件验证码已过期，请重新发送");
        }
        String lastSession = (String)redisUtil.hget(getEmailKey(email),"session");
        if (!session.getId().equals(lastSession)){
            throw new RuntimeException("非法请求");//session与发送邮箱的不一致
        }
        String remoteCode = (String) redisUtil.hget(getEmailKey(email),"code");
        if(!remoteCode.equals(inputCode)){
            throw new RuntimeException("验证码错误");
        }
        redisUtil.del(getEmailKey(email));
    }

    /**
     * 返回redis的key
     *
     * @param k 后缀
     */
    private String getRobotKey(String k) {
        return RedisKey.REDIS_VERIFY_ROBOT + k;
    }

    /**
     * 返回redis的key
     *
     * @param k 后缀
     */
    private String getEmailKey(String k) {
        return RedisKey.REDIS_VERIFY_EMAIL + k;
    }
}
