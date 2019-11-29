package com.taotao.factory;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

import com.taotao.utils.ConfigUtils;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

public class JedisClusterFactory implements FactoryBean<JedisCluster>, InitializingBean{
	
	private JedisCluster cluster;
	@Value("classpath:resource/redis.properties")
	private Resource resource;
	private GenericObjectPoolConfig genericObjectPoolConfig;
	private Integer timeout;  
    private Integer maxAttempts; 
	@Override
	public void afterPropertiesSet() throws Exception {
		Properties prop = new Properties();
		prop.load(this.resource.getInputStream());
		Set<HostAndPort> nodes = new HashSet<HostAndPort>();
		String servers = (String) prop.get("servers");
		Set<String> addrs = ConfigUtils.split(servers);
		for (String addr : addrs) {
			String[] ipAndPort = addr.split(":");
			nodes.add(new HostAndPort(ipAndPort[0], Integer.parseInt(ipAndPort[1])));
		}
		cluster = new JedisCluster(nodes, timeout, maxAttempts, genericObjectPoolConfig);
	}
	
	@Override
	public JedisCluster getObject() throws Exception {
		return cluster;
	}

	@Override
	public Class<?> getObjectType() {
		return JedisCluster.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public Integer getTimeout() {
		return timeout;
	}

	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
	}

	public Integer getMaxAttempts() {
		return maxAttempts;
	}

	public void setMaxAttempts(Integer maxAttempts) {
		this.maxAttempts = maxAttempts;
	}

	public GenericObjectPoolConfig getGenericObjectPoolConfig() {
		return genericObjectPoolConfig;
	}

	public void setGenericObjectPoolConfig(GenericObjectPoolConfig genericObjectPoolConfig) {
		this.genericObjectPoolConfig = genericObjectPoolConfig;
	}
	
}
