/*
 * File:       TextInputDAO.java
 * Date        Author          Changes
 * 16/01/2006  Nayera Mohamed  Created
 * 30/01/2006  Nayera Mohamed  Updated to include index
 * 18/03/2006  Nayera Mohamed  Updated to include extract date
 *
 * Responsible for retrieving Data in case of Text Input
 */

package com.itworx.vaspp.datacollection.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import eg.com.vodafone.model.DataColumn;
import eg.com.vodafone.model.TextInputStructure;
import eg.com.vodafone.model.VInput;
import eg.com.vodafone.model.VInputStructure;
import org.apache.log4j.Logger;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.objects.InputData;
import com.itworx.vaspp.datacollection.util.FileHandler;
import com.itworx.vaspp.datacollection.util.FtpHandler;
import com.itworx.vaspp.datacollection.util.PersistenceAttributes;
import com.itworx.vaspp.datacollection.util.PersistenceManager;
import com.itworx.vaspp.datacollection.util.converters.AbstractTextConverter;

public class TextInputDAO implements InputDAO {

	protected FtpHandler ftpHandler;
	protected PersistenceManager persistenceManager;
	protected Logger logger;
	
	protected SimpleDateFormat targetf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
	protected SimpleDateFormat outDateFormat = new SimpleDateFormat("MM/00/yyyy");

	public TextInputDAO(PersistenceManager persistenceManager) {
		this.persistenceManager = persistenceManager;
	}
	/*added by suzan tadrous */
	public void retrieveData(VInput input, Date targetDate) throws InputException, ApplicationException {
		retrieveData(input, targetDate, false);
	}
	
