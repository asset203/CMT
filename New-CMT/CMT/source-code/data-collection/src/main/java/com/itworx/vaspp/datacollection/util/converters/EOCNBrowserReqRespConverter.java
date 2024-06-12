package com.itworx.vaspp.datacollection.util.converters;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;
import com.itworx.vaspp.datacollection.util.Utils;
import org.apache.log4j.Logger;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class EOCNBrowserReqRespConverter extends AbstractTextConverter {

	private Logger logger;
	private Map<String, RequestResponse> reqResp = new HashMap<String, RequestResponse>();
	private long requests = 0;
	private long responses = 0;
	
	public EOCNBrowserReqRespConverter() {
	}

	public File[] convert(File[] inputFiles, String systemName)
			throws ApplicationException, InputException {

		this.logger = Logger.getLogger(systemName);
		this.logger.debug("Inside EOCNBrowserReqRespConverter convert - started converting input files");

		String path = PropertyReader.getConvertedFilesPath();
		File[] outputFiles = new File[1];
		File outputFile = new File(path, inputFiles[0].getName());

		BufferedReader inputStream = null;
		try {
            BufferedWriter outputStream = new BufferedWriter(new FileWriter(outputFile));
			String dateByHour = null;
			String prevHour = "";
			String shortCode = "";
			for (int i = 0; i < inputFiles.length; i++) {

				this.logger.debug("EOCNBrowserReqRespConverter.convert() - converting file "
						+ inputFiles[i].getName());
				System.out
						.println("EOCNBrowserReqRespConverter.convert() - converting file "
								+ inputFiles[i].getName());

                inputStream = Utils.getGZIPAwareBufferdReader(inputFiles[i]);
				//browser_511_2013011610.log
                /**
                 * Commented by Alia.Adel on 2014.04.13
                 */
				/*String[] fileNameArray = inputFiles[i].getName().split("_");
				shortCode = fileNameArray[1];*/
                String[] fileNameArray = inputFiles[i].getName().split("_");
                shortCode = fileNameArray[1];

				while (inputStream.ready()) {
                    String line = inputStream.readLine();
                    String[] lineParts = (String[])null;

					if (line.contains("[")) {
						lineParts = line.split(" \\[");

						try {
							dateByHour = getDate(lineParts[0]);
						} catch (Exception exc) {
							//logger.error(exc);
							continue;
						}

						// If a new hour came //
						if (!prevHour.equals(dateByHour)) {
							addNewHourDetails(prevHour,shortCode);
						}
						prevHour = dateByHour;
						// If a new hour came //

						if (line.contains("Request"))// the request
						{
                            this.requests += 1;
						} else if (line.contains("Response"))// the response
						{
                            this.responses += 1;
						}
					}
				}
				inputStream.close();
				if(inputFiles.length > i){
					addNewHourDetails(dateByHour,shortCode);
                }

			}

			addNewHourDetails(dateByHour,shortCode);

			Iterator it = this.reqResp.keySet().iterator();
			while (it.hasNext()) {
				Object key = it.next();
				RequestResponse reqRespPerHour = (RequestResponse)this.reqResp.get(key);
				outputStream.write(key  + "," + reqRespPerHour.getRequests()
						+ "," + reqRespPerHour.getResponses());
				System.out.println(key +  "," + reqRespPerHour.getRequests()
						+ "," + reqRespPerHour.getResponses());
				outputStream.newLine();
			}
			inputStream.close();
			outputStream.close();
			outputFiles[0] = outputFile;

			logger.debug("EOCNConverter.convert() - finished converting input files successfully ");

		} catch (FileNotFoundException e) {
			logger.error("EOCNConverter.convert() - Input file not found ", e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger.error("EOCNConverter.convert() - Couldn't read input file", e);
			throw new ApplicationException(e);
		}

		logger.debug("EOCNConverter.convert() - finished converting input files successfully ");
		return outputFiles;
	}

	private void addNewHourDetails(String dateByHour, String shortCode) {
		String key = dateByHour+","+shortCode;
		if (this.reqResp.containsKey(key))// exist hour
		{
			RequestResponse oldReqResp = (RequestResponse)this.reqResp.get(key);
			oldReqResp.setRequests(this.requests + oldReqResp.getRequests());
			oldReqResp.setResponses(this.responses + oldReqResp.getResponses());
			this.reqResp.remove(key);
			this.reqResp.put(key, oldReqResp);
		} else if (!dateByHour.equals("")) {
			RequestResponse newReqResp = new RequestResponse();
			newReqResp.setRequests(this.requests);
			newReqResp.setResponses(this.responses);
			this.reqResp.put(key, newReqResp);
		}
		requests = 0;
		responses = 0;
	}

	public static void main(String ag[]) {

		try {
			String path = "E:\\Projects\\VFE_VAS_VNPP_2012_Phase3\\Trunk\\SourceCode\\DataCollection\\";
			PropertyReader.init(path);
			PropertyReader.getUssdGWSubCodes();
			EOCNBrowserReqRespConverter s = new EOCNBrowserReqRespConverter();
			File[] input = new File[1];

			input[0] = new File(
					"E:\\Projects\\VFE_VAS_VNPP_2012_Phase3\\Trunk\\SourceCode\\Builds\\04-12-2012\\EOCN_BROWSERS_1354614024933_1_(511)_browser_511_2012120508.log");
		//	input[1] = new File(
		//			"E:\\Projects\\VFE_VAS_VNPP_2012_Phase3\\Trunk\\SourceCode\\Builds\\04-12-2012\\EOCN_BROWSERS_1354614024933_1_(512)_browser_512_2012120508.log");
			
			

			s.convert(input, "Test");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private String getDate(String line) throws ParseException {
		Date date = new Date();
		String dateString;
		SimpleDateFormat inDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss.SSS");
		SimpleDateFormat outDateFormat = new SimpleDateFormat(
				"MM/dd/yyyy");

		date = inDateFormat.parse(line);
		dateString = outDateFormat.format(date);
		return dateString;

	}

	class RequestResponse {
		public long requests = 0;
		public long responses = 0;
		public String shortCode;
		
		

		public String getShortCode() {
			return this.shortCode;
		}

		public void setShortCode(String shortCode) {
			this.shortCode = shortCode;
		}

		public long getRequests() {
			return this.requests;
		}

		public void setRequests(long requests) {
			this.requests = requests;
		}

		public long getResponses() {
			return this.responses;
		}

		public void setResponses(long responses) {
			this.responses = responses;
		}
	}
}
