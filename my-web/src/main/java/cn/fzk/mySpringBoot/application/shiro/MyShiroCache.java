package cn.fzk.mySpringBoot.application.shiro;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2018/5/30.
 * .实现cache共享
 */
public class MyShiroCache<K, V> implements Cache<K, V> {
    private static final String REDIS_SHIRO_CACHE = "shiro:";
    private String cacheKey;
    private RedisTemplate<K, V> redisTemplate;
    //缓存超时时间，单位为毫秒
    private long globExpire = 1800000;

    @SuppressWarnings({"rawtypes", "unchecked"})
    public MyShiroCache(String name, RedisTemplate redisTemplate) {
        this.cacheKey = REDIS_SHIRO_CACHE + name + ":";
        this.redisTemplate = redisTemplate;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public MyShiroCache(String name, RedisTemplate redisTemplate, long globExpire) {
        this.cacheKey = REDIS_SHIRO_CACHE + name + ":";
        this.redisTemplate = redisTemplate;
        this.globExpire = globExpire;
    }

    @Override
    public V get(K k) throws CacheException {
        redisTemplate.boundValueOps(getCacheKey(k)).expire(globExpire, TimeUnit.MILLISECONDS);
        return redisTemplate.boundValueOps(getCacheKey(k)).get();
    }

    @Override
    public V put(K k, V v) throws CacheException {
        V old = get(k);
        redisTemplate.boundValueOps(getCacheKey(k)).set(v);
        return old;
    }

    @Override
    public V remove(K k) throws CacheException {
        V old = get(k);
        redisTemplate.delete(getCacheKey(k));
        return old;
    }

    @Override
    public void clear() throws CacheException {
        redisTemplate.delete(keys());
    }

    @Override
    public int size() {
        return keys().size();
    }

    @Override
    public Set<K> keys() {
        return redisTemplate.keys(getCacheKey("*"));
    }

    @Override
    public Collection<V> values() {
        Set<K> set = keys();
        List<V> list = new ArrayList<V>();
        for (K s : set) {
            list.add(get(s));
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    private K getCacheKey(Object k) {
        return (K) (this.cacheKey + k);
    }
}
