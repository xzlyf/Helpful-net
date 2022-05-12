package com.xz.helpful.service;

/**
 * @Author: xz
 * @Date: 2022/4/22
 */
public interface WalletServer {

    /**
     * 钱包初始化
     */
    void initWallet(Integer userId);

    /**
     * 查询积分
     *
     * @return 积分
     */
    Integer queryMoneyByUserId(Integer userId);


    Integer updateMoneyByUserId(Integer userId, Integer money);

}
