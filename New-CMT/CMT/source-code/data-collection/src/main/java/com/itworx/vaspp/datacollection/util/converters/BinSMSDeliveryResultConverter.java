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

public class BinSMSDeliveryResultConverter extends AbstractTextConverter{
	private Logger logger;
public BinSMSDeliveryResultConverter()
{}
private Map  <String ,Long > dateVsCount=new HashMap<String,Long>() ;
public File[] convert(File[] inputFiles, String systemName)
throws ApplicationException, InputException {

	logger = Logger.getLogger(systemName);

	logger
			.debug("Inside BinSMSDeliveryResultConverter convert - started converting input files");
	String path = PropertyReader.getConvertedFilesPath();
	File[] outputFiles = new File[1];
	File outputFile = new File(path, "SMSDelivery"+inputFiles[0].getName());
	BufferedWriter outputStream;
//	BufferedReader inputStream;
	try{
		outputStream = new BufferedWriter(new FileWriter(outputFile));
		for (int i = 0; i < inputFiles.length; i++) {

			   String command = "./tp_log.pl"+" "+inputFiles[i];

	           /*Process p =Runtime.getRuntime().exec(command);
	           p.waitFor();		 */
	          BufferedReader inputStream = new BufferedReader(new FileReader(PropertyReader.getImportedFilesPath()+"/"+inputFiles[i].getName()+"c"));
	          // inputStream = new BufferedReader(new FileReader(inputFiles[i]));

	           String line;
				String date = "";
				String deliveryResult ="";
				String key="";
	            while(inputStream.ready())
	            {
	            	line= inputStream.readLine();

	            	if(line.contains("_")&&line.contains("File:"))
	            	{
	            		try
	            		{
	            			if(line.split("_").length >=4)
	            			{

	            		    date = getDate (line.split("_")[line.split("_").length-3]+line.split("_")[line.split("_").length-2]);

	            			}
	            		 }catch(ParseException exc){ logger.error(exc) ; continue ;}
	            		 catch(java.lang.ArrayIndexOutOfBoundsException e){System.out.println("line"+line);continue;};
	            	}
	            	else if (line.contains(" deliveryResult ="))
	            	{
	            		deliveryResult = line.split(" deliveryResult =")[1];

	            		key =date+","+deliveryResult;
	            		if(dateVsCount.containsKey(key))
	            		{
	            			long count=dateVsCount.get(key);
	            			dateVsCount.remove(key);
	            			dateVsCount.put(key, count +1);
	            		}
	            		else
	            		{
	            			dateVsCount.put(key,new Long (1));
	            		}
	            	}

	            }
	            inputStream.close();
		}//end of files
		Iterator it=dateVsCount.keySet().iterator();

		while(it.hasNext())
		{
			Object key=it.next();
			outputStream.write(key+","+dateVsCount.get(key));
			outputStream.newLine();

		}
		outputStream.close();
		outputFiles[0]=outputFile;

   }/*catch(InterruptedException  e)
   {
	   e.printStackTrace();
   }*/
	catch(IllegalThreadStateException  e)
   {
	   e.printStackTrace();
   }

   catch (FileNotFoundException e) {
		logger
				.error("BinSMSDeliveryResultConverter.convert() - Input file not found "
						+ e);
		throw new ApplicationException(e);
	} catch(IOException e)
    {
    	e.printStackTrace();
    }
	logger
			.debug("BinSMSDeliveryResultConverter.convert() - finished converting input files successfully ");
	return outputFiles;
}
public static void  main(String ag[])
{

	 try {
            String path ="D:\\build\\phase9\\phase9Builds\\DataCollection";
    		PropertyReader.init(path);
    		BinSMSDeliveryResultConverter s = new BinSMSDeliveryResultConverter();
    		File[] input = new File[1];
    		input[0]=new File("D:\\build\\phase9\\phase9Builds\\DataCollection\\log_mo_mt_mbegvf-rtr05_20110228_104100_714.dat");
    		s.convert(input,"Maha_Test");
            System.out.println("finish file");
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
}
private String getDate(String line) throws ParseException {
	String[] tokens = null;
	Date date = new Date();
	String dateString;
	SimpleDateFormat inDateFormat = new SimpleDateFormat(
			"yyyyMMddHHmmss");
	SimpleDateFormat outDateFormat = new SimpleDateFormat(
			"MM/dd/yyyy HH:00:00");


	date = inDateFormat.parse(line);
	dateString = outDateFormat.format(date);
	return dateString;

}
}
