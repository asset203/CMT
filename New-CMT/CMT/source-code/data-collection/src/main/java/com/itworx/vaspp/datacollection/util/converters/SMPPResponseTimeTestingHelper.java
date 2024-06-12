package com.itworx.vaspp.datacollection.util.converters;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Set;

import org.apache.log4j.Logger;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;
import com.itworx.vaspp.datacollection.util.converters.SMPPResponseTimeConverter.SMPPData;

public class SMPPResponseTimeTestingHelper extends AbstractTextConverter {
	
	private Logger logger;
	HashMap hashmap = new HashMap();
	HashMap hashmapvalues = new HashMap();
	
	class SMPPData
	{
		public String SendTime = new String();
		public String InqueueTime = new String();
		public String DequeueTime = new String();
		public String ConfirmationTime = new String();				
	}
	
	public File[] convert(File[] inputFiles, String systemName)
		throws ApplicationException, InputException {
		
		logger = Logger.getLogger(systemName);
		logger.debug("SMPPResponseTimeTestingHelper - Starting");		
		File[] outputFiles = new File[1];
	
		try
		{
			String path = PropertyReader.getConvertedFilesPath();
			File output = new File(path, "SMPPResponseTimeTestingHelperOutput");
			BufferedWriter outputStream = new BufferedWriter(new FileWriter(output));
			BufferedReader inputStream;
			String line="";
		
			for (int i = 0; i < inputFiles.length; i++) 
			{
				logger.debug( "SMPPResponseTimeTestingHelper.convert() - converting file "
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
						UpdateHashMap(line,outputStream);
					}
				}
				inputStream.close();
			}
			

			
			outputStream.close();
			outputFiles[0] = output;
			logger.debug("SMPPResponseTimeTestingHelper.convert() - "
					+ inputFiles[0].getName() + " converted");	
		}
		catch(FileNotFoundException e)
		{
			logger.debug("SMPPResponseTimeTestingHelper.convert() - input file not found" + e);
			throw new ApplicationException(e);
		}
		catch(IOException e)
		{
			logger.debug("SMPPResponseTimeTestingHelper.convert() - Couldn't read input file" + e);
			throw new ApplicationException(e);
		}		
		logger.debug( "SMPPResponseTimeTestingHelper.convert() - finished converting input files Successfully Converted");
		return outputFiles;		
	}
	
	/**
	 * Extract the output from SMPP records Hashmap 
	 * and write it in the required format in the outputfile.
	 * 
	 * @param outputStream
	 */
	public void WriteAll(BufferedWriter outputStream)
	{
		Set keySet = hashmap.keySet();
		Object []keyObject = keySet.toArray();
		String keyValue;
		String line;
		
		try
		{			
			for(int i=0;i<keyObject.length;i++)
			{
				keyValue = (String)keyObject[i];
				SMPPData value = (SMPPData)hashmap.get(keyValue);
				if(value.DequeueTime.length() == 0||value.InqueueTime.length() == 0||value.SendTime.length() == 0||value.ConfirmationTime.length() == 0)
				{
					//System.out.println("FOUND Missing "+value.SendTime);
					logger.debug( "SMPPResponseTimeConverter.convert() - converting file : "
							+ "FOUND Missing "+value.SendTime);
					continue;
				}
				// the output line: date_hour, application_name, message_id, Inqueue_Time, Dequeue_Time, Send_Time, Confirmation_Time  
				line =keyValue.substring(0,13)+":00:00" + ","+ keyValue.substring(13,keyValue.indexOf("*"))+ ","+ keyValue.substring(keyValue.indexOf("*")+1)  +","+ value.InqueueTime + ","+value.DequeueTime +","+ value.SendTime + ","+ value.ConfirmationTime ;
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
	public String FormatDateHour(String date)
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
	public void UpdateHashMap(String line,BufferedWriter outputStream) throws ApplicationException
	{
		String key = "";//line.substring(0, 13) + ExtractApplicationName(line);
		
		SMPPData data = new SMPPData();
		boolean Dequeue=false,Inqueue=false,Send=false,Reply=false;
		Dequeue = isCorrect(line,"D");		
		Inqueue = isCorrect(line,"I");
		Send    = isCorrect(line,"S");
		Reply   = isCorrect(line,"R");
		
		String applicationName = ExtractApplicationName(line);
		
		if(Dequeue){
			key = FormatDate2(line.substring(0,10))+" "+line.substring(11,13) + applicationName+"*"+ExtractMessageID(line);
			data.DequeueTime =FormatDate2(line.substring(0,10))+ line.substring(10,23);		
		}	
		if(Send){	
			key = FormatDate2(line.substring(0,10))+" "+line.substring(11,13) + applicationName+"*"+ExtractMessageID(line);
			data.SendTime = FormatDate2(line.substring(0,10))+ line.substring(10,23);		
		}
		if(Reply){
			String sendTime = ExtractSendTime(line);
			key = FormatDate2(sendTime.substring(0,10))+" "+sendTime.substring(11,13) + applicationName+"*"+ExtractMessageID(line);
			data.ConfirmationTime =FormatDate2(line.substring(0,10))+ line.substring(10,23);
		}		
		UpdateHashMap2(data,key,outputStream);
		
		if(Inqueue){
			SMPPData data2 = new SMPPData();
			String dummy = "|| I || ";
			int index = line.indexOf(dummy);
			/*key = FormatDate(line.substring(index+dummy.length(),index+dummy.length()+8))
			+" "+ line.substring(index+dummy.length()+9,index+dummy.length()+11)+ applicationName+"*"+
			ExtractMessageID(line);*/
			key = FormatDate2(line.substring(0,10))+" "+line.substring(11,13) + applicationName+"*"+ExtractMessageID(line);
			data2.InqueueTime = FormatInqueueTime(line.substring(index+dummy.length(),line.length()));
			
			UpdateHashMap2(data2,key,outputStream);
		}		
	}
	
	/**
	 * Flexible format that enable adding the millisecond if not existed.
	 * 
	 * @param indate
	 * @return String
	 */
	public String FormatInqueueTime(String indate)
	{
		String dummy = FormatDate(indate.substring(0,8));
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
	public String ExtractMessageID(String line)
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
	public String ExtractSendTime(String line)
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
	public void UpdateHashMap2(SMPPData data,String key,BufferedWriter outputStream) throws ApplicationException
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
			
			this.processCompletedSMPPData(a, key,outputStream);
		}	else	{
			hashmap.put(key, data);		
		}
		
	}
	/**
	 * Processing the completed SMPP Data and remove the object from the hashmap
	 * @throws ApplicationException 
	 *
	 */
	public void processCompletedSMPPData(SMPPData data,String keyValue,BufferedWriter outputStream) throws ApplicationException
	{
		if(data.DequeueTime.length() != 0 && data.InqueueTime.length() != 0 && data.SendTime.length() != 0 && data.ConfirmationTime.length() != 0)
		{
			int index = ((String)keyValue).indexOf("*");
			
			SMPPData value = (SMPPData)hashmap.get(keyValue);
			// the output line: date_hour, application_name, message_id, Inqueue_Time, Dequeue_Time, Send_Time, Confirmation_Time  
			String line =keyValue.substring(0,13)+":00:00" + ","+ keyValue.substring(13,keyValue.indexOf("*"))+ ","+ keyValue.substring(keyValue.indexOf("*")+1)  +","+ value.InqueueTime + ","+value.DequeueTime +","+ value.SendTime + ","+ value.ConfirmationTime ;
			//System.out.println(line);
			try {
				outputStream.write(line);
				outputStream.newLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			hashmap.remove(keyValue);
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
	public String FormatDate(String date)
	{
		return date.substring(3,5)+"/"+ date.substring(0,2)+"/"+"20"+date.substring(6,8);
	}
	
	/**
	 * Format the date string from "27/02/2008" to "02/27/2008".
	 * 
	 * @param date
	 * @return String
	 */
	public String FormatDate2(String date)
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
	public String ExtractApplicationName(String line)
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
			
			PropertyReader.init("D:\\VASPortalWF\\Source Code\\DataCollection");
			SMPPResponseTimeTestingHelper s = new SMPPResponseTimeTestingHelper();
			File[] input = new File[1];
			input[0]=new File("D:\\VASPortalWF\\Source Code\\DataCollection\\resources\\ftpfolder\\SMPP_OPS.log.2008-02-27-16.log");
			//input[1]=new File("D:\\Work\\VFE_VAS_Performance_Portal\\Phase IV\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection\\resources\\ftpfolder\\SMPP_OPS.log.2008-02-27-17.log");
			s.convert(input,"Database");
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

}
