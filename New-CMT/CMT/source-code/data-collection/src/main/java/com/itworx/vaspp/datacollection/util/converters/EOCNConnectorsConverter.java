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

public class EOCNConnectorsConverter extends AbstractTextConverter{
	private Logger logger;
	private Map  <String ,ReqResp > dateVSReqResp=new HashMap<String,ReqResp>() ;
	private Map  <String ,String > mobileNumberVsKey=new HashMap<String,String>() ;
	public EOCNConnectorsConverter()
	{}
	public File[] convert(File[] inputFiles, String systemName)
	throws ApplicationException, InputException {
		logger = Logger.getLogger(systemName);
		logger
				.debug("Inside EOCNConnectorsConverter convert - started converting input files");
		String path = PropertyReader.getConvertedFilesPath();
		File[] outputFiles = new File[1];
		File outputFile = new File(path, inputFiles[0].getName());

		BufferedReader inputStream = null;
		BufferedWriter outputStream;
		try {
			outputStream = new BufferedWriter(new FileWriter(outputFile));
			for (int i = 0; i < inputFiles.length; i++) {
				logger
						.debug("EOCNConnectorsConverter.convert() - converting file "
								+ inputFiles[i].getName());
				
				inputStream = new BufferedReader(new FileReader(inputFiles[i]));
	            System.out.println("File [" + i +"]");
				String line;
				String key="";
				String date = "";
				while (inputStream.ready()) {
					line = inputStream.readLine();
					String columns[]=line.split(",");
					try {
					date =getDate(columns[0]);
					//System.out.println("the date "+date);
					
					if(line.contains("SMPP Request:"))
					{
						String sdpId=((columns[2].split("SMPP Request")[1]).split("\\[")[1]).split("\\]")[0]!=null?((columns[2].split("SMPP Request")[1]).split("\\[")[1]).split("\\]")[0]:"";
						String reqNumber=(columns[3].split("\\[")[1]).split("\\]")[0]!=null?(columns[3].split("\\[")[1]).split("\\]")[0]:"";
						key=date+","+sdpId;
						mobileNumberVsKey.put(reqNumber, key);
						if(dateVSReqResp.containsKey(key))
						{
							ReqResp reqResp=dateVSReqResp.get(key);
							reqResp.setReq(reqResp.getReq()+1);
							dateVSReqResp.remove(key);
							dateVSReqResp.put(key, reqResp);
						}
						else
						{
							ReqResp reqResp = new ReqResp();
							reqResp.setReq(new Long(1));
							dateVSReqResp.put(key, reqResp);
						}
					}
					else if(line.contains("Connector response written to pipe:1"))
					{   String resMobNo="";
						if(columns[2]!=null)
							resMobNo=columns[2].split("\\[")[1].split("\\]")[0]!=null?columns[2].split("\\[")[1].split("\\]")[0]:"";
						if(mobileNumberVsKey.containsKey(resMobNo))
						{
							mobileNumberVsKey.remove(resMobNo);
							if(dateVSReqResp.containsKey(key))
							{
								ReqResp reqResp=dateVSReqResp.get(key);
								reqResp.setResponse(reqResp.getResponse()+1);
								dateVSReqResp.remove(key);
								dateVSReqResp.put(key, reqResp);
							}
							else
							{
								ReqResp reqResp = new ReqResp();
								reqResp.setResponse(new Long(1));
								dateVSReqResp.put(key, reqResp);
							}
						}
					}
					
					else
					{continue;}
					}  catch(ParseException exc){ //logger.error(exc) ; 
						continue ;}
					   catch(ArrayIndexOutOfBoundsException ex){ logger.error(ex) ; continue ;}
				}
			}//end of the file
			Iterator it=dateVSReqResp.keySet().iterator();
			while(it.hasNext())
			{
				 Object key=it.next();
				 ReqResp reqResp=dateVSReqResp.get(key);
		    	 outputStream.write(key+","+reqResp.getReq()+","+reqResp.getResponse());
		    	 //System.out.println("the key "+key+","+reqResp.getReq()+","+reqResp.getResponse());
				 outputStream.newLine();
			}
			inputStream.close();
			
			outputStream.close();
			outputFiles[0]=outputFile;
			logger.debug("EOCNConnectorsConverter.convert() - finished converting input files successfully ");
		
		}
		catch (FileNotFoundException e) {
				logger
						.error("EOCNConnectorsConverter.convert() - Input file not found "
								+ e);
				throw new ApplicationException(e);
			} catch (IOException e) {
				logger
						.error("EOCNConnectorsConverter.convert() - Couldn't read input file"
								+ e);
				throw new ApplicationException(e);
			}
			logger
					.debug("EOCNConnectorsConverter.convert() - finished converting input files successfully ");
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
			
			PropertyReader.init("D:\\build\\pahse8\\DataCollection");
			EOCNConnectorsConverter s = new EOCNConnectorsConverter();
			File[] input = new File[1];
			input[0]=new File("D:\\build\\pahse8\\DataCollection\\eocn_connectors20100908.log");
			   s.convert(input,"Maha_Test");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	class ReqResp
	{
		public Long req=new Long(0);
		public Long getReq() {
			return req;
		}
		public void setReq(Long req) {
			this.req = req;
		}
		public Long getResponse() {
			return response;
		}
		public void setResponse(Long response) {
			this.response = response;
		}
		public Long response=new Long(0);
	}
}
