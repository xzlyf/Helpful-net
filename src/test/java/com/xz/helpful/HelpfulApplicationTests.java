package com.xz.helpful;

import com.xz.helpful.pojo.Task;
import com.xz.helpful.service.TaskService;
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
        Integer userIdByEmail = userServer.findUserIdByEmail("11@qq.com");
        System.out.println("用户id："+userIdByEmail);
    }

    @Test
    void textMain(){
        String st = "哈哈哈哈";
        String st2 = "哈哈哈哈";
        System.out.println(st.hashCode());
        System.out.println(st2.hashCode());
    }

}
