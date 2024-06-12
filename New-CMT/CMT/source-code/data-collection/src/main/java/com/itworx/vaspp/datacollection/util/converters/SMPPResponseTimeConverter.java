package com.itworx.vaspp.datacollection.util.converters;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import org.apache.log4j.Logger;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;

public class SMPPResponseTimeConverter extends AbstractTextConverter {
	
	private Logger logger;
	HashMap hashmap = new HashMap();
	HashMap hashmapvalues = new HashMap();
	
	public SMPPResponseTimeConverter(){
		super();
	}
	
	class SMPPData
	{
		public String SendTime = new String();
		public String InqueueTime = new String();
		public String DequeueTime = new String();
		public String ConfirmationTime = new String();				
	}
	
	class SMPPValues
	{
		public double maxDequeue;
		public double minDequeue;
		public double avgDequeue;
		public double maxSend;
		public double minSend;
		public double avgSend;
		public double maxRouting;
		public double minRouting;
		public double avgRouting;
		public double maxSms;
		public double minSms;
		public double avgSms;
		public long count;
	}
	
	public File[] convert(File[] inputFiles, String systemName)
		throws ApplicationException, InputException {
		
		logger = Logger.getLogger(systemName);
		logger.debug("SMPPResponseTimeConverter - Starting");		
		File[] outputFiles = new File[1];
	
		try
		{
			String path = PropertyReader.getConvertedFilesPath();
			File output = new File(path, "SMPPResponseTimeConverterOutput");
			BufferedWriter outputStream = new BufferedWriter(new FileWriter(output));
			BufferedReader inputStream;
			String line="";
		
			for (int i = 0; i < inputFiles.length; i++) 
			{
				logger.debug( "SMPPResponseTimeConverter.convert() - converting file "
						+ inputFiles[i].getName() );
				inputStream = new BufferedReader(new FileReader(inputFiles[i]));
				
				//int cnt =0;
				String dummy="";
				if(inputStream.ready())
					dummy = inputStream.readLine();
				while((inputStream.ready()) || (dummy!="" && dummy!=null))
				{				
					line = "";
					do
					{			
						line = line + dummy;		
						dummy = inputStream.readLine();

					}while(inputStream.ready()&&(dummy.length()<=23||!isdate(dummy.substring(0,23))));
					
					line.trim();
					
					if(line.length()>=23 && isdate(line.substring(0,23)))
					{
						//System.out.println(line);
						updateHashMap(line);
					}
				}
				inputStream.close();
			}
			
			//processHashMap();
			writeAll(outputStream);
			
			outputStream.close();
			outputFiles[0] = output;
			logger.debug("SMPPResponseTimeConverter.convert() - "
					+ inputFiles[0].getName() + " converted");	
		}
		catch(FileNotFoundException e)
		{
			logger.debug("SMPPResponseTimeConverter.convert() - input file not found" + e);
			throw new ApplicationException(e);
		}
		catch(IOException e)
		{
			logger.debug("SMPPResponseTimeConverter.convert() - Couldn't read input file" + e);
			throw new ApplicationException(e);
		}		
		logger.debug( "SMPPResponseTimeConverter.convert() - finished converting input files Successfully Converted");
		return outputFiles;		
	}
	
	/**
	 * Process for each hour.
	 * 
	 * @throws ApplicationException 
	 *
	 */
	/*public void processHashMap() throws ApplicationException
	{
		Set keySet = hashmap.keySet();
		Object []keyObject = keySet.toArray();		
		for(int i=0;i<keyObject.length;i++)
		{
			SMPPData value = (SMPPData)hashmap.get(keyObject[i]);
			int index = ((String)keyObject[i]).indexOf("*");
			//String line =(String)keyObject[i] + ","+ value.ConfirmationTime + ","+value.DequeueTime+","+value.InqueueTime +","+value.SendTime;
			//System.out.println(line);
			updateValues(((String)keyObject[i]).substring(0,index),value);
						
		}		
	}*/
	
