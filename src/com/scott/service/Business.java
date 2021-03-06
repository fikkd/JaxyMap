package com.scott.service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.scott.common.CommonUtil;
import com.scott.common.KParam;
import com.scott.dao.FiDAO;
import com.scott.model.QyInfo_Map;
import com.scott.model.QyInfo_Map_Level;
import com.scott.thread.LocationTask;
import com.scott.thread.LocationTaskOn;
import com.scott.thread.MoveDataTask;



public class Business implements IBusiness {

	private FiDAO fiDAO;
	private ThreadPoolTaskExecutor taskExecutor;
	
	@Override
	public void deleteMap() {
		
		fiDAO.deleteMap();
	}

	/**
	 * 
	 * 删除QYINFO_MAP_LEVEL表所有数据
	 * 为了重新构造各个层级数据做准备
	 * 
	 *
	 * @变更记录 2018年1月10日 上午9:53:03 李瑞辉 创建
	 *
	 */
	@Override
	public void deleteMapLevel() {
		
		fiDAO.deleteMapLevel();
	}
	/*
	 * Double类型拼接成字符串
	 * 
	 * 首先拼接成字符串000.0000000000,000.000000000,000.000000000,000.000000000
	 * 其次通过正则调整为小数点后面保留6位的格式 000.000000,000.000000,000.000000,000.000000
	 * 
	 */
	private String doubleToString(Double LNG_F, Double LNG_B, Double LAT_F, Double LAT_B) {		
		return new StringBuffer().append(LNG_F).append(",").append(LNG_B).append(",").append(LAT_F).append(",").append(LAT_B).toString().replaceAll("(\\d{1,}\\.\\d{6})\\d*(,\\d{1,}\\.\\d{6})\\d*(,\\d{1,}\\.\\d{6})\\d*(,\\d{1,}\\.\\d{6})\\d*", "$1$2$3$4");		
	}
	@Override
	public void generateLevelData(String pre, String next, int cell) {
		
		/**
		 * 获取最大、最小经纬度坐标
		 */
		Double maxLng = fiDAO.getMaxLng(next);
		Double minLng = fiDAO.getMinLng(next);
		Double maxLat = fiDAO.getMaxLat(next);
		Double minLat = fiDAO.getMinLat(next);

		/**
		 * 方格四个方向分别向外扩张0.00001单位,目的在于让所有坐标处在方格内部
		 */
		maxLng = maxLng + 0.000001;
		minLng = minLng - 0.000001;
		maxLat = maxLat + 0.000001;
		minLat = minLat - 0.000001;

		/**
		 * 100*100方格
		 */
		int LNG_NUMBER = cell;
		int LAT_NUMBER = cell;

		/**
		 * 每个方格的边长
		 */
		Double LNG_DISTINCE = (maxLng - minLng) / LNG_NUMBER;
		Double LAT_DISTINCE = (maxLat - minLat) / LAT_NUMBER;

		/**
		 * Double[] LNG_ARR = {minLng, minLng+LNG_DISTINCE*1, minLng+LNG_DISTINCE*2, minLng+LNG_DISTINCE*3, minLng+LNG_DISTINCE*4, ... ,minLng+LNG_DISTINCE*100};
		 */
		Double[] LNG_ARR = new Double[LNG_NUMBER + 1];
		for (int i = 0; i <= LNG_NUMBER; i++) {
			LNG_ARR[i] = minLng + LNG_DISTINCE * i;
		}

		/**
		 * Double[] LAT_ARR = {minLat, minLat+LAT_DISTINCE*1, minLat+LAT_DISTINCE*2, minLat+LAT_DISTINCE*3, minLat+LAT_DISTINCE*4, ... ,minLat+LAT_DISTINCE*100};
		 */
		Double[] LAT_ARR = new Double[LAT_NUMBER + 1];
		for (int i = 0; i <= LAT_NUMBER; i++) {
			LAT_ARR[i] = minLat + LAT_DISTINCE * i;
		}

		/**
		 * 方案一
		 * 查询每个方格内部的数据 计算
		 */
		Double LNG_F , LNG_B , LAT_F , LAT_B;
		for (int i = 0; i < LNG_NUMBER; i++) {
			LNG_F = LNG_ARR[i];
			LNG_B = LNG_ARR[i + 1];
			for (int j = 0; j < LAT_NUMBER; j++) {
				LAT_F = LAT_ARR[j];
				LAT_B = LAT_ARR[j + 1];
				this.saveMapData(LNG_F, LNG_B, LAT_F, LAT_B, pre, next);
			}
		}
	}
	public FiDAO getFiDAO() {
		return fiDAO;
	}

	
	/**
	 * 
	 * 查询信用地图专用表总数量
	 * 
	 * @return
	 *
	 * @变更记录 2018年1月9日 上午12:02:20 李瑞辉 创建
	 *
	 */
	public int getPageCountOfMap() {
		
		return fiDAO.getPageCountOfMap();		
	}
	
	
	public ThreadPoolTaskExecutor getTaskExecutor() {
		return taskExecutor;
	}
	
	@Override
	public void moveData(CountDownLatch latch, Map<String, String> map, Properties prop) {

		Set<String> set = map.keySet();
		Iterator<String>it = set.iterator();
		
		while (it.hasNext()) {
			String tName = it.next();
			Runnable task = new MoveDataTask(this, latch, tName, map.get(tName), prop);
			taskExecutor.execute(task);
		}
	}
	
