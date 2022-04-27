package com.xz.helpful;

import com.xz.helpful.pojo.User;
import com.xz.helpful.pojo.vo.BaseVo;
import com.xz.helpful.pojo.vo.RegisterVo;
import com.xz.helpful.service.UserServer;
import com.xz.helpful.service.WalletServer;
import com.xz.helpful.utils.RandomUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class HelpfulApplicationTests {


    @Autowired
    WalletServer walletServer;
    @Test
    void contextLoads() {
        walletServer.initWallet(2);
    }

}