	/**
	 * Calculate the processing times for each hour
	 * 
	 * @param key
	 * @param data
	 * @throws ApplicationException 
	 */
	public void updateValues(String key,SMPPData data) throws ApplicationException
	{
		
		try
		{
			SMPPValues a= (SMPPValues)hashmapvalues.get(key);
			Date DequeueTime=new Date(),InqueueTime=new Date(),SendTime=new Date(),ConfirmationTime=new Date();
			long Dqueuediff=0,SMSdiff=0,Senddiff=0,Routingdiff=0;
			SimpleDateFormat sdf = new SimpleDateFormat();
			sdf.applyPattern("MM/dd/yyyy HH:mm:ss.S");
			if(data.DequeueTime.length() == 0||data.InqueueTime.length() == 0||data.SendTime.length() == 0||data.ConfirmationTime.length() == 0)
			{
				//System.out.println("FOUND Missing "+data.SendTime);
				logger.debug( "SMPPResponseTimeConverter.convert() - converting file : "
						+ "FOUND Missing "+data.SendTime);
				return ;
			}
			//System.out.println(data.DequeueTime + " " + data.InqueueTime + " " + data.SendTime + " " + data.ConfirmationTime);
			DequeueTime      = sdf.parse(data.DequeueTime);
			InqueueTime      = sdf.parse(data.InqueueTime);
			SendTime         = sdf.parse(data.SendTime);
			ConfirmationTime = sdf.parse(data.ConfirmationTime);
			Dqueuediff = DequeueTime.getTime() - InqueueTime.getTime();
			Senddiff = SendTime.getTime() - DequeueTime.getTime();
			Routingdiff = ConfirmationTime.getTime() -SendTime.getTime();
			SMSdiff = ConfirmationTime.getTime() - InqueueTime.getTime();
			
			if(a != null)
			{
				hashmapvalues.remove(key);
				// Calculate the Avg by suming all processing times then divide it on its count
				a.avgDequeue += Dqueuediff;
				a.avgRouting +=Routingdiff;
				a.avgSend += Senddiff;
				a.avgSms +=SMSdiff;
				a.count++;
				// Calculate the Max and Min times
				if(a.maxDequeue < Dqueuediff)
					a.maxDequeue = Dqueuediff;
				if (a.minDequeue > Dqueuediff)
					a.minDequeue = Dqueuediff;
				if(a.maxRouting <Routingdiff )
					a.maxRouting=Routingdiff;
				if(a.minRouting > Routingdiff)
					a.minRouting = Routingdiff;
				if(a.maxSend<Senddiff)
					a.maxSend = Senddiff;
				if(a.minSend > Senddiff)
					a.minSend = Senddiff;
				if(a.maxSms <SMSdiff )
					a.maxSms = SMSdiff;
				if(a.minSms>SMSdiff)
					a.minSms = SMSdiff;
				hashmapvalues.put(key, a);
					
			}	else	{
				SMPPValues b = new SMPPValues();
				b.avgDequeue = b.maxDequeue = b.minDequeue = Dqueuediff;
				b.avgRouting = b.maxRouting = b.minRouting = Routingdiff;
				b.avgSend = b.maxSend = b.minSend = Senddiff;
				b.avgSms = b.maxSms = b.minSms = SMSdiff;
				b.count = 1;
				hashmapvalues.put(key,b);			
			}
		}
		catch (ParseException e) {
			logger.debug("SMPPResponseTimeConverter.convert() - Invalid date in input file " + e);
			
			throw new ApplicationException(e);
		}			
	}
	
	/**
	 * Extract the output from SMPP records Hashmap 
	 * and write it in the required format in the outputfile.
	 * 
	 * @param outputStream
	 */
	public void writeAll(BufferedWriter outputStream)
	{
		Set keySet = hashmapvalues.keySet();
		Object []keyObject = keySet.toArray();
		
		try
		{
			DecimalFormat df = new DecimalFormat("0.00");
			
			for(int i=0;i<keyObject.length;i++)
			{
				SMPPValues value = (SMPPValues)hashmapvalues.get(keyObject[i]);				
				String line =formatDateHour((String)keyObject[i]) + ","+ 
				df.format(value.maxDequeue) +","+df.format(value.minDequeue) +","+df.format(value.avgDequeue/value.count)+","+				
				df.format(value.maxSend) +","+df.format(value.minSend) +","+df.format(value.avgSend/value.count)+","+
				df.format(value.maxRouting) +","+ df.format(value.minRouting) +","+df.format(value.avgRouting/value.count)+","+
				df.format(value.maxSms) +","+df.format(value.minSms) +","+df.format(value.avgSms/value.count)+","+
				((String)keyObject[i]).substring(13,((String)keyObject[i]).length());
				//System.out.println(line);
				outputStream.write(line);
				outputStream.newLine();
			}
		}catch(IOException e)
		{
			logger.debug("SMPPResponseTimeConverter.convert() - Error while writing to output file " + e);
		}
	}
	
