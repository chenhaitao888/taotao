package com.taotao.redisson;

import java.io.IOException;
import java.io.InputStream;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

public class RedssionFactoryBean implements FactoryBean<RedissonClient>, InitializingBean{
	private RedissonClient redisson;
	
	@Value("classpath:resource/redisson.yml")
	private Resource resource;
	@Override
	public void afterPropertiesSet() throws Exception {
		Config config = loadConifg();
		redisson = Redisson.create(config);
	}

	private Config loadConifg() {
		Config config = new Config();
		if(resource != null){
			try {
				InputStream inputStream = resource.getInputStream();
				config = Config.fromYAML(inputStream);
				return config;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return config;
	}




	@Override
	public RedissonClient getObject() throws Exception {
		return redisson;
	}



	@Override
	public Class<?> getObjectType() {
		return (this.redisson != null ? this.redisson.getClass() : RedissonClient.class);
	}


	@Override
	public boolean isSingleton() {
		return true;
	}

}
