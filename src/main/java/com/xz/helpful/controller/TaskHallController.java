package com.xz.helpful.controller;

import com.xz.helpful.global.RedisKey;
import com.xz.helpful.pojo.Task;
import com.xz.helpful.pojo.vo.BaseVo;
import com.xz.helpful.service.TaskService;
import com.xz.helpful.service.WalletServer;
import com.xz.helpful.utils.RedisUtil;
import com.xz.helpful.utils.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

/**
 * 任务大厅接口
 */
@Slf4j
@Controller
@RequestMapping("/taskhall")
public class TaskHallController {
    @Autowired
    private TaskService taskService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private WalletServer walletServer;
    @Autowired
    private UUIDUtil uuidUtil;

    @RequestMapping("/get")
    public ModelAndView getRandom(HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        Subject subject = SecurityUtils.getSubject();
        String email = subject.getPrincipal().toString();
        if (email == null) {
            modelAndView.setViewName("view/task-error");
            modelAndView.addObject("msg", "未登录");
            return modelAndView;
        }
        //开始取任务
        Task one = taskService.getOne(email);
        //没有任务了
        if (one == null) {
            modelAndView.setViewName("view/task-error");
            modelAndView.addObject("msg", "暂时没有新的任务了，休息下再来吧~");
        } else {
            //使用view页面返回html
            modelAndView.setViewName("view/task");
            modelAndView.addObject("task", one);
        }
        Integer id = (Integer) session.getAttribute(RedisKey.SESSION_USER_ID);
        if (id != null) {
            Integer wallet = walletServer.queryMoneyByUserId(id);
            modelAndView.addObject("wallet", wallet);
        }
        return modelAndView;
    }

    /**
     * 记录任务开始，任务结束返回随机码，通过该随机码来判断完成任务
     */
    @ResponseBody
    @GetMapping("/startTask")
    public Object startTask(HttpSession session,
                            @RequestParam String taskId) throws InterruptedException {
        Thread.sleep(15000);
        String r = uuidUtil.getUUID32();
        //存入uuid，做校验用，60秒后过期
        redisUtil.set(RedisKey.REDIS_TASK_CHECK + session.getId() + taskId, r, 60);
        return BaseVo.success(r);
    }

    /**
     * 校验任务
     *
     * @param taskId 任务id
     * @param r      startTask返回的随机值
     */
    @ResponseBody
    @GetMapping("/doneTask")
    @Transactional(rollbackFor = Exception.class)
    public Object doneTask(HttpSession session,
                           @RequestParam Integer taskId,
                           @RequestParam String r) {
        String key = RedisKey.REDIS_TASK_CHECK + session.getId() + taskId;
        String remote = (String) redisUtil.get(key);
        if (remote == null) {
            return BaseVo.failed("校验已过期", -2);
        }
        if (!remote.equals(r)) {
            return BaseVo.failed("校验失败");
        }
        String email = (String) session.getAttribute(RedisKey.SESSION_USER_EMAIL);
        Integer userId = (Integer) session.getAttribute(RedisKey.SESSION_USER_ID);
        if (email == null || userId == null) {
            return BaseVo.failed("登录已过期", -1);
        }
        try {
            taskService.finishOne(email, userId, taskId);
            redisUtil.del(key);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return BaseVo.success("校验成功");
    }

}
