package com.xz.helpful.dao;

import org.springframework.stereotype.Repository;

/**
 * @Author: xz
 * @Date: 2022/4/20
 */
@Repository
public interface WalletMapper {

    Integer queryWallet(Integer userId);

}
