package com.xz.helpful;

import com.xz.helpful.controller.IndexController;
import com.xz.helpful.pojo.User;
import com.xz.helpful.pojo.vo.UserVo;
import com.xz.helpful.service.UserServer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
class HelpfulApplicationTests {

    @Autowired
    IndexController controller;

    @Test
    void contextLoads() {
    }

}
