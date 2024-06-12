package com.itworx.vaspp.datacollection.util.converters;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;
import org.apache.log4j.Logger;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class ServicesUSSD505Converter extends AbstractTextConverter{
	private Logger logger;
	private Map  <String ,ReqObject> keyVsReqObj=new HashMap<String,ReqObject>() ;
	private Map  <String ,Long > msisdnVsDate=new HashMap<String,Long>() ;
	public ServicesUSSD505Converter()
	{}
	public File[] convert(File[] inputFiles, String systemName)
	throws ApplicationException, InputException {
		logger = Logger.getLogger(systemName);
		logger
				.debug("Inside ServicesUSSD505Converter convert - started converting input files");
		String path = PropertyReader.getConvertedFilesPath();
		File[] outputFiles = new File[1];
		File outputFile = new File(path, inputFiles[0].getName());

		BufferedReader inputStream = null;
		BufferedWriter outputStream;
		try {
			outputStream = new BufferedWriter(new FileWriter(outputFile));
			for (int i = 0; i < inputFiles.length; i++) {
				logger
						.debug("ServicesUSSD505Converter.convert() - converting file "
								+ inputFiles[i].getName());
				
				inputStream = new BufferedReader(new FileReader(inputFiles[i]));
	            System.out.println("File [" + i +"]");
				String line;
				String date = "";
				String msisdn="";
				String key="";
				while (inputStream.ready()) {
					line = inputStream.readLine();
					if(line.contains(" ")&&line.split(" ").length>=6)
					{
						try
						{	
							date = getDate(line.split(" ")[0]+" "+line.split(" ")[1]);	
							
					          }catch(ParseException exc){ logger.error(exc) ; continue ;}
						if(line.contains("Request:")&&line.contains("[op:1]"))
						{   
							if(line.contains("[")&&line.contains("]"))
							msisdn=line.split("\\[")[1].split("\\]")[0];
							key=date+","+msisdn;
							if(msisdnVsDate.containsKey(key))
							{
								long count =msisdnVsDate.get(key);
								msisdnVsDate.remove(key);
								msisdnVsDate.put(key, count +1);
							}
							else
							{
								msisdnVsDate.put(key, new Long (1));
							}
							if(keyVsReqObj.containsKey(key))
							{
								ReqObject obj=	keyVsReqObj.get(key);
								obj.setReqCount(obj.getReqCount()+1);
								keyVsReqObj.remove(key);
								keyVsReqObj.put(key, obj);
							}else
							{
								ReqObject obj= new ReqObject ();
								obj.setReqCount(1);
								keyVsReqObj.put(key, obj);
							}
							
						}
						else if(line.contains("Response")&& line.contains("Message sent successfully")&&line.contains("[op:32]"))
						{
							if(line.contains("[")&&line.contains("]"))
								msisdn=line.split("\\[")[1].split("\\]")[0];
							key=date+","+msisdn;
							if(msisdnVsDate.containsKey(key))
							{
								long count=msisdnVsDate.get(key);
								msisdnVsDate.remove(key);
								if(count >0)
								{
									msisdnVsDate.put(key, count-1);
								if(keyVsReqObj.containsKey(key))
								{
									ReqObject obj=	keyVsReqObj.get(key);
									obj.setSuccReqCount(obj.getSuccReqCount()+1);
									keyVsReqObj.remove(key);
									keyVsReqObj.put(key, obj);
								}else
								{
									ReqObject obj= new ReqObject ();
									obj.setSuccReqCount(1);
									keyVsReqObj.put(key, obj);
								}
								}
							}
						}
						
					}
					else
						continue;
				}
			}//end of files
			
			inputStream.close();
			Iterator it1 = keyVsReqObj.keySet().iterator();
			//System.out.println("keyVsReqObj "+keyVsReqObj.size());
			while ( it1.hasNext()) {
				Object key = it1.next();	
			//	System.out.println("key "+key);
				ReqObject obj=keyVsReqObj.get(key);
				outputStream.write(key + "," +obj.getReqCount()+","+obj.getSuccReqCount());

				outputStream.newLine();
			}
			outputStream.close();
			outputFiles[0]=outputFile;
			logger.debug("ServicesUSSD505Converter.convert() - finished converting input files successfully ");
		
		}
		catch (FileNotFoundException e) {
				logger
						.error("ServicesUSSD505Converter.convert() - Input file not found "
								+ e);
				throw new ApplicationException(e);
			} catch (IOException e) {
				logger
						.error("ServicesUSSD505Converter.convert() - Couldn't read input file"
								+ e);
				throw new ApplicationException(e);
			}
			logger
					.debug("ServicesUSSD505Converter.convert() - finished converting input files successfully ");
			return outputFiles;
	}
	private String getDate(String line) throws ParseException {
		String[] tokens = null;
		Date date = new Date();
		String dateString;
		SimpleDateFormat inDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss.SSS");
		SimpleDateFormat outDateFormat = new SimpleDateFormat(
				"MM/dd/yyyy");

		
			date = inDateFormat.parse(line);
		dateString = outDateFormat.format(date);
		return dateString;

	}
	public static void main(String ag[]) {
		try {
			
			PropertyReader.init("D:\\build\\phase9\\phase9Builds\\DataCollection");
			ServicesUSSD505Converter s = new ServicesUSSD505Converter();
			File[] input = new File[1];
			input[0]=new File("D:\\build\\phase9\\phase9Builds\\DataCollection\\browser_505_2009020409.log");
			   s.convert(input,"Maha_Test");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	class ReqObject 
	{
		public long reqCount=0;
		public long succReqCount=0;
		public long getReqCount() {
			return reqCount;
		}
		public void setReqCount(long reqCount) {
			this.reqCount = reqCount;
		}
		public long getSuccReqCount() {
			return succReqCount;
		}
		public void setSuccReqCount(long succReqCount) {
			this.succReqCount = succReqCount;
		}
	}
}
