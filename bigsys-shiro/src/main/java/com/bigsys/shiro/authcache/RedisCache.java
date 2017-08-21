package com.bigsys.shiro.authcache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.io.*;
import java.util.*;

/**
 * Created by fangzhipeng on 2017/8/16.
 */
public class RedisCache<K, V> implements Cache<K, V> {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * The wrapped Jedis instance.
     */
    private Jedis cache;

    /**
     * The Redis key prefix for the sessions
     */
    private String keyPrefix = "shiro_session:";

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

    /**
     * 通过一个JedisManager实例构造RedisCache
     */
    public RedisCache(Jedis cache) {
        if (cache == null) {
            throw new IllegalArgumentException("Cache argument cannot be null.");
        }
        this.cache = cache;
    }

    /**
     * Constructs a cache instance with the specified
     * Redis manager and using a custom key prefix.
     * @param cache The cache manager instance
     * @param prefix The Redis key prefix
     */
    public RedisCache(Jedis cache, String prefix) {

        this(cache);

        // set the prefix
        this.keyPrefix = prefix;
    }

    /**
     * 获得byte[]型的key
     * @param key
     * @return
     */
    private byte[] getByteKey(K key) {
        if (key instanceof String) {
            String preKey = this.keyPrefix + key;
            return preKey.getBytes();
        } else if(key instanceof PrincipalCollection){
            String preKey = this.keyPrefix + key.toString();
            return preKey.getBytes();
        }
        return new byte[1];
    }

    @Override
    public V get(K key) throws CacheException {
        logger.debug("根据key从Redis中获取对象 key [" + key + "]");
        try {
            if (key == null) {
                return null;
            } else {
                byte[] rawValue = cache.get(getByteKey(key));
                @SuppressWarnings("unchecked") V value = (V) byteToObject(rawValue);
                return value;
            }
        } catch (Throwable t) {
            t.printStackTrace();
            return null;
        }

    }
    public String getStr(String key) throws CacheException {
        logger.debug("根据key从Redis中获取对象 key [" + key + "]");
        try {
            if (key == null) {
                return null;
            } else {
                return cache.get(key);
            }
        } catch (Throwable t) {
            throw new CacheException(t);
        }

    }

    @Override
    public V put(K key, V value) throws CacheException {
        logger.debug("根据key从存储 key [" + key + "]");
        try {
            cache.set(getByteKey(key), objectToByte(value));
            return value;
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    public String putStr(String key, String value) throws CacheException {
        logger.debug("根据key从存储 key [" + key + "]");
        try {
            cache.set(key, value);
            return value;
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

//    public String put(String key,String value, int expire) throws CacheException {
//        logger.debug("根据key从存储 key [" + key + "]");
//        try {
//            cache.set(key, value, expire);
//            cache.set
//            return value;
//        } catch (Throwable t) {
//            throw new CacheException(t);
//        }
//    }

    public String removeString(String key) throws CacheException {
        logger.debug("从redis中删除 key [" + key + "]");
        try {
            String previous = cache.get(key);
            cache.del(key);
            return previous;
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    @Override
    public V remove(K key) throws CacheException {
        logger.debug("从redis中删除 key [" + key + "]");
        try {
            V previous = get(key);
            cache.del(getByteKey(key));
            return previous;
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    @Override
    public void clear() throws CacheException {
        logger.debug("从redis中删除所有元素");
        try {
            cache.flushDB();
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    @Override
    public int size() {
        try {
            Long longSize = new Long(cache.dbSize());
            return longSize.intValue();
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<K> keys() {
        try {
            Set<String> keys = cache.keys(this.keyPrefix + "*");
            if (CollectionUtils.isEmpty(keys)) {
                return Collections.emptySet();
            } else {
                Set<K> newKeys = new HashSet<K>();
                for (String key : keys) {
                    newKeys.add((K) key);
                }
                return newKeys;
            }
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    @Override
    public Collection<V> values() {
        try {
            Set<String> keys = cache.keys(this.keyPrefix + "*");
            if (!CollectionUtils.isEmpty(keys)) {
                List<V> values = new ArrayList<V>(keys.size());
                for (String key : keys) {
                    @SuppressWarnings("unchecked") V value = get((K) key);
                    if (value != null) {
                        values.add(value);
                    }
                }
                return Collections.unmodifiableList(values);
            } else {
                return Collections.emptyList();
            }
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    // 把session对象转化为byte保存到redis中
    public byte[] objectToByte(Object session){
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        byte[] bytes = null;
        try {
            ObjectOutputStream oo = new ObjectOutputStream(bo);
            oo.writeObject(session);
            bytes = bo.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    // 把byte还原为session
    public Object byteToObject(byte[] bytes){
        ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
        ObjectInputStream in;
        Object session = null;
        try {
            in = new ObjectInputStream(bi);
            session = in.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return session;
    }

}
