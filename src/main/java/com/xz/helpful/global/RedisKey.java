package com.xz.helpful.global;

/**
 * redis key
 */
public class RedisKey {
    //任务列表+userId
    public static final String REDIS_TASK_KEY = "user:task:";

    //用户邮箱绑定id
    public static final String REDIS_USER_ID = "user:email:";

    //Session key
    public static final String REDIS_SESSION_ID = "session:id:";
}
