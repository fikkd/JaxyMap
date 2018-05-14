package com.scott.thread;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;


/**
 * �̳߳ؾܾ�������
 * 
 * ������δʹ��
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
