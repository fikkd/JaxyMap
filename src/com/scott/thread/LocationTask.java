package com.scott.thread;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.http.HttpHeaders;

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
	private Semaphore semaphore;
	private Properties prop;
	private List<QyInfo_Map> list;
	private KParam param;
	private FiDAO fiDAO;

	public LocationTask(CountDownLatch latch, Semaphore semaphore, Properties prop, List<QyInfo_Map> list, FiDAO fiDAO,
			KParam param) {
		this.latch = latch;
		this.semaphore = semaphore;
		this.prop = prop;
		this.list = list;
		this.fiDAO = fiDAO;
		this.param = param;
	}

	@Override
	public void run() {
		HttpClient client = new HttpClient();
		client.getHttpConnectionManager().getParams().setConnectionTimeout(7 * 1000); // ���ӳ�ʱ
		client.getHttpConnectionManager().getParams().setSoTimeout(10 * 1000); // ��ȡ��ʱ

		String url = prop.get("url").toString();
		try {
			for (QyInfo_Map info : list) {
				try {
					setLocation(client, info, url);
				} catch (Throwable ignore) {
					ignore.printStackTrace();
				} finally {
				}
			}
		} finally {
			semaphore.release();
			latch.countDown();
		}

	}

	// http://api.map.baidu.com/lbsapi/geocoding-api.htm
	public void setLocation(HttpClient client, QyInfo_Map info, String url) {


		GetMethod method = null;
		try {
			url = url + java.net.URLEncoder.encode(info.getC_name(), "UTF-8")
					+ "&output=json&ak=0F6lvW7RH7VsRymFTCT7hYYOYVn5ezWk&city="
					+ java.net.URLEncoder.encode(param.getCityName(), "UTF-8");

			method = new GetMethod(url);
			method.setRequestHeader(HttpHeaders.CONNECTION, "close");
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
			} else {
				// ignore
			}
		} catch (Throwable ignore) {
				// ignore
		} finally {
			method.releaseConnection();
		}

	}

}
