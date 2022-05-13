package com.xz.helpful.controller;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;


/**
 * @author XiaoZe
 * @email czr2001@outlook.com
 * @date 2022/5/13 16:35
 */
@Slf4j
@Controller
@RequestMapping("/taskManager")
public class TaskController {

    /**
     * bilibili api 获取视频信息 ：http://api.bilibili.com/x/web-interface/view?bvid=
     *
     * @param bv bvid号
     */
    @ResponseBody
    @GetMapping("/checkBV")
    public Object checkBV(@RequestParam String bv) {
        Document document = null;
        try {
            document =
                    Jsoup.connect("http://api.bilibili.com/x/web-interface/view?bvid=" + bv)
                            .userAgent("Mozilla/5.0 (Windows NT 5.1; zh-CN) AppleWebKit/535.12 (KHTML, like Gecko) Chrome/22.0.1229.79 Safari/535.12")
                            .ignoreContentType(true)
                            .timeout(3000)
                            .get();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return document.body().text();
    }
}
