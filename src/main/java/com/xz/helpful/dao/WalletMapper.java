package com.xz.helpful.dao;

import org.springframework.stereotype.Repository;

/**
 * @Author: xz
 * @Date: 2022/4/20
 */
@Repository
public interface WalletMapper {
    void insertWallet(Integer userId, Integer money);

    Integer queryMoneyByUserId(Integer userId);

    Integer queryMoneyById(Integer id);

    /**
     * 增加money，返回更新后的值
     */
    Integer addMoney(int userId, int money);

    /**
     * 减去money，返回更新后的值
     */
    Integer subMoney(int userId, int money);
}
