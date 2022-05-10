package com.xz.helpful.pojo;

import java.io.Serializable;
import java.util.Date;

public class Order implements Serializable {
    private Integer id;
    private Integer userId;
    private Integer taskId;
    private Date createTime;
    private Date updateTime;
}
