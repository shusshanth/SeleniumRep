package com.ddf.zoho.testcases;

import java.util.Hashtable;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
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

public class DealsTest extends Base {
	SampleRead read;
	SoftAssert soft;
	
	@Test(priority=1,dataProvider="getdata")
	public void CreateDealTest(Hashtable<String, String> data){
		
		
		test=rep.startTest("CreateDealTest");
		
		if(!DataUtil.isRunnable("CreateDealTest", read)||data.get("Runmode").equals("N")){
			test.log(LogStatus.INFO, data.toString());
			test.log(LogStatus.SKIP, "Skipped due to Runmode as No");
			throw new SkipException("Skipped due to Runmode as No");
		}

		openbrowser(data.get("Browser"));
		navigate("Appurl");
		dologin(pro.getProperty("username"), pro.getProperty("password"));
		click("crmlink_xpath");
		waitForPageToLoad();
		click("DealsTab_xpath");
		waitForPageToLoad();
		click("CreateDeal_xpath");
	  
	   type("DealName_xpath", data.get("DealName"));
	   type("AccountDealName_xpath", data.get("AccountName"));
	   click("StageDeal_xpath");
       Wait(1);
      selectValue(data.get("Stage"));
      Wait(1);
      click("ClosingDate_xpath");
      selectDate(data.get("ClosingDate"));
      click("SaveDealButton_xpath");
      //validate
	
	}

	@Test(priority=2)
	public void DeleteDealTest(){
		//assignment
	test=rep.startTest("DeleteDealTest");
	reportPass("Test Passed");
		
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
		return DataUtil.getTestData(read, "CreateDealTest");
		
}
	
	
}
