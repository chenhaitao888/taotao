package com.taotao.retry;

import java.util.concurrent.TimeUnit;

import com.github.rholder.retry.Retryer;

public interface RetryAdapter {
	<T> Retryer<T> build();
	void setRetryCount(int retryCount);
	void setWaitTime(long waitTime);
	void setTimeUnit(TimeUnit timeUnit);
	void setResult(boolean result);
}
