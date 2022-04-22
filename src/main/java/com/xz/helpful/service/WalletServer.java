package com.xz.helpful.service;

/**
 * @Author: xz
 * @Date: 2022/4/22
 */
public interface WalletServer {
    /**
     * 查询积分
     * @return 积分
     */
    Integer queryWallet(Integer userId);

    /**
     * 计入一笔账单
     * @return
     */
    Integer newWallet();
}
