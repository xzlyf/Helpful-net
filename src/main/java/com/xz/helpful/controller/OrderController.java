package com.xz.helpful.controller;

import com.xz.helpful.global.RedisKey;
import com.xz.helpful.pojo.Order;
import com.xz.helpful.pojo.vo.BaseVo;
import com.xz.helpful.pojo.vo.OrderVo;
import com.xz.helpful.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author XiaoZe
 * @email czr2001@outlook.com
 * @date 2022/5/15 16:43
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 查询所有订单
     */
    @GetMapping("/findAll")
    public Object findAll(HttpSession session) {
        Integer userId = (Integer) session.getAttribute(RedisKey.SESSION_USER_ID);
        if (userId == null) {
            return BaseVo.failed("未登录");
        }
        List<OrderVo> all = orderService.findAll(userId);
        if (all == null || all.size() == 0) {
            return BaseVo.failed("没有任何记录");
        }
        return BaseVo.success(all);
    }
}
