package com.scott.service;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

public interface IBusiness {
	
	void saveQyInfoMap(String tName, String tColumns, Properties prop);
	
	void moveData(CountDownLatch latch, Map<String, String> map, Properties prop);
	
	void setLocation(CountDownLatch latch, int page);
	
	void generateLevelData(String pre, String next, int cell);
	

}