	public void retrieveData(VInput input, Date targetDate ,boolean hour )
			throws InputException, ApplicationException {
		logger = Logger.getLogger(input.getSystemName());
		logger.debug("TextInputDAO.retrieveData() - started retrieveData for inputid:" + input.getId() + " targetDate:" + targetDate);
		VInputStructure[] inputStructures = input.getInputStructures();
		PersistenceAttributes attrs=null;
		try {
			File[] originalInputFiles = new FileHandler().getFiles(input);
			for (int z = 0; z < inputStructures.length; z++) {
				boolean foundData = false;
				TextInputStructure inputStructure = (TextInputStructure) inputStructures[z];
				
				InputData inputData = new InputData();
				
				String inputID = inputStructure.getId();
				inputID = inputID.substring(0, inputID.lastIndexOf("_struct"));
				inputData.setInputStructureId(inputStructure.getId());
				inputData.setInputID(inputID);
				inputData.setNodeName(input.getNodeName());
				inputData.setSystemName(input.getSystemName());
				inputData.setDate(targetDate);
				// get header from node and set in input data object
				DataColumn[] header = inputStructure.getColumns();
				inputData.setHeader(header);
				String converterName = inputStructure.getConverter();
				// create an instance of the associated converter
				AbstractTextConverter converter = (AbstractTextConverter) Class.forName(
						"com.itworx.vaspp.datacollection.util.converters."
								+ converterName).newInstance();
				converter.setParametersMap((HashMap<String,String>)inputStructure.getParametersMap());
				File[] inputFiles = converter.convert(originalInputFiles, input
						.getSystemName());
				
        attrs = persistenceManager.getPersistenceAttributes(inputData);
				int noOfColumns = header.length;
				for (int j = 0; j < inputFiles.length; j++) {
					
					/*BufferedReader  inputStream = new BufferedReader(
							new FileReader(inputFiles[j]));*/
					Reader reader = new InputStreamReader(new FileInputStream(inputFiles[j]), "UTF-8"); 
					BufferedReader inputStream = new BufferedReader(reader);
					if(inputStructure.getLastCallClassName()!=null)
					{
						inputData.setLastCallClassName(inputStructure.getLastCallClassName());
					}
					if (inputStructure.isExtractDate()) {
						if(inputStructure.isExtractDateMonthly()) {
							inputData.setExtractDateMonthly(true);
						}
						int dateIndex = -1;
						// find the index of the column of type date
						for (int w = 0; w < noOfColumns; w++) {
							if (header[w].getType().equals("date")) {
								dateIndex = w;
							}
						}
						attrs.getPersistenceProperties().put("DateFormat",inputStructure.getDateFormat());
						SimpleDateFormat f = new SimpleDateFormat(inputStructure.getDateFormat());
						f.applyPattern(inputStructure.getDateFormat());
						
						while (inputStream.ready()) {
							String[] dataRow = new String[noOfColumns];
							String dataLine = inputStream.readLine();
							String[] dataLineSplit = dataLine.split(",");
							if (dataLineSplit.length == 0
									|| dataLineSplit.length < noOfColumns) {
								break;
							}
							for (int i = 0; i < dataRow.length; i++) {
								dataRow[i] = dataLineSplit[header[i].getIndex()];
							}
							if(dateIndex!=-1) {
								if(!dateMatches(input,targetDate,f,dataRow[dateIndex],hour,inputData.isExtractDateMonthly())){
									continue;
								}
							}
							persistenceManager.createPersistenceObject(inputData,dataRow,attrs);
							foundData=true;
						}
					} else {
						while (inputStream.ready()) {
							String[] dataRow = new String[noOfColumns];
							String dataLine = inputStream.readLine();
							dataLine=new String (dataLine.getBytes("UTF-8"), "UTF-8");
							String[] dataLineSplit = dataLine.split(",");
							if (dataLineSplit.length == 0
									|| dataLineSplit.length < noOfColumns) {
								break;
							}
							for (int i = 0; i < dataRow.length; i++) {
								dataRow[i] = dataLineSplit[header[i].getIndex()];
							}
							persistenceManager.createPersistenceObject(inputData,dataRow,attrs);
							foundData=true;
						}
					}
					inputStream.close();
				}
				if ( foundData == false ) {
					logger.error("-"+ input.getNodeName() +"- IN-1000: TextInputDAO.retrieveData() - No data found in file "+inputStructures[z].getId());
					if(inputStructures.length == 1)
						throw new InputException("-"+ input.getNodeName() +"- No data found in file for input structure "+inputStructures[z].getId());
				} else {
					persistenceManager.persistObjects(inputData,attrs,hour);
		        }
			}            
//			for (int i = 0; i < originalInputFiles.length; i++) {
//				originalInputFiles[i].delete();
//			}
		} catch (InstantiationException e) {
            logger.error(e);
			logger.error("-"+ input.getNodeName() +"- TX-1000:TextInputDAO.retrieveData() - Couldn't instatntiate text converter class: ", e);
			throw new ApplicationException("" + e);
		} catch (FileNotFoundException e) {
            logger.error(e);
			logger.error("-"+ input.getNodeName() +"- IN-1002:TextInputDAO.retrieveData() - Couldn't find input file: ", e);
			throw new InputException("" + e);
		} catch (IOException e) {
            logger.error(e);
			logger.error("-"+ input.getNodeName() +"- IN-1003:TextInputDAO.retrieveData() - Couldn't read input file: ", e);
			throw new InputException("" + e);
		} catch (IllegalAccessException e) {
            logger.error(e);
			logger.error("-"+ input.getNodeName() +"- TX-1000:TextInputDAO.retrieveData() - Couldn't acces text converter class method: ", e);
			throw new ApplicationException("" + e);
		} catch (ClassNotFoundException e) {
            logger.error(e);
			logger.error("-"+ input.getNodeName() +"- TX-1000:TextInputDAO.retrieveData() - Couldn't find text converter class: ", e);
			throw new ApplicationException("-"+ input.getNodeName() +"- " + e);
		} finally {
	      if ( attrs != null ) {
	        persistenceManager.clear(attrs);
	      }
	    }
		logger.debug("TextInputDAO.retrieveData() - finished retrieveData for inputid:" + input.getId() + " targetDate:" + targetDate);
	}
	
