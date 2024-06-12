package com.itworx.vaspp.datacollection.util.converters;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
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
import com.itworx.vaspp.datacollection.util.Utils;

public class EOCNConverter extends AbstractTextConverter{

	private static final String USSN_CONFIRM = "USSN Confirm";
	private static final String SRI_SM_RESPONSE = "SRI_SM Response.";
	private static final String REQUEST_CLOSER = "Close ussd dialogue";
	private static final String REQUEST_STARTER = "New Network Initiated Dialogue";
	private static final String MSG = "MSG:";
	private static final String COMMA = ",";
	private static final String MSISDN = "MSISDN:";
	private Logger logger;
	private Map<String, RequestResponse> numOfResReq = new HashMap<String, RequestResponse>();

	private String lastSecond = null;
	private long lastSecondRequestsCount = 0;
	private List<String> msisdn = new ArrayList<String>();
	//
	private Map<String, String> incomingRequests = new HashMap<String, String>();
	/*
	 * incomingRequestsForSuccessAvg map will be used to hold all the incoming requests to be used in calculating 
	 * the average response time for success requests
	 */
	private Map<String, String> incomingRequestsForSuccessAvg = new HashMap<String, String>();
	
	
	private List<Integer> differences = new ArrayList<Integer>();
	
	/*
	 * successRequestsTimedifferences , this list will hold all the time differences for all success requests 
	 * to be used in calculating the average response time for success requests 
	 */
	private List<Integer> successRequestsTimedifferences = new ArrayList<Integer>();
	
	private SimpleDateFormat inDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private SimpleDateFormat outDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	private SimpleDateFormat defaultFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	
	private DecimalFormat decimalFormat = new DecimalFormat("####0.00");

	public EOCNConverter() {
	}

