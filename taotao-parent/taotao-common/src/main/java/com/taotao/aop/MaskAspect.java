package com.taotao.aop;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.taotao.annotation.MaskAnnotation;
import com.taotao.pojo.BaseDto;
import com.taotao.utils.MaskFieldByAnnotationUtil;
import com.taotao.utils.ResultMapUtils;

@Aspect
@Component
public class MaskAspect {
	@SuppressWarnings("rawtypes")
	private static final MaskFieldByAnnotationUtil util = MaskFieldByAnnotationUtil.getInstance();
	private Logger logger = LoggerFactory.getLogger(MaskAspect.class);

	@SuppressWarnings("unchecked")
	@Around("@annotation(mask)")
	public Object mask(ProceedingJoinPoint point, MaskAnnotation mask) {
		Object result = null;
		try {
			result = point.proceed();
			Map<String, Object> map = (Map<String, Object>) result;
			Object data = map.get("data");
			if (data == null) {
				return result;
			}
			Object[] o = null;
			if (!data.getClass().isArray()) {
				o = new Object[] { data };
			} else {
				o = (Object[]) data;
			}
			for (Object object : o) {
				if (object instanceof List) {
					List<?> list = (List<?>) object;
					if (CollectionUtils.isEmpty(list)) {
						return ResultMapUtils.response("003", "未查到数据");
					}
					for (Object object2 : list) {
						util.maskField((BaseDto) object2);
					}
				}
				if (object instanceof BaseDto) {
					util.maskField((BaseDto) object);
				}
			}
		} catch (Throwable e) {
			logger.info("掩码异常：", e);
			return ResultMapUtils.response("002", "系统异常");
		}
		return result;
	}
}