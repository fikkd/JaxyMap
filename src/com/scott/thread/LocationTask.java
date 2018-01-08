package com.scott.thread;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

import com.scott.common.KParam;
import com.scott.dao.FiDAO;
import com.scott.model.QyInfo_Map;

/**
 * ��ѯ�ٶ�API ��Qyinfo_Map���е���ҵ�������ö�Ӧ�ľ�γ��, �Ӷ������ڵ�ͼ��չʾ
 *
 * @since 2017��11��2��
 * @author �����
 *
 */
public class LocationTask implements Runnable {

	private CountDownLatch latch;
	private List<QyInfo_Map> list;
	private KParam param;
	private GetMethod method;
	private FiDAO fiDAO;

	public LocationTask(CountDownLatch latch, List<QyInfo_Map> list, FiDAO fiDAO, KParam param) {
		this.latch = latch;
		this.list = list;
		this.fiDAO = fiDAO;
		this.param = param;
	}

	@Override
	public void run() {
		HttpClient client = new HttpClient();
		try {
			
			for (QyInfo_Map info : list) {
				setLocation(client, info);				
			}
		} catch (Exception e) {

		} finally {
			method.releaseConnection();
		}
		latch.countDown();
	}
	
	// http://api.map.baidu.com/lbsapi/geocoding-api.htm
	public void setLocation(HttpClient client, QyInfo_Map info) throws Exception {
		String url = "http://api.map.baidu.com/geocoder?address=" + java.net.URLEncoder.encode(info.getQymc(), "UTF-8")
				+ "&output=json&key=zkcsXqlktTGQLBcgqGXLBKZu&city="
				+ java.net.URLEncoder.encode(param.getCityName(), "UTF-8");
		
		method = new GetMethod(url);

		client.executeMethod(method);
		if (method.getStatusCode() == 200) {
			String reString = method.getResponseBodyAsString();
			reString = reString.replaceAll("(?s).*\"lng\":(\\d*\\.\\d*).*\"lat\":(\\d*\\.\\d*).*", "$1,$2");
			if (reString.matches(".*\\d.*")) { // ����滻�ɹ�
				String[] arr = reString.split(",");
				info.setM_lng(arr[0]);
				info.setM_lat(arr[1]);
				fiDAO.update(info);
			}
		}
		
	}
	
	
}
