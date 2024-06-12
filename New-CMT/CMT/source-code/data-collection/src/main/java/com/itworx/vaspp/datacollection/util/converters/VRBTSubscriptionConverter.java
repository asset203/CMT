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
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;

public class VRBTSubscriptionConverter extends AbstractTextConverter{
	private Logger logger;
public VRBTSubscriptionConverter()
{
}
public File[] convert(File[] inputFiles, String systemName)
throws ApplicationException, InputException {
	logger = Logger.getLogger(systemName);
	logger
			.debug("Inside VRBTSubscriptionConverter - started converting input files");
	String path = PropertyReader.getConvertedFilesPath();
	File[] outputFiles = new File[1];
	File outputFile = new File(path, inputFiles[0].getName());

	BufferedReader inputStream = null;
	BufferedWriter outputStream;
	try {
		outputStream = new BufferedWriter(new FileWriter(outputFile));
		for (int i = 0; i < inputFiles.length; i++) {
			logger
					.debug("VRBTSubscriptionConverter.convert() - converting file "
							+ inputFiles[i].getName());

			inputStream = new BufferedReader(new FileReader(inputFiles[i]));

			String line;
			String date = "";
			String status;
			String key="";
			if(inputFiles[i].getName().contains("_"))

				date=inputFiles[i].getName().split("_")[inputFiles[i].getName().split("_").length-1].split(".csv")[0];

			try {
			date = getDate(date);

			}catch(ParseException exc){ logger.error(exc) ; continue ;}
			double count;
			while (inputStream.ready())
			{
				line = inputStream.readLine();
				try {
				//date=getDate(line);
                    /**
                     * This line was updated by Alia.Adel on 2013.11.24
                     * because input file header changed to "status, sum"
                     */
                    if(line.contains("status,count") || line.contains("status,sum"))
                    {continue;}

                    if(line.split(",").length==1)
                    {
                        status=line.split(",")[0];
                        count=0;
                        key=date+","+status+","+count;

                        outputStream.write(key);
                        outputStream.newLine();
                    }
                    else if(line.contains(",")&&line.split(",").length>=2)
                    {


                            status=line.split(",")[0];
                            count=Double.parseDouble(line.split(",")[1]!=null?line.split(",")[1]:"0");
                            key=date+","+status+","+count;
                            outputStream.write(key);
                            outputStream.newLine();

                    }
				}//catch(ParseException exc){ logger.error(exc) ; continue ;}
				catch(java.lang.NumberFormatException ex){ logger.error(ex) ; continue ;}
			}

	}
	inputStream.close();

	outputStream.close();
	outputFiles[0]=outputFile;
	logger.debug("VRBTSubscriptionConverter.convert() - finished converting input files successfully ");

}
catch (FileNotFoundException e) {
		logger
				.error("VRBTSubscriptionConverter.convert() - Input file not found "
						+ e);
		throw new ApplicationException(e);
	} catch (IOException e) {
		logger
				.error("VRBTSubscriptionConverter.convert() - Couldn't read input file"
						+ e);
		throw new ApplicationException(e);
	}
	logger
			.debug("VRBTSubscriptionConverter.convert() - finished converting input files successfully ");
	return outputFiles;
}
public static void main(String ag[]) {
try {

	PropertyReader.init("D:\\build\\phase10\\DataCollection");
	VRBTSubscriptionConverter s = new VRBTSubscriptionConverter();
	File[] input = new File[1];
	input[0]=new File("D:\\build\\phase10\\DataCollection\\VFEgyptSubscriberBaseNumbers_20110611.csv");
	 s.convert(input,"Maha_Test");


} catch (Exception e) {
	e.printStackTrace();
}
}
private String getDate(String line) throws ParseException {
	String[] tokens = null;
	Date date = new Date();
	String dateString;
	SimpleDateFormat inDateFormat = new SimpleDateFormat(
			"yyyyMMdd");
	SimpleDateFormat outDateFormat = new SimpleDateFormat(
			"MM/dd/yyyy");


		date = inDateFormat.parse(line);
	dateString = outDateFormat.format(date);
	return dateString;

}
}
