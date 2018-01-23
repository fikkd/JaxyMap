package com.scott.thread;

import java.util.List;
import java.util.Properties;
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
	private Properties prop;
	private List<QyInfo_Map> list;
	private KParam param;
	private FiDAO fiDAO;

	public LocationTask(CountDownLatch latch, Properties prop, List<QyInfo_Map> list, FiDAO fiDAO, KParam param) {
		this.latch = latch;
		this.prop = prop;
		this.list = list;
		this.fiDAO = fiDAO;
		this.param = param;
	}

	@Override
	public void run() {
		HttpClient client = new HttpClient();
		String url = prop.get("url").toString();
		for (QyInfo_Map info : list) {
			setLocation(client, info, url);
		}

		latch.countDown();
	}

	// http://api.map.baidu.com/lbsapi/geocoding-api.htm
	public void setLocation(HttpClient client, QyInfo_Map info, String url) {
		GetMethod method = null;
		try {
			url = url + java.net.URLEncoder.encode(info.getC_name(), "UTF-8")
					+ "&output=json&ak=5Wlafr2Q2Y43Wq26HTv63oXeHyf5WA32&city="
					+ java.net.URLEncoder.encode(param.getCityName(), "UTF-8");
			
			method = new GetMethod(url);

			client.executeMethod(method);
			if (method.getStatusCode() == 200) {
				String reString = method.getResponseBodyAsString();
				reString = reString.replaceAll("(?s).*\"lng\":(\\d*\\.\\d*).*\"lat\":(\\d*\\.\\d*).*", "$1,$2");
				if (reString.matches("\\d+\\.\\d+,\\d+\\.\\d+")) { // ����滻�ɹ�
					String[] arr = reString.split(",");
					info.setM_lng(arr[0]);
					info.setM_lat(arr[1]);
					fiDAO.updateMap(info);
				}
			}
		} catch (Exception ignore) {

		} finally {
			method.releaseConnection();
		}

	}

}
