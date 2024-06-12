package com.itworx.vaspp.datacollection.util.converters;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import org.apache.log4j.Logger;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;

public class SMPPMessagesConverter extends AbstractTextConverter{
	
	private Logger logger;
	HashMap hashmap = new HashMap();
	
	public SMPPMessagesConverter() {
		super();
	}
	
	class SMPPData
	{
		public long inqueue;
		public long dequeue;
		public long send;
		// SuccessZero,SuccessOne,SuccessTwo,SuccessThree,SuccessFour,SuccessFive
		public long []sucess = new long[6];
		public long failure;		
	}
	
	/**
	 * 
	 * @param inputFiles -
	 *            array of the input files to be converted
	 * @param systemName -
	 *            name of targeted system for logging
	 * 
	 * @exception ApplicationException
	 *                if input file couldn't be found if input file couldn't be
	 *                opened
	 * @exception InputException
	 *                if ParseException occured
	 */
	public File[] convert(File[] inputFiles, String systemName)
			throws ApplicationException, InputException {
		
		logger = Logger.getLogger(systemName);
		logger.debug("SMPPMessagesConverter - Starting");		
		File[] outputFiles = new File[1];	
		
		try
		{
			String path = PropertyReader.getConvertedFilesPath();
			File output = new File(path, "SMPPMessagesConverterOutput");
			BufferedWriter outputStream = new BufferedWriter(new FileWriter(output));
			BufferedReader inputStream;
			String line;
		
			for (int i = 0; i < inputFiles.length; i++) 
			{
				logger.debug( "SMPPMessagesConverter.convert() - converting file "
						+ inputFiles[i].getName() );
				inputStream = new BufferedReader(new FileReader(inputFiles[i]));
				
				String dummy="";
				while(inputStream.ready()) {
					line = inputStream.readLine();
					
					dummy="";
					do
					{			
						line = line + dummy;
						inputStream.mark(1000);		
						dummy = inputStream.readLine();

					}while(dummy != null && (dummy.length()<=23||!isdate(dummy.substring(0,23))));
					
					if(inputStream.ready())	
					{
						dummy="";
						inputStream.reset();
					}
					
					line.trim();
					
					if(isdate(line.substring(0,23)))
					{
						updateHashMap(line);
					}
				}
				//for the last value if exist
				if(dummy != null && isdate(dummy.substring(0,23)))
				{
					updateHashMap(dummy);
				}
				inputStream.close();				
			}			
			
			writeAll(outputStream);			
			outputStream.close();
			outputFiles[0] = output;
			logger.debug("SMPPMessagesConverter.convert() - "
					+ inputFiles[0].getName() + " converted");	
		}
		catch(FileNotFoundException e)
		{
			logger.debug("SMPPMessagesConverter.convert() - input file not found" + e);
			throw new ApplicationException(e);
		}
		catch(IOException e)
		{
			logger.debug("SMPPMessagesConverter.convert() - Couldn't read input file" + e);
			throw new ApplicationException(e);
		}		
		logger.debug( "SMPPMessagesConverter.convert() - finished converting input files Successfully Converted");
		return outputFiles;	
	}
	
	/**
	 * Extract the output from SMPP records Hashmap 
	 * and write it in the required format in the outputfile.
	 * 
	 * @param outputStream
	 * @throws ApplicationException 
	 */
	public void writeAll(BufferedWriter outputStream) throws ApplicationException
	{
		
		Set keySet = hashmap.keySet();
		Object []keyObject = keySet.toArray();
		
		try
		{
			for(int i=0;i<keyObject.length;i++)
			{
				SMPPData value = (SMPPData)hashmap.get(keyObject[i]);
				String line = formatDateHour((String)keyObject[i])+"," + value.inqueue+"," + value.dequeue +","
				+ value.send ;
				for(int j=0;j<6;j++){
					line += ","+ value.sucess[j];
				}
				line += "," +value.failure;
				// add the application name
				line += "," +((String)keyObject[i]).substring(13,((String)keyObject[i]).length());
				//System.out.println(line);
				outputStream.write(line);
				outputStream.newLine();
			}
		}catch(IOException e)
		{
			logger.debug("SMPPMessagesConverter.convert() - Couldn't write the output" + e);
			throw new ApplicationException(e);
		}
	}
	
