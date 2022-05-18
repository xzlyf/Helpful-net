package com.xz.helpful.global;

/**
 * @author Xiaoze
 * redis key
 */
public class RedisKey {

    //session attribute user id
    public static final String SESSION_USER_ID = "user:id";
    //session attribute user email
    public static final String SESSION_USER_EMAIL = "user:email";
    //任务列表
    public static final String REDIS_TASK_KEY = "user:task:";
    //任务校验用
    public static final String REDIS_TASK_CHECK = "user:task:check:";
    //用户缓存
    public static final String REDIS_USER_TEMP = "user:temp:";
    //用户常见信息缓存
    public static final String REDIS_USER_INFO = "user:info:";
    //shiro session key
    public static final String REDIS_SHIRO_SESSION = "session:id:";
    //人机验证码
    public static final String REDIS_VERIFY_robot = "verify:robot:";
    //邮箱验证码
    public static final String REDIS_VERIFY_email = "verify:email:";
}
