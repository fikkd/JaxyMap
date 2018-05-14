package com.scott.thread;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;


/**
 * 线程池拒绝策略类
 * 
 * 此类暂未使用
 *
 */
public class LocationCallerRunsPolicy implements RejectedExecutionHandler {
	
	public LocationCallerRunsPolicy() { }

	@Override
	public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
		
		if (!executor.isShutdown()) {			
            r.run();
        }
	}

}
