package com.taotao.utils;

import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;


public class ConfigUtils {
	public static Set<String> split(String text){
		if(text != null && text.length() > 0){
			Set<String> set = new LinkedHashSet<>();
			String[] split = StringUtils.split(text, ",");
			for (String string : split) {
				if(string != null && (string = string.trim()).length() > 0){
					set.add(string);
				}
			}
			return set;
		}
		return null;
	}
}
