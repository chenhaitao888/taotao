package com.taotao.factory;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

import com.taotao.annotation.MaskCodeAnnotation;
import com.taotao.enums.MaskLevelEnum;
import com.taotao.strategy.MaskCardLevel;
import com.taotao.strategy.MaskCommonLevel;
import com.taotao.strategy.MaskLargeLevel;
import com.taotao.strategy.MaskNameLevel;
import com.taotao.strategy.MaskStrategy;
import com.taotao.utils.MaskCodeUtil;

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

	public static <T> void getMaskStrategy(MaskCodeAnnotation annotation, String str, Field field, T t,
			Map<String, String> describe) throws Exception {
		MaskLevelEnum maskLevel = annotation.maskLevel();
		throwing(str, "长度不可为负数");
		if (maskLevel == MaskLevelEnum.LARGELEVEL) {
			//return new MaskLargeLevel<T>(str, "长度不可为负数", field, t);
			int defaultIndex = 1;
			start(() -> maskCommon(str, defaultIndex, str.length(), '*', field, t));
		}else if (maskLevel == MaskLevelEnum.CARDLEVEL) {
			 //return new MaskCardLevel<T>(str, "长度不可为负数", field, t, describe.get(annotation.key()));
			start(() -> {
				MaskExecutor<T> executor = MaskFactory.getExecutor(describe.get(annotation.key()), str, field, t);
				executor.process();
			});

		}else if (maskLevel == MaskLevelEnum.NAMELEVEL) {
			 //return new MaskNameLevel<T>(str, "长度不可为负数", field, t);
			if (str.length() == 1) {
				//throw new Exception("姓名长度不可为一位");
				return;
			}
			int length = str.length();
			int noMaskLength = length / 2;
			start(() -> maskCommon(str, noMaskLength + 1, str.length(), '*', field, t));
		} else {
			//return new MaskCommonLevel<T>(str, "长度不可为负数", field, t, maskLevel);
			int beginIndex = maskLevel.getBeginIndex();
			int endIndex = maskLevel.getEndIndex();
			start(() -> {
				if (endIndex == Integer.MAX_VALUE) {
					maskCommon(str, beginIndex, str.length(), '*', field, t);
				} else {
					maskCommon(str, beginIndex, endIndex, '*', field, t);
				}
			});
		}
	}

	public static void start(MaskStrategy strategy) throws Exception {
		strategy.mask();
	}

	public static <T> void maskCommon(String str, int beginIndex, int endIndex, char cover, Field field, T t)
			throws Exception {
		String maskSubWay = MaskCodeUtil.getMaskSubWay(str, beginIndex, endIndex, '*');
		field.setAccessible(true);
		field.set(t, maskSubWay);
	}

	public static void throwing(String str, String warning) throws Exception {
		if (str.length() < 0) {
			throw new Exception(warning);
		}
	}
	
	public static void main(String[] args) {
		String a = "1";
		if("1".equals(a)){
			System.out.println("aaa");
		}else if("2".equals(a)){
			System.out.println("bbb");
		}else{
			System.out.println("ccc");
		}
	}
}
