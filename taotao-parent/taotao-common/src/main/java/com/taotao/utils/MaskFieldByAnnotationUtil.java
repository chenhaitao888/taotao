package com.taotao.utils;

import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.taotao.BeanConfig;
import com.taotao.ResolvableParameter;
import com.taotao.annotation.MaskCodeAnnotation;
import com.taotao.factory.MaskFactory;
import com.taotao.pojo.BaseDto;
import com.taotao.strategy.MaskStrategy;

public class MaskFieldByAnnotationUtil<T extends BaseDto> {
	private MaskFieldByAnnotationUtil() {
	}

	private static volatile MaskFieldByAnnotationUtil maskUtil = null;
	private static final int defaultIndex = 1;
	private Logger logger = LoggerFactory.getLogger(MaskFieldByAnnotationUtil.class);

	public void maskField(T t) throws Exception {
		Map<String, String> describe = BeanUtils.describe(t);
		BeanConfig bean = new BeanConfig(describe, t);
		List<ResolvableParameter> restFields = bean.getRestFields(t.getClass());
		for (ResolvableParameter parameter : restFields) {
			mask(parameter);
		}
	}

	private void mask(ResolvableParameter parameter) throws Exception {
		if (parameter.getParameterType() == parameter.P_DEFAULT) {
			maskProtery(parameter);
		}
		if (parameter.getParameterType() == parameter.P_COLLECTION) {
			maskCollectionProperty(parameter);
		}
	}

	private void maskCollectionProperty(ResolvableParameter parameter) throws Exception {
		if (parameter.getBindType() == parameter.B_BEAN) {
			List<Object> list = JSONArray.parseArray(parameter.getFieldValue(), parameter.getBindClass());
			if (CollectionUtils.isNotEmpty(list)) {
				for (Object o : list) {
					maskField((T) o);
				}
			}
			parameter.getField().setAccessible(true);
			parameter.getField().set(parameter.getT(), list);
		}
	}

	@SuppressWarnings("unchecked")
	private void maskProtery(ResolvableParameter parameter) throws Exception {
		if (parameter.getBindType() == parameter.B_STRING) {
			_mask(parameter);
		} else if (parameter.getBindType() == parameter.B_BEAN) {
			Object o = JSON.parseObject(parameter.getFieldValue(), parameter.getBindClass());
			maskField((T) o);
			parameter.getField().setAccessible(true);
			parameter.getField().set(parameter.getT(), o);
		}
	}

	private void _mask(ResolvableParameter parameter) throws Exception {
		if (StringUtils.isNotBlank(parameter.getFieldValue())
				&& parameter.getField().getAnnotation(MaskCodeAnnotation.class) != null) {
			MaskStrategy maskStrategy = MaskFactory.getMaskStrategy(
					parameter.getField().getAnnotation(MaskCodeAnnotation.class), parameter.getFieldValue(),
					parameter.getField(), parameter.getT(), parameter.getDescribe());
			maskStrategy.mask();
		}
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
