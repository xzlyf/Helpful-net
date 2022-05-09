package com.xz.helpful.shiro.session;

import com.xz.helpful.global.RedisKey;
import com.xz.helpful.utils.RedisUtil;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author XiaoZe
 * @email czr2001@outlook.com
 * @date 2022/5/9 15:47
 * <p>
 * 重写session 增删改查，与redis接入
 */
@Service
public class RedisSessionDao extends AbstractSessionDAO {
    //Session过期时间,单位180（秒）
    private long expireTime = 180L;
    @Autowired
    private RedisUtil redisUtil;

    public RedisSessionDao() {
        super();
    }

    public RedisSessionDao(long expireTime) {
        super();
        this.expireTime = expireTime;
    }

    //新建Session存储
    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = this.generateSessionId(session);
        this.assignSessionId(session, sessionId);
        redisUtil.set(RedisKey.REDIS_SESSION_ID + session.getId(), session, expireTime);
        return sessionId;
    }

    //读取session by Id
    @Override
    protected Session doReadSession(Serializable sessionId) {
        if (sessionId == null) {
            return null;
        }
        return (Session) redisUtil.get(RedisKey.REDIS_SESSION_ID + sessionId);
    }

    //更新session
    @Override
    public void update(Session session) throws UnknownSessionException {
        if (session == null || session.getId() == null) {
            return;
        }
        session.setTimeout(expireTime * 1000);
        redisUtil.set(RedisKey.REDIS_SESSION_ID + session.getId(), session, expireTime);

    }

    //删除session
    @Override
    public void delete(Session session) {
        if (session == null) {
            return;
        }
        redisUtil.del(RedisKey.REDIS_SESSION_ID + session.getId());
    }

    //获取所有活跃的Session
    @Override
    public Collection<Session> getActiveSessions() {
        Set<Session> sessions = new HashSet<>();
        Set<String> keys = redisUtil.keys(RedisKey.REDIS_SESSION_ID + "*");
        if (keys != null && keys.size() > 0) {
            for (String key : keys) {
                Session s = (Session) redisUtil.get(key);
                sessions.add(s);
            }
        }
        return sessions;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

}
