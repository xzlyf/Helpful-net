package com.xz.helpful;

import com.xz.helpful.pojo.Task;
import com.xz.helpful.service.TaskService;
import com.xz.helpful.service.UserServer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Slf4j
class HelpfulApplicationTests {


    @Autowired
    TaskService taskService;
    @Test
    void contextLoads() {
        //List<Task> all = taskService.findAll(52);
        //log.info("大小："+all.size());
    }

    @Test
    void textMain(){
        String st = "哈哈哈哈";
        String st2 = "哈哈哈哈";
        System.out.println(st.hashCode());
        System.out.println(st2.hashCode());
    }

}
