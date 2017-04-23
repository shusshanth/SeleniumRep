package com.ddf.zoho;

import java.util.Hashtable;



public class DataUtil {

	

	public static Object[][] getTestData(SampleRead read, String testCaseName){
		String sheetName="Data";
		// reads data for only testCaseName
		
		int testStartRowNum=1;
		while(!read.getCellData(sheetName, 0, testStartRowNum).equals(testCaseName)){
			testStartRowNum++;
		}
		System.out.println("Test starts from row - "+ testStartRowNum);
		int colStartRowNum=testStartRowNum+1;
		int dataStartRowNum=testStartRowNum+2;
		
		// calculate rows of data
		int rows=0;
		while(!read.getCellData(sheetName, 0, dataStartRowNum+rows).equals("")){
			rows++;
		}
		System.out.println("Total rows are  - "+rows );
		
		//calculate total cols
		int cols=0;
		while(!read.getCellData(sheetName, cols, colStartRowNum).equals("")){
			cols++;
		}
		System.out.println("Total cols are  - "+cols );
		Object[][] data = new Object[rows][1];
		//read the data
		int dataRow=0;
		Hashtable<String,String> table=null;
		for(int rNum=dataStartRowNum;rNum<dataStartRowNum+rows;rNum++){
			table = new Hashtable<String,String>();
			for(int cNum=0;cNum<cols;cNum++){
				String key=read.getCellData(sheetName,cNum,colStartRowNum);
				String value= read.getCellData(sheetName, cNum, rNum);
				table.put(key, value);
				// 0,0 0,1 0,2
				//1,0 1,1
			}
			data[dataRow][0] =table;
			dataRow++;
		}
		
		return data;
	}
	
	public static boolean isRunnable(String testName, SampleRead read){
		String sheet="TestCases";
		int rows = read.getRowCount(sheet);
		for(int r=2;r<=rows;r++){
			String tName=read.getCellData(sheet, "TCID", r);
			if(tName.equals(testName)){
				String runmode=read.getCellData(sheet, "Runmode", r);
				if(runmode.equals("Y"))
					return true;
				else
					return false;

			}
		}
		return false;
	}

}
