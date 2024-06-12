package com.itworx.vaspp.datacollection.util.converters;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.lang.Math;

import org.apache.log4j.Logger;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;
import com.itworx.vaspp.datacollection.util.Utils;


public class PleaseCallMeCharValueConverter extends AbstractTextConverter{
	private Logger logger;
	private Map  <String ,String> msisdnVSdate=new HashMap<String,String>() ;
	private Map  <String ,Long> ChargeVSdate=new HashMap<String,Long>() ;
public PleaseCallMeCharValueConverter()
{}
public File[] convert(File[] inputFiles, String systemName)
throws ApplicationException, InputException {
	logger = Logger.getLogger(systemName);
	logger
			.debug("Inside PleaseCallMeCharValueConverter convert - started converting input files");
	String path = PropertyReader.getConvertedFilesPath();
	File[] outputFiles = new File[1];
	File outputFile = new File(path, inputFiles[0].getName());

	BufferedReader inputStream = null;
	BufferedWriter outputStream;
	try {
		outputStream = new BufferedWriter(new FileWriter(outputFile));
		for (int i = 0; i < inputFiles.length; i++) {
			logger
					.debug("PleaseCallMeCharValueConverter.convert() - converting file "
							+ inputFiles[i].getName());
			
			inputStream = new BufferedReader(new FileReader(inputFiles[i]));
            System.out.println("File [" + i +"]");
			String line;
			String date = "";
			String msisdn =null;
			while (inputStream.ready()) {
				line = inputStream.readLine();
				if(line.contains(","))
				{
					try {
					date =getDate(line.split(",")[0]);
					if(line.split(",")[2].contains("[")&&line.split(",")[2].contains("]"))
						msisdn=Utils.stringBetween(line.split(",")[2], "[", "]");
					if(line.contains("AppRequest:")&& line.contains("SERVICE_PARAM_3$505"))
					{
						if(!msisdnVSdate.containsKey(msisdn))
						{
							msisdnVSdate.put(msisdn, date);
						}
					}else if (line.contains("AppResponse ErrCode:0") && line.contains("Cost-Information.Unit-Value1.Value-Digits=")&&line.contains("Cost-Information.Unit-Value1.Exponent="))
					{
						if(msisdnVSdate.containsKey(msisdn))
						{
							String respDate=msisdnVSdate.get(msisdn);
							msisdnVSdate.remove(msisdn);
							 double base=0;
							 double exp=0;
							if((line.split("Cost-Information.Unit-Value1.Value-Digits=")[1].split(" ")[0]).contains("'"))							
								base=Double.parseDouble(line.split("Cost-Information.Unit-Value1.Value-Digits=")[1].split(" ")[0].split("'")[1].split("'")[0]);
							if((line.split("Cost-Information.Unit-Value1.Exponent=")[1].split(" ")[0]).contains("'"))							
								exp=Double.parseDouble(line.split("Cost-Information.Unit-Value1.Exponent=")[1].split(" ")[0].split("'")[1].split("'")[0]);
							double any=java.lang.Math.pow(10, exp);
							any=any*base;
							
							String respKaye=respDate+","+any;
							if(ChargeVSdate.containsKey(respKaye))
							{
								long count=ChargeVSdate.get(respKaye);
								ChargeVSdate.put(respKaye, count+1);
							}
							else
							{
								ChargeVSdate.put(respKaye, new Long(1));
							}
						    
						}
					}	
						
					}
					  catch(ParseException exc){ logger.error(exc) ; continue ;}
				}
			}
			}
	inputStream.close();
	Iterator it=ChargeVSdate.keySet().iterator();
	while(it.hasNext())
	{
		Object key=it.next();
		outputStream.write(key+","+ChargeVSdate.get(key));
		 outputStream.newLine();
	}
			outputStream.close();
			outputFiles[0]=outputFile;
			logger.debug("PleaseCallMeCharValueConverter.convert() - finished converting input files successfully ");
		
		
	}
		catch (FileNotFoundException e) {
				logger
						.error("PleaseCallMeCharValueConverter.convert() - Input file not found "
								+ e);
				throw new ApplicationException(e);
			} catch (IOException e) {
				logger
						.error("PleaseCallMeCharValueConverter.convert() - Couldn't read input file"
								+ e);
				throw new ApplicationException(e);
			}
			logger
					.debug("PleaseCallMeCharValueConverter.convert() - finished converting input files successfully ");
			return outputFiles;
			
	}
private String getDate(String line) throws ParseException {
	String[] tokens = null;
	Date date = new Date();
	String dateString;
	SimpleDateFormat inDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss.SSS");
	SimpleDateFormat outDateFormat = new SimpleDateFormat(
			"MM/dd/yyyy HH:00:00");

	
		date = inDateFormat.parse(line);
	dateString = outDateFormat.format(date);
	return dateString;

}
public static void main(String ag[]) {
	try {
		
		PropertyReader.init("D:\\build\\phase10\\DataCollection");
		PleaseCallMeCharValueConverter s = new PleaseCallMeCharValueConverter();
		File[] input = new File[1];
		input[0]=new File("D:\\build\\phase10\\DataCollection\\ipcconnector_2011061012.log");
		   s.convert(input,"Maha_Test");
		
	} catch (Exception e) {
		e.printStackTrace();
	}
}
}
