package cn.fzk.mySpringBoot.application.shiro;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.ValidatingSession;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2018/5/30.
 */
@Component
public class MySessionDao extends CachingSessionDAO {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    // 保存到Redis中key的前缀 prefix+sessionId
    private String prefix = "shiro:shiro-activeSessionCache:";
    // Session超时时间，单位为毫秒
    private long expireTime = 1800000;
    @Resource(name = "redisTemplateObj")
    private RedisTemplate redisTemplate;// Redis操作类

    // 更新session
    @Override
    protected void doUpdate(Session session) {
        //如果会话过期/停止 没必要再更新了
        try {
            if (session instanceof ValidatingSession && !((ValidatingSession) session).isValid()) {
                return;
            }
        } catch (Exception e) {
            logger.error("ValidatingSession error");
        }

        if (session == null || session.getId() == null) {
            return;
        }

        String key = prefix + session.getId().toString();
        if (!redisTemplate.hasKey(key)) {
            redisTemplate.opsForValue().set(key, session, expireTime, TimeUnit.MILLISECONDS);
        }
        redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
    }

    @Override
    protected void doDelete(Session session) {
        logger.info("删除session缓存");
        if (null == session) {
            return;
        }
        redisTemplate.opsForValue().getOperations().delete(prefix + session.getId());
    }

    @Override
    protected Serializable doCreate(Session session) {
        logger.info("创建session缓存");
        Serializable sessionId = this.generateSessionId(session);
        this.assignSessionId(session, sessionId);

        redisTemplate.opsForValue().set(prefix + session.getId(), session, expireTime, TimeUnit.MILLISECONDS);
        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable serializable) {
        logger.info("读取session缓存");
        if (serializable == null) {
            return null;
        }
        return (Session) redisTemplate.opsForValue().get(serializable);
    }

    // 获取活跃的session，可以用来统计在线人数，如果要实现这个功能，可以在将session加入redis时指定一个session前缀，
    // 统计的时候则使用keys("session-prefix*")的方式来模糊查找redis中所有的session集合
    @Override
    public Collection<Session> getActiveSessions() {
        return redisTemplate.keys(prefix + "*");
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }
}
