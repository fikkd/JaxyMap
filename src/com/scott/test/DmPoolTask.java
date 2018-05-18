package com.scott.test;

import java.util.concurrent.CountDownLatch;

public class DmPoolTask implements Runnable {
	
	private CountDownLatch latch;
	
	public DmPoolTask(CountDownLatch latch) {
		this.latch = latch;
	}

	@Override
	public void run() {

		
		
		try {
			Thread.sleep(1000 * 30);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		latch.countDown();
		System.out.println(Thread.currentThread().getName()+"   countDown");
		
		
	}

}
