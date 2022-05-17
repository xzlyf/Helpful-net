package com.xz.helpful.pojo.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: xz
 * @Date: 2022/4/20
 */
@Data
public class UserVo implements Serializable {
    private String name;
    private String email;
    private String passwd;
    private String code;//使用的邀请码
    private String mycode;//自己的邀请码
    private Integer wallet;//积分余额
    private Date createTime;
}
