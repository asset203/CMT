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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;
import com.itworx.vaspp.datacollection.util.converters.VSSMMemoryConverter.Memory;

public class SDPEOCNConnectorConverter extends AbstractTextConverter{
	private Logger logger;
	private Map  <String ,SDPObject> dateVSObject=new HashMap<String,SDPObject>() ;
	public SDPEOCNConnectorConverter()
	{}
	public File[] convert(File[] inputFiles, String systemName)
	throws ApplicationException, InputException {
		logger = Logger.getLogger(systemName);
		logger
				.debug("Inside SDPEOCNConnectorConverter convert - started converting input files");
		String path = PropertyReader.getConvertedFilesPath();
		File[] outputFiles = new File[1];
		File outputFile = new File(path, inputFiles[0].getName());

		BufferedReader inputStream = null;
		BufferedWriter outputStream;
		try {
			outputStream = new BufferedWriter(new FileWriter(outputFile));
			for (int i = 0; i < inputFiles.length; i++) {
				logger
						.debug("SDPEOCNConnectorConverter.convert() - converting file "
								+ inputFiles[i].getName());
				
				inputStream = new BufferedReader(new FileReader(inputFiles[i]));
	            System.out.println("File [" + i +"]");
				String line;
				String date = "";
				boolean error=false;
				boolean blockReaded= false;
				int succReqFound=0;
				long outGoing=0;
				long inComming=0;
				long succReq=0;
				long failedReq=0;
				double trans= 0;
				
				while (inputStream.ready()) {
					line = inputStream.readLine();
					if(line.contains("Statistics"))
					{
						try
						{
							error =false;
							date=getDate(line.split("Statistics:")[1].trim());
						    //System.out.println("date "+date );
						}   catch(ParseException exc){ error= true ;logger.error(exc) ; continue ;}
					}
					else if(!error)/*if there is problem in the date so that we should skip it's data block*/
					{
					
					try
					{/*to get the Incomming and outgoing*/
						if(line.contains("Outgoing:") &&line.contains("Incomming:"))
						{
								outGoing=Long.parseLong((line.split("Outgoing:")[1].split("Incomming:")[0]).trim());
								inComming=Long.parseLong(line.split("Incomming:")[1].trim());
								continue;
						}
						if(line.contains("Total ThroughPut ="))
						{
							
							trans=Double.parseDouble((line.split("Total ThroughPut =")[1].split("\\(requests")[0]).trim());
							continue;
						}
						if(line.contains("Number of samples"))
						{
							if(succReqFound==0)
							{
								succReqFound=succReqFound+1;
								succReq=Long.parseLong((line.split(" Number of samples:")[1]).trim());
								continue;
							}
							else
							{
								succReqFound=0;
								failedReq=Long.parseLong((line.split(" Number of samples:")[1]).trim());
								continue;
							}
						}
						if(line.contains("Received Messages"))/*end of the 
*/
						blockReaded= true;
					}catch(Exception exc){ logger.error(exc) ; continue ;}
					/*to fill the data readed from the block one by one*/
					if(blockReaded){
						blockReaded=false;
					if(dateVSObject.containsKey(date))
					{
						SDPObject sdpObj=dateVSObject.get(date);
						sdpObj.setOutgoing(sdpObj.getOutgoing()+outGoing);
						sdpObj.setIncomming(sdpObj.getIncomming()+inComming);
						sdpObj.setSuccReq(sdpObj.getSuccReq()+succReq);
						sdpObj.setFailedReq(sdpObj.getFailedReq()+failedReq);
						sdpObj.getMaxTransactions().add(trans);
						dateVSObject.remove(date);
						dateVSObject.put(date, sdpObj);
					}
					else
					{
						SDPObject sdpObj= new SDPObject();
						sdpObj.setOutgoing(outGoing);
						sdpObj.setIncomming(inComming);
						sdpObj.setFailedReq(failedReq);
						sdpObj.setSuccReq(succReq);
						List transactions = new ArrayList();
						transactions.add(trans);
						sdpObj.setMaxTransactions(transactions);
						dateVSObject.put(date, sdpObj);
						
					}
					}
				}
					}
			}
		//end of the file
		Iterator it=dateVSObject.keySet().iterator();
		while(it.hasNext())
		{
			 Object key=it.next();
			 SDPObject obj=(SDPObject)dateVSObject.get(key);
			 Double maxTrans=calculateMax(obj.getMaxTransactions());
			 outputStream.write(key+","+obj.getIncomming()+","+obj.getOutgoing()+","+obj.getSuccReq()+","+obj.getFailedReq()+","+maxTrans.doubleValue());
			// System.out.println("the key "+key+","+obj.getIncomming()+","+obj.getOutgoing()+","+obj.getSuccReq()+","+obj.getFailedReq()+","+maxTrans.doubleValue());
			 outputStream.newLine();
		}
		inputStream.close();
		
		outputStream.close();
		outputFiles[0]=outputFile;
		logger.debug("SDPEOCNConnectorConverter.convert() - finished converting input files successfully ");
	
	}
	catch (FileNotFoundException e) {
			logger
					.error("SDPEOCNConnectorConverter.convert() - Input file not found "
							+ e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger
					.error("SDPEOCNConnectorConverter.convert() - Couldn't read input file"
							+ e);
			throw new ApplicationException(e);
		}
		logger
				.debug("SDPEOCNConnectorConverter.convert() - finished converting input files successfully ");
		return outputFiles;
		
}
	private String getDate(String line) throws ParseException {
		String[] tokens = null;
		Date date = new Date();
		String dateString;
		SimpleDateFormat inDateFormat = new SimpleDateFormat(
				"EEE MMM dd HH:mm:ss yyyy");
		SimpleDateFormat outDateFormat = new SimpleDateFormat(
				"MM/dd/yyyy HH:00:00");

		
			date = inDateFormat.parse(line);
		dateString = outDateFormat.format(date);
		return dateString;

	}
	public static void main(String ag[]) {
		try {
			
			String path = "D:\\Projects\\VAS Portal Project\\VFE_VAS_Portal_2010\\SourceCode\\DataCollection\\";
			//PropertyReader.init("D:\\build\\pahse8\\DataCollection");
			PropertyReader.init(path);
			SDPEOCNConnectorConverter s = new SDPEOCNConnectorConverter();
			File[] input = new File[1];
			//input[0]=new File("D:\\build\\pahse8\\DataCollection\\SMS_stats.txt");
			input[0]=new File("D:\\Projects\\VAS Portal Project\\VFE_VAS_Portal_2010\\Documents\\Requirements\\Requirments Gathering\\SDP EOCN Connector\\SMS_stats.txt");
			   s.convert(input,"Maha_Test");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	class SDPObject
	{
		public long outgoing=0;
		public long getOutgoing() {
			return outgoing;
		}
		public void setOutgoing(long outgoing) {
			this.outgoing = outgoing;
		}
		public long getIncomming() {
			return incomming;
		}
		public void setIncomming(long incomming) {
			this.incomming = incomming;
		}
		public long getSuccReq() {
			return succReq;
		}
		public void setSuccReq(long succReq) {
			this.succReq = succReq;
		}
		public long getFailedReq() {
			return failedReq;
		}
		public void setFailedReq(long failedReq) {
			this.failedReq = failedReq;
		}
		public List getMaxTransactions() {
			return maxTransactions;
		}
		public void setMaxTransactions(List maxTransactions) {
			this.maxTransactions = maxTransactions;
		}
		public long incomming=0;
		public long succReq=0;
		public long failedReq=0;
		public List maxTransactions=new ArrayList();
		
	
	}
	private Double calculateMax(List data)
	{
		Double min,max,avg;
		Double any;
		Iterator listIterator = data.iterator(); 
		any=(Double)listIterator.next();
		max = any ;
		min = any ;
		while(listIterator.hasNext())
		{
			Double element = (Double)listIterator.next(); 
		
			if(element<min)
			{
				min=element;
			}
			if(element>max)
			{
				max=element;
			}
		} 
		
		return max ;
	}
}
