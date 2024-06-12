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
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;

public class AlaElnotaConverter extends AbstractTextConverter{
	private static SimpleDateFormat inDateFormat = new SimpleDateFormat(
			"yyyyMMdd HH:mm:ss");
	private static SimpleDateFormat outDateFormat = 
			 new SimpleDateFormat("MM/dd/yyyy HH:00:00");
	Logger logger = null;
	private static final String SEP = ",";
	private static final String ALNOTABOROWING="3ALNOTABOROWING";
	@Override
	public File[] convert(File[] inputFiles, String systemName)
			throws ApplicationException, InputException {
		logger = Logger.getLogger(systemName);
		Map<String, Long> dateVCount = new HashMap<String, Long>();
		logger.debug("Inside AlaElnotaConverter - started converting input files");
		String path = PropertyReader.getConvertedFilesPath();
		File[] outputFiles = new File[1];
		File outputFile = new File(path, inputFiles[0].getName());

		BufferedReader inputStream = null;
		BufferedWriter outputStream = null;

		String key = null;
		String line = null;
		String date = null;
		String serviceClass=null;
		
		try {
			outputStream = new BufferedWriter(new FileWriter(outputFile));
			for (int i = 0; i < inputFiles.length; i++) {
				logger.debug("AlaElnotaConverter.convert() - converting file "
						+ inputFiles[i].getName());
				inputStream = new BufferedReader(new FileReader(inputFiles[i]));
				try {
					while ((line = inputStream.readLine()) != null) {
						if(!(line.contains(SEP)&&line.split(SEP).length>=8&&line.split(SEP)[7].equalsIgnoreCase(ALNOTABOROWING)))
							continue;
						String []columns=line.split(SEP);
						  try{
                              date=getDate(columns[3]);
                              serviceClass=columns[2];
                              key=date+SEP+serviceClass;
                              if(dateVCount.containsKey(key))
                              {
                            	  long count=dateVCount.get(key);
                            	  dateVCount.put(key, count+1);
                              }else
                              {
                            	  dateVCount.put(key, new Long(1));  
                              }
                              
						  }catch(ParseException e)
					      {
					    	  logger.error(
										"AlaElnotaConverter.convert() - error occurred while processing date ["
												+ inputFiles[i].getName() + "] in line ["
												+ line + "]", e);
					      }
					}
				} catch (Exception e) {
					logger.error(
							"AlaElnotaConverter.convert() - error occurred while processing file ["
									+ inputFiles[i].getName() + "] in line ["
									+ line + "]", e);
					System.out.println(e);
				} finally {
					inputStream.close();
				}
			}// end of files

			for (Entry<String, Long> Entry : dateVCount.entrySet()) {
				key = Entry.getKey();
				
				outputStream.write(key+SEP+dateVCount.get(key));
				System.out.println(key+SEP+dateVCount.get(key));
				outputStream.newLine();
			}
			outputFiles[0] = outputFile;
			dateVCount.clear();
			logger.debug("AlaElnotaConverter.convert() - finished converting input files successfully ");
		} catch (FileNotFoundException e) {
			logger.error(
					"AlaElnotaConverter.convert() - Input file not found ",
					e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger.error(
					"AlaElnotaConverter.convert() - Couldn't read input file",
					e);
			throw new ApplicationException(e);
		} catch (Exception e) {
			logger.error(
					"AlaElnotaConverter.convert() - Couldn't read input file",
					e);
			throw new ApplicationException(e);
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					logger.error(
							"AlaElnotaConverter.convert() - Couldn't read input file",
							e);
					throw new ApplicationException(e);
				}
			}
		}
		logger.debug("AlaElnotaConverter.convert() - finished converting input files successfully ");
		return outputFiles;

	}
	private String getDate(String str) throws ParseException {
		Date date = null;
		String dateString;
		date = inDateFormat.parse(str);
		dateString = outDateFormat.format(date);
		return dateString;
	}
	public static void main(String ag[]) {
		try {
			PropertyReader.init("D:\\VFE_VAS_SOURCE_CODE\\DataCollection");
			AlaElnotaConverter s = new AlaElnotaConverter();
			File[] input = new File[1];
			input[0] = new File("D:\\VFE_VAS_SOURCE_CODE\\DataCollection\\PPAS-BONUSADJUST_2013_02_14.TXT");	
			
			s.convert(input, "Test");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
