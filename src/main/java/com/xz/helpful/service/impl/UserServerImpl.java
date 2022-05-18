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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * @Author: xz
 * @Date: 2022/4/20
 */
@Service
public class UserServerImpl implements UserServer {
    private static final Logger log = LoggerFactory.getLogger(UserServerImpl.class);
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
    public BaseVo verify(HttpSession session, RegisterVo registerVo) {
        boolean verify = verify(registerVo.getUser());
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

        //查询是否近期发送过验证码
        boolean has = redisUtil.hasKey(getRedisKey(registerVo.getUser().getEmail()));
        if (has) {
            return BaseVo.success(0);
        }


        //TODO 发送邮件验证码。。。
        String code = RandomUtil.getRandomNum(4);
        log.info("邮箱：" + registerVo.getUser().getEmail() + "\t验证码：" + code);//todo debug
        registerVo.setVerifyCode(code);//移除原先人机验证码，替换成邮箱验证码供后面使用
        //存入session，校验邮件验证码时会用到
        registerVo.setSession(session.getId());
        //把待注册的信息写入redis，email作为key,绑定session请求 , 邮箱验证码与5分钟后过期，1分钟内不可重发
        redisUtil.set(getRedisKey(registerVo.getUser().getEmail()), registerVo, 60 * 5);
        return BaseVo.success(60);
    }

    @Override
    @Transactional
    public User register(String email, String code, String session) throws IOException {
        //校验邮件验证码
        if (!redisUtil.hasKey(getRedisKey(email))) {
            throw new IOException("邮件验证码已过期，请重新发送");
        }
        RegisterVo registerVo = (RegisterVo) redisUtil.get(getRedisKey(email));
        if (!session.equalsIgnoreCase(registerVo.getSession())) {
            throw new IOException("非法请求");//session不一致
        }
        if (!code.equalsIgnoreCase(registerVo.getVerifyCode())) {
            throw new IOException("验证码错误");
        }
        //验证完成后删除redis
        redisUtil.del(getRedisKey(email));

        registerVo.getUser().setName("互助侠" + RandomUtil.getRandomNum(6));
        registerVo.getUser().setMycode(RandomUtil.getRandomNum(6));
        try {
            userMapper.save(registerVo.getUser());
        } catch (Exception e) {
            throw new IOException("邮箱已注册");
        }
        try {
            //主键已获取registerVo.getUser().getId()
            walletServer.initWallet(registerVo.getUser().getId());
        } catch (Exception e) {
            //手动事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return registerVo.getUser();
    }

    /**
     * 返回redis的key
     *
     * @param k 后缀
     */
    private String getRedisKey(String k) {
        return RedisKey.REDIS_VERIFY_email + k;
    }
}
