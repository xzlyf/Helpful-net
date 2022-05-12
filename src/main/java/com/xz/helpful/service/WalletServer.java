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


    /**
     * 在原值上加减，不用先查积分
     * @param userId userId
     * @param money 整数加，负数减
     */
    void updateMoneyByUserId(Integer userId, Integer money);

}
