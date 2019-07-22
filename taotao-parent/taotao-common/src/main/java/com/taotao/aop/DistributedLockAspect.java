package com.taotao.aop;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taotao.annotation.DistributedLock;
import com.taotao.redisson.DistributedFactory;
import com.taotao.redisson.DistributedLockCallBack;

@Aspect
@Component
public class DistributedLockAspect {
	@Autowired
	private DistributedFactory distributedFactory;
	
	@Around("@annotation(distributedLock)")
	public Object doAround(ProceedingJoinPoint point, DistributedLock distributedLock) throws Exception{
		Class<? extends Object> targetClass = point.getTarget().getClass();
		//使用注解的方法
		String methodName = point.getSignature().getName();
		Class<?>[] parameterTypes = ((MethodSignature) point.getSignature()).getMethod().getParameterTypes();
		Method method = targetClass.getMethod(methodName, parameterTypes);
		Object[] args = point.getArgs();
		final String lockName = getLockName(method, args);
		return lock(point, method, lockName);
		
	}

	private Object lock(ProceedingJoinPoint point, Method method, String lockName) {
		DistributedLock annotation = method.getAnnotation(DistributedLock.class);
		boolean fairLock = annotation.fairLock();
        boolean tryLock = annotation.tryLock();
        if (tryLock) {
            return tryLock(point, annotation, lockName, fairLock);
        } else {
            return lock(point,annotation, lockName, fairLock);
        }
	}

	private Object lock(final ProceedingJoinPoint point, DistributedLock annotation, final String lockName, boolean fairLock) {
		long leaseTime = annotation.leaseTime();
		TimeUnit timeUnit = annotation.timeUnit();
		return distributedFactory.lock(new DistributedLockCallBack<Object>() {

			@Override
			public Object process() {
				return proceed(point);
			}

			@Override
			public String lockName() {
				return lockName;
			}
		}, leaseTime, timeUnit, fairLock);
	}

	protected Object proceed(ProceedingJoinPoint point) {
		try {
            return point.proceed();
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
	}

	private Object tryLock(final ProceedingJoinPoint point, DistributedLock annotation, final String lockName, boolean fairLock) {
		long waitTime = annotation.waitTime(),
                leaseTime = annotation.leaseTime();
        TimeUnit timeUnit = annotation.timeUnit();
		return distributedFactory.tryLock(new DistributedLockCallBack<Object>() {

			@Override
			public Object process() {
				return proceed(point);
			}

			@Override
			public String lockName() {
				return lockName;
			}
		}, leaseTime, waitTime, timeUnit, fairLock);
	}

	private String getLockName(Method method, Object[] args) {
		Objects.requireNonNull(method);
		DistributedLock annotation = method.getAnnotation(DistributedLock.class);
		String lockName = annotation.lockName(),param = annotation.param();
		if(StringUtils.isBlank(lockName)){
			if(args.length > 0){
				if(StringUtils.isNotBlank(param)){
					Object arg;
					if(annotation.argNum() > 0){
						arg = args[annotation.argNum() - 1];
					}else{
						arg = args[0];
					}
					lockName = String.valueOf(getParam(arg, param));
				}else if(annotation.argNum() > 0){
					lockName = args[annotation.argNum() - 1].toString();
				}
				return lockName;
			}
		}else{
			String lockSuffix = annotation.lockSuffix();
			String separator = annotation.separator();
			StringBuilder builder = new StringBuilder();
			builder.append(lockName);
			if(StringUtils.isNotBlank(lockSuffix)){
				builder.append(separator).append(lockSuffix);
			}
			lockName = builder.toString();
			return lockName;
		}
		throw new IllegalArgumentException("Can't get or generate lockName accurately!");
	}

	private Object getParam(Object arg, String param) {
		if(StringUtils.isNotBlank(param) && arg != null){
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
