package com.taotao.spi;

import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.Enumeration;
import java.util.Map;

import com.taotao.CustomClassLoader;


/**
 * SPI
 * <p>Title: ServiceProviderInterface</p>
 * <p>@Description:TODO</p>
 * <p>Company: www.chenhaitao.com</p>	
 * @author chenhaitao
 * @date:2019年6月3日下午8:32:53
 * @version:1.0
 */
public class ServiceProviderInterface {
	private static CustomClassLoader classLoader = new CustomClassLoader();;
	private static final ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
	@SuppressWarnings("unchecked")
	public static <T> T loadClass(String clazzName) throws Exception{
		classLoader.setSystemClassLoader(Thread.currentThread().getContextClassLoader());
		Class<?> loadClass = classLoader.loadClass(clazzName);
		Constructor<?> constructor = loadClass.getConstructor();
		return (T) constructor.newInstance();
	}
}
