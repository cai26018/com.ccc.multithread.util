package com.ccc.multithread.util.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MultiThreadCore {
	
	private static Logger logger = LoggerFactory.getLogger(MultiThreadCore.class);
	
	private Integer threadNum;
	private Class<? extends RunnableCore> runnableCoreClass;

	private CountDownLatch countDownLatch;
	private ConcurrentHashMap<Integer, Long> timeCosts;
	private List<RunnableCore> runnableCores;

	private long totalCost;
	
	public MultiThreadCore(int threadNum, Class<? extends RunnableCore> runnableCoreClass) {
		this.threadNum = threadNum;
		this.runnableCoreClass = runnableCoreClass;
	}
	
	private void before() throws Exception {
		this.runnableCores = new ArrayList<RunnableCore>();
		this.countDownLatch = new CountDownLatch(this.threadNum);
		this.timeCosts = new ConcurrentHashMap<Integer, Long>();
		for(int i=0; i<this.threadNum; i++) {
			RunnableCore runnableCore = this.runnableCoreClass.newInstance();
			runnableCore.setIndex(i);
			runnableCore.setCountDownLatch(this.countDownLatch);
			runnableCore.setTimeCosts(this.timeCosts);
			runnableCores.add(runnableCore);
		}
	}
	
	public void start() throws Exception {
		logger.info("######## START 性能测试开始");
		
		this.before();
		
		logger.info("######## START 初始化线程");
		for(RunnableCore runnableCore : runnableCores) {
			runnableCore.init();
		}
		
		logger.info("######## START 启动线程");
		long start = System.currentTimeMillis();
		for(RunnableCore runnableCore : runnableCores) {
			new Thread(runnableCore).start();
		}
		this.countDownLatch.await();
		this.totalCost = System.currentTimeMillis() - start;
		
		logger.info("######## END 总线程数: "+this.threadNum+" 个.");
		logger.info("######## END 总耗时: "+this.getTotalCost()+" ms.");
		logger.info("######## END 平均耗时: "+this.getAvgCost()+" ms.");
		logger.info("######## END 最小耗时: "+this.getMinCost()+" ms.");
		logger.info("######## END 最大耗时: "+this.getMaxCost()+" ms.");
		logger.info("######## END 性能测试结束");
	}
	
	public long getTotalCost() {
		return this.totalCost;
	}
	
	private long getAvgCost() {
		long total = 0l;
		for(int i=0; i<this.threadNum; i++) {
			total = total + this.timeCosts.get(i);
		}
		return total/this.threadNum;
	}
	private long getMinCost() {
		long min = this.timeCosts.get(0);
		for(int i=0; i<this.threadNum; i++) {
			if(this.timeCosts.get(i) < min) {
				min = this.timeCosts.get(i);
			}
		}
		return min;
	}
	private long getMaxCost() {
		long max = this.timeCosts.get(0);
		for(int i=0; i<this.threadNum; i++) {
			if(this.timeCosts.get(i) > max) {
				max = this.timeCosts.get(i);
			}
		}
		return max;
	}
	
}
