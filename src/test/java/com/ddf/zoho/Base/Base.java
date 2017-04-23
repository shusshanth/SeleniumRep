package com.ddf.zoho.Base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import com.ddf.zoho.SampleRead;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
public class Base {
	public WebDriver driver;
	public Properties pro;
	public SampleRead read;
	public ExtentReports rep=com.ddf.utility.ExtentManager.getinstance();
	public ExtentTest test;
	
	boolean gridRun=true;
	
	public void init(){
		if(pro==null){
			pro= new Properties();
			try {
				FileInputStream fis=new FileInputStream(System.getProperty("user.dir")+"//src//test//resources//Config.properties");
				pro.load(fis);
			} catch (Exception e) {
				
				e.printStackTrace();
			}
		}
	}
	public void openbrowser(String browser){
		//intiliaze the properties file
		
		test.log(LogStatus.INFO, "Opening browser "+browser );
		if(!gridRun){
			if(browser.equals("Mozilla"))
				//System.setProperty("webdriver.firefox.marionette", pro.getProperty("Mozilla_exe"));
			
				driver=new FirefoxDriver();
		}
			else if(browser.equals("Chrome")){
				System.setProperty("webdriver.chrome.driver", pro.getProperty("Chrome_exe"));
				driver=new ChromeDriver();
			}
			else if (browser.equals("IE")){
				System.setProperty("webdriver.chrome.driver", pro.getProperty("iedriver_exe"));
				driver= new InternetExplorerDriver();
			}
		
		else{// grid run
			
			DesiredCapabilities cap=null;
			if(browser.equals("Mozilla")){
			
				cap = DesiredCapabilities.firefox();
			
			
				cap.setBrowserName("firefox");
				
				cap.setJavascriptEnabled(true);
				cap.setPlatform(org.openqa.selenium.Platform.WINDOWS);
				
			}else if(browser.equals("Chrome")){
				 cap = DesiredCapabilities.chrome();
				 cap.setBrowserName("chrome");
				 cap.setPlatform(org.openqa.selenium.Platform.WINDOWS);
			}
			
			try {
				driver= new RemoteWebDriver(new URL("http://192.168.1.3:4444/wd/hub"), cap);
			
			} catch (Exception e) {
			
				e.printStackTrace();
			}
			
		}
		
		
		
		
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		test.log(LogStatus.INFO, "Browser opened successfully "+ browser);

		
	}
	
	
	public void navigate(String urlkey){
		test.log(LogStatus.INFO, "Navigating to "+pro.getProperty(urlkey));
		driver.get(pro.getProperty(urlkey));
		
	}
	
	public void type(String locatorkey, String data){
		test.log(LogStatus.INFO, "Typing in "+locatorkey+".Data -"+data );

		getElement(locatorkey).sendKeys(data);
		test.log(LogStatus.INFO, "Typed  successfully "+locatorkey+".Data -"+data );
	}
	
	public void click(String locatorkey){
		test.log(LogStatus.INFO, "clicking on "+locatorkey);
		getElement(locatorkey).click();
		test.log(LogStatus.INFO, "clicked  successfully on "+locatorkey);
	}
	
	public void clickAndWait(String locator_clicked, String locator_exp){
		test.log(LogStatus.INFO, "Clicking and Waiting "+ locator_clicked);
		int count=5;
		for(int i=0;i<count;i++){
			getElement(locator_clicked).click();
			Wait(2);
			if(isElementpresent(locator_exp))
				break;
		}
	}
		public String getText(String locatorkey){
			test.log(LogStatus.INFO, "getting text  from "+locatorkey);	
			return getElement(locatorkey).getText();
		
	}
	//finding the element
	public WebElement getElement(String locatorkey){
		WebElement web=null;
		try{
		if(locatorkey.endsWith("_id"))
			web=driver.findElement(By.id(pro.getProperty(locatorkey)));
		else if(locatorkey.endsWith("_xpath"))
		web=	driver.findElement(By.xpath(pro.getProperty(locatorkey)));
		else if(locatorkey.endsWith("_name"))
		web=	driver.findElement(By.name(pro.getProperty(locatorkey)));
		else{
			reportFail("locator not found "+locatorkey);
			Assert.fail("locator not found "+locatorkey);
		}
		
	}catch(Exception e){
		//fail the test and it will report
		reportFail(e.getMessage());
		e.printStackTrace();
		Assert.fail();
	}
		return web;
	}
	/*******verifications******/
	
	public boolean VerifyTitle(){
		return false;
	}
	
	public boolean Verifytext(String locatorkey, String Expected){
		String actualtext=getElement(locatorkey).getText();
		String Expectedtext=pro.getProperty(Expected);
		if(actualtext.equals(Expectedtext))
		return true;
		else 
			return false;
	}
	
	public boolean isElementpresent(String locatorkey){
		List<WebElement> elelist=null;
		if(locatorkey.endsWith("_id"))
			elelist=driver.findElements(By.id(pro.getProperty(locatorkey)));
		else if(locatorkey.endsWith("_xpath"))
			elelist=	driver.findElements(By.xpath(pro.getProperty(locatorkey)));
		else if(locatorkey.endsWith("_name"))
			elelist=	driver.findElements(By.name(pro.getProperty(locatorkey)));
		else{
			reportFail("locator not found "+locatorkey);
			Assert.fail("locator not found "+locatorkey);
		}
		if(elelist.size()==0)
			return false;
			else 
				return true;
	}
	
