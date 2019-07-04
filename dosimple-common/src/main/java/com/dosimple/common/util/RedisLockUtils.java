package com.dosimple.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import redis.clients.jedis.Jedis;

import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author dosimple
 */
@Slf4j
public class RedisLockUtils {
    private RedisTemplate redisTemplate = null;
    private Jedis jedis = null;

    private static final long LOCK_TIMEOUT = 10000 * 5;

    private static final RedisLockUtils lock = new RedisLockUtils();

    private RedisLockUtils() {
        redisTemplate = SpringContextHolder.getBean("redisTemplate");
    }

    public static RedisLockUtils getInstance() {
        return lock;
    }

    private static final String LOCK_SUCCESS = "OK";
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "PX";
    private static final String UNLOCK_LUA = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

    /**
     * 尝试获取分布式锁
     *
     * @param lockKey    锁
     * @param requestId  请求标识
     * @param expireTime 超期时间
     * @return 是否获取成功
     */
    public boolean tryGetDistributedLock(Jedis jedis, String lockKey, String requestId, int expireTime) {
        String result = jedis.set(lockKey, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expireTime);
        if (LOCK_SUCCESS.equals(result)) {
            return true;
        }
        return false;
    }

    private static final Long RELEASE_SUCCESS = 1L;

    /**
     * 释放分布式锁
     *
     * @param lockKey   锁
     * @param requestId 请求标识
     * @return 是否释放成功
     */
    public boolean releaseDistributedLock(Jedis jedis, String lockKey, String requestId) {
        Object result = jedis.eval(UNLOCK_LUA, Collections.singletonList(lockKey), Collections.singletonList(requestId));
        if (RELEASE_SUCCESS.equals(result)) {
            return true;
        }
        return false;
    }

    /**
     * 加锁
     * 取到锁加锁，取不到锁一直等待知道获得锁
     *
     * @param lockKey
     * @param requestId
     * @return
     */
    public Boolean lock(String lockKey, String requestId) {
        if (StringUtils.isBlank(lockKey)) {
            log.error("lockKey is empty");
            return false;
        }
        log.debug("requestId >>> " + requestId + "等待加锁");
        Boolean flag = redisTemplate.opsForValue().setIfAbsent(lockKey, requestId, LOCK_TIMEOUT, TimeUnit.MILLISECONDS);
        return flag;
    }

    /**
     * 解锁
     *
     * @param lockKey
     * @param requestId
     */
    public Boolean releaseLock(String lockKey, String requestId) {
        // 释放锁的时候，有可能因为持锁之后方法执行时间大于锁的有效期，此时有可能已经被另外一个线程持有锁，所以不能直接删除
        try {
            DefaultRedisScript<Long> script = new DefaultRedisScript<>(UNLOCK_LUA);
            script.setResultType(Long.class);
            Long o = (Long)redisTemplate.execute(script, Collections.singletonList(lockKey),  requestId);
            return o != null && o > 0;
        } catch (Exception e) {
            log.error("release lock occured an exception", e);
        }
        return false;
    }
}
