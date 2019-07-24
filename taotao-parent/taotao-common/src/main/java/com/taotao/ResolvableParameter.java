package com.taotao;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

import com.taotao.pojo.BaseDto;
import com.taotao.utils.GenericUtils;

/**
 * 
 * <p>Title: ResolvableParameter</p>
 * <p>@Description:TODO</p>
 * <p>Company: www.chenhaitao.com</p>	
 * @author chenhaitao
 * @date:2019年7月24日下午7:25:46
 * @version:1.0
 * @param <T>
 */
public abstract class ResolvableParameter<T extends BaseDto> {
	private int parameterType;
	private Class<?> bindClass;
	private int bindType;
	private Class<?> paramClass;
	private String fieldValue;
	protected Field field;
	protected Class<?> targetClass;
	private Map<String, String> describe;
	private T t;
	public static final int P_DEFAULT = 0;
	public static final int P_COLLECTION = 1;
	public static final int B_UNKOWN = 0;
	public static final int B_BEAN = 1;
	public static final int B_STRING = 2;

	public ResolvableParameter(Class<?> paramClass, Type genericType, String fieldValue, Map<String, String> describe,
			T t) {
		this.paramClass = paramClass;
		this.fieldValue = fieldValue;
		this.describe = describe;
		this.t = t;
		if (Collection.class.isAssignableFrom(paramClass)) {
			parameterType = P_COLLECTION;
			Class<?> gclass = GenericUtils.getCollectionGenericType(genericType);
			if (gclass == null) {
				this.bindType = B_UNKOWN;
			} else {
				resolveBindClass(gclass);
			}
		} else {
			resolveBindClass(paramClass);
		}
	}

	private void resolveBindClass(Class<?> gclass) {
		this.bindClass = gclass;
		resolveBindType(bindClass);
	}

	private void resolveBindType(Class<?> gclass) {
		if (BaseDto.class.isAssignableFrom(gclass)) {
			this.bindType = B_BEAN;
		} else if (gclass.equals(String.class)) {
			this.bindType = B_STRING;
		} else {
			this.bindType = B_UNKOWN;
		}
	}

	public abstract void set(Object target, Object value) throws Exception;
	public abstract Object get(Object target) throws Exception;
	public int getParameterType() {
		return parameterType;
	}

	public void setParameterType(int parameterType) {
		this.parameterType = parameterType;
	}

	public Class<?> getBindClass() {
		return bindClass;
	}

	public void setBindClass(Class<?> bindClass) {
		this.bindClass = bindClass;
	}

	public int getBindType() {
		return bindType;
	}

	public void setBindType(int bindType) {
		this.bindType = bindType;
	}

	public Class<?> getParamClass() {
		return paramClass;
	}

	public void setParamClass(Class<?> paramClass) {
		this.paramClass = paramClass;
	}

	public String getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public Class<?> getTargetClass() {
		return targetClass;
	}

	public void setTargetClass(Class<?> targetClass) {
		this.targetClass = targetClass;
	}

	public Map<String, String> getDescribe() {
		return describe;
	}

	public void setDescribe(Map<String, String> describe) {
		this.describe = describe;
	}

	public T getT() {
		return t;
	}

	public void setT(T t) {
		this.t = t;
	}
	
}
