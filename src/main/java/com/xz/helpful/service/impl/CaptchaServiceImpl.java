package com.xz.helpful.service.impl;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.xz.helpful.service.CaptchaService;
import com.xz.helpful.utils.RedisUtil;
import com.xz.helpful.utils.UUIDUtil;
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
        data.put("expire",TIMEOUT);
        //存入redis
        if (redisUtil.hasKey(session.getId())) {
            redisUtil.del(session.getId());
        }
        redisUtil.set(session.getId(), text);
        redisUtil.expire(session.getId(), TIMEOUT);
        return data;
    }

    //验证输入的验证码是否正确
    @Override
    public boolean versifyCaptcha(HttpSession session,String inputCode) {
        //根据前端传回的token在redis中找对应的value
        if (redisUtil.hasKey(session.getId())) {
            //验证通过, 删除对应的key
            if (redisUtil.get(session.getId()).equals(inputCode)) {
                redisUtil.del(session.getId());
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
