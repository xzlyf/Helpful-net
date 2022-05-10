package com.xz.helpful.global;

/**
 * redis key
 */
public class RedisKey {

    //session attribute user id
    public static final String SESSION_USER_ID = "user:id";
    //session attribute user email
    public static final String SESSION_USER_EMAIL = "user:email";
    //任务列表+userId
    public static final String REDIS_TASK_KEY = "user:task:";
    //Session key
    public static final String REDIS_SESSION_ID = "session:id:";
    //人机验证码
    public static final String REDIS_VERIFY_robot = "verify:robot:";
    //邮箱验证码
    public static final String REDIS_VERIFY_email = "verify:email:";
}
