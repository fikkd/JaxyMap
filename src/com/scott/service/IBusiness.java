package com.scott.service;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

public interface IBusiness {
	
	void moveData(CountDownLatch latch, Map<String, String> map, Properties prop);

	void saveQyInfoMap(String tName, String tColumns, Properties prop);
	
	int getPageCountOfMap();
	
	void deleteMap();
	
	void deleteMapLevel();
	
	void updateLocation(CountDownLatch latch, Semaphore semaphore, Properties prop, int page);
	
	void generateLevelData(String pre, String next, int cell);

	void updateLocation(Properties prop);
	

}
