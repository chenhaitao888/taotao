package com.taotao.utils;

import java.util.HashMap;
import java.util.Map;

public class ResultMapUtils {
	public static Map<String, Object> response(String responseCode, String responseMessage){
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("responseCode", responseCode);
		resultMap.put("responseMessage", responseMessage);
		return resultMap;
	}
}
