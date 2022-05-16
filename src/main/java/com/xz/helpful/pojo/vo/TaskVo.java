package com.xz.helpful.pojo.vo;


import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author XiaoZe
 * @email czr2001@outlook.com
 * @date 2022/5/15 21:57
 */
@Data
public class TaskVo implements Serializable {
    private Integer taskId;
    private String fromUser;
    private String typeName;
    private Integer taskType;
    private String taskDesc;
    private Integer taskPay;
    private String taskImg;
    private String taskUrl;
    private String taskMid;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
