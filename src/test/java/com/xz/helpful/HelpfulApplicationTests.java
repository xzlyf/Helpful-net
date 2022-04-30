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

//@SpringBootTest
class HelpfulApplicationTests {


    @Autowired
    WalletServer walletServer;
    @Test
    void contextLoads() {
        walletServer.initWallet(2);
    }

    @Test
    void textMain(){
        String st = "11@qq.com";
        String st2 = "22@qq.com";
        System.out.println(st.hashCode());
        System.out.println(st2.hashCode());
    }

}