	/**
	 * format the date from "02/27/2008 11" to "02/27/2008 11:00:00",
	 *  to be the same format of date in hibarnate.
	 *  
	 * @param date
	 * @return
	 */
	public String formatDateHour(String date)
	{
		date = date.substring(0,13);
		return date + ":00:00";
	}
	
	/**
	 * Update the Hashmap with the new record's data.
	 * 
	 * @param line
	 * @throws ApplicationException 
	 */
	public void updateHashMap(String line) throws ApplicationException
	{
		String key = "";//line.substring(0, 13) + ExtractApplicationName(line);
		
		SMPPData data = new SMPPData();
		boolean Dequeue=false,Inqueue=false,Send=false,Reply=false;
		Dequeue = isCorrect(line,"D");		
		Inqueue = isCorrect(line,"I");
		Send    = isCorrect(line,"S");
		Reply   = isCorrect(line,"R");
		
		String applicationName = extractApplicationName(line);
		
		if(Dequeue){
			key = formatDate2(line.substring(0,10))+" "+line.substring(11,13) + applicationName+"*"+extractMessageID(line);
			data.DequeueTime =formatDate2(line.substring(0,10))+ line.substring(10,23);		
		}	
		if(Send){	
			key = formatDate2(line.substring(0,10))+" "+line.substring(11,13) + applicationName+"*"+extractMessageID(line);
			data.SendTime = formatDate2(line.substring(0,10))+ line.substring(10,23);		
		}
		if(Reply){
			String sendTime = extractSendTime(line);
			key = formatDate2(sendTime.substring(0,10))+" "+sendTime.substring(11,13) + applicationName+"*"+extractMessageID(line);
			data.ConfirmationTime =formatDate2(line.substring(0,10))+ line.substring(10,23);
		}		
		updateHashMap2(data,key);
		
		if(Inqueue){
			SMPPData data2 = new SMPPData();
			String dummy = "|| I || ";
			int index = line.indexOf(dummy);
			/*key = FormatDate(line.substring(index+dummy.length(),index+dummy.length()+8))
			+" "+ line.substring(index+dummy.length()+9,index+dummy.length()+11)+ applicationName+"*"+
			ExtractMessageID(line);*/
			key = formatDate2(line.substring(0,10))+" "+line.substring(11,13) + applicationName+"*"+extractMessageID(line);
			data2.InqueueTime = formatInqueueTime(line.substring(index+dummy.length(),line.length()));
			
			updateHashMap2(data2,key);
		}
		
	}
	
	
	/**
	 * Flexible format that enable adding the millisecond if not existed.
	 * 
	 * @param indate
	 * @return String
	 */
	public String formatInqueueTime(String indate)
	{
		String dummy = formatDate(indate.substring(0,8));
		if(indate.length()<=17)
			dummy = dummy + indate.substring(8,17)+".000";
		else
			dummy = dummy + indate.substring(8,indate.length());
		return dummy;
	}
	
	/**
	 * It extract the Message Id from the input line ex:
	 *  the line would be 27/02/2008 16:00:00.536 ||  INFO || D || VASPBT || 2809241 || I || 27-02-08 16:00:00
	 *  return is 2809241.
	 *  
	 * @param line
	 * @return
	 */
	public String extractMessageID(String line)
	{
		int index = line.indexOf("||");
		index = line.indexOf("||",index+1);
		index = line.indexOf("||",index+1);
		index = line.indexOf("||",index+1);
		int index2 = line.indexOf("||",index+1);
		if(index2 <=0)
			index2 = line.length();
		return line.substring(index+2,index2).trim();
	}
	
	/**
	 * It extract the Send Hour from the input line ex:
	 *  the line would be 27/02/2008 17:00:00.582 ||  INFO || R || VASPBT || 2842511 || SUCCESS || 0 || msgID= 0182A1DC15 || 27/02/2008 16:00:00.439 || SrcNumber = Vodafone || DstNumber = 20103670202 || Message =  г дёб 5 ћдне бя гд 0104190502
	 *  return is 16.
	 *  
	 * @param line
	 * @return
	 */
	public String extractSendTime(String line)
	{
		int index = line.indexOf("||");
		index = line.indexOf("||",index+1);
		index = line.indexOf("||",index+1);
		index = line.indexOf("||",index+1);
		index = line.indexOf("||",index+1);
		index = line.indexOf("||",index+1);
		index = line.indexOf("||",index+1);
		index = line.indexOf("||",index+1);
		int index2 = line.indexOf("||",index+1);
		if(index2 <=0)
			index2 = line.length();
		return (line.substring(index+2,index2).trim());
	}
	
