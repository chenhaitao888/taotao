package com.taotao;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import com.taotao.pojo.BaseDto;
import com.taotao.utils.GenericUtils;

/**
 * 
 * <p>Title: MaskParameter</p>
 * <p>@Description:TODO</p>
 * <p>Company: www.chenhaitao.com</p>	
 * @author chenhaitao
 * @date:2019年7月24日下午7:28:24
 * @version:1.0
 * @param <T>
 */
public class MaskParameter<T extends BaseDto> extends ResolvableParameter<T> {

	private Method setM;

	private Method getM;

	public MaskParameter(Field field, Method setM, Method getM, Class<?> targetClass, String fieldValue,
			Map<String, String> describe, T t) {
		super(field.getType(), GenericUtils.getGenericType(setM, getM), fieldValue, describe, t);
		this.setM = setM;
		this.getM = getM;
		this.targetClass = targetClass;
		this.field = field;
	}

	@Override
	public void set(Object target, Object value) throws Exception {
		if (setM == null) {
			return;
		}
		setM.invoke(target, value);
	}

	@Override
	public Object get(Object target) throws Exception {
		if (getM == null) {
			return null;
		}
		return getM.invoke(target);
	}

	public Method getSetM() {
		return setM;
	}

	public void setSetM(Method setM) {
		this.setM = setM;
	}

	public Method getGetM() {
		return getM;
	}

	public void setGetM(Method getM) {
		this.getM = getM;
	}

}
