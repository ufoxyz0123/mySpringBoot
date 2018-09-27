package cn.fzk.mySpringBoot.application.shiro;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2018/5/30.
 */
public class MyRedisCacheManager implements CacheManager {
    @Resource(name="redisTemplateObj")
    private RedisTemplate<String, Object> redisTemplate;

    private long globExpire=1800000;
    @Override
    public <K, V> Cache<K, V> getCache(String s) throws CacheException {
        return new MyShiroCache<K, V>(s, redisTemplate,globExpire);
    }
}
