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
import com.itworx.vaspp.datacollection.util.PersistenceManager;
import com.itworx.vaspp.datacollection.util.PropertyReader;
import com.itworx.vaspp.datacollection.util.SMSLGPInsertIntoDataBase;

/**
 * @author suzan.tadrous
 * 
 */
public class SMSLgpDetailsConverter extends AbstractTextConverter {
	public File[] convert(File[] inputFiles, String systemName)
			throws ApplicationException, InputException {
		Logger logger = Logger.getLogger(systemName);
		
		Map<String, Long> dateVSKeyCount = new HashMap<String, Long>();
		logger.debug("Inside SMSLgpDetailsConverter convert - started converting input files");
		String path = PropertyReader.getConvertedFilesPath();
		File[] outputFiles = new File[1];
		File outputFile = new File(path, inputFiles[0].getName() + "_"+systemName+"_details_" + new Date().getTime());

		BufferedReader inputStream = null;
		BufferedWriter outputStream = null;
		Date executionDate = null; 
		String sampleInvalidLines = "";
		int invalidLinesCount = 0;
		long linesCount = 0;
		try {
			outputStream = new BufferedWriter(new FileWriter(outputFile));
			String nodeName= " ";
			for (int i = 0; i < inputFiles.length; i++) {
				logger.debug("SMSLgpDetailsConverter.convert() - converting file " + inputFiles[i].getName());

				inputStream = new BufferedReader(new FileReader(inputFiles[i]));
				
				if(i == 0){
					String fileName = inputFiles[i].getName();
					if(fileName.contains("_")){
						String[] fileNameParts = fileName.split("_");
						if(fileNameParts.length > 1){
							executionDate = getDateFromString(fileNameParts[1],logger);
						}
					}
				}
				
				String line;
				String date = "";
				String key = "";
				String header = "";
				String rejCause = "";
				String subResult = "";
				String origCountry = "";
				String origNet = "";
				String appName = "";
				String appSC = "";
				String termCountry = "";
				String termNw = "";
				String sriAction = "";
				String sriQueryResult = "";
				String deliveryResult = "";
				String termMSISDN = "";
				String termMSC = "";
				String columns[] = null;
				
				String currentDate = "";
				try {
					try{
						nodeName = inputFiles[i].getParentFile().getName();
					}catch (Exception e) {
						logger.error("SMSLgpDetailsConverter.convert() - error while getting parent folder name" , e);
					}
					
					
					while ((line = inputStream.readLine()) != null) {
						if (!(line.contains(","))){
							continue;
						}
						if ((columns = line.split(",")).length < 300) {
							if(invalidLinesCount++ <= 15){
								sampleInvalidLines += "|"+line;
							}
							continue;
						}
						try {
							date = getDate(columns[0]);
							if(!date.equals(currentDate) && !"".equals(currentDate)){
								writeMapAndClear(dateVSKeyCount, outputStream);
							}
							header = columns[3] != null ? columns[3] : "";
							rejCause = columns[6] != null ? columns[6] : "";
							subResult = columns[16] != null ? columns[16] : "";
							origCountry = columns[62] != null ? columns[62]
									: "";
							origNet = columns[63] != null ? columns[63] : "";
							appName = columns[186] != null ? columns[186] : "";
							appSC = columns[189] != null ? columns[189] : "";
							if (header
									.equalsIgnoreCase("trustedMoFwdSmWithCountryAndNetworkInfo")
									|| header
											.equalsIgnoreCase("suspectMoFwdSmWithCountryAndNetworkInfo")) {
								termCountry = columns[95] != null ? columns[95]
										: "";
							} else {
								termCountry = columns[196] != null ? columns[196]
										: "";
							}
							if (termCountry.equalsIgnoreCase("eg")) {
								if (header
										.equalsIgnoreCase("trustedMoFwdSmWithCountryAndNetworkInfo")
										|| header
												.equalsIgnoreCase("suspectMoFwdSmWithCountryAndNetworkInfo")) {
									termNw = columns[96] != null ? columns[96]
											: "";
								} else {
									termNw = columns[197] != null ? columns[197]
											: "";
								}
							} else {
								termNw = "";
							}
							if (termNw.equalsIgnoreCase("EG-Vodafone")) {
								termMSC = columns[288] != null ? columns[288]
										: "";
								if (header
										.equalsIgnoreCase("trustedMoFwdSmWithCountryAndNetworkInfo")
										|| header
												.equalsIgnoreCase("suspectMoFwdSmWithCountryAndNetworkInfo")) {
									termMSISDN = columns[99] != null ? columns[99]
											: "";

								} else {
									termMSISDN = columns[200] != null ? columns[200]
											: "";
								}
								if (termMSISDN.length() >= 7)
									termMSISDN = termMSISDN.substring(0, 7);
							} else {
								termMSISDN = "";
								termMSC = "";
							}
							sriAction = columns[275] != null ? columns[275]
									: "";
							sriQueryResult = columns[279] != null ? columns[279]
									: "";
							deliveryResult = columns[299] != null ? columns[299]
									: "";
							key = date + "," + header + "," + rejCause + ","
									+ subResult + "," + origCountry + ","
									+ origNet + "," + appName + "," + appSC
									+ "," + termCountry + "," + termNw + ","
									+ termMSISDN + "," + sriAction + ","
									+ sriQueryResult + "," + termMSC + ","
									+ deliveryResult;
							if (dateVSKeyCount.containsKey(key)) {
								long count = dateVSKeyCount.get(key);
								dateVSKeyCount.put(key, count + 1);
							} else {
								dateVSKeyCount.put(key, new Long(1));
							}
						} catch (ParseException exc) {
							logger.error("SMSLgpDetailsConverter.convert() - parsing error while parsing line ("+line+")",exc);
							continue;
						} catch (Exception e) {
							logger.error("SMSLgpDetailsConverter.convert() - error while parsing line ("+line+")",e);
						}
						currentDate = date;
						

					}
				} catch (Exception e) {
					logger.error("SMSLgpDetailsConverter.convert() - error happned while reading file",e);
				} finally {
					inputStream.close();
				}

			}
			logger.error("SMSLgpDetailsConverter.convert() - finished with numbers of lines ("+linesCount+") and invalid number of lines ("+invalidLinesCount+")");
			logger.error("SMSLgpDetailsConverter.convert() - invalid number of lines :"+sampleInvalidLines+")");
			writeMapAndClear(dateVSKeyCount, outputStream);
	
			outputFiles[0] = outputFile;
			insertIntoDatabase(outputFiles,systemName,nodeName,executionDate);
			
			logger.debug("SMSLgpDetailsConverter.convert() - finished converting input files successfully ");

		} catch (FileNotFoundException e) {
			logger
					.error("SMSLgpDetailsConverter.convert() - Input file not found "
							, e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger
					.error("SMSLgpDetailsConverter.convert() - Couldn't read input file"
							, e);
			throw new ApplicationException(e);
		} finally {
			try {
				if (outputStream != null) {
					outputStream.close();
				}
			} catch (IOException e) {
				logger
						.error("SMSLgpDetailsConverter.convert() - Couldn't read input file"
								, e);
				throw new ApplicationException(e);
			}
		}
		logger.debug("SMSLgpDetailsConverter.convert() - finished converting input files successfully ");
		return new File[0];

	}

	private Date getDateFromString(String str,Logger logger) {
		Date d = null;
		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
		try {
			d = fmt.parse(str);
		} catch (Exception e) {
			logger.error("failed to parse string ("+str+") as date",e);
		}
		return d;
	}

	private void insertIntoDatabase(File[] input,String systemName, String nodeName,Date executionDate) throws ApplicationException, InputException{
		PersistenceManager persistenceManager = new PersistenceManager(true);
		
		SMSLGPInsertIntoDataBase s = new SMSLGPInsertIntoDataBase(persistenceManager,systemName);
		int batchSize = 1000;
		s.insertIntoDatabase(input, nodeName,executionDate,batchSize);
	}
	
	private String getDate(String dateLine) throws ParseException {
		Date date = new Date();
		String dateString;
		SimpleDateFormat inDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat outDateFormat = new SimpleDateFormat(
				"MM/dd/yyyy HH:00:00");

		date = inDateFormat.parse(dateLine);
		dateString = outDateFormat.format(date);
		return dateString;

	}
	
	private void writeMapAndClear(Map<String, Long> map,
			BufferedWriter outputStream) throws IOException {
		for (Entry<String, Long> counterEntry : map.entrySet()) {
			Object key = counterEntry.getKey();
			outputStream.write(key + "," + counterEntry.getValue());
			outputStream.newLine();
		}
		map.clear();
	}

	public static void main(String ag[]) {
		try {

			PropertyReader.init("D:\\build\\phase11\\DataCollection");
			SMSLgpDetailsConverter s = new SMSLgpDetailsConverter();
			File[] input = new File[1];
			input[0] = new File("D:\\OUT_20111129_VASQ");
			s.convert(input, "Maha_Test");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
