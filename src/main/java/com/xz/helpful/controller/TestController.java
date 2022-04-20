package com.xz.helpful.controller;

import com.xz.helpful.service.UserServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: xz
 * @Date: 2022/4/20
 */
@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    private UserServer userServer;

    @RequestMapping("/findAll")
    public Object findAll() {
        return userServer.findAll();
    }
}
