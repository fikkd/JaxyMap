package com.scott;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import com.scott.common.CommonUtil;
import com.scott.service.IBusiness;

public class Main {

	public static void main(String[] args) {
		ApplicationContext context = CommonUtil.getSpringApplicationContext();
		IBusiness service = (IBusiness) context.getBean("business");

		/** 
		 * ��һ��
		 * ��ԭʼ��ҵ��Ϣ�������ݿ��������õ�ͼר��ʹ�õ���ҵ��Ϣ��qyinfo_Map��  */
		service.copyToQyInfoMap();
		
		/**
		 * �ڶ���
		 * ��ѯ�ٶ�API
	     * ��Qyinfo_Map���е���ҵ�������ö�Ӧ�ľ�γ��, �Ӷ������ڵ�ͼ��չʾ
		 */
		service.setLocation();
		
		/**
		 * ������
		 * 
		 * ���ݼȶ�[����]���ɸ�����ͼ�㼶����Ҫ������ ���һ�㼶�������Ѿ��ڵڶ����������ұ�����Qyinfo_Map����
		 * �����㼶����������һ�㼶�������ұ�����Qyinfo_Map_Level����
		 * 
		 * ����: ������򵥵ľۺ��㷨���Լ��趨��һ���ֹ���ֵ */
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
	}
	
	public static void generateLevelData(Map<String, Integer> map, String levels, String zoom, IBusiness service) {
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
