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

import org.apache.log4j.Logger;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;

public class SwitchDivertAvgTimeConverter extends AbstractTextConverter{
	private Logger logger;	
	private Map  <String ,SumCount> keyVSSumCount=new HashMap<String,SumCount>() ;
	public SwitchDivertAvgTimeConverter()
	{}
	public File[] convert(File[] inputFiles, String systemName)
	throws ApplicationException, InputException {
		logger = Logger.getLogger(systemName);
		logger
				.debug("Inside SwitchDivertAvgTimeConverter convert - started converting input files");
		String path = PropertyReader.getConvertedFilesPath();
		File[] outputFiles = new File[1];
		File outputFile = new File(path, inputFiles[0].getName());

		BufferedReader inputStream = null;
		BufferedWriter outputStream;
		try {
			outputStream = new BufferedWriter(new FileWriter(outputFile));
			for (int i = 0; i < inputFiles.length; i++) {
				logger
						.debug("SwitchDivertAvgTimeConverter.convert() - converting file "
								+ inputFiles[i].getName());
				
				inputStream = new BufferedReader(new FileReader(inputFiles[i]));
	        	String line;
				String date = "";
				String Switch="";
				String UserAction="";
				long announcementID=0;
				long divertCondition=0;
				long sum=0;
				long count=0;
				
				while (inputStream.ready()) {
					line = inputStream.readLine();
					if(line.contains("|")&&line.split("\\|").length >=9)
					{
					if(line.contains("|")&&line.split("\\|").length>= 11)
					{
						try
						{	date=getDate(line.split("\\|")[4]);						   
						    sum=Long.parseLong(line.split("\\|")[6]!=null?line.split("\\|")[6]:"0");
						    
					} catch(ParseException exc){ logger.error(exc) ; continue ;}
					  catch(NumberFormatException exc){ logger.error(exc) ; continue ;}
					}
					else if((line.contains("|")&&line.split("\\|").length>= 9))
					{
						try
						{
							date=getDate(line.split("\\|")[3]);						   
						   sum=Long.parseLong(line.split("\\|")[5]!=null?line.split("\\|")[5]:"0");
						} 
						catch(ParseException exc){ logger.error(exc) ; continue ;}
						catch(NumberFormatException exc){ logger.error(exc) ; continue ;}
					}		
					if(keyVSSumCount.containsKey(date))
					{
						
							SumCount obj=keyVSSumCount.get(date);	
							obj.setSum(obj.getSum()+sum);
							obj.setCount(obj.getCount()+1);
						     keyVSSumCount.remove(date);
						     keyVSSumCount.put(date, obj);
					}else
					{   
						SumCount obj= new SumCount();
						obj.setSum(sum);
						obj.setCount(new Long(1));
						 keyVSSumCount.put(date, obj);
					}
				
					
				
			}
					else
				{continue;}
			}
				
				}
		    inputStream.close();
          Iterator it=keyVSSumCount.keySet().iterator();
			while(it.hasNext())
			{
				 Object key=it.next();	
				
				 double avgTime= new Double (((SumCount)keyVSSumCount.get(key)).getSum())/new Double (((SumCount)keyVSSumCount.get(key)).getCount());
				 outputStream.write(key+","+avgTime);
				 outputStream.newLine();
			}
			outputStream.close();
			outputFiles[0]=outputFile;
			logger.debug("SwitchDivertAvgTimeConverter.convert() - finished converting input files successfully ");
		
		}
		catch (FileNotFoundException e) {
				logger
						.error("SwitchDivertAvgTimeConverter.convert() - Input file not found "
								+ e);
				throw new ApplicationException(e);
			} catch (IOException e) {
				logger
						.error("SwitchDivertAvgTimeConverter.convert() - Couldn't read input file"
								+ e);
				throw new ApplicationException(e);
			}
			logger
					.debug("SwitchDivertAvgTimeConverter.convert() - finished converting input files successfully ");
			return outputFiles;
			
		}
	private String getDate(String line) throws ParseException {
		String[] tokens = null;
		Date date = new Date();
		String dateString;
		SimpleDateFormat inDateFormat = new SimpleDateFormat(
				"dd-MM-yyyy HH:mm:ss:SSSS");
		SimpleDateFormat outDateFormat = new SimpleDateFormat(
				"MM/dd/yyyy HH:00:00");

		
			date = inDateFormat.parse(line);
		dateString = outDateFormat.format(date);
		return dateString;

	}
	public static void main(String ag[]) {
		try {
			
			PropertyReader.init("D:\\build\\phase9\\phase9Builds\\DataCollection");
			SwitchDivertAvgTimeConverter s = new SwitchDivertAvgTimeConverter();
			File[] input = new File[1];
			input[0]=new File("D:\\build\\phase9\\phase9Builds\\DataCollection\\cdr.log-20110301");
			   s.convert(input,"Maha_Test");		   
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	class SumCount
	{
		public long sum=0;
		public long count=0;
		public long getSum() {
			return sum;
		}
		public void setSum(long sum) {
			this.sum = sum;
		}
		public long getCount() {
			return count;
		}
		public void setCount(long count) {
			this.count = count;
		}
		
	}
}
