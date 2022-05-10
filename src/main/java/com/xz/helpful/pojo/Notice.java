package com.xz.helpful.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: xz
 * @Date: 2022/4/21
 * 公告表
 */
@Data
public class Notice implements Serializable {
    private String text;
    private Integer isTop;
    private Date createTime;
}
