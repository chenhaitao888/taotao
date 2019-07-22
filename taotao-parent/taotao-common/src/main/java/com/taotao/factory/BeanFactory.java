package com.taotao.factory;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


public class BeanFactory implements ApplicationContextAware {
	private static ApplicationContext context; 
	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		BeanFactory.context = context;
	}
	
	public static<T> T getBean(Class<T> clazz){
		try {
			return context.getBean(clazz);
		} catch (BeansException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static<T> T getBean(String beanName){
		if(context.containsBean(beanName)){
			return (T) context.getBean(beanName);
		}else{
			return null;
		}
	}
	
}
