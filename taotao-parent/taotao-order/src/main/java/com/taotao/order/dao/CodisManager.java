package com.taotao.order.dao;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import com.taotao.result.TaotaoResult;

import io.codis.jodis.JedisResourcePool;
import io.codis.jodis.RoundRobinJedisPool;
import redis.clients.jedis.Jedis;

public class CodisManager implements InitializingBean{
	private String zkProxyDir;

    private String zkAddr;

    private int zkSessionTimeoutMs;
    
    // jedis处理资源池
 	private JedisResourcePool jedisPool;
 	
 	private AtomicBoolean poolInited = new AtomicBoolean(false);
 	
 	private static final Logger log = LoggerFactory.getLogger(CodisManager.class);
	@Override
	public void afterPropertiesSet() throws Exception {
		init();
	}
	
	/**
	 * 初始化方法,获取通过jodis获取jedis
	 * <p>Title: init</p>
	 * <p>Description: </p>: void
	 */
	public void init(){
		try {
			if (jedisPool != null)
				jedisPool.close();
			jedisPool = RoundRobinJedisPool.create()
					.curatorClient(zkAddr, zkSessionTimeoutMs)
					.zkProxyDir(zkProxyDir).build();
			poolInited.set(true);
			log.info("TAOTAO-CodisClient：codis-jedis连接池初始化完毕");
		} catch (IOException e) {
			poolInited.set(false);
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据key获取value
	 * <p>Title: get</p>
	 * <p>Description: </p>
	 * @param key
	 * @return: String
	 */
	public String get(String key) {
//		if(StringUtils.isBlank(key)){
//			return TaotaoResult.build("TAOTAO-CodisStorage：get value不允许key为null");
//		}
//		if(!poolInited.get()){
//			return TaotaoResult.build("TAOTAO-CodisStorage：jedis pool没有初始化");
//		}
		Jedis jedis = null;
		String result;
		try {
			jedis = jedisPool.getResource();
			result = jedis.get(key);
//			if(StringUtils.isBlank(result)){
//				return TaotaoResult.build("返回的value为空");
//			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return TaotaoResult.build("jedis连接出现异常");
		}finally {
			if(jedis != null){
				jedis.close();
			}
		}
	}
	/**
	 * 向redis中set值
	 * <p>Title: set</p>
	 * <p>Description: </p>
	 * @param key
	 * @param value
	 * @return: String
	 */
	public String set(String key,String value){
//		if(StringUtils.isBlank(key)){
//			return TaotaoResult.build("TAOTAO-CodisStorage：get value不允许key为null");
//		}
//		if(!poolInited.get()){
//			return TaotaoResult.build("TAOTAO-CodisStorage：jedis pool没有初始化");
//		}
		Jedis jedis = null;
		String result;
		try {
			jedis = jedisPool.getResource();
			result = jedis.set(key, value);
//			if(StringUtils.isBlank(result)){
//				return TaotaoResult.build("返回的value为空");
//			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return TaotaoResult.build("jedis连接出现异常");
		}finally {
			if(jedis != null){
				jedis.close();
			}
		}
	}
	
	/**
	 * 返回哈希表 hkey 中给定域 key 的值。
	 * <p>Title: hget</p>
	 * <p>Description: </p>
	 * @param hkey
	 * @param key
	 * @return: String
	 */
	public String hget(String hkey,String key){
//		if(StringUtils.isBlank(key) || StringUtils.isBlank(hkey)){
//			return TaotaoResult.build("TAOTAO-CodisStorage：get value不允许key或者hkey为null");
//		}
//		if(!poolInited.get()){
//			return TaotaoResult.build("TAOTAO-CodisStorage：jedis pool没有初始化");
//		}
		Jedis jedis = null;
		String result;
		try {
			jedis = jedisPool.getResource();
			result = jedis.hget(hkey, key);
//			if(StringUtils.isBlank(result)){
//				return TaotaoResult.build("返回的value为空");
//			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return TaotaoResult.build("jedis连接出现异常");
		}finally {
			if(jedis != null){
				jedis.close();
			}
		}
	}
	
	/**
	 * 将哈希表hkey中的域key的值设为value 。
	 * 如果key不存在,一个新的哈希表被创建并进行HSET操作。
     * 如果域key已经存在于哈希表中，旧值将被覆盖。
	 * <p>Title: hset</p>
	 * <p>Description: </p>
	 * @param hkey
	 * @param key
	 * @param value
	 * @return: Long
	 */
	public Long hset(String hkey,String key,String value){
		Jedis jedis = jedisPool.getResource();
		Long result = jedis.hset(hkey, key,value);
		if(jedis != null){
			jedis.close();
		}	
		return result;
	}
	
	/**
	 * 将 key 中储存的数字值增一。

	 * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCR 操作。

     * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。
	 * <p>Title: incr</p>
	 * <p>Description: </p>
	 * @param key
	 * @return: long
	 */
	public long incr(String key) {
		Jedis jedis = jedisPool.getResource();
		Long result = jedis.incr(key);
		if(jedis!=null){
			jedis.close();
		}
		return result;
	}
	/**
	 * 删除哈希表 key 中的一个或多个指定域，不存在的域将被忽略。
	 * <p>Title: hdel</p>
	 * <p>Description: </p>
	 * @param hkey
	 * @param key
	 * @return: int
	 */
	public long hdel(String hkey,String key){
		Jedis jedis = jedisPool.getResource();
		Long result = jedis.hdel(hkey, key);
		if(jedis != null){
			jedis.close();
		}
		return result;
	}
	public void setZkProxyDir(String zkProxyDir) {
		this.zkProxyDir = zkProxyDir;
	}
	public void setZkAddr(String zkAddr) {
		this.zkAddr = zkAddr;
	}
	public void setZkSessionTimeoutMs(int zkSessionTimeoutMs) {
		this.zkSessionTimeoutMs = zkSessionTimeoutMs;
	}
	public JedisResourcePool getJedisPool() {
		return jedisPool;
	}
}
