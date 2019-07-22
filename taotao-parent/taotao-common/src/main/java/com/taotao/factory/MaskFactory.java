package com.taotao.factory;

import java.lang.reflect.Field;

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
}
