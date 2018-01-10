package com.scott;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;

/**
 * 
 * ������
 * ����ĳ��ҵ�����Ƿ���Բ�ѯ������
 *
 * @since  2018��1��9��
 * @author LRH
 *
 */
public class MainLocation {


	public static void main(String[] args) throws HttpException, IOException {
		
		
		String qymc = "�˹�ҵ���������߹��̿���Ժ";
		HttpClient client = new HttpClient();

			
		String url = "http://api.map.baidu.com/geocoder?address=" + java.net.URLEncoder.encode(qymc, "UTF-8")
						+ "&output=json&key=zkcsXqlktTGQLBcgqGXLBKZu&city="
						+ java.net.URLEncoder.encode("�Ž���", "UTF-8");
				
		GetMethod method = new GetMethod(url);

		client.executeMethod(method);
		if (method.getStatusCode() == 200) {
			String reString = method.getResponseBodyAsString();
			System.out.println("���-1\t" + reString);
			reString = reString.replaceAll("(?s).*\"lng\":(\\d*\\.\\d*).*\"lat\":(\\d*\\.\\d*).*", "$1,$2");
			if (reString.matches(".*\\d.*")) { // ����滻�ɹ�
				String[] arr = reString.split(",");
				System.out.println("���-2\t" + arr);	
			}
		}
				
	}
			
}
