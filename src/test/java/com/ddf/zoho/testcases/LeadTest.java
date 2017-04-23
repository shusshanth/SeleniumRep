package com.ddf.zoho.testcases;

import java.util.Hashtable;

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
import com.thoughtworks.selenium.webdriven.commands.WaitForPageToLoad;

import junit.framework.Assert;

public class LeadTest extends Base{

SampleRead read;
SoftAssert soft;

	@Test(priority=1, dataProvider="getdata")
	public void CreateLeadTest(Hashtable<String, String>  data) throws Exception{
	test=rep.startTest("CreateLeadTest");
	
	test.log(LogStatus.INFO, data.toString());
	if(!DataUtil.isRunnable("CreateLeadTest", read) ||  data.get("Runmode").equals("N")){
		test.log(LogStatus.SKIP, "Skipping the test as runmode is N");
		throw new SkipException("Skipping the test as runmode is N");
	}

	openbrowser(data.get("Browser"));
	navigate("Appurl");
	dologin(pro.getProperty("username"), pro.getProperty("password"));
	click("crmlink_xpath");
	waitForPageToLoad();
	click("LeadLink_xpath");
	click("LeadCreateButton_xpath");
	
	type("LeadComapny_xpath", data.get("CompanyName"));
	type("LeadFirstName_xpath", data.get("FirstName"));
	type("LeadLastName_xpath", data.get("LastName"));
	click("Leadsavebutton_xpath");
	
	clickAndWait("LeadLink_xpath","LeadCreateButton_xpath" );
	//validate
	
	int rNum=getLeadrowNum(data.get("FirstName")+ " "+data.get("LastName"));

	if(rNum==-1)
		reportFail("Lead not found in lead table "+ data.get("FirstName")+ " "+data.get("LastName"));

	reportPass("Lead found in lead table "+data.get("FirstName")+ " " +data.get("LastName")); 
	//takeScreenshot();
	
	}
	
	
	
	@Test(priority=2,dataProvider="getdata")
	public void ConvertLeadTest(Hashtable<String, String>  data)
	{
		test=rep.startTest("ConvertLeadTest");
		
		if(!DataUtil.isRunnable("ConvertLeadTest", read)||data.get("Runmode").equals("N")){
			test.log(LogStatus.INFO, data.toString());
			test.log(LogStatus.SKIP, "Skipped due to Runmode as No");
			throw new SkipException("Skipped due to Runmode as No");
		}

		openbrowser(data.get("Browser"));
		navigate("Appurl");
		dologin(pro.getProperty("username"), pro.getProperty("password"));
		click("crmlink_xpath");
		Wait(2);
		click("LeadLink_xpath");
		
		clickonLead(data.get("FirstName")+ " "+data.get("LastName"));	
		click("LeadConvert_xpath");
		click("LeadConvertandsave_xpath");
		//validate
	}
	
	@Test(priority=3,dataProvider="DeleteLeadTest" )
	public void DeleteLeadAccountTest(Hashtable<String, String> data){
		
   test=rep.startTest("DeleteLeadAccountTest");
		
		if(!DataUtil.isRunnable("DeleteLeadAccountTest", read)||data.get("Runmode").equals("N")){
			test.log(LogStatus.INFO, data.toString());
			test.log(LogStatus.SKIP, "Skipped due to Runmode as No");
			throw new SkipException("Skipped due to Runmode as No");
		}

		openbrowser(data.get("Browser"));
		navigate("Appurl");
		dologin(pro.getProperty("username"), pro.getProperty("password"));
		click("crmlink_xpath");
		waitForPageToLoad();
		click("LeadLink_xpath");
		clickonLead(data.get("FirstName")+ " "+data.get("LastName"));	
		click("CustomizeLeadDeleteButton_xpath");
		Wait(1);
	
		acceptpopup();
		Wait(2);
		click("Gobacktolead_xpath");
		int rNum=getLeadrowNum(data.get("FirstName")+ " "+data.get("LastName"));
		if(rNum!=-1)
			reportFail("Could not delete the lead");
		reportPass("lead deleted successfully");
	}
	
	@DataProvider(parallel=true)
	public Object[][] getdata(){
	    super. init();
		read= new SampleRead(pro.getProperty("xlspath"));
		return DataUtil.getTestData(read, "CreateLeadTest");
		
}
	@DataProvider(parallel=true)
	public Object[][] DeleteLeadTest(){
	    super. init();
		read= new SampleRead(pro.getProperty("xlspath"));
		return DataUtil.getTestData(read, "DeleteLeadAccountTest");
		
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
		
	}

