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
		
		
		String qymc = "核工业华东二六七工程勘察院";
		HttpClient client = new HttpClient();

			
		String url = "http://api.map.baidu.com/geocoder?address=" + java.net.URLEncoder.encode(qymc, "UTF-8")
						+ "&output=json&key=zkcsXqlktTGQLBcgqGXLBKZu&city="
						+ java.net.URLEncoder.encode("九江市", "UTF-8");
				
		GetMethod method = new GetMethod(url);

		client.executeMethod(method);
		if (method.getStatusCode() == 200) {
			String reString = method.getResponseBodyAsString();
			System.out.println("结果-1\t" + reString);
			reString = reString.replaceAll("(?s).*\"lng\":(\\d*\\.\\d*).*\"lat\":(\\d*\\.\\d*).*", "$1,$2");
			if (reString.matches(".*\\d.*")) { // 如果替换成功
				String[] arr = reString.split(",");
				System.out.println("结果-2\t" + arr);	
			}
		}
				
	}
			
}
