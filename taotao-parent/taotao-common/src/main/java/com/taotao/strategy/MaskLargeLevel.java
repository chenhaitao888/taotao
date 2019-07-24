package com.taotao.strategy;

import java.lang.reflect.Field;

import com.taotao.factory.MaskBase;
/**
 * 全部掩码
 * <p>Title: MaskLargeLevel</p>
 * <p>@Description:TODO</p>
 * <p>Company: www.chenhaitao.com</p>	
 * @author chenhaitao
 * @date:2019年7月24日下午8:48:08
 * @version:1.0
 * @param <T>
 */
public class MaskLargeLevel<T> extends MaskBase<T> implements MaskStrategy {
	private static final int defaultIndex = 1;

	public MaskLargeLevel(String str, String warning, Field field, T t) {
		super(str, warning, field, t);
	}

	@Override
	public void mask() throws Exception {
		throwing();
		maskCommon(str, defaultIndex, str.length(), '*', field, t);
	}
}
