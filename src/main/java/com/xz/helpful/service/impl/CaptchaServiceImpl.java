package com.xz.helpful.service.impl;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.xz.helpful.service.CaptchaService;
import com.xz.helpful.utils.RedisUtil;
import com.xz.helpful.utils.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
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
    private UUIDUtil uuidUtil;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private DefaultKaptcha producer;
    //session过期时间 60秒
    private final Integer TIMEOUT = 60;

    //UUID为key, 验证码为Value放在Redis中
    @Override
    public Map<String, Object> createToken(String captcha) {
        //生成一个token
        String key = uuidUtil.getUUID32();
        //生成验证码对应的token  以token为key  验证码为value存在redis中
        redisUtil.set(key, captcha);
        //设置验证码过期时间
        redisUtil.expire(key, TIMEOUT);
        Map<String, Object> map = new HashMap<>();
        map.put("token", key);
        map.put("expire", TIMEOUT);
        return map;
    }

    //生成captcha验证码
    @Override
    public Map<String, Object> captchaCreator() throws IOException {
        //todo 解决大量刷新验证码问题，大量验证码占用redis存储，解决方案，和session绑定，刷新销毁上次的验证码
        //生成文字验证码
        String text = producer.createText();
        //生成文字对应的图片验证码
        BufferedImage image = producer.createImage(text);
        //将图片写出
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", outputStream);
        //对写出的字节数组进行Base64编码 ==> 用于传递8比特字节码
        BASE64Encoder encoder = new BASE64Encoder();
        //生成token
        Map<String, Object> token = createToken(text);
        token.put("img", encoder.encode(outputStream.toByteArray()));
        return token;
    }

    //验证输入的验证码是否正确
    @Override
    public boolean versifyCaptcha(String token, String inputCode) {
        //根据前端传回的token在redis中找对应的value
        if (redisUtil.hasKey(token)) {
            //验证通过, 删除对应的key
            if (redisUtil.get(token).equals(inputCode)) {
                redisUtil.del(token);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
