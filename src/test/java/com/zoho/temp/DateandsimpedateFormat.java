package com.zoho.temp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateandsimpedateFormat {

	public static void main(String[] args) throws Exception {
	Date d= new Date();
	System.out.println(d);
	
	String date="15/5/2015";
	
	SimpleDateFormat df= new SimpleDateFormat("dd/MM/yyyy");
	Date d1=df.parse(date);
	//System.out.println(d1);
	System.out.println(d.compareTo(d1));
	
	df= new SimpleDateFormat("yyyy");
	System.out.println(df.format(d1));
		
	}

}
