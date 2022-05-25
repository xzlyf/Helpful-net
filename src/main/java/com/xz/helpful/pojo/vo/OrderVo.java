package com.xz.helpful.pojo.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author XiaoZe
 * @email czr2001@outlook.com
 * @date 2022/5/15 17:30
 */
@Data
public class OrderVo implements Serializable {
    private String doneUserName;
    private String fromUserName;
    private String taskDesc;
    private String taskType;
    private String taskPay;

    /*
    JsonFormat注解是jackson的注解。
    JSONField注解是fastjson的注解。
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
