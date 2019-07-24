package com.taotao;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.fastjson.JSON;
import com.taotao.pojo.BaseDto;

/**
 * 
 * <p>Title: BeanConfig</p>
 * <p>@Description:TODO</p>
 * <p>Company: www.chenhaitao.com</p>	
 * @author chenhaitao
 * @date:2019年7月24日下午8:32:27
 * @version:1.0
 * @param <T>
 */
public class BeanConfig<T extends BaseDto> {
	private Map<Class<?>, List<ResolvableParameter>> cache;
	private Map<String, String> describe;
	private T t;

	public BeanConfig(Map<String, String> describe, T t) {
		this.cache = new ConcurrentHashMap<>();
		this.describe = describe;
		this.t = t;
	}

	public BeanConfig() {
		this.cache = new ConcurrentHashMap<>();
	}

	public List<ResolvableParameter> getRestFields(Class<?> clazz) throws IllegalAccessException {
		List<ResolvableParameter> fields = cache.get(clazz);
		if (fields == null) {
			synchronized (cache) {
				fields = getParameters(clazz);
				cache.put(clazz, fields);
			}
		}
		if (fields.size() == 0) {
			return null;
		} else {
			return fields;
		}
	}

	private List<ResolvableParameter> getParameters(Class<?> clazz) throws IllegalAccessException {
		List<ResolvableParameter> fields = new ArrayList<>();
		Field clazzFields[] = clazz.getDeclaredFields();
		Class<?> superClass = clazz.getSuperclass();
		if (superClass != null && !superClass.equals(Object.class)) {
			fields.addAll(getParameters(superClass));
		}
		for (Field clazzField : clazzFields) {
			if (Modifier.isStatic(clazzField.getModifiers())) {
				continue;
			}
			if (Modifier.isTransient(clazzField.getModifiers())) {
				continue;
			}
			Method getM = getWriteMethod(clazzField, clazz);
			Method setM = getReadMethod(clazzField, clazz);
			if (setM != null || getM != null) {
				clazzField.setAccessible(true);
				fields.add(
						new MaskParameter(
								clazzField, setM, getM, clazz, clazzField.get(t) instanceof String
										? clazzField.get(t).toString() : JSON.toJSONString(clazzField.get(t)),
								describe, t));
			}
		}
		return fields;
	}

	public Method getReadMethod(Field clazzField, Class<?> clazz) {
		String readMethodName = null;
		Class<?> fieldType = clazzField.getType();
		String name = clazzField.getName();
		if (fieldType == boolean.class || fieldType == null) {
			readMethodName = "is" + capitalizeBoolean(name);
		} else {
			readMethodName = "get" + capitalize(name);
		}
		try {
			Method[] methods = clazz.getDeclaredMethods();
			Method foundMethod = null;
			for (Method method : methods) {
				if ((method.getName().startsWith("is") || method.getName().startsWith("get"))) {
					if (method.getName().equals(readMethodName) && method.getParameterTypes().length == 0
							&& method.getReturnType() == fieldType) {
						int mods = method.getModifiers();
						if (Modifier.isPublic(mods)) {
							foundMethod = method;
							break;
						}
					}
					// 特殊处理boolean尅型，如：getIsxxx
					if (fieldType == boolean.class && foundMethod == null) {
						String readMethodName2 = "get" + capitalize(name);
						if (method.getName().equals(readMethodName2)) {
							int mods = method.getModifiers();
							if (Modifier.isPublic(mods)) {
								foundMethod = method;
								break;
							}
						}
					}
				}
			}
			return foundMethod;
		} catch (Exception e) {
		}
		return null;
	}

	public Method getWriteMethod(Field clazzField, Class<?> clazz) {
		String writeMethodName = null;
		String name = clazzField.getName();
		Class<?> fieldType = clazzField.getType();
		if (fieldType == boolean.class || fieldType == null) {
			writeMethodName = "set" + capitalizeBoolean(name);
		} else {
			writeMethodName = "set" + capitalize(name);
		}
		Method[] methods = clazz.getDeclaredMethods();
		Method foundMethod = null;
		for (Method method : methods) {
			if (method.getName().startsWith("set")) {
				if (method.getName().equals(writeMethodName) && method.getParameterTypes().length == 1
						&& method.getParameterTypes()[0] == fieldType) {
					int mods = method.getModifiers();
					if (Modifier.isPublic(mods)) {
						foundMethod = method;
						break;
					}
				}
				// 特殊处理boolean类型的，如：setIsxxx
				if (fieldType == boolean.class && foundMethod == null) {
					String writeMethodName2 = "set" + capitalize(name);
					if (method.getName().equals(writeMethodName2)) {
						int mods = method.getModifiers();
						if (Modifier.isPublic(mods)) {
							foundMethod = method;
							break;
						}
					}
				}
			}
		}
		return foundMethod;
	}

	public String capitalizeBoolean(String name) {
		if (name == null || name.length() == 0) {
			return name;
		}

		if (!name.startsWith("is")) {
			return capitalize(name);
		} else {
			// 布尔类型，is开头，第三位大写，则要去掉is
			if (Character.isUpperCase(name.charAt(2))) {
				return name.substring(2, name.length());
			}
			return capitalize(name);
		}
	}

	public String capitalize(String name) {
		if (name == null || name.length() == 0) {
			return name;
		}
		// 如果第二位是大写,则返回原值
		if (name.length() > 1 && Character.isUpperCase(name.charAt(1))) {
			return name;
		}
		// 其他第一位则是大写
		return name.substring(0, 1).toUpperCase(Locale.ENGLISH) + name.substring(1);
	}
}
