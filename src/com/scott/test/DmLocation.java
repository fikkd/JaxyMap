package com.scott.test;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
/**
 * 
 * 测试
 * 根据企业获取经纬度
 *
 */
public class DmLocation {

	public static void main(String[] args) {
		HttpClient client = new HttpClient();
		GetMethod method = null;
		
//		String qyinfo = "九江市华庆贸易有限公司";
//		String qyinfo = "九江市建设规划局八里湖新区规划分局";
		String qyinfo = "南昌市联泰建筑机械租赁有限公司永修分公司";
		
		try {
			String url = "http://api.map.baidu.com/geocoder/v2/?address="
					+ java.net.URLEncoder.encode(qyinfo, "UTF-8")                                                 
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
