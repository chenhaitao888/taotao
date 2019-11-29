package com.taotao.utils;

import java.util.Collections;

import redis.clients.jedis.JedisCluster;

public class RedisClusterUtil {
	public static String set(String key, String value, JedisCluster cluster){
		String set = cluster.set(key, value);
		return set;
	}
	
	public static String get(String key, JedisCluster cluster){
		String value = cluster.get(key);
		return value;
	}
	
	public static Long delete(String key, JedisCluster cluster){
		Long del = cluster.del(key);
		return del;
	}
	
	private static final String LOCK_SUCCESS = "OK";
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "PX";
 
    /**
     * 尝试获取分布式锁
     * @param jedis Redis客户端
     * @param lockKey 锁
     * @param requestId 请求标识
     * @param expireTime 超期时间
     * @return 是否获取成功
     */
    public static boolean tryGetDistributedLock(JedisCluster cluster, String lockKey, String requestId, int expireTime) {
 
        String result = cluster.set(lockKey, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expireTime);
 
        if (LOCK_SUCCESS.equals(result)) {
            return true;
        }
        return false;
 
    }
    
    private static final Long RELEASE_SUCCESS = 1L;
    
    /**
     * 释放分布式锁
     * @param jedis Redis客户端
     * @param lockKey 锁
     * @param requestId 请求标识
     * @return 是否释放成功
     */
    public static boolean releaseDistributedLock(JedisCluster cluster, String lockKey, String requestId) {
 
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Object result = cluster.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));
 
        if (RELEASE_SUCCESS.equals(result)) {
            return true;
        }
        return false;
 
    }

}
