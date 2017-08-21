package com.bigsys.redis.service;

import redis.clients.jedis.Jedis;

/**
 * Created by fangzhipeng on 2017/8/15.
 */
public interface Event {

    void excuate(Jedis jedis);

}
