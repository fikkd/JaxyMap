package com.scott.common;

import org.apache.log4j.xml.DOMConfigurator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class CommonUtil {

	private static ApplicationContext context = null;
	
	public static ApplicationContext getSpringApplicationContext() {
		String[] locations = new String[] { "conf/applicationContext.xml", "conf/index_dao.xml", "conf/params.xml" };
		if (context == null) {
			context = new FileSystemXmlApplicationContext(locations); 			
		}
		return context;
	}
	
	
	public static void configureLog4j() {		
		DOMConfigurator.configure("conf/log4j.xml");
	}

}
