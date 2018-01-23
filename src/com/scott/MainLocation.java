package com.scott;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;

/**
 * 
 * 测试类
 * 测试某企业名称是否可以查询到坐标
 *
 * @since  2018年1月9日
 * @author LRH
 *
 */
public class MainLocation {


	public static void main(String[] args) throws HttpException, IOException {
		
		
		String qymc = "九江市罗莎数码影视有限公司";
		HttpClient client = new HttpClient();

		
		// http://api.map.baidu.com/lbsapi/cloud/geocoding-api.htm
		/*		
		String url = "http://api.map.baidu.com/geocoder?address=" + java.net.URLEncoder.encode(qymc, "UTF-8")
						+ "&output=json&key=zkcsXqlktTGQLBcgqGXLBKZu&city="
						+ java.net.URLEncoder.encode("九江市", "UTF-8");
		*/
		
		String url = "http://api.map.baidu.com/geocoder/v2/?address=" + java.net.URLEncoder.encode(qymc, "UTF-8")
					+ "&output=json&ak=5Wlafr2Q2Y43Wq26HTv63oXeHyf5WA32&city="
					+ java.net.URLEncoder.encode("九江市", "UTF-8");
				
		GetMethod method = new GetMethod(url);

		client.executeMethod(method);
		if (method.getStatusCode() == 200) {
			String reString = method.getResponseBodyAsString();
			System.out.println("结果-1\t" + reString);
			// {"status":0,"result":{"location":{"lng":115.99984802155373,"lat":29.71963952612237},"precise":0,"confidence":10,"level":"城市"}}
			// "{\"status\":1,\"msg\":\"Internal Service Error:无相关结果\",\"results\":[]}";
			reString = reString.replaceAll("(?s).*\"lng\":(\\d*\\.\\d*).*\"lat\":(\\d*\\.\\d*).*", "$1,$2");
			if (reString.matches("\\d+\\.\\d+,\\d+\\.\\d+")) { // 如果替换成功
				String[] arr = reString.split(",");
				System.out.println("结果-2\t" + arr[0] + "   " + arr[1]);
				
			}
		}
				
	}
			
}
