package com.xz.helpful.pojo.vo;

import lombok.Data;

/**
 * @Author: xz
 * @Date: 2022/4/20
 */
@Data
public class BaseVo {
    private int code;
    private String msg;
    private Object data;

    public BaseVo(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static BaseVo success(Object data) {
        return new BaseVo(0, "成功", data);
    }

    public static BaseVo failed(Object data) {
        return new BaseVo(-1, "失败", data);
    }

    public static BaseVo failed(Object data, String msg) {
        return new BaseVo(-1, msg, data);
    }

    public static BaseVo failed(Object data, String msg, int code) {
        return new BaseVo(code, msg, data);
    }
}
