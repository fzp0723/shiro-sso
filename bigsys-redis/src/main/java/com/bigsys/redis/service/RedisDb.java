package com.bigsys.redis.service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.swing.*;

/**
 * Created by fangzhipeng on 2017/8/15.
 */
public class RedisDb {

    private static JedisPool jedisPool;

    static {
        jedisPool = new JedisPool("127.0.0.1");
    }

    public static Jedis getJedis() {
        return jedisPool.getResource();
    }

    public static void setObject(final byte[] key, final byte[] object) {
        deal((jedis) -> {jedis.set(key, object);});
    }

    public static void delString(String key) {
        deal(jedis -> jedis.del(key));
    }

    public static byte[] getObject(byte[] key) {
        Result result = new Result();
        deal(jedis -> {result.setObject(jedis.get(key));});
        return (byte[]) result.getObject();
    }


    private static void deal(Event event) {
        Jedis jedis = jedisPool.getResource();
        event.excuate(jedis);
        jedis.close();
    }

    static class Result {
        Object object;

        public Object getObject() {
            return object;
        }

        public void setObject(Object object) {
            this.object = object;
        }
    }
}
