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

    Integer queryMoneyById(Integer id);

    /**
     * 计入一笔交易，同时写入订单
     *
     * @return
     */
    void updateMoney(int userId, int taskId);
}
