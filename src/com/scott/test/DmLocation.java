package com.scott.test;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
/**
 * 
 * ����
 * ������ҵ��ȡ��γ��
 *
 */
public class DmLocation {

	public static void main(String[] args) {
		HttpClient client = new HttpClient();
		GetMethod method = null;
		
//		String qyinfo = "�Ž��л���ó�����޹�˾";
//		String qyinfo = "�Ž��н���滮�ְ���������滮�־�";
		String qyinfo = "�ϲ�����̩������е�������޹�˾���޷ֹ�˾";
		
		try {
			String url = "http://api.map.baidu.com/geocoder/v2/?address="
					+ java.net.URLEncoder.encode(qyinfo, "UTF-8")                                                 
					+ "&output=json&ak=0F6lvW7RH7VsRymFTCT7hYYOYVn5ezWk&city="
					+ java.net.URLEncoder.encode("�Ž���", "UTF-8");

			method = new GetMethod(url);
			client.executeMethod(method);
			if (method.getStatusCode() == 200) {
				String reString = method.getResponseBodyAsString();
				System.out.println(reString);
				reString = reString.replaceAll("(?s).*\"lng\":(\\d*\\.\\d*).*\"lat\":(\\d*\\.\\d*).*", "$1,$2");
				if (reString.matches("\\d+\\.\\d+,\\d+\\.\\d+")) { // ����滻�ɹ�
					String[] arr = reString.split(",");
					System.out.println(arr[0]+"\t"+arr[1]);
				}
			}
		} catch (Exception ignore) {

		} finally {
			method.releaseConnection();
		}

	}

}
