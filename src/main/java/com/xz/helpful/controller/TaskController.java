package com.xz.helpful.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xz.helpful.annotation.LimitRequest;
import com.xz.helpful.global.RedisKey;
import com.xz.helpful.pojo.BiliMovie;
import com.xz.helpful.service.TaskService;
import com.xz.helpful.utils.RedisUtil;
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

import javax.servlet.http.HttpSession;


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
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private TaskService taskService;

    /**
     * bilibili api 获取视频信息 ：http://api.bilibili.com/x/web-interface/view?bvid=
     *
     * @param bv bvid号
     */
    @GetMapping("/checkBV")
    @ResponseBody
    @LimitRequest(count = 8, time = 60000)
    public Object checkBV(HttpSession session, @RequestParam String bv) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("view/task-error");

        ResponseEntity<String> responseEntity;
        try {
            //通过bili api获取视频信息
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
        //存入session,待用户确认，60秒后过期
        redisUtil.set(RedisKey.REDIS_USER_TEMP + session.getId(), data, 60);
        modelAndView.setViewName("view/task-check");
        modelAndView.addObject("info", data);
        return modelAndView;
    }

    @GetMapping("/affirmBV")
    @LimitRequest(count = 8, time = 30000)
    public Object affirmBV(HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("view/task-error");
        BiliMovie data = (BiliMovie) redisUtil.get(RedisKey.REDIS_USER_TEMP + session.getId());
        if (data == null) {
            modelAndView.addObject("msg", "发布失败，请重试！");
            return modelAndView;
        }
        Integer userId = (Integer) session.getAttribute(RedisKey.SESSION_USER_ID);
        if (userId == null) {
            modelAndView.addObject("msg", "身份验证失败，请重新登录");
            return modelAndView;
        }
        taskService.createOne(userId, data);
        modelAndView.addObject("msg", "任务发布成功");
        return modelAndView;
    }
}
