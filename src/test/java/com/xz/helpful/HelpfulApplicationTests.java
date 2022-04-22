package com.xz.helpful;

import com.xz.helpful.pojo.User;
import com.xz.helpful.pojo.vo.BaseVo;
import com.xz.helpful.pojo.vo.RegisterVo;
import com.xz.helpful.service.UserServer;
import com.xz.helpful.utils.RandomUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class HelpfulApplicationTests {


    @Autowired
    UserServer userServer;
    @Test
    void contextLoads() {
        RegisterVo registerVo = new RegisterVo();
        User u =new User();
        u.setName("互助侠"+ RandomUtil.getRandomNum(6));
        u.setEmail(RandomUtil.getRandomNum(6)+"@qq.com");
        u.setPasswd("654564123");
        registerVo.setUser(u);
        BaseVo register = userServer.verify(registerVo);
        System.out.println(register.toString());
    }

}
