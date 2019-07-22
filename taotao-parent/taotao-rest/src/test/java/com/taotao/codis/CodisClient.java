package com.taotao.codis;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.taotao.rest.dao.CodisManager;

import io.codis.jodis.JedisResourcePool;
import io.codis.jodis.RoundRobinJedisPool;
import redis.clients.jedis.Jedis;
/**
 * codis测试
 * <p>Title: CodisClient</p>
 * <p>@Description:TODO</p>
 * <p>Company: www.chenhaitao.com</p>	
 * @author chenhaitao
 * @date:2017年3月3日下午2:57:36
 * @version:1.0
 */
public class CodisClient {
	@Test
	public void testCodis(){
		JedisResourcePool jedisPool = RoundRobinJedisPool.create()
		        .curatorClient("192.168.98.143:2181", 30000).zkProxyDir("/jodis/test").build();
		try (Jedis jedis = jedisPool.getResource()) {
		    jedis.set("foo", "bar");
		    String value = jedis.get("foo");
		    System.out.println(value);
		}
	}
	
	@Test
	public void testCodis2(){
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");
		CodisManager bean = (CodisManager) applicationContext.getBean("codisManager");
		System.out.println(bean.get("foo"));
	}
}