	protected boolean dateMatches(VInput input,Date targetDate,SimpleDateFormat f,String dateString,boolean hour,boolean extractDateMonthly) throws InputException {
		/**
		 * @author ahmad.abushady
		 * This part was modified on 24 Dec. 2007
		 * It was modified to fix an error in the extract date
		 * It used to ignore the year, but now it compare the whole date
		 */
		try {
			Date date = f.parse(dateString);
			Calendar c1 = Calendar.getInstance();
			c1.setTime(date);
			c1.set(Calendar.HOUR_OF_DAY, 0);
			
			if(hour){
				c1.set(Calendar.HOUR_OF_DAY,date.getHours());
			}
			c1.set(Calendar.MINUTE, 0);
			c1.set(Calendar.SECOND, 0);
			c1.set(Calendar.MILLISECOND, 0);
			Date tDate = targetf.parse(targetDate.toString());
			String outDateString = outDateFormat.format(tDate);
			Date outDate =outDateFormat.parse(outDateString);
			
			Calendar c2 = Calendar.getInstance();
			c2.setTime(targetDate);
			c2.set(Calendar.HOUR_OF_DAY, 0);
			if(extractDateMonthly) {
				c2.setTime(outDate);
			}
			if(hour){
				c2.set(Calendar.HOUR_OF_DAY,targetDate.getHours());
			}
			c2.set(Calendar.MINUTE, 0);
			c2.set(Calendar.SECOND, 0);
			c2.set(Calendar.MILLISECOND, 0);
			
			if (c1.compareTo(c2) != 0) {
				return false;
			}
			return true;
			/**
			 * @author ahmad.abushady
			 * The previous part was modified on 24 Dec. 2007
			 */
		} catch (ParseException e) {
			logger.error("-"+ input.getNodeName() +"- IN-1001 :TextInputDAO.retrieveData() - Date doesn't match format ",e);
            throw new InputException("-"+ input.getNodeName() +"- TextInputDAO.retrieveData() - date doesn't match format "+ e);
		}
	}
	/*added by suzan tadrous to support hourly */
	public void retrieveHourlyData(VInput input, Date targetDate) throws InputException, ApplicationException {
		retrieveData(input, targetDate, true);
	}

	public void retrieveHourlyDataa(VInput input, Date targetDate) throws InputException, ApplicationException {
		throw new RuntimeException("method TextInputDAO.retrieveHourlyData() is not implemented yet");
	}
	
	public static void main(String[] args) {
		//Properties prop=new Properties();
		//System.out.println("com.itworx.vaspp.datacollection.dao.TextInputDAO.main(): "+prop.getProperty("x"));
		String s1 = "val1-val2,val3-val4|val5-val6-val7";
		String s2 = "1220668485.190506[20080906 04:34:45.190506] Module: AUV-XmlRpcVoucherUsageClientIf/2.0/A/1 Event: Connection failed ID: 450008 Type: -317603840 Count: 1 Aff.Obj: http://vs2a:10020/VoucherUsage Info: Connection failed";
		String delimiter1 = "\\-|\\,|\\|";
		String delimiter2 = "[-,|]";
		String delimiter3 = "(\\.)|(\\[)|(\\])|(\\s)|(\\:)";
		String[] arr1 = s1.split(delimiter1);
		String[] arr2 = s1.split(delimiter2);
		String[] arr3 = s2.split(delimiter3);
		
		System.out.println("array length : "+arr1.length);
		for (String value : arr1) {
			System.out.println(value);
		}
		System.out.println("---------------");
		System.out.println("array length : "+arr2.length);
		for (String value : arr2) {
			System.out.println(value);
		}
		
		System.out.println("---------------");
		System.out.println("array length : "+arr3.length);
		for (String value : arr3) {
			System.out.println(value);
		}
	}
}