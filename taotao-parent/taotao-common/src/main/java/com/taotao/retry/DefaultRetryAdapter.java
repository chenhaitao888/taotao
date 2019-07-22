package com.taotao.retry;


import com.github.rholder.retry.Retryer;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;
import com.github.rholder.retry.WaitStrategies;
import com.google.common.base.Predicates;

public class DefaultRetryAdapter extends AbstractRetryAdapter implements RetryAdapter {
	private boolean result;
	@SuppressWarnings("unchecked")
	@Override
	public Retryer<Boolean> build() {
		Retryer<Boolean> newBuilder = RetryerBuilder.<Boolean>newBuilder()
				.retryIfException()
				.retryIfResult(Predicates.equalTo(result))
				.withWaitStrategy(WaitStrategies.fixedWait(this.waitTime, this.timeUnit))
                //尝试次数
                .withStopStrategy(StopStrategies.stopAfterAttempt(this.retryCount))
                .build();
		return newBuilder;
	}

	public void setResult(boolean result) {
		this.result = result;
	}
}
