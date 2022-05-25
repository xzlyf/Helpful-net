package com.xz.helpful.service.impl;

import com.xz.helpful.dao.UserMapper;
import com.xz.helpful.global.RedisKey;
import com.xz.helpful.pojo.User;
import com.xz.helpful.pojo.vo.BaseVo;
import com.xz.helpful.pojo.vo.RegisterVo;
import com.xz.helpful.pojo.vo.UserVo;
import com.xz.helpful.service.CaptchaService;
import com.xz.helpful.service.UserServer;
import com.xz.helpful.service.WalletServer;
import com.xz.helpful.utils.RandomUtil;
import com.xz.helpful.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * @Author: xz
 * @Date: 2022/4/20
 */
@Slf4j
@Service
public class UserServerImpl implements UserServer {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WalletServer walletServer;
    @Autowired
    private CaptchaService captchaService;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public List<User> findAll() {
        return userMapper.findAll();
    }

    @Override
    public boolean norm(User user) {
        if (user == null) {
            return false;
        }
        if (!user.getEmail().matches("[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?")) {
            return false;
        }
        if (user.getPasswd() == null) {
            return false;
        }
        return user.getPasswd().length() >= 6;
    }

    @Override
    public String getUserPassword(String email) {
        return userMapper.getPassword(email);
    }


    @Override
    public UserVo findInfoByEmail(String email) {
        return userMapper.findByEmail(email);
    }

    @Override
    public Integer findUserIdByEmail(String email) {
        return userMapper.findUserIdByEmail(email);
    }

    @Override
    public String findUserNameByEmail(@NonNull String email) {
        String name = (String) redisUtil.get(RedisKey.REDIS_USER_INFO + email);
        if (name == null) {
            name = userMapper.findUserNameByEmail(email);
            redisUtil.set(RedisKey.REDIS_USER_INFO + email, name, 60 * 60 * 24);
        }
        return name;
    }

    @Override
    public String findUserNameById(Integer id) {
        return userMapper.findUserNameById(id);
    }

    @Override
    public BaseVo verify(HttpSession session, RegisterVo registerVo) {
        boolean verify = norm(registerVo.getUser());
        if (!verify) {
            return BaseVo.failed("邮箱或密码不符合规范");
        }
        if (ObjectUtils.isEmpty(registerVo.getVerifyCode())) {
            return BaseVo.failed("验证码不符合规范");
        }
        boolean isOk = captchaService.versifyCaptcha(session, registerVo.getVerifyCode());
        if (!isOk) {
            return BaseVo.failed("验证码校验失败请重试");
        }
        Integer userId = findUserIdByEmail(registerVo.getUser().getEmail());
        if (userId != null) {
            return BaseVo.failed("邮箱已注册！");
        }
        //发送邮箱验证码
        try {
            captchaService.sendEmailCaptcha(session, registerVo.getUser().getEmail());
        } catch (Exception e) {
            e.printStackTrace();
            return BaseVo.failed("异常：" + e.getMessage());
        }
        //把用户数据存入redis,待用户通过验证拿来写入数据库
        redisUtil.set(RedisKey.REDIS_USER_TEMP + registerVo.getUser().getEmail(), registerVo.getUser(), 60 * 5);
        //返回验证码可再次刷新的倒计时
        return BaseVo.success(60);
    }

    /**
     * 注册接口用，再次发送邮箱验证码
     */
    @Override
    public void sendEmailAgain(HttpSession session, String email) throws Exception {
        //判断用户的注册信息还在不在缓存
        boolean b = redisUtil.hasKey(RedisKey.REDIS_USER_TEMP + email);
        if (!b) {
            throw new RuntimeException("注册信息已过期，请刷新网页重试！");
        }
        //判断距离上次发送时间的间隔
        long expire = redisUtil.getExpire(RedisKey.REDIS_USER_TEMP + email);
        long sub = 300 - expire;
        if (sub <= 60) {
            throw new RuntimeException("发送验证码频繁，请稍后重试！");
        }
        //发送邮箱验证码
        captchaService.sendEmailCaptcha(session, email);
    }

    @Override
    @Transactional
    public User register(String email, String code, HttpSession session) throws Exception {
        //校验邮件验证码，校验失败抛出异常
        captchaService.verifyEmailCaptcha(session, email, code);
        //从缓存拿到用户信息
        User user = (User) redisUtil.get(RedisKey.REDIS_USER_TEMP + email);

        //用户基本信息生成
        user.setName("互助侠" + RandomUtil.getRandomNum(6));
        user.setMycode(RandomUtil.getRandomNum(6));
        try {
            userMapper.save(user);
        } catch (Exception e) {
            throw new IOException("邮箱已注册");
        }
        try {
            //此时以及获取到主键
            walletServer.initWallet(user.getId());
        } catch (Exception e) {
            throw new IOException("钱包初始化失败，事务回滚");
        }
        return user;
    }

    /**
     * 重置密码接口用，与注册接口的不一样
     * 发送邮件验证码
     * @param email   目标邮箱
     * @throws RuntimeException 无异常操作成功
     */
    @Override
    public void resetVerify(HttpSession session, String email) throws RuntimeException {
        //调用邮件发送服务
        captchaService.sendEmailCaptcha(session,email);
    }

    @Override
    public void resetPasswd(HttpSession session, String email, String code, String newPwd) throws RuntimeException {
        if (newPwd.length() < 6) {
            throw new RuntimeException("密码长度不能小于6位");
        }
        //校验邮件验证码
        captchaService.verifyEmailCaptcha(session, email, code);
        //修改密码
        userMapper.updateUserPasswd(email, newPwd);
    }


}
