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
    public Integer queryWallet(Integer userId) {
        return walletMapper.queryWallet(userId);
    }

    @Override
    public Integer newWallet() {
        //todo 库存问题 锁
        return null;
    }
}
