/*
 * File:       ExcelInputDAO.java
 * Date        Author          Changes
 * 16/01/2006  Nayera Mohamed  Created
 * 30/01/2006  Nayera Mohamed  Updated to include index
 * 20/02/2006  Nayera Mohamed  Updated to read horizontal data
 *
 * Responsible for retrieving Data in case of Excel Input
 */

package com.itworx.vaspp.datacollection.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import eg.com.vodafone.model.DataColumn;
import eg.com.vodafone.model.ExcelInputStructure;
import eg.com.vodafone.model.VInput;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.objects.InputData;
import com.itworx.vaspp.datacollection.util.FileHandler;
import com.itworx.vaspp.datacollection.util.PersistenceAttributes;
import com.itworx.vaspp.datacollection.util.PersistenceManager;
import com.itworx.vaspp.datacollection.util.Utils;

public class ExcelInputDAO implements InputDAO {
	private Logger logger;
  private PersistenceManager persistenceManager;
    
	public ExcelInputDAO(PersistenceManager persistenceManager) {
    this.persistenceManager = persistenceManager;
	}

	/**
	 * 
	 * @param input -
	 * @param targetDate -
	 * 
	 * @return InputData - object holding the data read from Excel Input File
	 * @exception InputException
	 * 
	 * @exception ApplicationException
	 * 
	 */
	public void retrieveData(VInput input, Date targetDate)
			throws InputException, ApplicationException {
		logger = Logger.getLogger(input.getSystemName());
		logger
				.debug("ExcelInputDAO.retrieveData() - started retrieveData for inputid:"
						+ input.getId() + " targetDate:" + targetDate);
		// get header from inpur and set in input data object
		// read data from file and collect in vector then pass to input data
		// object
	
		InputData inputData = new InputData();
		inputData.setInputID(input.getId());
		inputData.setNodeName(input.getNodeName());
		inputData.setSystemName(input.getSystemName());
		
		inputData.setDate(targetDate);
		
    PersistenceAttributes attrs=null;
		try {
		
			
			ExcelInputStructure inputStructure = (ExcelInputStructure) input
					.getInputStructure();
			/**/
			if(inputStructure.getParametersMap()!=null&&inputStructure.getParametersMap().size()!=0)
			{
				HashMap<String, String> parameters = (HashMap<String, String>)inputStructure.getParametersMap();
				Iterator it =parameters.keySet().iterator();
				while(it.hasNext())
				{         Object key =it.next();
					if(key.toString().equalsIgnoreCase("Monthly"))
					{ 
						
						if(parameters.get(key).equalsIgnoreCase("true"))
						{  
							Calendar c= Calendar.getInstance();
							c.setTime(targetDate);
							c.add(Calendar.MONTH,-1 );							
							targetDate=c.getTime();
							String inputName=input.getOriginalInputName();
							input.setInputName(Utils.resolveName(inputName, targetDate));
							inputData.setDate(c.getTime());
							break;
						}
					}
				}
			
			}File[] inputFiles = new FileHandler().getFiles(input);
			/**/
        attrs = persistenceManager.getPersistenceAttributes(inputData);
			boolean useSheetData = inputStructure.isUseSheetInData();
			if (useSheetData) {
				this.getHeaderWithSheets(inputStructure, inputData);
				for (int i = 0; i < inputFiles.length; i++) {
					HSSFWorkbook workbook = new HSSFWorkbook(
							new FileInputStream(inputFiles[i]));
					this.getDataWithSheets(inputStructure, workbook, inputData,
							attrs ,targetDate);
					// System.out.println(inputFiles[i].delete());
				}
			} else {
				inputData.setHeader(inputStructure.getColumns());
				
				
				for (int i = 0; i < inputFiles.length; i++) {
					HSSFWorkbook workbook = new HSSFWorkbook(
							new FileInputStream(inputFiles[i]));
					
					
					
					this.getDataNoSheets(inputStructure, workbook, inputData,
							attrs,targetDate);
					// System.out.println(inputFiles[i].delete());
				}
			}
//			if (data.size() == 0) {
//				logger.error("IN-1000: ExcelInputDAO.retrieveData() - No data found in file");
//				throw new InputException("No data found in file");
//			}
//			else
//      {
          persistenceManager.persistObjects(inputData,attrs);
//      }
			logger
					.debug("ExcelInputDAO.retrieveData() - Finished retrieveData for inputid:"
							+ input.getId() + " targetDate:" + targetDate);
		} catch (IOException e) {
			logger
					.error("-"+ input.getNodeName() +"- IN-1003: ExcelInputDAO.retrieveData() - Couldn't read input file: "
							+ e);
			throw new InputException("" + e);
		} catch (ClassCastException e) {
			logger
					.error("-"+ input.getNodeName() +"- ExcelInputDAO.retrieveData() - Wrong input Structure "
							+ e);
			throw new ApplicationException("-"+ input.getNodeName() +"- wrong input Structure " + e);
		}
    finally
    {
      if ( attrs != null ) {
        persistenceManager.clear(attrs);
      }
    }
	}

