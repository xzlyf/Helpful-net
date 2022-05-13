package com.xz.helpful.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * @author XiaoZe
 * @email czr2001@outlook.com
 * @date 2022/5/13 16:35
 */
@Slf4j
@Controller
@RequestMapping("/taskManager")
public class TaskController {

    @ResponseBody
    @GetMapping("/checkBV")
    public Object checkBV(@RequestParam String bv) {
        //todo 获取视频封面标题等数据
        return bv;
    }
}
