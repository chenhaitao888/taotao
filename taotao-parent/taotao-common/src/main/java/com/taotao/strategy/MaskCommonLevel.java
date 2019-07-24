package com.taotao.strategy;

import java.lang.reflect.Field;

import com.taotao.enums.MaskLevelEnum;
import com.taotao.factory.MaskBase;

public class MaskCommonLevel<T> extends MaskBase<T> implements MaskStrategy {
	private MaskLevelEnum maskLevel;

	public MaskCommonLevel(String str, String warning, Field field, T t) {
		super(str, warning, field, t);
	}

	public MaskCommonLevel(String str, String warning, Field field, T t, MaskLevelEnum maskLevel) {
		super(str, warning, field, t);
		this.maskLevel = maskLevel;
	}

	@Override
	public void mask() throws Exception {
		throwing();
		int beginIndex = maskLevel.getBeginIndex();
		int endIndex = maskLevel.getEndIndex();
		if (endIndex == Integer.MAX_VALUE) {
			logger.info("endIndex: " + endIndex);
			maskCommon(str, beginIndex, str.length(), '*', field, t);
		} else {
			maskCommon(str, beginIndex, endIndex, '*', field, t);
		}
	}
}
