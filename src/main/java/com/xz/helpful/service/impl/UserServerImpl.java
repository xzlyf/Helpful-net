package com.xz.helpful.service.impl;

import com.xz.helpful.dao.UserMapper;
import com.xz.helpful.dao.WalletMapper;
import com.xz.helpful.pojo.User;
import com.xz.helpful.pojo.vo.UserVo;
import com.xz.helpful.service.UserServer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public List<User> findAll() {
        return userMapper.findAll();
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
        if (wallet==null){
            wallet = 0;
        }
        UserVo vo = new UserVo();
        BeanUtils.copyProperties(login, vo);
        vo.setWallet(wallet);
        return vo;
    }
}
