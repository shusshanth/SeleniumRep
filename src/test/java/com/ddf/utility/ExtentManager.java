package com.ddf.utility;

import java.io.File;
import java.util.Date;

import com.relevantcodes.extentreports.DisplayOrder;
import com.relevantcodes.extentreports.ExtentReports;

public class ExtentManager {
	private static ExtentReports report;
	
	public static ExtentReports getinstance(){
		if(report==null){
			Date d= new Date();
		String filename=	d.toString().replace(":", "_").replace(" ", "_")+".html";
			report= new ExtentReports("F:\\ExtentReport\\"+filename, true, DisplayOrder.NEWEST_FIRST);
			report.loadConfig(new File(System.getProperty("user.dir")+"//extent-config.xml"));
			
			report.addSystemInfo("Selenium version","3.3.1").addSystemInfo("Environment", "Testing");
		}
		return report;
	}
}