	public File[] convert(File[] inputFiles, String systemName)
			throws ApplicationException, InputException {
		logger = Logger.getLogger(systemName);
		logger.debug("Inside EOCNConverter convert - started converting input files");

		String path = PropertyReader.getConvertedFilesPath();
		File[] outputFiles = new File[1];

		File outputFile = new File(path, inputFiles[0].getName());
	
		BufferedReader inputStream = null;
		BufferedWriter outputStream;
		try {
			outputStream = new BufferedWriter(new FileWriter(outputFile));
			String line;
			String dateByHour = "";
			String dateBySecond = "";
			DateDetails dateDetails;
			String prevHour = null;
			for (int i = 0; i < inputFiles.length; i++) {

				logger.debug("EOCNConverter.convert() - converting file "
						+ inputFiles[i].getName());
				logger.info("EOCNConverter.convert() - Start converting file "
						+ inputFiles[i].getName() + " @ " + new Date());
				System.out.println("EOCNConverter.convert() - Start converting file "
						+ inputFiles[i].getName() + " @ " + new Date());
				inputStream = new BufferedReader(new FileReader(inputFiles[i]));

				while (inputStream.ready()) {

					line = inputStream.readLine();
					if (line.contains(MSG)) {
						String[] lineParts = line.split(COMMA);
						String mobileNomber = "";
						if (lineParts != null && lineParts.length >= 3
								&& lineParts[2].contains(MSISDN)) {
							mobileNomber = lineParts[2].split(MSISDN)[1];
						} else
							continue;
						try {
							dateDetails = getDate(lineParts[0]);
							dateByHour = dateDetails.getDateByHour();
							dateBySecond = dateDetails.getDateBySecond();
							// System.out.println("the date is "+date);
						} catch (Exception exc) {
							logger.error(exc);
							continue;
						}

						if (lastSecond == null) {
							lastSecond = dateBySecond;
							lastSecondRequestsCount = 0;
						}
						
						if(prevHour == null)
							prevHour =dateByHour;

						if (!dateBySecond.equals(lastSecond)) {
							/*try {
								secondsWriter.write(lastSecond+" , "+lastSecondRequestsCount);
								secondsWriter.newLine();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}*/
							updateLastHourMaxRequests(dateBySecond, true);
							
							
						}
						/**
						 * calculate the average HLR response time for the previous hour
						 */
						//Added BY Islam - Computing HLR Avg//
						if (!prevHour.equals(dateByHour)) {
							if(!incomingRequests.isEmpty())
								updateReqRespWithAvgHlrResp(prevHour);
							if(!incomingRequestsForSuccessAvg.isEmpty())
								updateReqRespWithSuccessReqAvgRepsponseTime(prevHour);
						}
						
						
						prevHour =dateByHour;	
						
						//Added BY Islam - Computing HLR Avg//
						
						
						if (line.contains(REQUEST_STARTER)) { // the request
							incomingRequests.put(mobileNomber, lineParts[0]);
							incomingRequestsForSuccessAvg.put(mobileNomber, lineParts[0]);
							msisdn.add(mobileNomber);
							lastSecondRequestsCount++;
							
							if (numOfResReq.containsKey(dateByHour)) {
								RequestResponse reqResp = numOfResReq
										.get(dateByHour);
								long req = reqResp.getRequests();
								reqResp.setRequests(req + 1);
								// if(!dateBySecond.equals(lastSecond)){
								// if(reqResp.getMaxRequests()<lastSecondRequestsCount)
								// reqResp.setMaxRequests(lastSecondRequestsCount);
								// temp.put(lastSecond,new
								// Long(lastSecondRequestsCount));
								// lastSecond=dateBySecond;
								// lastSecondRequestsCount=0;
								// }
								//numOfResReq.remove(dateByHour);
								numOfResReq.put(dateByHour, reqResp);

							} else {
								RequestResponse reqResp = new RequestResponse();
								reqResp.setRequests(1);
								reqResp.setMaxRequests(1);
								numOfResReq.put(dateByHour, reqResp);
							}

						} else if (line.contains(REQUEST_CLOSER)) {// the responses
							if (msisdn.contains(mobileNomber)) {
								msisdn.remove(mobileNomber);
								if (numOfResReq.containsKey(dateByHour)) {
									RequestResponse reqResp = numOfResReq
											.get(dateByHour);
									long resp = reqResp.getResponses();
									reqResp.setResponses(resp + 1);
									//numOfResReq.remove(dateByHour);
									numOfResReq.put(dateByHour, reqResp);
								} else {
									RequestResponse reqResp = new RequestResponse();
									reqResp.setResponses(1);
									numOfResReq.put(dateByHour, reqResp);

								}
							}
						} else if (line.contains(SRI_SM_RESPONSE)) {
							if (numOfResReq.containsKey(dateByHour)) {
								RequestResponse reqResp = numOfResReq
										.get(dateByHour);
								long resp = reqResp.getHlrResponse();
								reqResp.setHlrResponse(resp + 1);
								//numOfResReq.remove(dateByHour);
								numOfResReq.put(dateByHour, reqResp);
							} else {
								RequestResponse reqResp = new RequestResponse();
								reqResp.setHlrResponse(1);
								numOfResReq.put(dateByHour, reqResp);
							}

							//Added BY Islam - Computing HLR Avg//
							if (incomingRequests.get(mobileNomber) != null) {
								Date dateFrom = new Date(), dateTo = new Date();
								dateFrom = convertToDate(incomingRequests.get(mobileNomber));
								dateTo = convertToDate(lineParts[0]);
								Integer diff = new Integer((int) Utils.getTimeDifferenceinMills(dateTo, dateFrom));
								
								differences.add(diff);
								incomingRequests.remove(mobileNomber);

							}
							//Added BY Islam - Computing HLR Avg//

						} else if (line.contains(USSN_CONFIRM)) {
							if (numOfResReq.containsKey(dateByHour)) {
								RequestResponse reqResp = numOfResReq
										.get(dateByHour);
								long resp = reqResp.getSuccessReq();
								reqResp.setSuccessReq(resp + 1);
								//numOfResReq.remove(dateByHour);
								numOfResReq.put(dateByHour, reqResp);
							} else {
								RequestResponse reqResp = new RequestResponse();
								reqResp.setSuccessReq(1);
								numOfResReq.put(dateByHour, reqResp);

							}
							
							//Added BY Elsayed - Computing Success Requests Avg Response Time //
							if (incomingRequestsForSuccessAvg.get(mobileNomber) != null) {
								Date dateFrom = new Date(), dateTo = new Date();
								dateFrom = convertToDate(incomingRequestsForSuccessAvg.get(mobileNomber));
								dateTo = convertToDate(lineParts[0]);
								Integer diff = new Integer((int) Utils.getTimeDifferenceinMills(dateTo, dateFrom));
								
								successRequestsTimedifferences.add(diff);
								incomingRequestsForSuccessAvg.remove(mobileNomber);

							}
							//Added BY Elsayed - Computing Success Requests Avg Response Time //
						}

					} else {
						continue;
					}

				}
				
				inputStream.close();

				updateLastHourMaxRequests(lastSecond, false);
				logger.info("EOCNConverter.convert() - finish converting file "
						+ inputFiles[i].getName() + " @ " + new Date());
				System.out.println("EOCNConverter.convert() - finish converting file "
						+ inputFiles[i].getName() + " @ " + new Date());
			}

			//Added BY Islam - Computing HLR Avg//
			if (!differences.isEmpty())
				updateReqRespWithAvgHlrResp(dateByHour);
			//Added BY Islam - Computing HLR Avg//
			
			if(!successRequestsTimedifferences.isEmpty())
				updateReqRespWithSuccessReqAvgRepsponseTime(dateByHour);
		

			Iterator it = numOfResReq.keySet().iterator();
			while (it.hasNext()) {
				Object key = it.next();
				RequestResponse reqResp = numOfResReq.get(key);
				double avgHlr = reqResp.getAvrgHlrresp();
				if(avgHlr < 0)
					avgHlr = 0;
				outputStream.write(key + COMMA + reqResp.getRequests() + COMMA
						+ reqResp.getResponses() + COMMA
						+ reqResp.getSuccessReq() + COMMA
						+ reqResp.getHlrResponse() + COMMA
						+ reqResp.getMaxRequests()+ COMMA
						+ decimalFormat.format(avgHlr)+COMMA
						+ decimalFormat.format(reqResp.getSuccesReqsAvgRespTime())/*+","
						+ reqResp.getNoOfSucessReqWithAvgRespTimeLT10()+","
						+ reqResp.getNoOfSucessReqWithAvgRespTimeBT10And20()+","
						+ reqResp.getNoOfSucessReqWithAvgRespTimeBT20And30()+","
						+ reqResp.getNoOfSucessReqWithAvgRespTimeBT30And60()+","
						+ reqResp.getNoOfSucessReqWithAvgRespTimeGT60()+","
						
						+ reqResp.getNoOfSucessReqWithAvgRespTimeLT5()+","
						+ reqResp.getNoOfSucessReqWithAvgRespTimeBT5And20()*/
					);
				System.out.println(key + COMMA + reqResp.getRequests() + COMMA
						+ reqResp.getResponses() + COMMA
						+ reqResp.getSuccessReq() + COMMA
						+ reqResp.getHlrResponse() + COMMA
						+ reqResp.getMaxRequests()+ COMMA
						+ decimalFormat.format(avgHlr)+COMMA
						+ decimalFormat.format(reqResp.getSuccesReqsAvgRespTime()));
				outputStream.newLine();
			}
			inputStream.close();
			//secondsWriter.close();
			outputStream.close();
			outputFiles[0] = outputFile;

			logger.debug("EOCNConverter.convert() - finished converting input files successfully ");

		} catch (FileNotFoundException e) {
			logger.error("EOCNConverter.convert() - Input file not found " + e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger.error("EOCNConverter.convert() - Couldn't read input file"
					+ e);
			throw new ApplicationException(e);
		}
		logger.debug("EOCNConverter.convert() - finished converting input files successfully ");
		return outputFiles;
	}

	//Added BY Islam - Computing HLR Avg//
	private void updateReqRespWithAvgHlrResp(String dateByHourKey) {
		
		double avgHLR = calculateAvg(differences);
		if (numOfResReq.containsKey(dateByHourKey)) {
			
			RequestResponse reqResp = numOfResReq.get(dateByHourKey);
			reqResp.setAvrgHlrresp(avgHLR);
			//numOfResReq.remove(dateByHourKey);
			numOfResReq.put(dateByHourKey, reqResp);
			
		}else{
			RequestResponse reqResp = new RequestResponse();
			reqResp.setAvrgHlrresp(avgHLR);
			numOfResReq.put(dateByHourKey, reqResp);
		}
		differences.clear();		
	}
	
	
	private void updateReqRespWithSuccessReqAvgRepsponseTime(String dateByHourKey) {
		if(successRequestsTimedifferences==null)
			return;
		double avgSuccessRespTime = calculateAvg(successRequestsTimedifferences);
		/*long reqLT5=0;
		long reqBT5And20=0;
		long reqBT20And30=0;
		long reqBT30And60=0;
		long reqGT60=0;
		
		long reqLT10=0;
		long reqBT10And20=0;
		for (Iterator iterator = successRequestsTimedifferences.iterator(); iterator.hasNext();) {
			Integer difference = (Integer) iterator.next();
			
			if(difference.intValue() < 10000){
				reqLT10++;
				if(difference.intValue() < 5000)
					reqLT5++;
			}
			if (difference.intValue() >= 5000 && difference.intValue() <= 20000)
				reqBT5And20++;
			if (difference.intValue() >= 10000 && difference.intValue() <= 20000)
				reqBT10And20++;
			else if (difference.intValue() > 20000 && difference.intValue() <= 30000)
				reqBT20And30++;
			else if (difference.intValue() > 30000 && difference.intValue() <= 60000)
				reqBT30And60++;
			else
				reqGT60++;
		}*/
		
		RequestResponse reqResp=null;
		if (numOfResReq.containsKey(dateByHourKey)) {
			reqResp = numOfResReq.get(dateByHourKey);
			numOfResReq.remove(dateByHourKey);
		}else{
			reqResp = new RequestResponse();
		}
		reqResp.setSuccesReqsAvgRespTime(Math.round(avgSuccessRespTime));
		/*reqResp.setNoOfSucessReqWithAvgRespTimeLT5(reqLT5);
		reqResp.setNoOfSucessReqWithAvgRespTimeBT5And20(reqBT5And20);
		reqResp.setNoOfSucessReqWithAvgRespTimeBT20And30(reqBT20And30);
		reqResp.setNoOfSucessReqWithAvgRespTimeBT30And60(reqBT30And60);
		reqResp.setNoOfSucessReqWithAvgRespTimeGT60(reqGT60);
		
		reqResp.setNoOfSucessReqWithAvgRespTimeLT10(reqLT10);
		reqResp.setNoOfSucessReqWithAvgRespTimeBT10And20(reqBT10And20);*/
		
		numOfResReq.put(dateByHourKey, reqResp);
		
		successRequestsTimedifferences.clear();		
	}

	private double calculateAvg(List<Integer> diferencesList ) {
		double sum = 0.0;
		if (diferencesList == null || diferencesList.size() == 0)
			return 0;
		for (int i = 0; i < diferencesList.size(); i++) {
			sum += diferencesList.get(i).intValue();
		}
		return sum / diferencesList.size();
	}
	//Added BY Islam - Computing HLR Avg//

	private DateDetails getDate(String line) throws ParseException {
		Date date = null;
		String dateString;
		date = inDateFormat.parse(line);
		dateString = outDateFormat.format(date);
		String[] dateParts = dateString.split(" ");
		String[] timeParts = dateParts[1].split(":");
		DateDetails dateDetails = new DateDetails();
		dateDetails.setDate(dateParts[0]);
		dateDetails.setHour(timeParts[0]);
		dateDetails.setMinute(timeParts[1]);
		dateDetails.setSecond(timeParts[2]);
		return dateDetails;
	}
	
	public Date convertToDate(String dateString) throws InputException {
		try {
			Date date = defaultFormat.parse(dateString);
			return date;
		} catch (ParseException e) {
			throw new InputException("Invalid date in input file ");
		}
	}

	private void updateLastHourMaxRequests(String currentDateBySecond,
			boolean reset) {
		if(lastSecond == null || lastSecond.length() == 0)
			return;
		String lastHour = lastSecond.substring(0, 13) + ":00:00";
		if (numOfResReq.containsKey(lastHour)) {
			RequestResponse reqResp = numOfResReq.get(lastHour);
			if (reqResp.getMaxRequests() < lastSecondRequestsCount)
				reqResp.setMaxRequests(lastSecondRequestsCount);
			//numOfResReq.remove(lastHour);
			numOfResReq.put(lastHour, reqResp);
			if (reset) {
				lastSecond = currentDateBySecond;
				lastSecondRequestsCount = 0;
			}
		}
		
	}

	public static void main(String ag[]) {

		try {

			// String path = "D:\\build\\pahse8\\DataCollection\\";
			//String path = "D:\\VNPP\\phase8\\DataCollection\\";
			String path = "E:\\Projects\\VFE_VAS_VNPP_2012_Phase3\\Trunk\\SourceCode\\DataCollection";
			PropertyReader.init(path);
			PropertyReader.getUssdGWSubCodes();
			EOCNConverter s = new EOCNConverter();
			File[] input = new File[1];
			/*input[0]= new File("D:\\VNPP\\phase8\\trace2010091309.log") ;
			input[1] = new File("D:\\VNPP\\phase8\\trace2010081119.log");
			input[2] = new File("D:\\VNPP\\phase8\\trace2010081816.log");
			input[3] = new File("D:\\VNPP\\phase8\\trace2010081817.log");
			input[4] = new File("D:\\VNPP\\phase8\\trace2010081818.log");
			input[5] = new File("D:\\VNPP\\phase8\\trace2010090718.log");
			input[6] = new File("D:\\VNPP\\phase8\\trace2010090818.log");
			 */
			input[0]= new File("E:\\Projects\\VFE_VAS_VNPP_2012_Phase3\\Trunk\\SourceCode\\Builds\\17-01-2013\\trace2013011613.log");
			
			
			
			s.convert(input, "Test");
			// DateDetails d = s.getDate("2010-08-18 16:00:02.050");
			// System.out.println(d.getDateBySecond());
			// String date = "08/18/2010 16:03:00";
			// String lastHour=date.substring(0,13)+":00:00";
			// System.out.println(lastHour);

		} catch (Exception e) {
			e.printStackTrace();
		}

		

	}

	class DateDetails {
		String date;
		String hour;
		String minute;
		String second;

		public String getDate() {
			return date;
		}

		public void setDate(String date) {
			this.date = date;
		}

		public String getHour() {
			return hour;
		}

		public void setHour(String hour) {
			this.hour = hour;
		}

		public String getMinute() {
			return minute;
		}

		public void setMinute(String minute) {
			this.minute = minute;
		}

		public String getSecond() {
			return second;
		}

		public void setSecond(String second) {
			this.second = second;
		}

		public String getDateByHour() {
			return date + " " + hour + ":00:00";
		}

		public String getDateBySecond() {
			return date + " " + hour + ":" + minute + ":" + second;
		}

	}

	class RequestResponse {
		public long requests = 0;
		public long responses = 0;
		public long successReq = 0;
		public long hlrResponse = 0;
		public long maxRequests = 0;
		public double avrgHlrresp = 0;
		public double succesReqsAvgRespTime=0;
		/*public long NoOfSucessReqWithAvgRespTimeLT5;
		public long NoOfSucessReqWithAvgRespTimeBT5And20;
		public long NoOfSucessReqWithAvgRespTimeBT20And30;
		public long NoOfSucessReqWithAvgRespTimeBT30And60;
		public long NoOfSucessReqWithAvgRespTimeGT60;
		
		public long NoOfSucessReqWithAvgRespTimeLT10;
		public long NoOfSucessReqWithAvgRespTimeBT10And20;*/
		
		public long getRequests() {
			return requests;
		}
		public void setRequests(long requests) {
			this.requests = requests;
		}
		public long getResponses() {
			return responses;
		}
		public void setResponses(long responses) {
			this.responses = responses;
		}
		public long getSuccessReq() {
			return successReq;
		}
		public void setSuccessReq(long successReq) {
			this.successReq = successReq;
		}
		public long getHlrResponse() {
			return hlrResponse;
		}
		public void setHlrResponse(long hlrResponse) {
			this.hlrResponse = hlrResponse;
		}
		public long getMaxRequests() {
			return maxRequests;
		}
		public void setMaxRequests(long maxRequests) {
			this.maxRequests = maxRequests;
		}
		public double getAvrgHlrresp() {
			return avrgHlrresp;
		}
		public void setAvrgHlrresp(double avrgHlrresp) {
			this.avrgHlrresp = avrgHlrresp;
		}
		public double getSuccesReqsAvgRespTime() {
			return succesReqsAvgRespTime;
		}
		public void setSuccesReqsAvgRespTime(double succesReqsAvgRespTime) {
			this.succesReqsAvgRespTime = succesReqsAvgRespTime;
		}
		/*public long getNoOfSucessReqWithAvgRespTimeLT5() {
			return NoOfSucessReqWithAvgRespTimeLT5;
		}
		public void setNoOfSucessReqWithAvgRespTimeLT5(
				long noOfSucessReqWithAvgRespTimeLT5) {
			NoOfSucessReqWithAvgRespTimeLT5 = noOfSucessReqWithAvgRespTimeLT5;
		}
		public long getNoOfSucessReqWithAvgRespTimeBT5And20() {
			return NoOfSucessReqWithAvgRespTimeBT5And20;
		}
		public void setNoOfSucessReqWithAvgRespTimeBT5And20(
				long noOfSucessReqWithAvgRespTimeBT5And20) {
			NoOfSucessReqWithAvgRespTimeBT5And20 = noOfSucessReqWithAvgRespTimeBT5And20;
		}
		public long getNoOfSucessReqWithAvgRespTimeBT20And30() {
			return NoOfSucessReqWithAvgRespTimeBT20And30;
		}
		public void setNoOfSucessReqWithAvgRespTimeBT20And30(
				long noOfSucessReqWithAvgRespTimeBT20And30) {
			NoOfSucessReqWithAvgRespTimeBT20And30 = noOfSucessReqWithAvgRespTimeBT20And30;
		}
		public long getNoOfSucessReqWithAvgRespTimeBT30And60() {
			return NoOfSucessReqWithAvgRespTimeBT30And60;
		}
		public void setNoOfSucessReqWithAvgRespTimeBT30And60(
				long noOfSucessReqWithAvgRespTimeBT30And60) {
			NoOfSucessReqWithAvgRespTimeBT30And60 = noOfSucessReqWithAvgRespTimeBT30And60;
		}
		public long getNoOfSucessReqWithAvgRespTimeGT60() {
			return NoOfSucessReqWithAvgRespTimeGT60;
		}
		public void setNoOfSucessReqWithAvgRespTimeGT60(
				long noOfSucessReqWithAvgRespTimeGT60) {
			NoOfSucessReqWithAvgRespTimeGT60 = noOfSucessReqWithAvgRespTimeGT60;
		}


	public long getNoOfSucessReqWithAvgRespTimeLT10() {
			return NoOfSucessReqWithAvgRespTimeLT10;
		}
		public void setNoOfSucessReqWithAvgRespTimeLT10(
				long noOfSucessReqWithAvgRespTimeLT10) {
			NoOfSucessReqWithAvgRespTimeLT10 = noOfSucessReqWithAvgRespTimeLT10;
		}
		public long getNoOfSucessReqWithAvgRespTimeBT10And20() {
			return NoOfSucessReqWithAvgRespTimeBT10And20;
		}
		public void setNoOfSucessReqWithAvgRespTimeBT10And20(
				long noOfSucessReqWithAvgRespTimeBT10And20) {
			NoOfSucessReqWithAvgRespTimeBT10And20 = noOfSucessReqWithAvgRespTimeBT10And20;
		}
		*/
		
		
	}

}