	/**
	 * get the value of this cell using the appropriate method based on its type
	 * 
	 * @param type -
	 *            the type of the column as specified in inputStructure
	 * @param cell -
	 *            the cell instance
	 * 
	 * @return String - the resolved name
	 */
	private String getValue(String type, HSSFCell cell) {
		
		if (cell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
			
			return null;
		}
		if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING
				&& cell.getStringCellValue() == "") {
			return null;
		}
		try {
			if (type.equalsIgnoreCase("number")) {
				return "" + new Double(cell.getNumericCellValue()).longValue();
			} else if (type.equalsIgnoreCase("float")) {
				return ""
						+ new Double(cell.getNumericCellValue()).doubleValue();
			} else if (type.equalsIgnoreCase("date")) {
				SimpleDateFormat f = new SimpleDateFormat();
				Date date = cell.getDateCellValue();
				StringBuffer s = f.format(date, new StringBuffer(),
						new FieldPosition(f.DATE_FIELD));
				return "" + s;
			} else if (type.equalsIgnoreCase("time")) {
				return "" + cell.getDateCellValue();
			} else if (type.equalsIgnoreCase("string")) {
				if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
					return ""
							+ new Double(cell.getNumericCellValue())
									.longValue();
				} else {
					return "" + cell.getStringCellValue();
				}
			} else {
				return null;
			}
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 
	 * @param inputStructure -
	 * @param workbook -
	 * @param inputData -
	 * @param // data -
	 * 
	 * @exception InputException
	 * 
	 */

	private void getDataNoSheets(ExcelInputStructure inputStructure,
			HSSFWorkbook workbook, InputData inputData, PersistenceAttributes attrs,Date targetDate)
			throws InputException,ApplicationException {
		DataColumn[] header = inputData.getHeader();
		int noOfColumns = header.length;
		String[] sheets = inputStructure.getSheetNames();
		for(int sheet=0;sheet<sheets.length;sheet++)
		{
			sheets[sheet]=Utils.resolveName(sheets[sheet], targetDate);
		}
		int startRowIndex = inputStructure.getSkip();
		if (inputStructure.isHorizontal()) {
			for (int i = 0; i < sheets.length; i++) {
				HSSFSheet sheet = workbook.getSheet(sheets[i]);
				if (sheet == null) {
					logger
							.error("- "+ inputData.getNodeName() +" -EX-1001: ExcelInputDAO.getDataNoSheets() - Sheet with name "
									+ sheets[i] + " not found");
					throw new InputException("- "+ inputData.getNodeName() +" - Sheet with name " + sheets[i]
							+ " not found");
				}
				HSSFRow rows[] = new HSSFRow[noOfColumns];
				for (int j = 0; j < noOfColumns; j++) {
					short index = (short) header[j].getIndex();
					rows[j] = sheet.getRow(index);
				}
				short num = (short) startRowIndex;
				outer: while (true) {
					String[] dataRow = new String[noOfColumns];
					for (int j = 0; j < rows.length; j++) {
						HSSFCell cell = rows[j].getCell(num);
						if (cell == null) {
							break outer;
						}
						dataRow[j] = this.getValue(header[j].getType(), cell);
						if (dataRow[j] == null) {
							break outer;
						}
					}
					persistenceManager.createPersistenceObject(inputData,dataRow,attrs);
					num++;
				}
			}
		} else {
			for (int i = 0; i < sheets.length; i++) {
				HSSFSheet sheet = workbook.getSheet(sheets[i]);
				Iterator it = sheet.rowIterator();
				int w = 1;
				// skip rows
				while (it.hasNext() && w <= startRowIndex) {
					it.next();
					w++;
				}
				int lastRowNum = 0;
				int rowNum = startRowIndex - 1;
                 
				outer: while (it.hasNext()) {
					
					lastRowNum = rowNum;
					HSSFRow row = (HSSFRow) it.next();
					rowNum = row.getRowNum();
					if (rowNum != lastRowNum + 1) {
						break outer;
					}
				
					String[] dataRow = new String[noOfColumns];
					for (int j = 0; j < noOfColumns; j++) {
						short index = (short) header[j].getIndex();
						
						HSSFCell cell = row.getCell(index);
						if (cell == null) {
							
							break outer;
						}
						dataRow[j] = this.getValue(header[j].getType(), cell);
						if (dataRow[j] == null) {
							
							break outer;
						}
					}
					
					persistenceManager.createPersistenceObject(inputData,dataRow,attrs);
				}
			}
		}
	}

