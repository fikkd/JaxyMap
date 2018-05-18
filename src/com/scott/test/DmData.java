package com.scott.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

import org.springframework.context.ApplicationContext;

import com.scott.common.CommonUtil;
import com.scott.service.IBusiness;

/**
 * 测试 
 * 构造30万条企业存入数据库 高并发测试需要
 *
 */
public class DmData {

	public static void main(String[] args) {
		ApplicationContext context = CommonUtil.getSpringApplicationContext();
		IBusiness service = (IBusiness) context.getBean("business");

		CommonUtil.configureLog4j();
		LineNumberReader lnr = null;
		try {
			lnr = new LineNumberReader(
					new InputStreamReader(new FileInputStream(new File("G:\\space\\d.txt")), "UTF-8"));
			String info = null;
			while ((info = lnr.readLine()) != null) {				
				service.saveQyinfo(info);
				System.out.println(lnr.getLineNumber());
			}
		} catch (Exception e) {

		} finally {
			if (lnr != null) {
				try {
					lnr.close();
				} catch (Exception ignore) {
				}
			}
		}
	}

}
