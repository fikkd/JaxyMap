package com.scott.test;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

public class MyMain {

	public static void main(String[] args) {
		HttpClient client = new HttpClient();
		GetMethod method = null;
		try {
			String url = "http://api.map.baidu.com/geocoder/v2/?address="
//					+ java.net.URLEncoder.encode("�Ž��̵ش󶼻�ʵҵ���޹�˾", "UTF-8")             
//					+ java.net.URLEncoder.encode("�ӱ��������ѧ�����о���", "UTF-8")                           
//					+ java.net.URLEncoder.encode("�Ž��ڰ��ɲ���ѵ����", "UTF-8")                          
//					+ java.net.URLEncoder.encode("�˹�ҵ���������߹��̿���Ժ", "UTF-8")                          
					+ java.net.URLEncoder.encode("�Ž����ÿ��������ؽ�������", "UTF-8")                          
//					+ java.net.URLEncoder.encode("�Ž��з���ѧ��", "UTF-8")                          
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