	/**
	 * 
	 * @param inputStructure -
	 * @param inputData -
	 * 
	 */
	private void getHeaderWithSheets(ExcelInputStructure inputStructure,
			InputData inputData) {
		DataColumn[] rawHeader = inputStructure.getColumns();
		int noOfColumns = rawHeader.length + 1;
		DataColumn[] header = new DataColumn[noOfColumns];
		header[0] = new DataColumn("Sheet_name", "string");
		for (int i = 0; i < header.length - 1; i++) {
			header[i + 1] = rawHeader[i];
		}
		inputData.setHeader(header);
	}

	/**
	 * 
	 * @param inputStructure -
	 * @param workbook -
	 * @param inputData -
	 * @param ///data -
	 * 
	 * @exception InputException
	 * 
	 */
	private void getDataWithSheets(ExcelInputStructure inputStructure,
			HSSFWorkbook workbook, InputData inputData, PersistenceAttributes attrs,Date targetDate)
			throws InputException,ApplicationException {
		DataColumn[] header = inputData.getHeader();
		int noOfColumns = header.length;
		String[] sheets = inputStructure.getSheetNames();
		for(int sheet=0;sheet<sheets.length;sheet++)
		{
			sheets[sheet]=Utils.resolveName(sheets[sheet], targetDate);
		}
		int startRowIndex = inputStructure.getSkip();
		for (int i = 0; i < sheets.length; i++) {
			HSSFSheet sheet = workbook.getSheet(sheets[i]);
			if (sheet == null) {
				logger
						.error("-"+ inputData.getNodeName() +"- EX-1001:ExcelInputDAO.getDataWithSheets() - Sheet with name "
								+ sheets[i] + " not found");
				throw new InputException("-"+ inputData.getNodeName() +"- Sheet with name " + sheets[i]
						+ " not found");
			}
			Iterator it = sheet.rowIterator();
			int w = 1;
			// skip rows
			while (it.hasNext() && w <= startRowIndex) {
				it.next();
				w++;
			}
			int lastRowNum = 0;
			int rowNum = startRowIndex - 1;
			outer: while (it.hasNext()) {
				lastRowNum = rowNum;
				// System.out.println("***********");
				HSSFRow row = (HSSFRow) it.next();
				rowNum = row.getRowNum();
				if (rowNum != lastRowNum + 1) {
					break outer;
				}
				String[] dataRow = new String[noOfColumns];
				dataRow[0] = sheets[i];
				for (int j = 0; j < noOfColumns - 1; j++) {
					short index = (short) header[j + 1].getIndex();
					HSSFCell cell = row.getCell(index);
					if (cell == null) {
						break outer;
					}
					dataRow[j + 1] = this.getValue(header[j + 1].getType(),
							cell);
					if (dataRow[j + 1] == null) {
						break outer;
					}
				}
				persistenceManager.createPersistenceObject(inputData,dataRow,attrs);
			}
		}
	}
	public void retrieveHourlyData(VInput input, Date targetDate) throws InputException, ApplicationException {
		//TODO Auto-generated method stub
		throw new RuntimeException("method ExcelInputDAO.retrieveHourlyData() is not implemented yet");
	}

	// for testing
//	public static void main(String arg[]) {
//		try {
//			ExcelInputDAO d = new ExcelInputDAO();
//			VInput input = new VInput();
//			input.setInputName("myfileklsdjfl");
//			d.retrieveData(input, new Date());
//		} catch (ApplicationException e) {
//			e.printStackTrace();
//		} catch (InputException e) {
//			e.printStackTrace();
//		}
//	}

}