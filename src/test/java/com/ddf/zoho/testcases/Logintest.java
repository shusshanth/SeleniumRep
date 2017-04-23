package com.ddf.zoho.testcases;


import java.util.Hashtable;

import org.openqa.selenium.By;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.ddf.zoho.DataUtil;
import com.ddf.zoho.SampleRead;
import com.ddf.zoho.Base.Base;
import com.relevantcodes.extentreports.LogStatus;

public class Logintest extends Base{
	String testCaseName="LoginTest";
	SoftAssert soft;
	SampleRead read;
	
	@Test(dataProvider="getdata")
	public void doLoginTest(Hashtable<String, String> data){
		
		  test	=rep.startTest("Logintest");
		    if(!DataUtil.isRunnable(testCaseName, read)||data.get("Runmode").equals("N")){
		    	test.log(LogStatus.INFO, data.toString());
		    	test.log(LogStatus.SKIP, "Skipped due to Runmode as N");
		    	throw new SkipException("Skipped due to Runmode as N");
		    }
		
		   openbrowser(data.get("Browser"));
		   navigate("Appurl");
		  boolean actuaresult= dologin(data.get("Username"), data.get("Password"));
		  boolean expectedresult=false;
		  if(data.get("Expected Result").equals("Y"))
			  expectedresult=true;
			  else
				  expectedresult=false;
		  if(expectedresult!=actuaresult)
			  reportFail("Test Failed");//critical error
		  
		  reportPass("Login Test Passed");
		 
		  }

		
	
		@BeforeMethod
		public void init(){
		     soft= new SoftAssert();
		   
		}
		
		@AfterMethod
		public void teardown()
		{
			try{
				soft.assertAll();
				}catch(Error e){
					test.log(LogStatus.FAIL, e.getMessage());
				}
			if(rep!=null){
				rep.endTest(test);
				rep.flush();
			}
			if(driver!=null)
				driver.quit();
			
			
		}
		
		
		
		@DataProvider(parallel=true)
		public Object[][] getdata(){
		    super. init();
			read= new SampleRead(pro.getProperty("xlspath"));
			return DataUtil.getTestData(read, testCaseName);
			
	}
		
	}
	

