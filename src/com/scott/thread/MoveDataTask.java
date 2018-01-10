package com.scott.thread;

import java.util.Properties;
import java.util.concurrent.CountDownLatch;

import com.scott.service.IBusiness;

/**
 * 
 * 将源企业信息表中数据拷贝到信用地图专用的企业信息表qyinfo_Map中
 *
 */
public class MoveDataTask implements Runnable {
	
	private IBusiness service;
	private CountDownLatch latch;
	private String tName;
	private String tColumns;
	private Properties prop;

	public MoveDataTask(IBusiness service, CountDownLatch latch, String tName, String tColumns, Properties prop) {
		this.service = service;
		this.latch = latch;
		this.tName = tName;
		this.tColumns = tColumns;
		this.prop = prop;
	}

	@Override
	public void run() {
		service.saveQyInfoMap(tName, tColumns, prop);
		latch.countDown();
	}

}