	/**
	 * format the date from "02/27/2008 11" to "02/27/2008 11:00:00",
	 *  to be the same format of date in hibarnate.
	 * 
	 * @param date
	 * @return String
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
	 */
	public void updateHashMap(String line)
	{		
		String key = "";//line.substring(0, 13) + ExtractApplicationName(line);
		
		SMPPData data = new SMPPData();
		boolean Dequeue=false,Inqueue=false,Send=false,Reply=false;
		Dequeue = isCorrect(line,"D");		
		Inqueue = isCorrect(line,"I");
		Send    = isCorrect(line,"S");
		Reply   = isCorrect(line,"R");
		
		String applicationName = extractApplicationName(line);
		// This is the used key in case of "Dequeue,Send,Reply"
		key = formatDate2(line.substring(0,10))+" "+line.substring(11,13) + applicationName;
		
		if(Dequeue){					
			data.dequeue=1;			
		}		
		if(Send){
			data.send=1;			
		}
		if(Reply){
			updateReply(line,data);
		}		
		updateHashMap2(data,key);
		
		if(Inqueue){
			SMPPData data2 = new SMPPData();
			String dummy = "|| I || ";
			int index = line.indexOf(dummy);
			key = formatDate(line.substring(index+dummy.length(),index+dummy.length()+8))
			+" "+ line.substring(index+dummy.length()+9,index+dummy.length()+11)+ applicationName;
			data2.inqueue=1;
			
			updateHashMap2(data2,key);
		}		
	}
	
	/**
	 * Update the existed SMPP records in hashmap with the new record's data.
	 * 
	 * @param data
	 * @param key
	 */
	public void updateHashMap2(SMPPData data,String key)
	{		
		SMPPData a = (SMPPData)hashmap.get(key);
		if(a != null){
			hashmap.remove(key);
			a.dequeue += data.dequeue;
			a.inqueue +=data.inqueue;
			a.failure +=data.failure;
			a.send +=data.send;
			
			for(int i=0;i<6;i++)
				a.sucess[i] +=data.sucess[i];
			hashmap.put(key, a);
			
		}	else	{
			hashmap.put(key, data);		
		}		
	}
	
	/**
	 * Update the SMPP Data Record with the number of retrials in success or one in case it is failure.
	 * 
	 * @param line
	 * @param data
	 */
	public void updateReply(String line, SMPPData data)
	{
		int index = line.indexOf("|| NBTrials = ");
		index += (new String("|| NBTrials = ")).length() ;
		int index2 = line.indexOf("||",index);
		int value = Integer.parseInt(line.substring(index,index2).trim());
		if(line.indexOf(" || SUCCESS || ")>=0)
			data.sucess[value]= 1;
		else
			data.failure= 1;
		
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
		if(index >=0){
			return true;
		}
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
	
	public String extractHourFromDateHour(String DateHour)
	{
		return DateHour.substring(11, 13);
	}
	public String extractDateFromDateHour(String DateHour)
	{
		return DateHour.substring(0,11);
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
			SMPPMessagesConverter s = new SMPPMessagesConverter();
			File[] input = new File[1];
			//input[0]=new File("D:\\Work\\VFE_VAS_Performance_Portal\\Phase IV\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection\\resources\\ftpfolder\\SMPP_OPS.log.2008-02-27-16.log");
			input[0]=new File("D:\\Work\\VFE_VAS_Performance_Portal\\Phase IV\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection\\resources\\ftpfolder\\SMPP_OPS.log.2008-02-27-17.log");
			s.convert(input,"Database");
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

}
