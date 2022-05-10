package com.xz.helpful.pojo.vo;

import com.xz.helpful.pojo.User;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: xz
 * @Date: 2022/4/22
 */
@Data
public class RegisterVo implements Serializable {
    private String verifyCode;
    private String session;
    private User user;
}
