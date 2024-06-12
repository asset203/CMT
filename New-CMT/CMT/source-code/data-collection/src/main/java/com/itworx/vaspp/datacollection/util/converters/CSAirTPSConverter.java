package com.itworx.vaspp.datacollection.util.converters;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;
import com.itworx.vaspp.datacollection.util.Utils;

public class CSAirTPSConverter  extends AbstractTextConverter {
	public static String[] COULUMNS = new String[]{
		"/Air:GetFaFList_3.0:In",
		"/Air:UpdateFaFList_3.0:In",
		"/Air:GetAllowedServiceClasses_3.0:In",
		"/Air:UpdateServiceClass_3.0:In",
		"/Air:GetAccumulators_3.0:In",
		"/Air:GetBalanceAndDate_3.0:In",
		"/Air:UpdateAccountDetails_3.0:In",
		"/Air:UpdateCommunityList_3.0:In",
		"/Air:UpdateAccumulators_3.0:In",
		"/Air:UpdateSubscriberSegmentation_3.0:In",
		"/Air:GetAccountDetails_3.0:In",
		"/Air:UpdateBalanceAndDate_3.0:In",
		"/Air:UpdateTemporaryBlocked_3.0:In",
		"/Air:UpdateRefillBarring_3.0:In",
		"/Air:UpdatePromotionPlan_3.0:In",
		"/Air:GetPromotionPlans_3.0:In",
		"/Air:GetPromotionCounters_3.0:In",
		"/Air:UpdatePromotionCounters_3.0:In",
		"/Air:InstallSubscriber_3.0:In",
		"/Air:LinkSubordinateSubscriber_3.0:In",
		"/Air:DeleteSubscriber_3.0:In",
		"/Air:Refill_3.0:In",
		"/Air:GetRefillOptions_3.0:In",
		"/Air:GetFaFList_3.1:In",
		"/Air:UpdateFaFList_3.1:In",
		"/Air:GetAllowedServiceClasses_3.1:In",
		"/Air:UpdateServiceClass_3.1:In",
		"/Air:GetAccumulators_3.1:In",
		"/Air:GetBalanceAndDate_3.1:In",
		"/Air:UpdateAccountDetails_3.1:In",
		"/Air:UpdateCommunityList_3.1:In",
		"/Air:UpdateAccumulators_3.1:In",
		"/Air:UpdateSubscriberSegmentation_3.1:In",
		"/Air:GetAccountDetails_3.1:In",
		"/Air:UpdateBalanceAndDate_3.1:In",
		"/Air:UpdateTemporaryBlocked_3.1:In",
		"/Air:UpdateRefillBarring_3.1:In",
		"/Air:UpdatePromotionPlan_3.1:In",
		"/Air:GetPromotionPlans_3.1:In",
		"/Air:GetPromotionCounters_3.1:In",
		"/Air:UpdatePromotionCounters_3.1:In",
		"/Air:InstallSubscriber_3.1:In",
		"/Air:LinkSubordinateSubscriber_3.1:In",
		"/Air:DeleteSubscriber_3.1:In",
		"/Air:Refill_3.1:In",
		"/Air:GetRefillOptions_3.1:In",
		"/Air:ValueVoucherRefillARequest_2.0:In",
		"/Air:StandardVoucherRefillARequest_2.0:In",
		"/Air:RefillARequest_2.0:In",
		"/Air:RefillTRequest_2.0:In",
		"/Air:StandardVoucherRefillTRequest_2.0:In",
		"/Air:ValueVoucherRefillTRequest_2.0:In",
		"/Air:ValueVoucherRefillARequest_2.1:In",
		"/Air:StandardVoucherRefillARequest_2.1:In",
		"/Air:RefillARequest_2.1:In",
		"/Air:RefillTRequest_2.1:In",
		"/Air:StandardVoucherRefillTRequest_2.1:In",
		"/Air:ValueVoucherRefillTRequest_2.1:In",
		"/Air:GetVoucherRefillOptionsTRequest_2.1:In",
		"/Air:ValueVoucherRefillARequest_2.2:In",
		"/Air:StandardVoucherRefillARequest_2.2:In",
		"/Air:RefillARequest_2.2:In",
		"/Air:RefillTRequest_2.2:In",
		"/Air:StandardVoucherRefillTRequest_2.2:In",
		"/Air:ValueVoucherRefillTRequest_2.2:In",
		"/Air:GetVoucherRefillOptionsTRequest_2.2:In",
		"/Air:ValueVoucherRefillARequest_2.3:In",
		"/Air:StandardVoucherRefillARequest_2.3:In",
		"/Air:RefillARequest_2.3:In",
		"/Air:RefillTRequest_2.3:In",
		"/Air:StandardVoucherRefillTRequest_2.3:In",
		"/Air:ValueVoucherRefillTRequest_2.3:In",
		"/Air:GetVoucherRefillOptionsTRequest_2.3:In",
		"/Air:SuperRefillT_2.3:In",
		"/Air:GetAccountDetailsTRequest_2.0:In",
		"/Air:BalanceEnquiryTRequest_2.0:In",
		"/Air:ValueVoucherEnquiryTRequest_2.0:In",
		"/Air:AccumulatorEnquiryTRequest_2.0:In",
		"/Air:UpdateAccountDetailsTRequest_2.0:In",
		"/Air:GetAccountDetailsTRequest_2.1:In",
		"/Air:UpdateAccountDetailsTRequest_2.1:In",
		"/Air:BalanceEnquiryTRequest_2.1:In",
		"/Air:ValueVoucherEnquiryTRequest_2.1:In",
		"/Air:AccumulatorEnquiryTRequest_2.1:In",
		"/Air:GetAccountDetailsTRequest_2.2:In",
		"/Air:UpdateAccountDetailsTRequest_2.2:In",
		"/Air:BalanceEnquiryTRequest_2.2:In",
		"/Air:ValueVoucherEnquiryTRequest_2.2:In",
		"/Air:AccumulatorEnquiryTRequest_2.2:In",
		"/Air:GetAccountDetailsTRequest_2.3:In",
		"/Air:UpdateAccountDetailsTRequest_2.3:In",
		"/Air:BalanceEnquiryTRequest_2.3:In",
		"/Air:ValueVoucherEnquiryTRequest_2.3:In",
		"/Air:AccumulatorEnquiryTRequest_2.3:In",
		"/Air:AdjustmentTRequest_2.0:In",
		"/Air:AdjustmentTRequest_2.1:In",
		"/Air:AdjustmentTRequest_2.2:In",
		"/Air:AdjustmentTRequest_2.3:In",
		"/Air:GetFaFListTRequest_2.1:In",
		"/Air:UpdateFaFListTRequest_2.1:In",
		"/Air:GetFaFListTRequest_2.2:In",
		"/Air:UpdateFaFListTRequest_2.2:In",
		"/Air:GetFaFListTRequest_2.3:In",
		"/Air:UpdateFaFListTRequest_2.3:In",
		"/Air:GetAllowedServiceClassChangesTRequest_2.1:In",
		"/Air:UpdateServiceClassTRequest_2.1:In",
		"/Air:GetAllowedServiceClassChangesTRequest_2.2:In",
		"/Air:UpdateServiceClassTRequest_2.2:In",
		"/Air:GetAllowedServiceClassChangesTRequest_2.3:In",
		"/Air:UpdateServiceClassTRequest_2.3:In"};
	public static double[] WEIGHTS = new double[]{1.5,
		1.5,1.5,1.5,1.5,1.5,1.5,1.5,1.5,1.5,1.5,1.5,
		1.5,1.5,1.5,1.5,1.5,1.5,1.5,1.5,1.5,1.5,1.5,
		1.5,1.5,1.5,1.5,1.5,1.5,1.5,1.5,1.5,1.5,1.5,
		1.5,1.5,1.5,1.5,1.5,1.5,1.5,1.5,1.5,1.5,1.5,
		1.5,1.5,1.5,1.5,1.5,1.5,1.5,1.5,1.5,1.5,1.5,
		1.5,1.5,1.5,1.5,1.5,1.5,1.5,1.5,1.5,1.5,1.5,
		1.5,1.5,1.5,1.5,1.5,1.5,1.5,1.5,1.0,1.0,1.0,
		1.5,1.5,1.5,1.0,1.0,1.0,1.5,1.5,1.0,1.0,1.0,
		1.5,1.5,1.0,1.0,1.0,1.5,1.5,1.5,1.5,1.5,1.5,
		1.5,1.5,1.5,1.5,1.5,1.5,1.5,1.5,1.5,1.5};
	Logger logger;
	public File[] convert(File[] inputFiles, String systemName) throws ApplicationException, InputException {
		logger = Logger.getLogger(systemName);
		File[] outputFiles = new File[1];
		String path = PropertyReader.getConvertedFilesPath();
		HashMap dayMap = new HashMap();
		
		try {
			File output = new File(path, "DCSAirTPS.log");
			BufferedWriter outputStream = new BufferedWriter(new FileWriter(output));
			for (int i = 0; i < inputFiles.length; i++) {
				//for every file update days Map values
				updateVariables(inputFiles[i], dayMap);
			}
			//write the values of days Map to the output file
			for (Iterator iter = dayMap.keySet().iterator(); iter.hasNext();) {
				String key = (String) iter.next();
				String[] val = ((String[])dayMap.get(key)); 
				//TPS equals (the max transaction per day) / 60
				double tps = Double.parseDouble(val[1])/60;
	
				outputStream.write(val[0]+","+tps);
				
				outputStream.newLine();
			}
			outputStream.flush();
			dayMap.clear();
			dayMap = null;
			outputFiles[0] = output;
		}
		catch (FileNotFoundException e) {
			logger.error("CSAirTPSConverter.convert() - Input file not found " + e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger.error("CSAirTPSConverter.convert() - Couldn't read input file" + e);
			throw new ApplicationException(e);
		}
		return outputFiles;
	}
	public void updateVariables(File newFile,HashMap dayMap) throws ApplicationException,FileNotFoundException,IOException
	{
		String line="";
		BufferedReader inputStream = new BufferedReader(new FileReader(newFile));
		String[] headers = null;
		String curKey = null;
		if(inputStream.ready())
		{
			String headerLine = inputStream.readLine();
			headers = headerLine.split(",");
		}
		if(headers != null)
		{
			while(inputStream.ready()){
				//for every line perform days Map varables update
				line=inputStream.readLine();
				if(line=="")
					continue;
				if(true)
				{
					line = line.trim();
					line = line.replace("  ", " ");
					String[] values = line.split(",");
					if(values.length != headers.length)
						continue;
					else//main extraction point
					{
						//get current dayHour key from first string in the line
						curKey = getKey(values[0]);
						//update hours map with line string array and passing headers also
						updateMap(curKey,dayMap,headers,values);
					}
				}
			}
		}
		inputStream.close();
	}
	
	private void updateMap(String curKey, HashMap dayMap, String[] headers, String[] values) {
		double transaction = 0;
		//begin with values first index ( 3 )
		for(int i = 3 ; i<values.length;i++)
		{
			//- if the current coulumn exists in the specified columns in the requirements 
			//- Transaction equation is the sum of the (Air columns multiplied by its weights)
			int idx = Utils.indexOf(headers[i], CSAirTransactionConverter.COULUMNS);
			if(idx != -1)
			{
				if(!values[i].equals("0"))
					transaction += (Double.parseDouble(values[i])*CSAirTransactionConverter.WEIGHTS[idx]);
			}
		}
		String[] dayValue = null;
		double dayTransactionValue = 0;
		//if current day exists in the map get dayTransactionValue else dayValue is = new String[]
		if(dayMap.containsKey(curKey))
		{
			dayValue = (String[])dayMap.get(curKey);
			dayTransactionValue = Double.parseDouble(dayValue[1]);
		}
		else
		{
			dayValue = new String[2];
		}
		//if new calculated day transaction > previous calculated day Transaction then 
		//- update day TPS time to the current dayTime
		//- update day TPS to the new one
		if(transaction>dayTransactionValue)
		{
			dayValue[0] = getDateTime(values[0]);
			dayValue[1] = transaction+"";
			dayMap.put(curKey, dayValue);
		}
		else if(dayTransactionValue == 0 && transaction == 0)
		{
			dayValue[0] = getDateTime(values[0]);
			dayValue[1] = transaction+"";
			dayMap.put(curKey, dayValue);
		}
	}
	
	
	private String getDateTime(String dateValue) {
		String time = "";
		String[] values = dateValue.split("-");
		String day = values[2];
		String month = values[1];
		String year = values[0];
		String hour = values[3].substring(0,2);
		String minute = values[3].substring(2,4); 
		time = month+"/"+day+"/"+year+" "+hour+":"+minute+":00";
		return time;
	}
	private String getKey(String dateValue) {
		//2008-07-30-2358
		String key = "00:00:00";
		String[] values = dateValue.split("-");
		String day = values[2];
		String month = values[1];
		String year = values[0];
		key = month+"/"+day+"/"+year+" "+key;
		return key;
	}

	public static void main(String[] args) {
		try {
				PropertyReader
			.init("D:\\VASPortalWF\\Source Code\\DataCollection");
				CSAirTPSConverter s =new CSAirTPSConverter();
			File[] input = new File[1];

			input[0]=new File("D:\\VASPortalWF\\SourceCode\\DataCollection\\resources\\ftpfolder\\FSC-AirXmlRpc_2.0_A_1-2008-07-30-0000.stat");
//			input[1]=new File("D:\\VASPortalWF\\SourceCode\\DataCollection\\resources\\ftpfolder\\FSC-AirXmlRpc_2.0_A_1-2008-07-29-0000.stat");

			s.convert(input,"MSP");
				
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}