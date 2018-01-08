package com.scott;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

import org.springframework.context.ApplicationContext;

import com.scott.common.CommonUtil;
import com.scott.service.IBusiness;

public class Main {

	public static void main(String[] args) {
		ApplicationContext context = CommonUtil.getSpringApplicationContext();
		IBusiness service = (IBusiness) context.getBean("business");

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
		
		
	}
	
	/**
	 * ��һ��
	 * ��Դ��ҵ��Ϣ�������ݿ��������õ�ͼר��ʹ�õ���ҵ��Ϣ��qyinfo_Map��
	 *
	 */
	private static void stepA(IBusiness service, Map<String, String> map, Properties prop) {
		System.out.println("��һ����ʼ");
		CountDownLatch latch = new CountDownLatch(4);
		try {			
			service.moveData(latch, map, prop);
			latch.await();
		} catch (InterruptedException ignore) {
			
		}
		System.out.println("��һ�����");
	}
	
	/**
	 * �ڶ���
	 * ��ѯ�ٶ�API
     * ��Qyinfo_Map���е���ҵ�������ö�Ӧ�ľ�γ��, �Ӷ������ڵ�ͼ��չʾ
	 */
	private static void stepB(IBusiness service, Properties prop) {
		CountDownLatch latc = new CountDownLatch(0);
		System.out.println("�ڶ�����ʼ");		
		try {
			
			service.setLocation(latc, 0);
			latc.await();
		} catch (InterruptedException e) {
			
		}
		System.out.println("�ڶ������");
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
		System.out.println("��������ʼ");
		
		
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
		String[] arr = "c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r".split(",");
		

		for (String zoom : arr) {
			generateLevelData(map, levels, zoom, service);
		}
		
		System.out.println("���������");
	}
	
	private static void generateLevelData(Map<String, Integer> map, String levels, String zoom, IBusiness service) {
		try {
			/** '�и�'����������Ĳ㼶��������һ�㼶��Ӧ����ĸ */
			String[] arr = levels.replaceAll("^[c-s]*(" + zoom + ")([c-s])[c-s]*$", "$1,$2").split(",");
			/** ������Ĳ㼶�ĵ�Ԫ���С */
			int cell = map.get(arr[0]);

			service.generateLevelData(arr[0], arr[1], cell);
		} catch (Exception e) {
			
		}
	}
	
}