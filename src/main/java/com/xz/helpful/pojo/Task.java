package com.xz.helpful.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class Task {
    private Integer id;
    private Integer taskType;
    private Integer taskPay;
    private Integer taskFrom;
    private String taskDesc;
    private Date createTime;
    private Date updateTime;
    private Integer walletId;
    private Integer isEnable;
    private Integer isHidden;
}
