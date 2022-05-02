package com.xz.helpful;

import com.xz.helpful.pojo.Task;
import com.xz.helpful.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class HelpfulApplicationTests {


    @Autowired
    TaskService taskService;
    @Test
    void contextLoads() {
        Task one = taskService.getOne(25);
        System.out.println(one.toString());
    }

    @Test
    void textMain(){
        String st = "哈哈哈哈";
        String st2 = "哈哈哈哈";
        System.out.println(st.hashCode());
        System.out.println(st2.hashCode());
    }

}
