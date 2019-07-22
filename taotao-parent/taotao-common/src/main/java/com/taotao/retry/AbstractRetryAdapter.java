package com.taotao.retry;

import java.util.concurrent.TimeUnit;

public abstract class AbstractRetryAdapter {
	public int retryCount;
	public long waitTime;
	public TimeUnit timeUnit;
	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}
	public void setWaitTime(long waitTime) {
		this.waitTime = waitTime;
	}
	public void setTimeUnit(TimeUnit timeUnit) {
		this.timeUnit = timeUnit;
	}
	
}
