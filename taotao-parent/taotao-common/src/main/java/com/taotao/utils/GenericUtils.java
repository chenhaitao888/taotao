package com.taotao.utils;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 泛型公共类
 * <p>
 * Title: GenericUtils
 * </p>
 * <p>
 * @Description:TODO
 * </p>
 * <p>
 * Company: www.chenhaitao.com
 * </p>
 * 
 * @author chenhaitao
 * @date:2019年7月24日下午7:13:04
 * @version:1.0
 */
public class GenericUtils {
	public static Type getGenericType(Method setM, Method getM) {
		if (setM != null) {
			return setM.getGenericReturnType();
		}
		if (getM != null) {
			return getM.getGenericParameterTypes()[0];
		}
		return null;
	}

	public static Class<?> getCollectionGenericType(Type genericType) {
		if (genericType == null) {
			return null;
		}
		return getGenericClass(genericType, 0);
	}

	private static Class<?> getGenericClass(Type t, int i) {
		if (t instanceof ParameterizedType) {
			ParameterizedType pt = (ParameterizedType) t;
			Type[] types = pt.getActualTypeArguments();
			if (types != null && types.length > i) {
				Type genericType = pt.getActualTypeArguments()[i];
				if (genericType != null && genericType instanceof Class) {
					Class<?> temp = (Class<?>) genericType;
					return temp;
				} else if (genericType != null && genericType instanceof ParameterizedType) {
					pt = (ParameterizedType) genericType;
					return (Class<?>) pt.getRawType();
				}
			}
		}
		return null;
	}
}
