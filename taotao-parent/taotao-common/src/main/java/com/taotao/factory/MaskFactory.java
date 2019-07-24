package com.taotao.factory;

import java.lang.reflect.Field;
import java.util.Map;

import com.taotao.annotation.MaskCodeAnnotation;
import com.taotao.enums.MaskLevelEnum;
import com.taotao.strategy.MaskCardLevel;
import com.taotao.strategy.MaskCommonLevel;
import com.taotao.strategy.MaskLargeLevel;
import com.taotao.strategy.MaskNameLevel;
import com.taotao.strategy.MaskStrategy;

public class MaskFactory {
	public static <T> MaskExecutor<T> getExecutor(String type, String str, Field field, T t) {
		if ("Ind01".equals(type)) {
			return new IdentityCardExecutor<T>(str, "身份证长度错误", field, t);
		} else if ("Ind03".equals(type) || "Ind20".equals(type)) {
			return new PassportExecutor<T>(str, "护照长度错误", field, t);
		} else if ("Ind04".equals(type) || "Ind15".equals(type) || "Ind16".equals(type)) {
			return new OfficerCardExecutor<T>(str, "军官证长度错误", field, t);
		} else if ("Ind11".equals(type)) {
			return new OtherCardExecutor<T>(str, "其他证件长度错误", field, t);
		} else {
			return new DefaultExecutor<T>(str, "", field, t);
		}
	}

	public static <T> MaskStrategy getMaskStrategy(MaskCodeAnnotation annotation, String str, Field field, T t,
			Map<String, String> describe) {
		MaskLevelEnum maskLevel = annotation.maskLevel();
		if (maskLevel == MaskLevelEnum.LARGELEVEL) {
			return new MaskLargeLevel<T>(str, "长度不可为负数", field, t);
		}
		if (maskLevel == MaskLevelEnum.CARDLEVEL) {
			return new MaskCardLevel<T>(str, "长度不可为负数", field, t, describe.get(annotation.key()));
		}
		if (maskLevel == MaskLevelEnum.NAMELEVEL) {
			return new MaskNameLevel<T>(str, "长度不可为负数", field, t);
		} else {
			return new MaskCommonLevel<T>(str, "长度不可为负数", field, t, maskLevel);
		}
	}
}
