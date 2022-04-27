package com.xz.helpful.pojo.vo;

import com.xz.helpful.pojo.User;
import lombok.Data;

/**
 * @Author: xz
 * @Date: 2022/4/22
 */
@Data
public class RegisterVo {
    private String verifyCode;
    private String session;
    private User user;
}
