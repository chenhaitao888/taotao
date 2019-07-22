package com.taotao.retry;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.github.rholder.retry.RetryException;
import com.github.rholder.retry.Retryer;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;
import com.github.rholder.retry.WaitStrategies;
import com.google.common.base.Predicates;

public class RetryTest {
	public void retryTest(){
		Retryer<Boolean> newBuilder = RetryerBuilder.<Boolean>newBuilder()
											.retryIfException()
											.retryIfResult(Predicates.equalTo(false))
											.withWaitStrategy(WaitStrategies.fixedWait(3, TimeUnit.SECONDS))
							                //尝试次数
							                .withStopStrategy(StopStrategies.stopAfterAttempt(3))
							                .build();
		try {
			newBuilder.call(new Callable<Boolean>() {
				
				@Override
				public Boolean call() throws Exception {
					System.out.println("重试测试");
					return false;
				}
			});
		} catch (ExecutionException | RetryException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		RetryTest test = new RetryTest();
		test.retryTest();
	}
 }
