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
import com.itworx.vaspp.datacollection.util.converters.GGSNTempConverter.TacDetails;

public class QMTacConverter extends AbstractTextConverter{
	private Logger logger;
	private Map  <String ,TacDetails> keyVsTak=new HashMap<String,TacDetails>() ;
public QMTacConverter()
{}
public File[] convert(File[] inputFiles, String systemName)
throws ApplicationException, InputException {
	logger = Logger.getLogger(systemName);
	logger
			.debug("Inside QMTacConverter - started converting input files");
	String path = PropertyReader.getConvertedFilesPath();
	File[] outputFiles = new File[1];
	File outputFile = new File(path, inputFiles[0].getName());

	BufferedReader inputStream = null;
	BufferedWriter outputStream;
	try {
		
		outputStream = new BufferedWriter(new FileWriter(outputFile));
		for (int i = 0; i < inputFiles.length; i++) {
			logger
					.debug("QMTacConverter.convert() - converting file "
							+ inputFiles[i].getName());
		
			
			inputStream = new BufferedReader(new FileReader(inputFiles[i]));
           
			String line;
			String key="";
			String date = "";
			long tak=0;
			long  dataVolume=0;
			
			while (inputStream.ready()) {
				
				line = inputStream.readLine();
				if(line.contains("|")&&line.split("\\|").length>=28)
				{
					try
					{
						
						date =getDate(line.split("\\|")[4]);
						
						if(line.split("\\|")[24].length()>=8)
						tak=Long.parseLong((line.split("\\|")[24].substring(0,8)));
						else
						{
						continue;
						}
						key =date+","+tak;						
						dataVolume=Long.parseLong((line.split("\\|")[14]!=null&&!line.split("\\|")[14].equalsIgnoreCase("")?line.split("\\|")[14].trim():"0"));
						if(keyVsTak.containsKey(key))
						{
							TacDetails obj=keyVsTak.get(key);
							obj.setDataVolume(obj.getDataVolume()+dataVolume);
							obj.setCount(obj.getCount()+1);
							keyVsTak.remove(obj);
							keyVsTak.put(key, obj);
						}else
						{
							TacDetails obj= new TacDetails();
							obj.setDataVolume(dataVolume);
							obj.setCount(1);
							keyVsTak.put(key, obj);
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
		outputStream.write(key + "," + takObj.getDataVolume()+ ","+ takObj.getCount());
		outputStream.newLine();
	}
	outputStream.close();
	outputFiles[0]=outputFile;
	logger.debug("QMTacConverter.convert() - finished converting input files successfully ");


}
catch (FileNotFoundException e) {
		logger
				.error("QMTacConverter.convert() - Input file not found "
						+ e);
		throw new ApplicationException(e);
	} catch (IOException e) {
		logger
				.error("QMTacConverter.convert() - Couldn't read input file"
						+ e);
		throw new ApplicationException(e);
	}
	logger
			.debug("QMTacConverter.convert() - finished converting input files successfully ");
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
			QMTacConverter s = new QMTacConverter();
			File[] input = new File[1];
			input[0]=new File("D:\\build\\phase9\\phase9Builds\\DataCollection\\CCN-QM-23012011082800908");
			   s.convert(input,"Maha_Test");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	class TacDetails
	{
		double dataVolume =0.0;
		long count =0;
		public double getDataVolume() {
			return dataVolume;
		}
		public void setDataVolume(double dataVolume) {
			this.dataVolume = dataVolume;
		}
		public long getCount() {
			return count;
		}
		public void setCount(long count) {
			this.count = count;
		}
		
		
		
		
		
		
		
	}			
		
}