	/**
	 * 查询指定范围方格内数据进行计算
	 * 
	 */
	private void saveMapData(Double LNG_F, Double LNG_B, Double LAT_F, Double LAT_B, String pre, String next) {
		String sRange = doubleToString(LNG_F, LNG_B, LAT_F, LAT_B);
		if (next.equals("s")) { // 生成r层级数据
			List<QyInfo_Map> list = fiDAO.findListCell(LNG_F, LNG_B, LAT_F, LAT_B);
			this.saveMapData(list, pre, sRange, "no'use");
		} else { // 生成[c-q]层级数据
			List<QyInfo_Map_Level> list = fiDAO.findListCell(LNG_F, LNG_B, LAT_F, LAT_B, next);
			this.saveMapData(list, pre, sRange);
		}
	}
	
	/*
	 * 生成[c-q]层级数据
	 * 
	 */
	private void saveMapData(List<QyInfo_Map_Level> list, String pre, String sRange) {

		/**
		 *  范围数据为空
		 */
		if (null == list || list.size() == 0) {
			return;
		}

		int size = list.size();
		if (size == 1) {// 方格内只有一点
			QyInfo_Map_Level map = list.get(0);

			fiDAO.saveMap(map.getM_lng(), map.getM_lat(), map.getM_count_qy(), sRange, pre);
		} else if (size > 1) {// 方格内有多个点
			Double x = 0.0;
			Double y = 0.0;
			Integer count = 0;
			QyInfo_Map_Level map;
			for (int k = 0; k < size; k++) {
				map = list.get(k);
				x += Double.valueOf(map.getM_lng());
				y += Double.valueOf(map.getM_lat());
				count += map.getM_count_qy();
			}

			fiDAO.saveMap(String.valueOf(x / size).replaceAll("(\\d*\\.\\d{6})\\d*", "$1"), String.valueOf(y / size).replaceAll("(\\d*\\.\\d{6})\\d*", "$1"), count, sRange, pre);
		}
	}
	
	
	
	/*
	 * 生成r层级数据
	 *
	 */
	private void saveMapData(List<QyInfo_Map> list, String pre, String sRange, String nouse) {
		/**
		 * 范围数据为空
		 */
		if (null == list || list.size() == 0) {
			return;
		}

		int size = list.size();
		if (size == 1) {// 方格内只有一点
			QyInfo_Map map = list.get(0);

			fiDAO.saveMap(map.getM_lng(), map.getM_lat(), 1, sRange, pre);
		} else if (size > 1) {// 方格内有多个点
			Double x = 0.0d;
			Double y = 0.0d;
			QyInfo_Map map;
			for (int k = 0; k < size; k++) {
				map = list.get(k);
				x += Double.valueOf(map.getM_lng());
				y += Double.valueOf(map.getM_lat());
			}

			fiDAO.saveMap(String.valueOf(x / size).replaceAll("(\\d*\\.\\d{6})\\d*", "$1"), String.valueOf(y / size).replaceAll("(\\d*\\.\\d{6})\\d*", "$1"), size, sRange, pre);
		}
	}
	
	/**
	 * 
	 * 源企业表数据存入信用地图专用表
	 * 
	 * @param tName
	 * @param tColumns
	 * @param prop
	 *
	 * @变更记录 2018年1月8日 下午11:59:26 李瑞辉 创建
	 *
	 */
	@Override
	public void saveQyInfoMap(String tName, String tColumns, Properties prop) {
		
		int count = fiDAO.getPageCount(tName);		
		int page = (count % 10 == 0) ? count / 10 : count / 10 + 1;
		
		String table = prop.get("tName").toString();
		String columns = prop.get("tColumns").toString();
		
		for (int i = 0; i < page; i++) {	
			// 分页查询源企业信息表数据
			List<Object[]> list = fiDAO.findQyInfoByPage(i, tName, tColumns);
			// 数据存入信用地图专用企业信息表中
			fiDAO.saveQyInfoMap(list, table, columns);
		}
		
	}
	
	public void setFiDAO(FiDAO fiDAO) {
		this.fiDAO = fiDAO;
	}
	
	/**
	 * 
	 * 设置坐标
	 * 
	 * @param latch
	 * @param page
	 * @throws InterruptedException 
	 *
	 * @变更记录 2018年1月8日 下午11:58:35 李瑞辉 创建
	 *
	 */
	@Override
	public void updateLocation(CountDownLatch latch, Semaphore semaphore, Properties prop, int page) throws InterruptedException {
		
		ApplicationContext context = CommonUtil.getSpringApplicationContext();
		KParam param = (KParam) context.getBean("kParam");

		List<QyInfo_Map> list;
		Runnable task;
		for (int i = 0; i < page; i++) {
			list = fiDAO.findQyInfoMapByPage(i);
			if (list != null) {
				task = new LocationTask(latch, semaphore, prop, list, fiDAO, param);
				
				semaphore.acquire();
				taskExecutor.execute(task);
			}
		}		
	}
	
	/**
	 * 弥补'遗失'的经纬度
	 * 
	 */
	public void updateLocation(Properties prop) {
		ApplicationContext context = CommonUtil.getSpringApplicationContext();
		KParam param = (KParam) context.getBean("kParam");
		
		List<QyInfo_Map> list = fiDAO.getListOfMapOn();
		Runnable task;
		for (;;) {
			try {
				task = new LocationTaskOn(prop, list, fiDAO, param);			
				task.run();
				if (list != null && list.size() < 10)
					break;
				list = fiDAO.getListOfMapOn();
			} catch (Exception ignore) {
				
			} finally {
				
			}
		}		
	}
	
	/**
	 * 构造30万条企业存入数据库 高并发测试需要
	 */
	public void saveQyinfo(String qyname) {
		fiDAO.saveQyinfo(qyname);
	}
	
	
	public void setTaskExecutor(ThreadPoolTaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}
	
	public int getLngisnull() {
		
		return fiDAO.getLngisnull();
	}

}
