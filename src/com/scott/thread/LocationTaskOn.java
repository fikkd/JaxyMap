package com.scott.thread;

import java.util.List;
import java.util.Properties;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

import com.scott.common.KParam;
import com.scott.dao.FiDAO;
import com.scott.model.QyInfo_Map;


public class LocationTaskOn implements Runnable {

	private Properties prop;
	private List<QyInfo_Map> list;
	private KParam param;
	private FiDAO fiDAO;
	
	public LocationTaskOn(Properties prop, List<QyInfo_Map> list, FiDAO fiDAO, KParam param) {
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
	}

	public void setLocation(HttpClient client, QyInfo_Map info, String url) {		 
		
		GetMethod method = null;
		try {
			
			url = url + java.net.URLEncoder.encode(info.getC_name(), "UTF-8")
					+ "&output=json&ak=0F6lvW7RH7VsRymFTCT7hYYOYVn5ezWk&city="
					+ java.net.URLEncoder.encode(param.getCityName(), "UTF-8");
			
			method = new GetMethod(url);

			client.executeMethod(method);
			if (method.getStatusCode() == 200) {
				String reString = method.getResponseBodyAsString();
				reString = reString.replaceAll("(?s).*\"lng\":(\\d*\\.\\d*).*\"lat\":(\\d*\\.\\d*).*", "$1,$2");
				if (reString.matches("\\d+\\.\\d+,\\d+\\.\\d+")) { // 如果替换成功
					String[] arr = reString.split(",");
					info.setM_lng(arr[0]);
					info.setM_lat(arr[1]);
					info.setM_on("1");  // 表示被处理
					fiDAO.updateMap(info);
				}
			}
		} catch (Throwable ignore) {
			ignore.printStackTrace();
		} finally {
			method.releaseConnection();
		}

	}

}
