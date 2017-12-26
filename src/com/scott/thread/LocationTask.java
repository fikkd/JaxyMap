package com.scott.thread;

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
				if (reString.matches(".*\\d.*")) { // ����滻�ɹ�
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
