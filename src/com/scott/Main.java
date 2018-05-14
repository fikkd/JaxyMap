package com.scott;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.scott.common.CommonUtil;
import com.scott.service.IBusiness;

public class Main {
	
	private static final Logger logger = LogManager.getLogger(Main.class.getName());
	
	public static void main(String[] args) {
		ApplicationContext context = CommonUtil.getSpringApplicationContext();
		IBusiness service = (IBusiness) context.getBean("business");

		CommonUtil.configureLog4j();
		
		run(service);
		
	}
	
	public static void run(IBusiness service) {
		
		/**
		 * 读取配置信息
		 */
		InputStream input;
		Properties prop = new Properties();
		try {
			input = new FileInputStream("conf/init.properties");
			prop.load(input);
		} catch (Exception ignore) {
		}
		
		String tName1 = prop.get("tName1").toString();
		String tColumns1 = prop.get("tColumns1").toString();
		String tName2 = prop.get("tName2").toString();
		String tColumns2 = prop.get("tColumns2").toString();
		String tName3 = prop.get("tName3").toString();
		String tColumns3 = prop.get("tColumns3").toString();
		String tName4 = prop.get("tName4").toString();
		String tColumns4 = prop.get("tColumns4").toString();
		
		Map<String, String> map = new HashMap<String, String>();
		map.put(tName1, tColumns1);
		map.put(tName2, tColumns2);
		map.put(tName3, tColumns3);
		map.put(tName4, tColumns4);
		
		
		stepA(service, map, prop);
		stepB(service, prop);		
//		stepC(service);
		
		ApplicationContext context = CommonUtil.getSpringApplicationContext();
		ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor) context.getBean("taskExecutor");
		taskExecutor.shutdown();
		
	}
	
	/**
	 * 第一步
	 * 将源企业信息表(4张表)中主要字段的数据拷贝到信用地图专用的企业信息表qyinfo_Map中
	 *
	 */
	private static void stepA(IBusiness service, Map<String, String> map, Properties prop) {

		logger.info("7秒以后进行删除数据");
		
		try {
			Thread.sleep(1000 * 7);
		} catch (InterruptedException ignore) {
		}
		service.deleteMap();
		logger.info("数据删除完成... 7秒以后重新构造数据");
		try {
			Thread.sleep(1000 * 7);
		} catch (InterruptedException ignore) {
		}
		logger.info("第一步开始");
		CountDownLatch latch = new CountDownLatch(4);
		try {			
			service.moveData(latch, map, prop);
			latch.await();
		} catch (InterruptedException ignore) {
			
		}
		logger.info("第一步完成");
	}
	
	/**
	 * 第二步
	 * 查询百度API
     * 给qyinfo_map表中的企业设置对应的经纬度, 从而方便在地图上展示
	 */
	private static void stepB(IBusiness service, Properties prop) {
		logger.info("第二步开始");
		
		int count = service.getPageCountOfMap();
		int page = (count % 10 == 0) ? count / 10 : count / 10 + 1;
		
		CountDownLatch latc = new CountDownLatch(page);		
		Semaphore semaphore = new Semaphore(10);
		
		/*
		 * 步骤2.1
		 */
		try {			
			service.updateLocation(latc, semaphore, prop, page);
			latc.await();
		} catch (InterruptedException e) {
			logger.error(e);
		}
				
		/* 
		 * 步骤2.2
		 * 由于百度地图API的并发限制, 第2.1中有一部分企业的经纬度并没有更新成功, 因此需要弥补
		 * 经过2.1并发调用已经解决了绝大多数的经纬度
		 * 此步2.2逐条调用百度地图API
		 * 
		 */
		service.updateLocation(prop);
		
		logger.info("第二步完成");
	}
	
	/**
	 * 第三步
	 * 
	 * 根据既定[规则]生成地图各个层级所需要的数据 
	 * 最后一层级的数据已经在第二步骤生成且保存在Qyinfo_Map表中
	 * 其他层级的数据由下一层级而生成且保存在Qyinfo_Map_Level表中
	 * 
	 * 规则: 根据最简单的聚合算法和自己设定的一部分规则值 */
	private static void stepC(IBusiness service) {
		logger.info("第三步开始");
		
		Map<String, Integer> map = new HashMap<>();		
		/**
		 * 规定每个层级对应的单元格大小
		 */
		map.put("c", 20);
		map.put("d", 30);
		map.put("e", 40);
		map.put("f", 55);
		map.put("g", 60);
		map.put("h", 65);
		map.put("i", 70);
		map.put("j", 75);
		map.put("k", 80);
		map.put("l", 85);
		map.put("m", 90);
		map.put("n", 100);
		map.put("o", 110);
		map.put("p", 130);
		map.put("q", 150);
		map.put("r", 200);
		
		/**
		 * cdefghijklmnopqrs中的每个字母代表一个层级
		 * 百度地图的层级zoom取值范围[3,19],因此设计对应的字母分别为[c-s]
		 */
		String levels = "cdefghijklmnopqrs";			
		String[] arr = "r,q,p,o,n,m,l,k,j,i,h,g,f,e,d,c".split(",");		
		
		
		/**
		 * 删除之前的数据
		 * 为了重新构造数据做准备
		 */
		service.deleteMapLevel();
		
		for (String zoom : arr) {
			generateLevelData(map, levels, zoom, service);
		}
		
		logger.info("第三步完成");
	}
	
	private static void generateLevelData(Map<String, Integer> map, String levels, String zoom, IBusiness service) {
		try {
			/** '切割'出来被计算的层级和它的下一层级对应的字母 */
			String[] arr = levels.replaceAll("^[c-s]*(" + zoom + ")([c-s])[c-s]*$", "$1,$2").split(",");
			/** 被计算的层级的单元格大小 */
			int cell = map.get(arr[0]);

			/**
			 * arr[0]被计算层级
			 * arr[1]已知的下一层级
			 */
			service.generateLevelData(arr[0], arr[1], cell);
		} catch (Exception e) {
			
		}
	}
	
}
