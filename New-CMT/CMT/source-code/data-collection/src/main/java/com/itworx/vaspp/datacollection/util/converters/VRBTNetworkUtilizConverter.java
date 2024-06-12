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

public class VRBTNetworkUtilizConverter extends AbstractTextConverter{
	private Logger logger;
	public VRBTNetworkUtilizConverter()
	{}
	public File[] convert(File[] inputFiles, String systemName)
	throws ApplicationException, InputException {
		logger = Logger.getLogger(systemName);
		logger
				.debug("Inside VRBTNetworkUtilizConverterconvert - started converting input files");
		String path = PropertyReader.getConvertedFilesPath();
		File[] outputFiles = new File[1];
		File outputFile = new File(path, inputFiles[0].getName());

		BufferedReader inputStream = null;
		BufferedWriter outputStream;
		try {
			outputStream = new BufferedWriter(new FileWriter(outputFile));
			for (int i = 0; i < inputFiles.length; i++) {
				logger
						.debug("VRBTNetworkUtilizConverter.convert() - converting file "
								+ inputFiles[i].getName());
				
				inputStream = new BufferedReader(new FileReader(inputFiles[i]));
	            System.out.println("File [" + i +"]");
				String line;
				String date = "";
				long dpc;
				long opc;
				long ports;
				long total_calls;
				long successful_calls;
				long failed_calls;
				double mean_hold_time;
				double erlangs;
				double utilization_in_percent;
				double asr;
				while (inputStream.ready())
				{
					line = inputStream.readLine();
					if(line.contains("date"))
					{
						continue;
					}
					else
					{
						String columns[]=line.split(",");
						try
						{
							if(columns.length>=13){
								
							String datStr=columns[0]+" "+columns[1];
							
							/*if(datStr!=null)
							{ */
								date=getDate(datStr);
								String site=(columns[2]!=null)?columns[2]:"";
								dpc=(columns[3]!=null)?Long.parseLong(columns[3]):0;
								opc=(columns[4]!=null)?Long.parseLong(columns[4]):0;
								ports=(columns[5]!=null)?Long.parseLong(columns[5]):0;
								total_calls=(columns[6]!=null)?Long.parseLong(columns[6]):0;
								successful_calls=(columns[7]!=null)?Long.parseLong(columns[7]):0;
								failed_calls=(columns[8]!=null)?Long.parseLong(columns[8]):0;
								mean_hold_time=(columns[9]!=null)?Double.parseDouble(columns[9]):0.0;
								erlangs=(columns[10]!=null)?Double.parseDouble(columns[10]):0.0;
							    utilization_in_percent=(columns[11]!=null)?Double.parseDouble(columns[11]):0.0;
								asr=(columns[12]!=null)?Double.parseDouble(columns[12]):0.0;
								 outputStream.write(date+","+site+","+dpc+","+opc+","+ports+","+total_calls+","+successful_calls
										 +","+failed_calls+","+mean_hold_time+","+erlangs+","+utilization_in_percent+","+asr);
								/* System.out.println("the key"+date+","+site+","+dpc+","+opc+","+ports+","+total_calls+","+successful_calls
										 +","+failed_calls+","+mean_hold_time+","+erlangs+","+utilization_in_percent+","+asr);*/
							     outputStream.newLine();
							/*}
							else
								{ 
								continue;
								}*/
							}
						     // System.out.println("date" +date);
						}catch(ParseException exc){ logger.error(exc) ; continue ;}
						catch(java.lang.NumberFormatException ex){ logger.error(ex) ; continue ;}
					}
				}
			}
			inputStream.close();
			
			outputStream.close();
			outputFiles[0]=outputFile;
			logger.debug("VRBTNetworkUtilizConverter.convert() - finished converting input files successfully ");

		}
		catch (FileNotFoundException e) {
				logger
						.error("VRBTNetworkUtilizConverter.convert() - Input file not found "
								+ e);
				throw new ApplicationException(e);
			} catch (IOException e) {
				logger
						.error("VRBTNetworkUtilizConverter.convert() - Couldn't read input file"
								+ e);
				throw new ApplicationException(e);
			}
			logger
					.debug("VRBTNetworkUtilizConverter.convert() - finished converting input files successfully ");
			return outputFiles;
	}
	public static void main(String ag[]) {
		try {
			
			PropertyReader.init("D:\\build\\pahse8\\DataCollection");
			VRBTNetworkUtilizConverter s = new VRBTNetworkUtilizConverter();
			File[] input = new File[1];
			input[0]=new File("D:\\build\\pahse8\\DataCollection\\Network Utilization report 20100902.csv");
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
				"MM/dd/yyyy HH");
		SimpleDateFormat outDateFormat = new SimpleDateFormat(
				"MM/dd/yyyy HH:mm:ss");

		
			date = inDateFormat.parse(line);
		dateString = outDateFormat.format(date);
		return dateString;

	}
}
