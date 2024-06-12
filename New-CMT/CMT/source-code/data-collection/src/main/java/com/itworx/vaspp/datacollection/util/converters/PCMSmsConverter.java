package com.itworx.vaspp.datacollection.util.converters;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
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
import java.util.Properties;

import org.apache.log4j.Logger;
import org.hibernate.cfg.Configuration;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;
import com.itworx.vaspp.datacollection.util.converters.EOCNBrowserReqRespConverter.RequestResponse;

public class PCMSmsConverter extends AbstractTextConverter{
	private  static Logger logger;
	static public final String SYSTEM_SEPARATOR = System
	.getProperty("file.separator");
	static private final String MAPPING_FILE_NAME = "resources" + SYSTEM_SEPARATOR
	+ "configuration" + SYSTEM_SEPARATOR + "mapping.properties";
	private static Properties properties = null;
	private static HashMap<String ,String> msgTextMapping=new HashMap<String, String>();
	public PCMSmsConverter()
	{}
	public File[] convert(File[] inputFiles, String systemName)
	throws ApplicationException, InputException {
		logger = Logger.getLogger(systemName);
	    Map<String, Long> dateVsCount = new HashMap<String, Long>();
		logger
				.debug("Inside PCMSmsConverter convert - started converting input files");
		String path = PropertyReader.getConvertedFilesPath();
		File[] outputFiles = new File[1];
		File outputFile = new File(path, inputFiles[0].getName());

		BufferedReader inputStream = null;
		BufferedWriter outputStream;
		try {
			outputStream = new BufferedWriter(new FileWriter(outputFile));
			for (int i = 0; i < inputFiles.length; i++) {
				logger
						.debug("PCMSmsConverter.convert() - converting file "
								+ inputFiles[i].getName());
				
				inputStream = new BufferedReader(new FileReader(inputFiles[i]));
	            System.out.println("File [" + i +"]");
				String line;
				String date = "";
				String key="";
				String totalMsg="";
				String message="";
				while (inputStream.ready()) {
					line = inputStream.readLine();
					if(line.contains("SUBMIT_SM"))
					{
						if(line.contains(","))
						{  
							try {
							date=getDate(line.split(",")[0]);
							
							if(line.contains("message"))
							{
								totalMsg=line.split("message")[1];
								
								if(totalMsg.contains("[")&&totalMsg.contains("]"))
								{
									message=(totalMsg.split("\\[")[1]).split("\\]")[0];
									//System.out.println("message "+message);
								}
								else if(totalMsg.contains("["))
								{
									message=totalMsg.split("\\[")[1];
									line=inputStream.readLine();
									while(!line.contains("]"))
									{
								   	message = message+line;
									line=inputStream.readLine();
									}
									//line=inputStream.readLine();
									if(line.contains("]"))
									{
										message=message+line.split("\\]")[0];
									}
									
																	}
							}
							//System.out.println("date"+date);
						       }
						  catch(ParseException exc){ logger.error(exc) ; continue ;}
						  message=message.replaceAll(",", ";");
						 key=date+","+message; //here 
						  if(dateVsCount.containsKey(key))
						  {
							  long count=dateVsCount.get(key);
							  dateVsCount.remove(key);
							  dateVsCount.put(key, count+1);
						  }else
						  {
							  dateVsCount.put(key, new Long (1));
						  }
						}
					}
					else
					{continue;}
				}
			}
			loadMapping();
			
			Iterator it =dateVsCount.keySet().iterator();
			while(it.hasNext())
			{
				Object key =it.next();
				String textMsg=key.toString().split(",")[1];
				textMsg=new String(textMsg.getBytes("UTF-8"),"UTF-8");
				if(msgTextMapping.get(textMsg)!=null)
				{   
					
					textMsg=msgTextMapping.get(textMsg);
					
				}
				outputStream.write(key.toString().split(",")[0]+","+textMsg+","+dateVsCount.get(key));
				outputStream.newLine();
			}
		//	System.out.println("the getPropertyValue name "+getPropertyValue("helloWord"));
   	inputStream.close();
			
			outputStream.close();
			outputFiles[0]=outputFile;
			logger.debug("PCMSmsConverter.convert() - finished converting input files successfully ");
		
		}
		catch (FileNotFoundException e) {
				logger
						.error("PCMSmsConverter.convert() - Input file not found "
								+ e);
				throw new ApplicationException(e);
			} catch (IOException e) {
				logger
						.error("PCMSmsConverter.convert() - Couldn't read input file"
								+ e);
				throw new ApplicationException(e);
			}
			logger
					.debug("PCMSmsConverter.convert() - finished converting input files successfully ");
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
			
			PropertyReader.init("D:\\build\\pahse8\\logmanager\\DataCollection");
			PCMSmsConverter s = new PCMSmsConverter();
			File[] input = new File[1];
			input[0]=new File("D:\\build\\pahse8\\logmanager\\DataCollection\\ipcconnector_2010112814.log");
			String Path =PropertyReader.getPersistencPropertyPath()+SYSTEM_SEPARATOR + "mapping.properties";
			System.out.println("path "+Path);
			 s.convert(input,"Maha_Test");
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/*public  void load() {
		properties = new Properties();
		try {
			
			properties.load(new FileInputStream(FILE_NAME));
		} catch (Exception e) {
			logger
					.debug("TrendAnalysisDAO.load() - Unable To Open properties file: app_config.properties");
		}
	}
	public  String getPropertyValue(String name) {
		if (properties == null)
			load();
		return properties.getProperty(name);
	}*/
	public void loadMapping()
	{
		try {
			
			String Path =PropertyReader.getPersistencPropertyPath()+SYSTEM_SEPARATOR + "PCM_SMS_Mapping.properties";
            BufferedReader propReader=new BufferedReader(new FileReader(new File(Path)));
           
            while(propReader.ready()){
                String line = propReader.readLine();
                String[] lineParts=line.split("=");
                if(lineParts==null|lineParts.length<2)
                                continue;
                String arabicMsg=new String(lineParts[0].getBytes("UTF-8"),"UTF-8").replaceAll(",", ";");
                
                String engMsg=lineParts[1].replaceAll(",", ";");
                msgTextMapping.put(arabicMsg,engMsg);
}
		}
	 catch (FileNotFoundException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
         } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
}
	}
}
