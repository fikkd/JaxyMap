package com.scott.test;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

public class MyMain {

	public static void main(String[] args) {
		HttpClient client = new HttpClient();
		GetMethod method = null;
		try {
			String url = "http://api.map.baidu.com/geocoder/v2/?address="
//					+ java.net.URLEncoder.encode("九江绿地大都会实业有限公司", "UTF-8")             
//					+ java.net.URLEncoder.encode("赣北早熟梨科学技术研究所", "UTF-8")                           
//					+ java.net.URLEncoder.encode("九江口岸干部培训中心", "UTF-8")                          
//					+ java.net.URLEncoder.encode("核工业华东二六七工程勘察院", "UTF-8")                          
					+ java.net.URLEncoder.encode("九江经济开发区土地交易中心", "UTF-8")                          
//					+ java.net.URLEncoder.encode("九江市发明学会", "UTF-8")                          
					+ "&output=json&ak=0F6lvW7RH7VsRymFTCT7hYYOYVn5ezWk&city="
					+ java.net.URLEncoder.encode("九江市", "UTF-8");

			method = new GetMethod(url);
			client.executeMethod(method);
			if (method.getStatusCode() == 200) {
				String reString = method.getResponseBodyAsString();
				System.out.println(reString);
				reString = reString.replaceAll("(?s).*\"lng\":(\\d*\\.\\d*).*\"lat\":(\\d*\\.\\d*).*", "$1,$2");
				if (reString.matches("\\d+\\.\\d+,\\d+\\.\\d+")) { // 如果替换成功
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
