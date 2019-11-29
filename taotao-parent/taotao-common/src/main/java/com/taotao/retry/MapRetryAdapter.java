package com.taotao.retry;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import com.github.rholder.retry.BlockStrategies;
import com.github.rholder.retry.Retryer;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;
import com.github.rholder.retry.WaitStrategies;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

public class MapRetryAdapter extends AbstractRetryAdapter implements RetryAdapter {
	private Map<String, Object> map = new HashMap<>();
	@SuppressWarnings("unchecked")
	@Override
	public Retryer<Map<String, Object>> build() {
		map.put("responseCode", "001");
		map.put("responseMsg", "系统异常");
		Retryer<Map<String, Object>> newBuilder = RetryerBuilder.<Map<String, Object>>newBuilder()
				.retryIfException()
				.retryIfResult(Predicates.equalTo(map))
				.withWaitStrategy(WaitStrategies.fixedWait(this.waitTime, this.timeUnit))
                //尝试次数
                //.withStopStrategy(StopStrategies.stopAfterAttempt(this.retryCount))
				.withStopStrategy(StopStrategies.neverStop())
                .withBlockStrategy(BlockStrategies.threadSleepStrategy())
                .build();
		return newBuilder;
	}

	
	public static void main(String[] args) throws InterruptedException {
		/*Map<String, Object> map = new HashMap<>();
		map.put("responseCode", "001");
		Predicate<Map<String, Object>> equalTo = Predicates.equalTo(map);
		System.out.println(equalTo);*/
		//AtomicReference<Thread> cas = new AtomicReference<Thread>();
		AtomicBoolean released = new AtomicBoolean(false);
		AtomicInteger init = new AtomicInteger(0);
		try{
			if(released.compareAndSet(false, true)){
				for(int i = 0; i< 10; i++){
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							init.incrementAndGet();
							
							
						}
					}).start();
				}
			}
		}finally{
			released.compareAndSet(true, false);
		}
		Thread.sleep(2000);
		System.out.println(init.get());
		if(released.compareAndSet(false, true)){
			System.out.println("bbbbb");
		}
	}


	@Override
	public void setResult(boolean result) {
		// TODO Auto-generated method stub
		
	}
}
