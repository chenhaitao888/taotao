package com.taotao.rest.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import com.taotao.result.TaotaoResult;
import com.taotao.utils.JsonUtils;

import io.codis.jodis.JedisResourcePool;
import io.codis.jodis.RoundRobinJedisPool;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;

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
	
	
	
	/**
	 *  单条增加树状结构
	 * @param <V>
	 * @param currentId 当前节点ID
	 * @param parentId  父ID
	 * @param value     当前节点值
	 */
	public <V> void saveTreeNode(String currentId,String parentId,V value){
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String svalue = JsonUtils.objectToJson(value);
			jedis.set(currentId, svalue);
			jedis.lpush(parentId, currentId);
		} catch (JedisException e) {
			//LOG.error(e.getMessage()+"执行CodisManager.expire()方法出错.", e);
		}finally{
			if (jedis!=null) {
				jedis.close();
			}
		}
	}
	
	
	/**
	 * 或者父节点下的所有子节点
	 * @param currentNodeId
	 * @return
	 */
	public List<String> getTreeNode(String currentNodeId){
		List<String> treeNodes = new ArrayList<>();
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			List<String>  datas =  jedis.lrange(currentNodeId, 0, -1);
			if (datas!=null&&datas.size()>0) {
				for (int i = 0; i < datas.size(); i++) {
					String treeNode = jedis.get(datas.get(i));
					if (treeNode!=null&&treeNode.length()>0) {
						treeNodes.add(treeNode);
					}
				}
			}
		} catch (JedisException e) {
			//LOG.error(e.getMessage()+"执行CodisManager.expire()方法出错.", e);
		}finally{
			if (jedis!=null) {
				jedis.close();
			}
		}
		return treeNodes;
	}
	
	/**
	 * 修改树状结构节点
	 * @param <V>
	 * @param currentId   新加节点ID
	 * @param parentId    父ID
	 * @param oldCurrentId 修改节点ID
	 * @param value       新加节点值
	 */
	public <V> void updateTreeNode(String currentId,String parentId,String oldCurrentId,V value ){
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String svalue = JsonUtils.objectToJson(value);
			jedis.del(oldCurrentId);
			jedis.lrem(parentId, 0, oldCurrentId);
			jedis.set(currentId, svalue);
			jedis.lpush(parentId, currentId);
		} catch (JedisException e) {
			//LOG.error(e.getMessage()+"执行CodisManager.expire()方法出错.", e);
		}finally{
			if (jedis!=null) {
				jedis.close();
			}
		}
		
	}
	
	/**
	 * 删除节点
	 * @param currentNodeId  当前节点
	 * @param parentNodeId   父节点
	 */
	public void delTreeNodes(String currentNodeId,String parentNodeId){
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.del(currentNodeId);
			jedis.lrem(parentNodeId, 0, currentNodeId);
			String nowPid = currentNodeId+":PID";
			if (jedis.llen(nowPid)>0) {
				List<String>  datads = jedis.lrange(nowPid, 0, -1);
				for (int i = 0; i < datads.size(); i++) {
					delTreeNodes(datads.get(i), nowPid);
				}
			}
		} catch (JedisException e) {
			//LOG.error(e.getMessage()+"执行CodisManager.expire()方法出错.", e);
		}finally{
			if (jedis!=null) {
				jedis.close();
			}
		}
	}
	
	/**
	 * 判断父节点列表是否为空
	 */
	public boolean isNotEmpty(String parentNodeId){
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Long llen = jedis.llen(parentNodeId);
			if(llen > 0){
				return true;
			}
			return false;
		} catch (JedisException e) {
			//LOG.error(e.getMessage()+"执行CodisManager.expire()方法出错.", e);
		}finally{
			if (jedis!=null) {
				jedis.close();
			}
		}
		return false;
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
