package com.xz.helpful;

import com.xz.helpful.pojo.User;
import com.xz.helpful.pojo.vo.UserVo;
import com.xz.helpful.service.UserServer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class HelpfulApplicationTests {

    @Autowired
    UserServer userServer;

    @Test
    void contextLoads() {
        User u = new User();
        u.setEmail("1076409897@qq.com");
        u.setPasswd("123456");
        UserVo login = userServer.login(u);
        System.out.println(login.toString());
    }

}
