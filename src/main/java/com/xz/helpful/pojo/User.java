package com.xz.helpful.pojo;

import lombok.Data;

import java.util.Date;

/**
 * @Author: xz
 * @Date: 2022/4/20
 */
@Data
public class User {
    private int id;
    private String name;
    private String email;
    private String passwd;
    private String code;//使用的邀请码
    private String mycode;//自己的邀请码
    private Date createTime;
    private Date updateTime;
}
