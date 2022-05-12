package com.xz.helpful.service.impl;

import com.xz.helpful.dao.WalletMapper;
import com.xz.helpful.service.WalletServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: xz
 * @Date: 2022/4/22
 */
@Service
public class WalletServerImpl implements WalletServer {
    @Autowired
    private WalletMapper walletMapper;

    @Override
    @Transactional
    public void initWallet(Integer userId) {
        walletMapper.insertWallet(userId, 20);
    }

    @Override
    public Integer queryMoneyByUserId(Integer userId) {
        return walletMapper.queryMoneyByUserId(userId);
    }

    @Override
    public void updateMoneyByUserId(Integer userId, Integer money) {
        if (money > 0) {
            walletMapper.addMoney(userId, money);
        } else {
            walletMapper.subMoney(userId, Math.abs(money));
        }
    }

}
