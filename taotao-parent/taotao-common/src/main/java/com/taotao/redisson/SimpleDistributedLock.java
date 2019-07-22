package com.taotao.redisson;

import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

public class SimpleDistributedLock implements DistributedFactory {
	
	private static final long DEFAULT_RELEASE_TIMEOUT = 5;
	
	private static final long DEFAULT_WAIT_TIMEOUT = 30;
	
	private static final TimeUnit DEFAULT_TIME_UNIT = TimeUnit.SECONDS;
	private RedissonClient redisson;
	
	public SimpleDistributedLock(RedissonClient redisson){
		this.redisson = redisson;
	}
	@Override
	public <T> T lock(DistributedLockCallBack<T> callBack, boolean fairLock) {
		
		return lock(callBack, DEFAULT_RELEASE_TIMEOUT, DEFAULT_TIME_UNIT, fairLock);
	}

	private RLock getLock(String lockName, boolean fairLock) {
		RLock lock;
		if(fairLock){
			lock = redisson.getFairLock(lockName);
		}else{
			lock = redisson.getLock(lockName);
		}
		return lock;
	}
	@Override
	public <T> T lock(DistributedLockCallBack<T> callBack, long releaseTime, TimeUnit timeUnit, boolean fairLock) {
		RLock lock = getLock(callBack.lockName(), fairLock);
		try {
			lock.lock(releaseTime, DEFAULT_TIME_UNIT);
			return callBack.process();
		} finally {
			if (lock != null && lock.isLocked()) {
                lock.unlock();
            }
		}
	}

	@Override
	public <T> T tryLock(DistributedLockCallBack<T> callBack, boolean fairLock) {
		return tryLock(callBack, DEFAULT_RELEASE_TIMEOUT, DEFAULT_WAIT_TIMEOUT, DEFAULT_TIME_UNIT, fairLock);
	}

	@Override
	public <T> T tryLock(DistributedLockCallBack<T> callBack, long releaseTime, long waitTime, TimeUnit timeUnit,
			boolean fairLock) {
		RLock lock = getLock(callBack.lockName(), fairLock);
		T t = null;
		try {
			if(lock.tryLock(waitTime, releaseTime, timeUnit)){
				t = callBack.process();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			if (lock != null && lock.isLocked()) {
                lock.unlock();
            }
		}
		return t;
	}
	public RedissonClient getRedisson() {
		return redisson;
	}
	public void setRedisson(RedissonClient redisson) {
		this.redisson = redisson;
	}
	
}
