package com.taotao;

import java.io.FileInputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;


import com.taotao.helper.IOHelper;

public class CustomClassLoader extends ClassLoader{
	private Map<String, Class<?>> classMap = new HashMap<>();
	private static ClassLoader myClassLoader;
	
	public synchronized static void setSystemClassLoader(ClassLoader systemClassLoader) {
		CustomClassLoader.setMyClassLoader(systemClassLoader);
	}
	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		return loadClass(name, false);
	}

	@Override
	protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
		Class<?> clazz = classMap.get(name);
		 if (clazz != null) {
	            return clazz;
	        }
		synchronized (getClassLoadingLock(name)) {
			clazz = findLoadedClass(name);
			if (clazz == null) {
				clazz = myClassLoader.loadClass(name);
				if(clazz == null){
					clazz = findClass(name);
				}
			}
			if (resolve) {
				resolveClass(clazz);
			}
			return clazz;
		}
	}
	
	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		try {
			FileInputStream in = new FileInputStream(nameToPath(name, false));
			byte[] bytes = IOHelper.readStreamBytesAndClose(in);
			Class<?> defineClass = defineClass(name, bytes, 0, bytes.length);
			if(defineClass != null){
				classMap.put(name, defineClass);
			}
			return defineClass;
		} catch (Exception e) {
			System.out.println("自定义类加载异常");
			e.printStackTrace();
			throw new ClassNotFoundException();
		}
	}
	
	 private String nameToPath(String binaryName, boolean withLeadingSlash) {
	        StringBuilder path = new StringBuilder(7 + binaryName.length());
	        if (withLeadingSlash) {
	            path.append('/');
	        }
	        path.append(binaryName.replace('.', '/'));
	        path.append(".class");
	        return path.toString();
	    }
	public static ClassLoader getMyClassLoader() {
		return myClassLoader;
	}
	public static void setMyClassLoader(ClassLoader myClassLoader) {
		CustomClassLoader.myClassLoader = myClassLoader;
	}
	
	public static void main(String[] args) throws Exception {
		CustomClassLoader l = new CustomClassLoader();
		l.setSystemClassLoader(Thread.currentThread().getContextClassLoader());
		Class<?> loadClass = l.loadClass("com.taotao.retry.DefaultRetryAdapter");
		Constructor<?> resultConstructor = loadClass.getConstructor();
 		Object newInstance = resultConstructor.newInstance();
 		
 		System.out.println(newInstance);
	}
}
