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
		 * 第一步
		 * 将原始企业信息表中数据拷贝到信用地图专用使用的企业信息表qyinfo_Map中  */
		service.copyToQyInfoMap();
		
		/**
		 * 第二步
		 * 查询百度API
	     * 给Qyinfo_Map表中的企业名称设置对应的经纬度, 从而方便在地图上展示
		 */
		service.setLocation();
		
		/**
		 * 第三步
		 * 
		 * 根据既定[规则]生成各个地图层级所需要的数据 最后一层级的数据已经在第二步骤生成且保存在Qyinfo_Map表中
		 * 其他层级的数据由下一层级而生成且保存在Qyinfo_Map_Level表中
		 * 
		 * 规则: 根据最简单的聚合算法和自己设定的一部分规则值 */
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
		
		
		String[] arr = "c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r".split(",");
		

		for (String zoom : arr) {			
			generateLevelData(map, levels, zoom, service);
		}
	}
	
	public static void generateLevelData(Map<String, Integer> map, String levels, String zoom, IBusiness service) {
		try {
			/** '切割'出来被计算的层级和它的下一层级对应的字母 */
			String[] arr = levels.replaceAll("^[c-s]*(" + zoom + ")([c-s])[c-s]*$", "$1,$2").split(",");
			/** 被计算的层级的单元格大小 */
			int cell = map.get(arr[0]);

			service.generateLevelData(arr[0], arr[1], cell);
		} catch (Exception e) {
			
		}
	}
	
	
	

}
