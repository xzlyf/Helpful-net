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

    Integer updateMoney(int userId, int money);
}