	/**
	 * Update the existed SMPP records in hashmap with the new record's data.
	 * 
	 * @param data
	 * @param key
	 * @throws ApplicationException 
	 */
	public void updateHashMap2(SMPPData data,String key) throws ApplicationException
	{
		SMPPData a = (SMPPData)hashmap.get(key);
		if(a!=null){
			hashmap.remove(key);
			if(a.ConfirmationTime.equals(""))
			a.ConfirmationTime = data.ConfirmationTime;
			if(a.DequeueTime.equals(""))
			a.DequeueTime = data.DequeueTime;
			if(a.InqueueTime.equals(""))
			a.InqueueTime = data.InqueueTime;
			if(a.SendTime.equals(""))
			a.SendTime = data.SendTime;
			hashmap.put(key, a);
			
			//	Processing the completed SMPP Data and remove the object from the hashmap
			this.processCompletedSMPPData(a, key);			
			
		}	else	{
			hashmap.put(key, data);		
		}
		
		
		
	}
	
	/**
	 * Processing the completed SMPP Data and remove the object from the hashmap
	 * 
	 * @throws ApplicationException 
	 *
	 */
	public void processCompletedSMPPData(SMPPData data,String key) throws ApplicationException
	{
		if(data.DequeueTime.length() != 0 && data.InqueueTime.length() != 0 && data.SendTime.length() != 0 && data.ConfirmationTime.length() != 0)
		{
			int index = ((String)key).indexOf("*");
			updateValues(key.substring(0,index),data);
			hashmap.remove(key);
		}
	}
	
	/**
	 * Check if that String is included in the line or not.
	 * 
	 * @param line
	 * @param ID
	 * @return boolean
	 */
	public boolean isCorrect(String line,String ID)
	{
		//27/02/2008 16:00:00.421 ||  INFO || D || VASPBT || 2809237 || I || 27-02-08 15:59:59
		int index = line.indexOf(" || "+ID+" || ");
		if(index >=0)
			return true;
		return false;
		
	}
	
	/**
	 * Format the date string from "27-02-08" to "02/27/2008".
	 * 
	 * @param date
	 * @return String
	 */
	public String formatDate(String date)
	{
		return date.substring(3,5)+"/"+ date.substring(0,2)+"/"+"20"+date.substring(6,8);
	}
	
	/**
	 * Format the date string from "27/02/2008" to "02/27/2008".
	 * 
	 * @param date
	 * @return String
	 */
	public String formatDate2(String date)
	{
		return date.substring(3,5)+"/" + date.substring(0,2) + "/" + date.substring(6,10);
	}
	
	/**
	 * Check if that is new line or not.
	 * 
	 * @param line
	 * @return boolean
	 */
	public boolean isdate(String line)
	{
		//The checking format is "27/02/2008 11:00:00.403"
		return line.matches("\\d\\d\\p{Punct}\\d\\d\\p{Punct}\\d\\d\\d\\d\\p{Space}\\d\\d:\\d\\d:\\d\\d.\\d\\d\\d");
	}

	/**
	 * It extract the application name from the input line ex:
	 *  the line would be 27/02/2008 16:00:03.604 ||  INFO || R || VASPBT || 2809265 || SUCCESS || 0 || msgID= 00077C8215 || 27/02/2008 16:00:03.433 || SrcNumber = Vodafone || DstNumber = 20100176670 || Message =  г дёб 5 ћдне бя гд 0163546542
	 *  return is VASPBT.
	 * 
	 * @param line
	 * @return String
	 */
	public String extractApplicationName(String line)
	{
		
		int index2;
		int index = line.indexOf("||");
		index = line.indexOf("||",index+1);
		index = line.indexOf("||",index+1);
		index2= line.indexOf("||",index+1);
		
		return line.substring(index+2,index2).trim();
	
	}
	
	public static void main(String[] args) throws Exception
	{
		try {
			
			PropertyReader.init("D:\\Work\\VFE_VAS_Performance_Portal\\Phase IV\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection");
			SMPPResponseTimeConverter s = new SMPPResponseTimeConverter();
			File[] input = new File[1];
			//input[0]=new File("D:\\Work\\VFE_VAS_Performance_Portal\\Phase IV\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection\\resources\\ftpfolder\\SMPP_OPS.log.2008-02-27-16.log");
			input[0]=new File("D:\\Work\\VFE_VAS_Performance_Portal\\Phase IV\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection\\resources\\ftpfolder\\SMPP_OPS.log.2008-02-27-17.log");
			s.convert(input,"Database");
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

}
