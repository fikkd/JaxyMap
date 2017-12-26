package com.scott.thread;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

import com.scott.common.KParam;
import com.scott.dao.FiDAO;
import com.scott.model.QyInfo_Map;

/**
 * 查询百度API 给Qyinfo_Map表中的企业名称设置对应的经纬度, 从而方便在地图上展示
 *
 * @since 2017年11月2日
 * @author 李瑞辉
 *
 */
public class LocationTask implements Runnable {

	private QyInfo_Map info;
	private KParam param;
	private GetMethod method;
	private FiDAO fiDAO;

	public LocationTask(QyInfo_Map info, FiDAO fiDAO, KParam param) {
		this.info = info;
		this.fiDAO = fiDAO;
		this.param = param;
	}

	@Override
	public void run() {
		HttpClient client = new HttpClient();
		String url;

		try {
			url = "http://api.map.baidu.com/geocoder?address=" + java.net.URLEncoder.encode(info.getQymc(), "UTF-8")
					+ "&output=json&key=zkcsXqlktTGQLBcgqGXLBKZu&city="
					+ java.net.URLEncoder.encode(param.getCityName(), "UTF-8");
			method = new GetMethod(url);

			client.executeMethod(method);
			if (method.getStatusCode() == 200) {
				String reString = method.getResponseBodyAsString();
				reString = reString.replaceAll("(?s).*\"lng\":(\\d*\\.\\d*).*\"lat\":(\\d*\\.\\d*).*", "$1,$2");
				if (reString.matches(".*\\d.*")) { // 如果替换成功
					String[] arr = reString.split(",");
					info.setM_lng(arr[0]);
					info.setM_lat(arr[1]);
					fiDAO.update(info);
				}
			}
		} catch (Exception e) {

		} finally {
			method.releaseConnection();
		}
	}
}
