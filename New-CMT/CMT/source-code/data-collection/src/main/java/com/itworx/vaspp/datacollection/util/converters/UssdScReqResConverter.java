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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.persistenceobjects.UssdShortCodesReqResData;
import com.itworx.vaspp.datacollection.util.PropertyReader;
import com.itworx.vaspp.datacollection.util.Utils;

public class UssdScReqResConverter extends AbstractTextConverter {

    private Logger logger;
    private Map<String, UssdShortCodesReqResData> dateVsCount = new HashMap<String, UssdShortCodesReqResData>();
    private HashMap hoursMap = new HashMap();

    public UssdScReqResConverter() {
    }

    /**
     * Checks if a line is new or not
     *
     * @param - the line to be checked
     * @return boolean - whether or not it is new
     */
    /*private boolean CheckNewLine(String lineDateString){
        return lineDateString.matches("\\d\\d\\d\\d-\\d\\d-\\d\\d\\s\\d\\d:\\d\\d:\\d\\d.\\d\\d\\d");
	}*/
    public static void main(String ag[]) {
        try {

            PropertyReader.init("F:\\Work\\VFE_SVN_PROD\\etc\\prodConfFiles\\zone2\\CMTDataCollection\\");
            UssdScReqResConverter s = new UssdScReqResConverter();
            File[] input = new File("F:\\Work\\VFE\\production\\20140310\\USSDShort_2\\files_20140309\\USSD_2").listFiles();

            s.convert(input, "ussd_sc");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * extract data.
     *
     * @param inputStream -
     *            the input file
     * @param lines -
     *            the arrays of lines to concatenate data
     *
     * @exception InputException
     *                if format of date string was invalid
     * @exception IOException
     *                if error occured while reading file
     */
    /*private void countRequestsResponses(String line, UssdShortCodesReqResData fileData) {
        String responseRegex="^.+\\[op:32[;\\]].*$";
		if (line.contains("Request:")&&line.contains("[op:1]")) {
			fileData.setNoOfRequests(fileData.getNoOfRequests()+1);
		//}else if (line.endsWith("[op:32]")) {
		}else if (line.matches(responseRegex)) {
			fileData.setNoOfResponses(fileData.getNoOfResponses()+1);
		}else{
			//System.out.println("something else:"+line);
		}
	}*/

    /**
     * Converting the input file to comma seperated file.
     *
     * @param inputFiles -
     *                   array of the input files to be converted
     * @param systemName -
     *                   name of targeted system for logging
     * @throws ApplicationException if input file couldn't be found if input file couldn't be
     *                              opened
     * @throws InputException       if ParseException occured
     */
    public File[] convert(File[] inputFiles, String systemName)
            throws ApplicationException, InputException {
        logger = Logger.getLogger(systemName);
        logger
                .debug("Inside UssdScReqResConverter convert - started converting input files");

        String path = PropertyReader.getConvertedFilesPath();
        File[] outputFiles = new File[1];
        File output = new File(path, inputFiles[0].getName());

        BufferedReader inputStream = null;
        BufferedWriter outputStream;
        try {
            outputStream = new BufferedWriter(new FileWriter(output));
            for (int i = 0; i < inputFiles.length; i++) {
                logger
                        .debug("UssdScReqResConverter.convert() - converting file "
                                + inputFiles[i].getName());

                inputStream = new BufferedReader(new FileReader(inputFiles[i]));

                String line = "";
                String date = "";
                String shourtCode = "";
                String key = "";
                String responseRegex = "^.+\\[op:32[;\\]].*$";
                while (inputStream.ready()) {
                    line = inputStream.readLine();

                    if (line.equals("")) {
                        continue;
                    } else {
                        if (line.contains(" ")) {
                            try {
                                if (line.split(" ").length < 2) {
                                    logger.error("Line length less than 2 and not contain date");
                                    continue;
                                }
                                date = getDate(line.split(" ")[0] + " " + line.split(" ")[1]);
                                if (line.contains("-"))
                                    shourtCode = line.split("-")[3];
                                key = date + "," + shourtCode;


                                if (line.contains("Request:") && line.contains("[op:1]")) {
                                    UssdShortCodesReqResData obj;

                                    if (dateVsCount.containsKey(key)) {
                                        obj = dateVsCount.get(key);
                                        obj.setNoOfRequests(obj.getNoOfRequests() + 1);
                                        obj.setSubCode(getSubCode(line));

                                        dateVsCount.remove(obj);
                                        dateVsCount.put(key, obj);

                                    } else {
                                        obj = new UssdShortCodesReqResData();
                                        obj.setNoOfRequests(1);
                                        obj.setSubCode(getSubCode(line));

                                        dateVsCount.put(key, obj);
                                    }


                                }
                                //else if (line.matches(responseRegex))
                                else if (line.contains("Response:") && line.matches(responseRegex))

                                {

                                    if (dateVsCount.containsKey(key)) {
                                        UssdShortCodesReqResData obj = dateVsCount.get(key);
                                        obj.setNoOfResponses(obj.getNoOfResponses() + 1);
                                        dateVsCount.remove(obj);
                                        dateVsCount.put(key, obj);

                                    } else {
                                        UssdShortCodesReqResData obj = new UssdShortCodesReqResData();
                                        obj.setNoOfResponses(1);
                                        dateVsCount.put(key, obj);
                                    }
                                } else if (line.contains("Response:") && (!line.matches(responseRegex))) {
                                    //String responseLine=line;
                                    String responsekey = date + "," + shourtCode;
                                    line = inputStream.readLine();
                                    while (inputStream.ready() && (line.contains("Request:") || (!line.matches(responseRegex)))) {

                                        if (line.contains("Request:")) {

                                            date = getDate(line.split(" ")[0] + " " + line.split(" ")[1]);
                                            if (line.contains("-"))
                                                shourtCode = line.split("-")[3];
                                            key = date + "," + shourtCode;
                                            if (dateVsCount.containsKey(key)) {
                                                UssdShortCodesReqResData obj = dateVsCount.get(key);
                                                obj.setNoOfRequests(obj.getNoOfRequests() + 1);
                                                dateVsCount.remove(obj);
                                                dateVsCount.put(key, obj);

                                            } else {
                                                UssdShortCodesReqResData obj = new UssdShortCodesReqResData();
                                                obj.setNoOfRequests(1);
                                                dateVsCount.put(key, obj);
                                            }
                                        }
                                        line = inputStream.readLine();
                                    }


                                    if ((line != null) && line.matches(responseRegex)) {
                                        if (dateVsCount.containsKey(responsekey)) {
                                            UssdShortCodesReqResData obj = dateVsCount.get(responsekey);
                                            obj.setNoOfResponses(obj.getNoOfResponses() + 1);
                                            dateVsCount.remove(obj);
                                            dateVsCount.put(responsekey, obj);

                                        } else {
                                            UssdShortCodesReqResData obj = new UssdShortCodesReqResData();
                                            obj.setNoOfResponses(1);
                                            dateVsCount.put(responsekey, obj);
                                        }
                                    }
                                }

                            } catch (ParseException exc) {
                                logger.error(exc);
                                continue;
                            }
                        }

                    }
                }

				/*while (inputStream.ready()) {
                    line = inputStream.readLine();

					if (line.equals("")) {
						continue;
					}else{
						String lineDateString = "";
						if(line.length() > 23){
							//  23 bits because the date in that format: "2007-12-09 11:00:00.403"
							lineDateString = line.substring(0,23);
						}else{
							lineDateString = line;
						}
						String[] allParts = line.split("-");
						fileData.setShortCode(allParts[3].trim());
						if(this.CheckNewLine(lineDateString)){

							dateHourString = lineDateString.substring(0,13).trim();
							if(firstTime || (!firstTime && !dateHourString.equals(lastDateHourString)) ){
								if(!firstTime && !dateHourString.equals(lastDateHourString)){

									// Write the previous record data
									this.countRequestsResponses(previousLine, fileData);
									outputLine = Utils.convertToDateString(fileData.getDateTime(), Utils.defaultFormat)+","+fileData.getNoOfRequests()+","+fileData.getNoOfResponses()+","+fileData.getShortCode();
									//System.out.println(outputLine);
									outputStream.write(outputLine);
									outputStream.newLine();
								}

								// Inialize new record data
								fileData = new UssdShortCodesReqResData();
								fileData.setDateTime(Utils.convertToDate(dateHourString, "yyyy-MM-dd HH"));
								String[] tokens = line.split("-");
								fileData.setShortCode(tokens[3].trim());

								// Initialize the checking parameters
								firstTime = false;
								lastDateHourString = dateHourString;
							}else{
								this.countRequestsResponses(previousLine, fileData);
							}
							previousLine = line;


						}else{
							previousLine += line;
						}

					}

				}*/
                inputStream.close();

            }
            Iterator it = dateVsCount.keySet().iterator();

            while (it.hasNext()) {
                Object key = it.next();
                String date = key.toString().split(",")[0];

                String shortCode = key.toString().split(",")[1];
                UssdShortCodesReqResData obj = dateVsCount.get(key);
                outputStream.write(date + "," + obj.getNoOfRequests() + "," + obj.getNoOfResponses() + "," + shortCode + ","
                        + ((obj.getSubCode() != null && !obj.getSubCode().trim().equals("")) ? obj.getSubCode() : "null") + ",");
                System.out.println(date + "," + obj.getNoOfRequests() + "," + obj.getNoOfResponses() + "," + shortCode + ","
                        + ((obj.getSubCode() != null && !obj.getSubCode().trim().equals("")) ? obj.getSubCode() : "null") + ",");
                outputStream.newLine();

            }

            outputStream.flush();
            outputStream.close();
            outputFiles[0] = output;

            logger.debug("UssdScReqResConverter.convert() - "
                    + inputFiles[0].getName() + " converted");

        } catch (FileNotFoundException e) {
            logger.error("UssdScReqResConverter.convert() - Input file not found " + e);
            throw new ApplicationException(e);
        } catch (IOException e) {
            logger.error("UssdScReqResConverter.convert() - Couldn't read input file"
                    + e);
            throw new ApplicationException(e);
        }
        logger.debug("UssdScReqResConverter.convert() - finished converting input files successfully ");
        return outputFiles;
    }

    private String getDate(String line) throws ParseException {
        String[] tokens = null;
        Date date = new Date();
        String dateString;
        SimpleDateFormat inDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss.SSS");
        /**
         * Updated by Alia.Adel on 2014.01.09
         * to collect sub codes
         */
        SimpleDateFormat outDateFormat = new SimpleDateFormat(
                "MM/dd/yyyy HH:mm:ss");


        date = inDateFormat.parse(line);
        dateString = outDateFormat.format(date);
        return dateString;

    }

    /**
     * Added to get the subCode of the request
     *
     * @param line
     * @return
     * @author Alia.Adel
     */
    private String getSubCode(String line) {

        String subCode = Utils.stringBetween(line, "Request: ", "# [op:1]");
        if (StringUtils.isEmpty(subCode)) {
            subCode = "";
        }
        return subCode;
    }


}
