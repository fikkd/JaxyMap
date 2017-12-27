package com.scott.service;

import java.util.concurrent.CountDownLatch;

public interface IBusiness {
		
	void testFun();
	
	int getPage();
	
	void copyToQyInfoMap(CountDownLatch latch, int page);
	
	void setLocation(CountDownLatch latch, int page);
	
	void generateLevelData(String pre, String next, int cell);
	

}
