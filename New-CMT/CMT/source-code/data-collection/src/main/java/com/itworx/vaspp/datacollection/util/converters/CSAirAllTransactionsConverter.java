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

import org.apache.log4j.Logger;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;

public class CSAirAllTransactionsConverter extends AbstractTextConverter{
	private Logger logger;
	public CSAirAllTransactionsConverter()
         {}
public File[] convert(File[] inputFiles, String systemName)
throws ApplicationException, InputException {
	logger = Logger.getLogger(systemName);
	logger
			.debug("Inside CSAirAllTransactionsConverter convert - started converting input files");
	String path = PropertyReader.getConvertedFilesPath();
	File[] outputFiles = new File[1];
	File outputFile = new File(path, inputFiles[0].getName());

	BufferedReader inputStream = null;
	BufferedWriter outputStream;
	try {
		outputStream = new BufferedWriter(new FileWriter(outputFile));
		for (int i = 0; i < inputFiles.length; i++) {
			logger
					.debug("CSAirAllTransactionsConverter.convert() - converting file "
							+ inputFiles[i].getName());
			
			inputStream = new BufferedReader(new FileReader(inputFiles[i]));
            System.out.println("File [" + i +"]");
			String line;
			String date = "";
			while (inputStream.ready()) {
				line = inputStream.readLine();
				if(line.contains("DATE/TIME"))
				{
					continue;
				}
				else 
				{
					String columns[]=line.split(",");
					if(columns!=null&&columns.length>=56)
					{
						try
						{
							date =getDate(columns[0]);
							//System.out.println("date "+date);
							long getBalanceAndDate_3_1_In =columns[31]!=null?Long.parseLong(columns[31].trim()):0;
							long updateSubscriberSegmentation_3_1_In  =columns[35]!=null?Long.parseLong(columns[35].trim()):0;
							long getAccountDetails_3_1_In =columns[36]!=null?Long.parseLong(columns[36].trim()):0;
							long updateBalanceAndDate_3_1_In  =columns[37]!=null?Long.parseLong(columns[37].trim()):0;
							long refill_3_1_In  =columns[47]!=null?Long.parseLong(columns[47].trim()):0;
							long valueVoucherRefillARequest_2_0_In  =columns[49]!=null?Long.parseLong(columns[49].trim()):0;
							long standardVoucherRefillARequest_2_0_In  =columns[50]!=null?Long.parseLong(columns[50].trim()):0;
							long refillARequest_2_0_In  =columns[51]!=null?Long.parseLong(columns[51].trim()):0;
							long refillTRequest_2_0_In  =columns[52]!=null?Long.parseLong(columns[52].trim()):0;
							long standardVoucherRefillTRequest_2_0_In  =columns[53]!=null?Long.parseLong(columns[53].trim()):0;
							long valueVoucherRefillTRequest_2_0_In  =columns[54]!=null?Long.parseLong(columns[54].trim()):0;
							long valueVoucherRefillARequest_2_1 =columns[55]!=null?Long.parseLong(columns[55].trim()):0;
							long standardVoucherRefillARequest_2_1 =columns[56]!=null?Long.parseLong(columns[56].trim()):0;
							outputStream.write(date+","+getBalanceAndDate_3_1_In+","+updateSubscriberSegmentation_3_1_In+","+getAccountDetails_3_1_In
									+","+updateBalanceAndDate_3_1_In+","+refill_3_1_In+","+valueVoucherRefillARequest_2_0_In
									+","+standardVoucherRefillARequest_2_0_In+","+refillARequest_2_0_In+","+refillTRequest_2_0_In+","+standardVoucherRefillTRequest_2_0_In
									+","+valueVoucherRefillTRequest_2_0_In+","+valueVoucherRefillARequest_2_1+","+standardVoucherRefillARequest_2_1);
							/*System.out.println("key "+date+","+getBalanceAndDate_3_1_In+","+updateSubscriberSegmentation_3_1_In+","+getAccountDetails_3_1_In
									+","+updateBalanceAndDate_3_1_In+","+refill_3_1_In+","+valueVoucherRefillARequest_2_0_In
									+","+standardVoucherRefillARequest_2_0_In+","+refillARequest_2_0_In+","+refillTRequest_2_0_In+","+standardVoucherRefillTRequest_2_0_In
									+","+valueVoucherRefillTRequest_2_0_In+","+valueVoucherRefillARequest_2_1+","+standardVoucherRefillARequest_2_1);*/
							 outputStream.newLine();
						}
						 catch(ParseException exc){ logger.error(exc) ; continue ;}
						 catch(NumberFormatException exc){ logger.error(exc) ; continue ;}
					}
				}
				
			}
		}
inputStream.close();
		
		outputStream.close();
		outputFiles[0]=outputFile;
		logger.debug("CSAirAllTransactionsConverter.convert() - finished converting input files successfully ");
	
	}
	catch (FileNotFoundException e) {
			logger
					.error("CSAirAllTransactionsConverter.convert() - Input file not found "
							+ e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger
					.error("CSAirAllTransactionsConverter.convert() - Couldn't read input file"
							+ e);
			throw new ApplicationException(e);
		}
		logger
				.debug("CSAirAllTransactionsConverter.convert() - finished converting input files successfully ");
		return outputFiles;
}
private String getDate(String line) throws ParseException {
	String[] tokens = null;
	Date date = new Date();
	String dateString;
	SimpleDateFormat inDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd-HHmm");
	SimpleDateFormat outDateFormat = new SimpleDateFormat(
			"MM/dd/yyyy HH:00:00");

	
		date = inDateFormat.parse(line);
	dateString = outDateFormat.format(date);
	return dateString;

}
public static void main(String ag[]) {
	try {
		
		PropertyReader.init("D:\\build\\pahse8\\build\\DataCollection");
		CSAirAllTransactionsConverter s = new CSAirAllTransactionsConverter();
		File[] input = new File[1];
		input[0]=new File("D:\\build\\pahse8\\build\\DataCollection\\FSC-AirXmlRpc_2.0_A_1-2010-10-12-0000.csv");
		   s.convert(input,"Maha_Test");
		
	} catch (Exception e) {
		e.printStackTrace();
	}
}
}
