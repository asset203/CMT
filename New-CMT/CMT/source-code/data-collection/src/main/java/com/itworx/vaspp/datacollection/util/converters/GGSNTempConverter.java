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

public class GGSNTempConverter extends AbstractTextConverter{
	private Logger logger;
	private Map  <String ,TacDetails> keyVsTak=new HashMap<String,TacDetails>() ;
	public File[] convert(File[] inputFiles, String systemName)
	throws ApplicationException, InputException {
		logger = Logger.getLogger(systemName);
		logger
				.debug("Inside GGSNTempConverter - started converting input files");
		String path = PropertyReader.getConvertedFilesPath();
		File[] outputFiles = new File[1];
		File outputFile = new File(path, inputFiles[0].getName());

		BufferedReader inputStream = null;
		BufferedWriter outputStream;
		try {
			String cc="";
			outputStream = new BufferedWriter(new FileWriter(outputFile));
			for (int i = 0; i < inputFiles.length; i++) {
				logger
						.debug("GGSNTempConverter.convert() - converting file "
								+ inputFiles[i].getName());
				cc=inputFiles[i].getName().split("_")[1];
				
				inputStream = new BufferedReader(new FileReader(inputFiles[i]));
	           
				String line;
				String key="";
				String date = "";
				long tak=0;
				long  upLink=0;
				long  downLink=0;
				while (inputStream.ready()) {
					
					line = inputStream.readLine();
					if(line.contains("|")&&line.split("\\|").length>=28)
					{
						try
						{
							
							date =getDate(line.split("\\|")[4]);
							if(line.split("\\|")[27].length()>=8)
							tak=Long.parseLong((line.split("\\|")[27].substring(0,8)));
							else
							{
							continue;
							}
							key =date+","+tak;
							upLink=Long.parseLong((line.split("\\|")[21]!=null&&!line.split("\\|")[21].equalsIgnoreCase("")?line.split("\\|")[21].trim():"0"));
							downLink=Long.parseLong((line.split("\\|")[20]!=null&&!line.split("\\|")[20].equalsIgnoreCase("")?line.split("\\|")[20].trim():"0"));
							
							if(keyVsTak.containsKey(key))
							{
								TacDetails takObj=keyVsTak.get(key);
								takObj.setUplink(takObj.getUplink()+upLink);
								takObj.setDownLink(takObj.getDownLink()+downLink);
								takObj.setCount(takObj.getCount()+1);
								keyVsTak.remove(takObj);
								keyVsTak.put(key, takObj);
							}
							else
							{
								TacDetails takObj= new TacDetails ();
								takObj.setUplink(upLink);
								takObj.setDownLink(downLink);
								takObj.setCount(1);
								keyVsTak.put(key, takObj);
							}
						} catch(ParseException exc){logger.error(exc) ; continue ;}
						catch(NumberFormatException exc){logger.error(exc) ; continue ;}
				    }
			}
			}
		inputStream.close();
		Iterator it1 = keyVsTak.keySet().iterator();
		while ( it1.hasNext()) {
			Object key = it1.next();
			TacDetails takObj=keyVsTak.get(key);
			outputStream.write(key + "," + takObj.getUplink()+ ","
					+ takObj.getDownLink() + "," +takObj.getCount());

			outputStream.newLine();
		}
		outputStream.close();
		outputFiles[0]=outputFile;
		logger.debug("GGSNTempConverter.convert() - finished converting input files successfully ");
	
	
}
	catch (FileNotFoundException e) {
			logger
					.error("GGSNTempConverter.convert() - Input file not found "
							+ e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger
					.error("GGSNTempConverter.convert() - Couldn't read input file"
							+ e);
			throw new ApplicationException(e);
		}
		logger
				.debug("GGSNTempConverter.convert() - finished converting input files successfully ");
		return outputFiles;
		
}
	private String getDate(String line) throws ParseException {
		Date date = new Date();
		String dateString;

		SimpleDateFormat inDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		SimpleDateFormat outDateFormat = new SimpleDateFormat(
				"MM/dd/yyyy HH:00:00");

		date = inDateFormat.parse(line);
		dateString = outDateFormat.format(date);
		return dateString;

	}
	public static void main(String ag[]) {
		try {
			
			PropertyReader.init("D:\\build\\phase9\\phase9Builds\\DataCollection");
			GGSNTempConverter s = new GGSNTempConverter();
			File[] input = new File[1];
			input[0]=new File("D:\\build\\phase9\\phase9Builds\\DataCollection\\IPC_PRE_2010120521.txt");
			   s.convert(input,"Maha_Test");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	class TacDetails
	{
		long downLink =0;
		long count =0;
		long uplink=0;
		
		
		public long getDownLink() {
			return downLink;
		}
		public void setDownLink(long downLink) {
			this.downLink = downLink;
		}
		public long getUplink() {
			return uplink;
		}
		public void setUplink(long uplink) {
			this.uplink = uplink;
		}
		public long getCount() {
			return count;
		}
		public void setCount(long count) {
			this.count = count;
		}
		
		
		
	}
}
