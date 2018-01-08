package com.scott.service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.scott.common.CommonUtil;
import com.scott.common.KParam;
import com.scott.dao.FiDAO;
import com.scott.model.QyInfo_Map;
import com.scott.model.QyInfo_Map_Level;
import com.scott.thread.LocationTask;
import com.scott.thread.MoveDataTask;



public class Business implements IBusiness {

	private FiDAO fiDAO;
	private ThreadPoolTaskExecutor taskExecutor;

	public FiDAO getFiDAO() {
		return fiDAO;
	}
	public void setFiDAO(FiDAO fiDAO) {
		this.fiDAO = fiDAO;
	}
	public ThreadPoolTaskExecutor getTaskExecutor() {
		return taskExecutor;
	}
	public void setTaskExecutor(ThreadPoolTaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
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
	
	@Override
	public void saveQyInfoMap(String tName, String tColumns, Properties prop) {
		
		int count = fiDAO.getPageCount(tName);		
		int page = (count % 10 == 0) ? count / 10 : count / 10 + 1;
		
		String table = prop.get("tName").toString();
		String columns = prop.get("tColumns").toString();
		
		for (int i = 0; i < page; i++) {			
			List<Object[]> list = fiDAO.findQyInfoByPage(i, tName, tColumns);
			fiDAO.saveQyInfoMap(list, table, columns);
		}
		
	}
	
	
	
	
	@Override
	public void setLocation(CountDownLatch latch, int page) {
		
		ApplicationContext context = CommonUtil.getSpringApplicationContext();
		KParam param = (KParam) context.getBean("kParam");

		List<QyInfo_Map> list;
		Runnable task;
		for (int i = 0; i < page; i++) {
			list = fiDAO.findQyInfoMapByPage(i);
			if (list != null) {
				task = new LocationTask(latch, list, fiDAO, param);
				taskExecutor.execute(task);
			}
		}
	}
	
	
	@Override
	public void generateLevelData(String pre, String next, int cell) {
		
		/**
		 * ��ȡ�����С��γ������
		 */
		Double maxLng = fiDAO.getMaxLng(next);
		Double minLng = fiDAO.getMinLng(next);
		Double maxLat = fiDAO.getMaxLat(next);
		Double minLat = fiDAO.getMinLat(next);

		/**
		 * �����ĸ�����ֱ���������0.00001��λ,Ŀ���������������괦�ڷ����ڲ�
		 */
		maxLng = maxLng + 0.000001;
		minLng = minLng - 0.000001;
		maxLat = maxLat + 0.000001;
		minLat = minLat - 0.000001;

		/**
		 * 100*100����
		 */
		int LNG_NUMBER = cell;
		int LAT_NUMBER = cell;

		/**
		 * ÿ������ı߳�
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
		 * ����һ
		 * ��ѯÿ�������ڲ������� ����
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
	
	/**
	 * ��ѯָ����Χ���������ݽ��м���
	 * 
	 */
	private void saveMapData(Double LNG_F, Double LNG_B, Double LAT_F, Double LAT_B, String pre, String next) {
		String sRange = doubleToString(LNG_F, LNG_B, LAT_F, LAT_B);
		if (next.equals("s")) { // ����r�㼶����
			List<QyInfo_Map> list = fiDAO.findListCell(LNG_F, LNG_B, LAT_F, LAT_B);
			this.saveMapData(list, pre, sRange, "no'use");
		} else { // ����[c-q]�㼶����
			List<QyInfo_Map_Level> list = fiDAO.findListCell(LNG_F, LNG_B, LAT_F, LAT_B, next);
			this.saveMapData(list, pre, sRange);
		}
	}
	
	/*
	 * ����[c-q]�㼶����
	 * 
	 */
	private void saveMapData(List<QyInfo_Map_Level> list, String pre, String sRange) {

		/**
		 *  ��Χ����Ϊ��
		 */
		if (null == list) {
			return;
		}

		int size = list.size();
		if (size == 0) {// ������û�е�
		} else if (size == 1) {// ������ֻ��һ��
			QyInfo_Map_Level map = list.get(0);

			fiDAO.saveMap(map.getM_lng(), map.getM_lat(), map.getM_count_qy(), sRange, pre);
		} else if (size > 1) {// �������ж����
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
	 * ����r�㼶����
	 *
	 */
	private void saveMapData(List<QyInfo_Map> list, String pre, String sRange, String nouse) {
		/**
		 * ��Χ����Ϊ��
		 */
		if (null == list) {
			return;
		}

		int size = list.size();
		if (size == 0) {// ������û�е�
		} else if (size == 1) {// ������ֻ��һ��
			QyInfo_Map map = list.get(0);

			fiDAO.saveMap(map.getM_lng(), map.getM_lat(), 1, sRange, pre);
		} else if (size > 1) {// �������ж����
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
	
	
	/*
	 * Double����ƴ�ӳ��ַ���
	 * 
	 * ����ƴ�ӳ��ַ���000.0000000000,000.000000000,000.000000000,000.000000000
	 * ���ͨ���������ΪС������汣��6λ�ĸ�ʽ 000.000000,000.000000,000.000000,000.000000
	 * 
	 */
	private String doubleToString(Double LNG_F, Double LNG_B, Double LAT_F, Double LAT_B) {		
		return new StringBuffer().append(LNG_F).append(",").append(LNG_B).append(",").append(LAT_F).append(",").append(LAT_B).toString().replaceAll("(\\d{1,}\\.\\d{6})\\d*(,\\d{1,}\\.\\d{6})\\d*(,\\d{1,}\\.\\d{6})\\d*(,\\d{1,}\\.\\d{6})\\d*", "$1$2$3$4");		
	}

}
