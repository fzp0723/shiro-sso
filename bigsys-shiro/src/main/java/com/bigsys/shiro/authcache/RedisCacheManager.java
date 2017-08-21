package com.bigsys.shiro.authcache;

import com.bigsys.redis.service.RedisDb;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by fangzhipeng on 2017/8/16.
 */
public class RedisCacheManager implements CacheManager {

    private static final Logger logger = LoggerFactory.getLogger(RedisCacheManager.class);

    // fast lookup by name map
    private final ConcurrentMap<String, Cache> caches = new ConcurrentHashMap<String, Cache>();

    private Jedis jedis = RedisDb.getJedis();

    /**
     * The Redis key prefix for caches
     */
    private String keyPrefix = "shiro_redis_cache:";

    /**
     * Returns the Redis session keys
     * prefix.
     * @return The prefix
     */
    public String getKeyPrefix() {
        return keyPrefix;
    }

    /**
     * Sets the Redis sessions key
     * prefix.
     * @param keyPrefix The prefix
     */
    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }

    @Override
    public <K, V> Cache<K, V> getCache(String name) throws CacheException {
//        logger.debug("获取名称为: " + name + " 的RedisCache实例");
        System.out.println("获取名称为: " + name + " 的RedisCache实例");
        Cache c = caches.get(name);

        if (c == null) {


            // create a new cache instance
            c = new RedisCache<K, V>(jedis, keyPrefix);

            // add it to the cache collection
            caches.put(name, c);
        }
        return c;
    }

}
