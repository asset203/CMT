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
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;

public class SDPDBUtilizeConverter extends AbstractTextConverter{
	private Logger logger;
	public File[] convert(File[] inputFiles, String systemName)
	throws ApplicationException, InputException {
		logger = Logger.getLogger(systemName);
		logger
				.debug("Inside SDPDBUtilizeConverter - started converting input files");
		String path = PropertyReader.getConvertedFilesPath();
		File[] outputFiles = new File[1];
		File outputFile = new File(path, inputFiles[0].getName());

		BufferedReader inputStream = null;
		BufferedWriter outputStream;
		try {
			outputStream = new BufferedWriter(new FileWriter(outputFile));
			for (int i = 0; i < inputFiles.length; i++) {
				logger
						.debug("SDPDBUtilizeConverter.convert() - converting file "
								+ inputFiles[i].getName());
				
				inputStream = new BufferedReader(new FileReader(inputFiles[i]));
				String fileName =inputFiles[i].getName();
				if(fileName.contains("_"))
					fileName=fileName.split("_")[6].split(".log")[0];
				System.out.println(fileName);
				String line;
				String date = "";
				String PERM_ALLOCATED_SIZE="0";
				String PERM_IN_USE_SIZE="0";
				String PERM_IN_USE_HIGH_WATER="0";
				String TEMP_ALLOCATED_SIZE="0";
				String TEMP_IN_USE_SIZE="0";
				String TEMP_IN_USE_HIGH_WATER="0";
				String column="0";
				try
				{
				date=getDate(fileName);
				
				while (inputStream.ready()) {
					line = inputStream.readLine();
					if(line.contains(":"))
					{
						column=line.split(":")[0];
						if(column.trim().equalsIgnoreCase("PERM_ALLOCATED_SIZE"))
						
							PERM_ALLOCATED_SIZE=line.split(":")[1].trim();
						
						
						else if(column.trim().equalsIgnoreCase("PERM_IN_USE_SIZE"))
							PERM_IN_USE_SIZE=line.split(":")[1].trim();
						
						else if(column.trim().equalsIgnoreCase("PERM_IN_USE_HIGH_WATER"))
							PERM_IN_USE_HIGH_WATER=line.split(":")[1].trim();
						
						else if(column.trim().equalsIgnoreCase("TEMP_ALLOCATED_SIZE"))
							TEMP_ALLOCATED_SIZE=line.split(":")[1].trim();
						
						else if(column.trim().equalsIgnoreCase("TEMP_IN_USE_SIZE"))
							TEMP_IN_USE_SIZE=line.split(":")[1].trim();
						
						else	if(column.trim().equalsIgnoreCase("TEMP_IN_USE_HIGH_WATER"))
							TEMP_IN_USE_HIGH_WATER=line.split(":")[1].trim();
						
					}
					
				}
				}catch(ParseException exc){ logger.error(exc) ; continue ;}
				outputStream.write(date+","+PERM_ALLOCATED_SIZE+","+PERM_IN_USE_SIZE+","+PERM_IN_USE_HIGH_WATER+","+TEMP_ALLOCATED_SIZE+","+TEMP_IN_USE_SIZE+","+TEMP_IN_USE_HIGH_WATER);
				 outputStream.newLine();
			}
			inputStream.close();
			
			outputStream.close();
			outputFiles[0]=outputFile;
			logger.debug("SDPDBUtilizeConverter.convert() - finished converting input files successfully ");

		
	}
		catch (FileNotFoundException e) {
				logger
						.error("SDPDBUtilizeConverter.convert() - Input file not found "
								+ e);
				throw new ApplicationException(e);
			} catch (IOException e) {
				logger
						.error("SDPDBUtilizeConverter.convert() - Couldn't read input file"
								+ e);
				throw new ApplicationException(e);
			}
			logger
					.debug("SDPDBUtilizeConverter.convert() - finished converting input files successfully ");
			return outputFiles;
			
		}
public SDPDBUtilizeConverter()
{
}
private String getDate(String line) throws ParseException {
	String[] tokens = null;
	Date date = new Date();
	String dateString;
	SimpleDateFormat inDateFormat = new SimpleDateFormat(
			"yyyy.MM.dd");
	SimpleDateFormat outDateFormat = new SimpleDateFormat(
			"MM/dd/yyyy");

	
		date = inDateFormat.parse(line);
	dateString = outDateFormat.format(date);
	return dateString;

}
public static void main(String ag[]) {
	try {
		
		PropertyReader.init("D:\\build\\phase10\\DataCollection");
		SDPDBUtilizeConverter s = new SDPDBUtilizeConverter();
		File[] input = new File[1];
		input[0]=new File("D:\\build\\phase10\\DataCollection\\TT.db.utilization_2011.06.06.log");
		   s.convert(input,"Maha_Test");
		System.out.println("FINISHED ");
	} catch (Exception e) {
		e.printStackTrace();
	}
}
}
