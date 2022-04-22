package com.xz.helpful.service.impl;

import com.xz.helpful.dao.UserMapper;
import com.xz.helpful.dao.WalletMapper;
import com.xz.helpful.pojo.User;
import com.xz.helpful.pojo.vo.BaseVo;
import com.xz.helpful.pojo.vo.RegisterVo;
import com.xz.helpful.pojo.vo.UserVo;
import com.xz.helpful.service.CaptchaService;
import com.xz.helpful.service.UserServer;
import com.xz.helpful.utils.RandomUtil;
import com.xz.helpful.utils.RedisUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * @Author: xz
 * @Date: 2022/4/20
 */
@Service
public class UserServerImpl implements UserServer {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WalletMapper walletMapper;
    @Autowired
    private CaptchaService captchaService;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public List<User> findAll() {
        return userMapper.findAll();
    }

    @Override
    public boolean verify(User user) {
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
    public User findByEmail(String email) {
        return userMapper.findByEmail(email);
    }

    @Override
    public UserVo findByEmailInfo(String email) {
        User login = userMapper.findByEmail(email);
        if (login == null) {
            return null;
        }
        Integer wallet = walletMapper.queryWallet(login.getId());
        if (wallet == null) {
            wallet = 0;
        }
        UserVo vo = new UserVo();
        BeanUtils.copyProperties(login, vo);
        vo.setWallet(wallet);
        return vo;
    }

    @Override
    public BaseVo verify(RegisterVo registerVo) {
        boolean verify = verify(registerVo.getUser());
        if (!verify) {
            return BaseVo.failed("邮箱或密码不符合规范");
        }
        if (ObjectUtils.isEmpty(registerVo.getToken()) || ObjectUtils.isEmpty(registerVo.getVerifyCode())) {
            return BaseVo.failed("验证码不符合规范");
        }
        boolean isOk = captchaService.versifyCaptcha(registerVo.getToken(), registerVo.getVerifyCode());
        if (!isOk) {
            return BaseVo.failed("验证码校验失败请重试");
        }
        User byEmail = findByEmail(registerVo.getUser().getEmail());
        if (byEmail != null) {
            return BaseVo.failed("邮箱已注册！");
        }

        //查询是否近期发送过验证码
        boolean has = redisUtil.hasKey(registerVo.getUser().getEmail());
        if (has) {
            return BaseVo.success(0);
        }

        //TODO 发送邮件验证码。。。
        String code = RandomUtil.getRandomNum(4);
        registerVo.setVerifyCode(code);//移除原先人机验证码，替换成邮箱验证码供后面使用
        //把待注册的信息写入redis，email作为key,绑定session请求
        redisUtil.set(registerVo.getUser().getEmail(), registerVo);
        //邮箱验证码与5分钟后过期，1分钟内不可重发
        redisUtil.expire(registerVo.getUser().getEmail(), 60 * 5);
        return BaseVo.success(60);
    }

    @Override
    public BaseVo register(String email, String code, String session) {
        //校验邮件验证码
        if (!redisUtil.hasKey(email)) {
            return BaseVo.failed("邮件验证码已过期，请重新发送");
        }
        RegisterVo registerVo = (RegisterVo) redisUtil.get(email);
        if (!session.equalsIgnoreCase(registerVo.getSession())) {
            return BaseVo.failed("非法请求");
        }
        if (!code.equalsIgnoreCase(registerVo.getVerifyCode())) {
            return BaseVo.failed("验证码错误");
        }
        //验证完成后删除redis
        redisUtil.del(email);

        registerVo.getUser().setName("互助侠" + RandomUtil.getRandomNum(6));
        registerVo.getUser().setMycode(RandomUtil.getRandomNum(6));
        try {
            userMapper.save(registerVo.getUser());
        } catch (Exception e) {
            return BaseVo.failed("邮箱已注册！", 2);
        }
        return BaseVo.success(registerVo.getUser().getPasswd());
    }
}
