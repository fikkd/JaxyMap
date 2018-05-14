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
		 * ��ȡ������Ϣ
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
	 * ��һ��
	 * ��Դ��ҵ��Ϣ��(4�ű�)����Ҫ�ֶε����ݿ��������õ�ͼר�õ���ҵ��Ϣ��qyinfo_Map��
	 *
	 */
	private static void stepA(IBusiness service, Map<String, String> map, Properties prop) {

		logger.info("7���Ժ����ɾ������");
		
		try {
			Thread.sleep(1000 * 7);
		} catch (InterruptedException ignore) {
		}
		service.deleteMap();
		logger.info("����ɾ�����... 7���Ժ����¹�������");
		try {
			Thread.sleep(1000 * 7);
		} catch (InterruptedException ignore) {
		}
		logger.info("��һ����ʼ");
		CountDownLatch latch = new CountDownLatch(4);
		try {			
			service.moveData(latch, map, prop);
			latch.await();
		} catch (InterruptedException ignore) {
			
		}
		logger.info("��һ�����");
	}
	
	/**
	 * �ڶ���
	 * ��ѯ�ٶ�API
     * ��qyinfo_map���е���ҵ���ö�Ӧ�ľ�γ��, �Ӷ������ڵ�ͼ��չʾ
	 */
	private static void stepB(IBusiness service, Properties prop) {
		logger.info("�ڶ�����ʼ");
		
		int count = service.getPageCountOfMap();
		int page = (count % 10 == 0) ? count / 10 : count / 10 + 1;
		
		CountDownLatch latc = new CountDownLatch(page);		
		Semaphore semaphore = new Semaphore(10);
		
		/*
		 * ����2.1
		 */
		try {			
			service.updateLocation(latc, semaphore, prop, page);
			latc.await();
		} catch (InterruptedException e) {
			logger.error(e);
		}
				
		/* 
		 * ����2.2
		 * ���ڰٶȵ�ͼAPI�Ĳ�������, ��2.1����һ������ҵ�ľ�γ�Ȳ�û�и��³ɹ�, �����Ҫ�ֲ�
		 * ����2.1���������Ѿ�����˾�������ľ�γ��
		 * �˲�2.2�������ðٶȵ�ͼAPI
		 * 
		 */
		service.updateLocation(prop);
		
		logger.info("�ڶ������");
	}
	
	/**
	 * ������
	 * 
	 * ���ݼȶ�[����]���ɵ�ͼ�����㼶����Ҫ������ 
	 * ���һ�㼶�������Ѿ��ڵڶ����������ұ�����Qyinfo_Map����
	 * �����㼶����������һ�㼶�������ұ�����Qyinfo_Map_Level����
	 * 
	 * ����: ������򵥵ľۺ��㷨���Լ��趨��һ���ֹ���ֵ */
	private static void stepC(IBusiness service) {
		logger.info("��������ʼ");
		
		Map<String, Integer> map = new HashMap<>();		
		/**
		 * �涨ÿ���㼶��Ӧ�ĵ�Ԫ���С
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
		 * cdefghijklmnopqrs�е�ÿ����ĸ����һ���㼶
		 * �ٶȵ�ͼ�Ĳ㼶zoomȡֵ��Χ[3,19],�����ƶ�Ӧ����ĸ�ֱ�Ϊ[c-s]
		 */
		String levels = "cdefghijklmnopqrs";			
		String[] arr = "r,q,p,o,n,m,l,k,j,i,h,g,f,e,d,c".split(",");		
		
		
		/**
		 * ɾ��֮ǰ������
		 * Ϊ�����¹���������׼��
		 */
		service.deleteMapLevel();
		
		for (String zoom : arr) {
			generateLevelData(map, levels, zoom, service);
		}
		
		logger.info("���������");
	}
	
	private static void generateLevelData(Map<String, Integer> map, String levels, String zoom, IBusiness service) {
		try {
			/** '�и�'����������Ĳ㼶��������һ�㼶��Ӧ����ĸ */
			String[] arr = levels.replaceAll("^[c-s]*(" + zoom + ")([c-s])[c-s]*$", "$1,$2").split(",");
			/** ������Ĳ㼶�ĵ�Ԫ���С */
			int cell = map.get(arr[0]);

			/**
			 * arr[0]������㼶
			 * arr[1]��֪����һ�㼶
			 */
			service.generateLevelData(arr[0], arr[1], cell);
		} catch (Exception e) {
			
		}
	}
	
}
