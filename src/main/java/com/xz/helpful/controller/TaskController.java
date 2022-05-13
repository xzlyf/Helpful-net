package com.xz.helpful.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xz.helpful.annotation.LimitRequest;
import com.xz.helpful.pojo.BiliMovie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;


/**
 * @author XiaoZe
 * @email czr2001@outlook.com
 * @date 2022/5/13 16:35
 */
@Slf4j
@Controller
@RequestMapping("/taskManager")
public class TaskController {
    public static final String BILI_MOVIE_INFO = "http://api.bilibili.com/x/web-interface/view?bvid=";
    @Autowired
    private RestTemplate restTemplate;

    /**
     * bilibili api 获取视频信息 ：http://api.bilibili.com/x/web-interface/view?bvid=
     *
     * @param bv bvid号
     */
    @GetMapping("/checkBV")
    @ResponseBody
    @LimitRequest(count = 8,time = 60000)
    public Object checkBV(@RequestParam String bv) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("view/task-error");

        ResponseEntity<String> responseEntity;
        try {
            responseEntity = restTemplate.getForEntity(BILI_MOVIE_INFO + bv, String.class);
        } catch (Exception e) {
            e.printStackTrace();
            modelAndView.addObject("msg", "数据源请求失败，请稍后重试！");
            return modelAndView;
        }
        String body = responseEntity.getBody();
        JSONObject obj = JSON.parseObject(body);
        if (obj == null || obj.getInteger("code") != 0) {
            modelAndView.addObject("msg", "没有找到视频号，请检查！");
            return modelAndView;
        }
        BiliMovie data = obj.getObject("data", BiliMovie.class);
        if (data == null) {
            modelAndView.addObject("msg", "数据源解析失败！");
            return modelAndView;
        }
        modelAndView.setViewName("view/task-check");
        modelAndView.addObject("info", data);
        return modelAndView;
    }
}
