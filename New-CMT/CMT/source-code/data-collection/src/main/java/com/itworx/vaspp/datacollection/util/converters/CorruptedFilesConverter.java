package com.itworx.vaspp.datacollection.util.converters;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import org.apache.log4j.Logger;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;
import com.itworx.vaspp.datacollection.util.Utils;

public class CorruptedFilesConverter extends AbstractTextConverter{

	private Logger logger;
	HashMap dataHashmap = new HashMap();

	class DataValues
	{
		public long corruptedFiles;
		public String server;
		public String serverNode;
		public String dateString;
	}
	
	public File[] convert(File[] inputFiles, String systemName)
		throws ApplicationException, InputException {
		
		logger = Logger.getLogger(systemName);
		logger.debug("CorruptedFilesConverter - Starting");		
		File[] outputFiles = new File[1];
	
		try
		{
			String path = PropertyReader.getConvertedFilesPath();
			File output = new File(path, "CorruptedFilesConverter");
			BufferedWriter outputStream = new BufferedWriter(new FileWriter(output));
			BufferedReader inputStream;
			String line="";
			String server="";
			String dateString="";
			String serverNode="";
			String corruptedFiles=""; 
		
			for (int i = 0; i < inputFiles.length; i++) 
			{
				logger.debug( "CorruptedFilesConverter.convert() - converting file "
						+ inputFiles[i].getName() );
				inputStream = new BufferedReader(new FileReader(inputFiles[i]));
				
				String dummy="";
				if(inputStream.ready())
					dummy = inputStream.readLine();
				
				while(inputStream.ready() && (dummy!="" && dummy!=null))
				{		
					line = dummy;
								
					if(line.startsWith("#Server")){
						// extract the date and server from "#Server1,Thu Jul 24 15:30:00 EEST 2008"
						String[] tokens = line.split(",");
						server = tokens[0].trim().substring(1);
						
						
						String timeCode = "EET";
						
						if(tokens[1].indexOf("EEST") != -1)
							timeCode = "EEST";
						
					
						//dateString = (tokens[1].trim()).substring(0, tokens[1].indexOf("EEST")) + tokens[1].substring(tokens[1].indexOf("EEST")+5, tokens[1].indexOf("EEST")+9).trim();
						dateString = (tokens[1].trim()).substring(0, tokens[1].indexOf(timeCode)) + tokens[1].substring(tokens[1].indexOf(timeCode)+timeCode.length()+1, tokens[1].indexOf(timeCode)+timeCode.length()+5).trim();
						
						Date date = Utils.convertToDate(dateString, "E MMM dd HH:mm:ss yyyy");
						dateString = Utils.convertToDateString(date, "MM/dd/yyyy HH:00:00");
						// extract all server node
						dummy = inputStream.readLine();
						while(inputStream.ready()&& !dummy.startsWith("#Server"))
						{
							if(dummy!="" && dummy!=null){
								String[] nodes = dummy.split(",");
								serverNode = nodes[0].trim();
								corruptedFiles = nodes[1].trim();
								// update the hashmap
								UpdateValues(dateString,server,serverNode,corruptedFiles);
							}
							dummy = inputStream.readLine();
						}
						
					}
				}
				if(dummy!="" && dummy!=null){
					String[] nodes = dummy.split(",");
					serverNode = nodes[0].trim();
					corruptedFiles = nodes[1].trim();
					// update the hashmap
					UpdateValues(dateString,server,serverNode,corruptedFiles);
				}
			}			
			WriteAll(outputStream);
			
			outputStream.close();
			outputFiles[0] = output;
			logger.debug("CorruptedFilesConverter.convert() - "
					+ inputFiles[0].getName() + " converted");	
		}
		catch(FileNotFoundException e)
		{
			logger.debug("CorruptedFilesConverter.convert() - input file not found" + e);
			throw new ApplicationException(e);
		}
		catch(IOException e)
		{
			logger.debug("CorruptedFilesConverter.convert() - Couldn't read input file" + e);
			throw new ApplicationException(e);
		}		
		logger.debug( "CorruptedFilesConverter.convert() - finished converting input files Successfully Converted");
		return outputFiles;		
	}
	
	/**
	 * Calculate the processing times for each hour
	 * 
	 * @param key
	 * @param data
	 * @throws ApplicationException 
	 */
	public void UpdateValues(String dateString,String server,String serverNode,String corruptedFiles) throws ApplicationException
	{		
		String key = dateString +"*"+ server +"*"+ serverNode;
		DataValues a= (DataValues)dataHashmap.get(key);
		
		if(a != null)
		{
			a.dateString = dateString;
			a.server = server;
			a.serverNode = serverNode;
			a.corruptedFiles += Long.parseLong(corruptedFiles);
			
			dataHashmap.put(key, a);
				
		}	else	{
			DataValues b = new DataValues();
			b.dateString = dateString;
			b.server = server;
			b.serverNode = serverNode;
			b.corruptedFiles = Long.parseLong(corruptedFiles);
			
			dataHashmap.put(key,b);			
		}				
	}
	
	
	/**
	 * Extract the output from SMPP records Hashmap 
	 * and write it in the required format in the outputfile.
	 * 
	 * @param outputStream
	 */
	public void WriteAll(BufferedWriter outputStream)
	{
		Set keySet = dataHashmap.keySet();
		Object []keyObject = keySet.toArray();
		
		try
		{			
			for(int i=0;i<keyObject.length;i++)
			{
				DataValues value = (DataValues)dataHashmap.get(keyObject[i]);				
				String line = value.dateString +","+ value.server +","+ value.serverNode +","+ value.corruptedFiles ;
				//System.out.println(line);
				outputStream.write(line);
				outputStream.newLine();
			}
		}catch(IOException e)
		{
			System.out.println("error writing to file");
		}
	}
	
	public static void main(String[] args) throws Exception
	{
		try {
			String test="hihi,hello";
			String back= test.replaceAll(",", ";");
			System.out.println("back "+back);
			
			/*PropertyReader.init("D:\\Basem\\Deployment - 15_1_2009\\Production Package\\Source Code\\DataCollection");
			CorruptedFilesConverter s = new CorruptedFilesConverter();
			File[] input = new File[1];
			input[0]=new File("D:\\Basem\\Deployment - 15_1_2009\\Production Package\\Source Code\\DataCollection\\resources\\ftpfolder\\VNPP_Corrupt_Report080724.txt");
			s.convert(input,"Database");*/
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
}
