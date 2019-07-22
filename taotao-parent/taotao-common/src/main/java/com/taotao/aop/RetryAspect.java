package com.taotao.aop;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;

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
	private static final Map<String, RetryAdapter> RETRY_MAP = new HashMap<>();
	private AtomicLong a;
	private static final long value = 0L;
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Around("@annotation(retryAnnotation)")
	public Object doAround(final ProceedingJoinPoint point, RetryAnnotation retryAnnotation) throws ExecutionException, RetryException {
		Retryer retryAdapter = getRetryAdapter(retryAnnotation);
		return retryAdapter.call(new Callable<Object>() {

			@Override
			public Object call() throws Exception {
				getA();
				a.incrementAndGet();
				Object o = proceed(point);
				System.out.println("测试值:" + a);
				return o;
			}
		});
		
	}
	public AtomicLong getA(){
		if(a == null){
			a = new AtomicLong(value);
		}
		return a;
	}
	
	public void setA(Long dynamicVaule){
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
	private Retryer getRetryAdapter(RetryAnnotation retryAnnotation) {
		RetryAdapter retryAdapter = RETRY_MAP.get(retryAnnotation.retryAdapter());
		if(retryAdapter == null){
			try {
				retryAdapter = ServiceProviderInterface.loadClass(retryAnnotation.retryAdapter());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(retryAdapter == null){
			System.out.println("未能加载重试适配器");
		}
		retryAdapter.setRetryCount(retryAnnotation.retryCount());
		retryAdapter.setResult(retryAnnotation.result());
		retryAdapter.setTimeUnit(retryAnnotation.timeUnit());
		retryAdapter.setWaitTime(retryAnnotation.waitTime());
		RETRY_MAP.put(retryAnnotation.retryAdapter(), retryAdapter);
		return retryAdapter.build();
	}
}