	/******Reporting******/
	
	public void reportFail(String msg){
		test.log(LogStatus.FAIL, msg);
		takeScreenshot();
		Assert.fail(msg);
	}
	
	public void reportPass(String msg){
		test.log(LogStatus.PASS, msg);
	}
	
	public void takeScreenshot(){
		Date d= new Date();
		String Screenshotfile=d.toString().replace(":", "_").replace(" ", "_")+".PNG";
		File src=((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		try{
			FileUtils.copyFile(src, new File(System.getProperty("user.dir")+"//Screenshots//"+Screenshotfile));
		}catch(Exception e){
			e.printStackTrace();
		}
		test.log(LogStatus.INFO,"Screenshot" +test.addScreenCapture(System.getProperty("user.dir")+"//Screenshots//"+Screenshotfile));
	}
	
	/*****Application function***/
	public void waitForPageToLoad() {
		Wait(2);
		JavascriptExecutor js=(JavascriptExecutor)driver;
		String state = (String)js.executeScript("return document.readyState");
		
		while(!state.equals("complete")){
			Wait(2);
			state = (String)js.executeScript("return  document.readyState");
		}
	}
	
	
	
	public void Wait(int timetoWaitinsec){
		try {
			Thread.sleep(timetoWaitinsec*1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void acceptpopup(){
		click("LeadDeleteButton_xpath");
		Wait(3);
		
		List<String> lst= new ArrayList<String>(driver.getWindowHandles());
		
		int totalwin=lst.size();
		
		System.out.println(totalwin);
		
				driver.switchTo().window(lst.get(0));
				driver.findElement(By.xpath(pro.getProperty("ConfirmDeleteButton_xpath"))).click();
				driver.switchTo().defaultContent();
		}
	

	
	public boolean dologin(String username, String password){
		test.log(LogStatus.INFO, "Login with "+username+","+password);
		click("Login_xpath");
		waitForPageToLoad();
	    driver.switchTo().frame(0);
		type("Emailusername_xpath",username );
		type("Password_xpath",password );
		click("Signinbutton_xpath");
		if(isElementpresent("crmlink_xpath")){
		test.log(LogStatus.INFO, "Login Success");
			return true;
		}
			else{
				test.log(LogStatus.INFO, "Login Failed");
				return false;
			}
				
			}
				
	public int getLeadrowNum(String leadname){
		test.log(LogStatus.INFO, "Finding the lead "+ leadname);
		List<WebElement> leadnames=driver.findElements(By.xpath(pro.getProperty("AllLeads_xpath")));
		for(int i=0;i<leadnames.size();i++){
			System.out.println(leadnames.get(i).getText());
			if(leadnames.get(i).getText().trim().equals(leadname)){
				test.log(LogStatus.INFO, "Lead found in a row num "+ (i+1));
				return(i+1);
			}
		}
		test.log(LogStatus.INFO, "Lead found  ");
		return -1;
		
	}
		
	public void clickonLead(String leadname){
		test.log(LogStatus.INFO, "Clicking on the lead "+leadname);
		int rNum=getLeadrowNum(leadname);
		driver.findElement(By.xpath(pro.getProperty("leadpart1_xpath") +rNum+ pro.getProperty("leadpart2_xpath"))).click();
		
	}
	
	public void selectValue(String value) {
		WebElement ul = driver.findElement(By.xpath(pro.getProperty("StageDropdown_xpath")));
        List<WebElement> options = ul.findElements(By.tagName("li"));

        for (WebElement option : options) {
            if (option.getText().contains(value)) { //Here you have select contains or some thing 
                option.click();
                break;
            }
        }
    }

  public void selectDate(String d){
	  
	  SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy");
	  
	  try {
		  Date dateTobeSelected = sdf.parse(d);
			Date currentDate = new Date();
			sdf = new SimpleDateFormat("MMM");
			String monthToBeSelected=sdf.format(dateTobeSelected);
			sdf = new SimpleDateFormat("yyyy");
			String yearToBeSelected=sdf.format(dateTobeSelected);
			sdf = new SimpleDateFormat("d");
			String dayToBeSelected=sdf.format(dateTobeSelected);
		//May 2017
		
		String monthyearToBeSelected=monthToBeSelected+ " "+yearToBeSelected;
		
		while(true){
			if(currentDate.compareTo(dateTobeSelected)==1){
				//back
				click("BackDate_xpath");
			}else if(currentDate.compareTo(dateTobeSelected)==-1){
				//front
				click("FrontDate_xpath");
			}if(monthyearToBeSelected.equals(getText("monthyeardisplay_xpath")))
		{
			break;
		}
			
		}
		
		driver.findElement(By.xpath("//td[text()='"+dayToBeSelected+"']")).click();
	  }catch (Exception e) {
		
		e.printStackTrace();
	}
	  
		}

  
  }
  







