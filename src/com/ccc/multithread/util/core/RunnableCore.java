package com.ccc.multithread.util.core;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

public abstract class RunnableCore implements Runnable {
	
	private CountDownLatch countDownLatch;
	private ConcurrentHashMap<Integer, Long> timeCosts;
	private Integer index;
	
	public abstract void init();
	
	public abstract void work();
	
	@Override
	public void run() {
		long start = System.currentTimeMillis();
		this.work();
		long duration = System.currentTimeMillis() - start;
		this.timeCosts.put(this.index, duration);
		this.countDownLatch.countDown();
	}

	public CountDownLatch getCountDownLatch() {
		return countDownLatch;
	}

	public void setCountDownLatch(CountDownLatch countDownLatch) {
		this.countDownLatch = countDownLatch;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public ConcurrentHashMap<Integer, Long> getTimeCosts() {
		return timeCosts;
	}

	public void setTimeCosts(ConcurrentHashMap<Integer, Long> timeCosts) {
		this.timeCosts = timeCosts;
	}
	
}
