package com.taotao.utils;

import java.lang.reflect.Field;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.taotao.annotation.MaskCodeAnnotation;
import com.taotao.enums.MaskLevelEnum;
import com.taotao.factory.MaskExecutor;
import com.taotao.factory.MaskFactory;
import com.taotao.pojo.BaseDto;

public class MaskFieldByAnnotationUtil<T extends BaseDto> {
	private MaskFieldByAnnotationUtil() {
	}

	private static volatile MaskFieldByAnnotationUtil maskUtil = null;
	private static final int defaultIndex = 1;
	private Logger logger = LoggerFactory.getLogger(MaskFieldByAnnotationUtil.class);

	public void maskField(T t) throws Exception {
		MaskLevelEnum maskLevel;
		/* 获取所有字段 */
		Field[] fields = t.getClass().getDeclaredFields();
		/* 获取父亲类的所有字段 */
		Field[] pields = t.getClass().getSuperclass().getDeclaredFields();
		Map<String, String> describe = BeanUtils.describe(t);
		for (Field field : fields) {
			// 获取字段中包含MaskCodeAnnotation注解的字段
			MaskCodeAnnotation annotation = field.getAnnotation(MaskCodeAnnotation.class);
			if (annotation != null) {
				String str = describe.get(field.getName());
				if (StringUtils.isNotBlank(str)) {
					maskLevel = annotation.maskLevel();
					if (maskLevel == MaskLevelEnum.LARGELEVEL) {
						maskCommon(str, defaultIndex, str.length(), '*', field, t);
					} else if (maskLevel == MaskLevelEnum.DEFAULTLEVEL) {

					} else if (maskLevel == MaskLevelEnum.CARDLEVEL) {
						String key = annotation.key();
						String cardType = describe.get(key);
						MaskExecutor<T> executor = MaskFactory.getExecutor(cardType, str, field, t);
						executor.process();
					} else {
						int beginIndex = maskLevel.getBeginIndex();
						int endIndex = maskLevel.getEndIndex();
						if (endIndex == Integer.MAX_VALUE) {
							logger.info("endIndex" + endIndex);
							maskCommon(str, beginIndex, str.length(), '*', field, t);
						} else {
							maskCommon(str, beginIndex, endIndex, '*', field, t);
						}
					}
				}
			}
		}

		for (Field field : pields) {
			MaskCodeAnnotation annotation = field.getAnnotation(MaskCodeAnnotation.class);
			if (annotation != null) {
				String str = describe.get(field.getName());
				if (StringUtils.isNotBlank(str)) {
					maskLevel = annotation.maskLevel();
					if (maskLevel == MaskLevelEnum.LARGELEVEL) {
						maskCommon(str, defaultIndex, str.length(), '*', field, t);
					} else if (maskLevel == MaskLevelEnum.CARDLEVEL) {
						String key = annotation.key();
						String cardType = describe.get(key);
						MaskExecutor<T> executor = MaskFactory.getExecutor(cardType, str, field, t);
						executor.process();
					} else {
						int beginIndex = maskLevel.getBeginIndex();
						int endIndex = maskLevel.getEndIndex();
						if (endIndex == Integer.MAX_VALUE) {
							logger.info("endIndex" + endIndex);
							maskCommon(str, beginIndex, str.length(), '*', field, t);
						} else {
							maskCommon(str, beginIndex, endIndex, '*', field, t);
						}
					}
				}
			}
		}

	}

	public void maskCommon(String str, int beginIndex, int endIndex, char cover, Field field, T t) throws Exception {
		String maskSubWay = MaskCodeUtil.getMaskSubWay(str, beginIndex, endIndex, '*');
		field.setAccessible(true);
		field.set(t, maskSubWay);
	}

	public static MaskFieldByAnnotationUtil getInstance() {
		if (maskUtil == null) {
			synchronized (MaskFieldByAnnotationUtil.class) {
				if (maskUtil == null) {
					maskUtil = new MaskFieldByAnnotationUtil();
				}
			}
		}
		return maskUtil;
	}
}
