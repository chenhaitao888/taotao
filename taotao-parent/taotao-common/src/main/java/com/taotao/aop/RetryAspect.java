package com.taotao.aop;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.github.rholder.retry.RetryException;
import com.github.rholder.retry.Retryer;
import com.taotao.annotation.RetryAnnotation;
import com.taotao.retry.RetryAdapter;
import com.taotao.spi.ServiceProviderInterface;

@Aspect
@Component
public class RetryAspect {
	private static final Map<String, RetryAdapter> RETRY_MAP = new ConcurrentHashMap<>();
	private AtomicLong a;
	private static final long value = 0L;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Around("@annotation(retryAnnotation)")
	public Object doAround(final ProceedingJoinPoint point, RetryAnnotation retryAnnotation)
			throws ExecutionException, RetryException {
		Object[] args = point.getArgs();
		Retryer retryAdapter = getRetryAdapter(retryAnnotation, args);
		return retryAdapter.call(new Callable<Object>() {

			@Override
			public Object call() {
				getA();
				a.incrementAndGet();
				Object o = proceed(point);
				//System.out.println("测试值:" + a);
				return o;
			}
		});

	}

	public AtomicLong getA() {
		if (a == null) {
			a = new AtomicLong(value);
		}
		return a;
	}

	public void setA(Long dynamicVaule) {
		a = new AtomicLong(dynamicVaule);
	}

	protected Object proceed(ProceedingJoinPoint point) {
		try {
			return point.proceed();
		} catch (Throwable throwable) {
			throw new RuntimeException(throwable);
		}
	}

	@SuppressWarnings("rawtypes")
	private Retryer getRetryAdapter(RetryAnnotation retryAnnotation, Object[] args) {
		int retryCount = getRetryCount(retryAnnotation, args);
		RetryAdapter retryAdapter = RETRY_MAP.get(retryAnnotation.retryAdapter() + retryCount);
		
			if (retryAdapter == null) {
				synchronized (RETRY_MAP) {
				try {
					retryAdapter = ServiceProviderInterface.loadClass(retryAnnotation.retryAdapter());
					if (retryAdapter == null) {
						throw new RuntimeException("未能加载重试适配器");
					}
					retryAdapter.setRetryCount(retryCount);
					retryAdapter.setResult(retryAnnotation.result());
					retryAdapter.setTimeUnit(retryAnnotation.timeUnit());
					retryAdapter.setWaitTime(retryAnnotation.waitTime());
					RETRY_MAP.put(retryAnnotation.retryAdapter(), retryAdapter);
				} catch (Exception e) {
					throw new RuntimeException("类加载失败", e);
				}
			}
		}
		return retryAdapter.build();
	}

	private int getRetryCount(RetryAnnotation retryAnnotation, Object[] args) {
		int retryCount = retryAnnotation.retryCount();
		if (args.length > 0) {
			Object arg;
			if (retryAnnotation.argNum() > 0) {
				arg = args[retryAnnotation.argNum() - 1];
			} else {
				arg = args[0];
			}
			if (arg instanceof Integer) {
				retryCount = (int) arg;
				if(retryCount < 0){
					retryCount = 0;
				}
			}
			
		}
		return retryCount;
	}

	private Object getParam(Object arg, String param) {
		if (StringUtils.isNotBlank(param) && arg != null) {
			try {
				Object result = PropertyUtils.getProperty(arg, param);
				return result;
			} catch (NoSuchMethodException e) {
				throw new IllegalArgumentException(arg + "没有属性" + param + "或未实现get方法。", e);
			} catch (Exception e) {
				throw new RuntimeException("", e);
			}
		}
		return null;
	}
}
