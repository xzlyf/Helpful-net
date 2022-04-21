package com.xz.helpful.pojo;

import lombok.Data;

import java.util.Date;

/**
 * @Author: xz
 * @Date: 2022/4/21
 * 公告表
 */
@Data
public class Notice {
    private String text;
    private Integer isTop;
    private Date createTime;
}
