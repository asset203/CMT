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
import com.itworx.vaspp.datacollection.util.converters.VSSMMemoryConverter.Memory;

public class ETOPUPPerVlrConverter extends AbstractTextConverter {
	private Logger logger;
	private Map  <String ,String> requestIDVsKey=new HashMap<String,String>() ;
	private Map  <String ,ReqResp> keyVSReqCount=new HashMap<String,ReqResp>() ;
	public ETOPUPPerVlrConverter()
	{}
	public File[] convert(File[] inputFiles, String systemName)
	throws ApplicationException, InputException {
		logger = Logger.getLogger(systemName);
		logger
				.debug("Inside ETOPUPPerVlrConverter convert - started converting input files");
		String path = PropertyReader.getConvertedFilesPath();
		File[] outputFiles = new File[1];
		File outputFile = new File(path, inputFiles[0].getName());

		BufferedReader inputStream = null;
		BufferedWriter outputStream;
		try {
			outputStream = new BufferedWriter(new FileWriter(outputFile));
			for (int i = 0; i < inputFiles.length; i++) {
				logger
						.debug("ETOPUPPerVlrConverter.convert() - converting file "
								+ inputFiles[i].getName());
				
				inputStream = new BufferedReader(new FileReader(inputFiles[i]));
	            System.out.println("File [" + i +"]");
				String line;
				String date = "";
				String reqId;
				String vlr;
				while (inputStream.ready()) {
					line = inputStream.readLine();
					if(line.contains(" "))
					{
						
						try
						{
						date= getDate(line.split(" ")[0]+" "+line.split(" ")[1]);
						
						} catch(ParseException exc){ logger.error(exc) ; continue ;}
						if(line.contains("Request")&&line.contains("MAP_HLR:"))
						{
							reqId=line.split(" ")[4]!=null?line.split(" ")[4]:"";							
							vlr=line.split("MAP_HLR:")[1].split(",")[0];							
							requestIDVsKey.put(reqId,date+","+vlr );
							if(keyVSReqCount.containsKey(date+","+vlr))
							{
								ReqResp obj=keyVSReqCount.get(date+","+vlr);
								obj.setRequest(obj.getRequest()+1);
								keyVSReqCount.remove(date+","+vlr);
								keyVSReqCount.put(date+","+vlr, obj);
							}else
							{
								ReqResp obj= new ReqResp();
								obj.setRequest(1);
								keyVSReqCount.put(date+","+vlr, obj);
							}
						}
						else if(line.contains("Response"))
						{
							reqId=line.split(" ")[4]!=null?line.split(" ")[4]:"";
							if(requestIDVsKey.containsKey(reqId))
							{
								String key=requestIDVsKey.get(reqId);
								if(keyVSReqCount.containsKey(key))
								{
									ReqResp obj=keyVSReqCount.get(key);
									obj.setResponse(obj.getResponse()+1);
									keyVSReqCount.remove(key);
									keyVSReqCount.put(key,obj);
								}
								else
								{
									ReqResp obj= new ReqResp();
									obj.setResponse(1);
									keyVSReqCount.put(key, obj);
								}
								requestIDVsKey.remove(reqId);
								
							}
						}
					}
					else
					{continue;
					}
					
				}
			}
			Iterator it=keyVSReqCount.keySet().iterator();
			while(it.hasNext())
			{
				 Object key=it.next();
				 ReqResp reqResp=(ReqResp)keyVSReqCount.get(key);				 
		    	 outputStream.write(key+","+reqResp.getRequest()+","+reqResp.getResponse());
				 outputStream.newLine();
			}
	         inputStream.close();
			
			outputStream.close();
			outputFiles[0]=outputFile;
			logger.debug("ETOPUPPerVlrConverter.convert() - finished converting input files successfully ");
		
		}
		catch (FileNotFoundException e) {
				logger
						.error("ETOPUPPerVlrConverter.convert() - Input file not found "
								+ e);
				throw new ApplicationException(e);
			} catch (IOException e) {
				logger
						.error("ETOPUPPerVlrConverter.convert() - Couldn't read input file"
								+ e);
				throw new ApplicationException(e);
			}
			logger
					.debug("ETOPUPPerVlrConverter.convert() - finished converting input files successfully ");
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
			
			PropertyReader.init("D:\\build\\phase9\\phase9Builds\\DataCollection");
			ETOPUPPerVlrConverter s = new ETOPUPPerVlrConverter();
			File[] input = new File[1];
			input[0]=new File("D:\\build\\phase9\\phase9Builds\\DataCollection\\browser_666_2011022209.log");
			   s.convert(input,"Maha_Test");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	class ReqResp
	{
		public long request=0;
		public long response=0;
		public long getRequest() {
			return request;
		}
		public void setRequest(long request) {
			this.request = request;
		}
		public long getResponse() {
			return response;
		}
		public void setResponse(long response) {
			this.response = response;
		}
	}

}
