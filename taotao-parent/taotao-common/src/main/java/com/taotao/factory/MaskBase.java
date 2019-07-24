package com.taotao.factory;

import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.taotao.utils.MaskCodeUtil;

public abstract class MaskBase<T> {
	public Logger logger = LoggerFactory.getLogger(MaskBase.class);
	public T t;
	public String str;
	public String warning;
	public Field field;

	public MaskBase(String str, String warning, Field field, T t) {
		this.str = str;
		this.warning = warning;
		this.field = field;
		this.t = t;
	}

	public void maskCommon(String str, int beginIndex, int endIndex, char cover, Field field, T t) throws Exception {
		String maskSubWay = MaskCodeUtil.getMaskSubWay(str, beginIndex, endIndex, '*');
		field.setAccessible(true);
		field.set(t, maskSubWay);
	}

	public void throwing() throws Exception {
		if (str.length() < 0) {
			throw new Exception(warning);
		}
	}
}
