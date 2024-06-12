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
import java.util.Vector;

import org.apache.log4j.Logger;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;

public class VASPTopRequestsConverter extends AbstractTextConverter{
	private Logger logger;
	public VASPTopRequestsConverter() {
	}

	
	public File[] convert(File[] inputFiles, String systemName)
			throws ApplicationException, InputException 
{
		logger = Logger.getLogger(systemName);
		String path = PropertyReader.getConvertedFilesPath();
		logger
				.debug("VASPTopRequestsConverter.convert() - started converting input files ");
		File[] outputFiles = new File[inputFiles.length];
		try
		{
			for (int i = 0; i < inputFiles.length; i++) 
			{
				logger.debug("VASPTopRequestsConverter.convert() - converting file "
						+ inputFiles[i].getName());
				File output = new File(path, inputFiles[i].getName());
				BufferedReader inputStream = new BufferedReader(new FileReader(
						inputFiles[i]));
				BufferedWriter outputStream = new BufferedWriter(new FileWriter(output));
				////////////////////////////////////////////////////////////////////////////////////////////
				String dateTime = null; 
				long requestsExecuting = 0 ;
				long requestsQueued = 0 ;
				long waitingTime = 0; 
				long executingTime = 0; 
			
				boolean firstLine = true; 
				
				String lineTokens []; 
				Vector records = new Vector();
				
				while (inputStream.ready()) 
				{
					String line = inputStream.readLine();
					lineTokens = line.split("\t"); 
					
					// skip the first line of file "columns' headers"
					if (firstLine == true)
						{
							line = inputStream.readLine(); 
							lineTokens = line.split("\t"); 
							firstLine = false; 
						}
					// skip empty lines 
					if (line.equals("")) 
						{
							continue;
						}
					lineTokens = parseData(lineTokens); 
					// add to top 100 if suitable
					updateTop(records, lineTokens); 
					
				} // end of while
				int j; 
				for ( j=0 ; j < records.size() && j<100 ; j++)
				{
					String [] record=(String [])records.get(j);
					
					//dateTime = parseTime(record); //record[0]; 
					String vaspDateTime = writeDate(updateDateFormat(record[0]));
					requestsExecuting = Long.parseLong(record[14]); 
					requestsQueued = Long.parseLong(record[18]); 
					waitingTime = Long.parseLong(record[12]); 
					executingTime = Long.parseLong(record[11]);
					
				//	System.out.println(vaspDateTime+","+requestsExecuting+","+requestsQueued+","+waitingTime+","+ executingTime);
					outputStream.write(vaspDateTime+","+requestsExecuting+","+requestsQueued+","+waitingTime+","+ executingTime);
					
					outputStream.newLine();
				}
				 
				////////////////////////////////////////////////////////////////////////////////////////////
				outputStream.flush();
				outputStream.close();
				outputFiles[i] = output;
				logger.debug("VASPTopRequestsConverter.convert() - "
						+ inputFiles[i].getName() + " converted");
			}
			logger
					.debug("VASPTopRequestsConverter.convert() - finished converting input files successfully ");
			return outputFiles;
		}catch (FileNotFoundException e) {
			logger.error("VASPTopRequestsConverter.convert() - Input file not found " + e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger.error("VASPTopRequestsConverter.convert() - Couldn't read input file"
					+ e);
			throw new ApplicationException(e);
		} 

}
	/**
	 * 
	 * @param Data : Any String array of data line tokens where each entry is surrounded by quotes. 
	 * @return : same String array but with quotes removed from each entry. 
	 */
	private String[] parseData(String[] Data)
	{
		for(int i=0; i<Data.length ; i++)
		{
			Data[i] = Data[i].substring(1); 
			Data[i] = Data[i].substring(0,Data[i].indexOf('"')); 
		}
		return Data; 
	}
	
	/**
	 * 
	 * @param Data : String array of data line tokens. 
	 * @return String containing the time field. 
	 */
	private String parseTime(String[] Data )
	{
		Date dateTime; 

		SimpleDateFormat frm = new SimpleDateFormat();
		String EST;
	

		frm.applyPattern("MM/dd/yyyy HH:mm:ss");
		
		EST = Data[0]; 
		EST = EST.substring(1,EST.indexOf(".")); 
		
		try {
		
			dateTime = frm.parse(EST);
			EST = frm.format(dateTime); 
		} 
		catch (Exception e)
		{
			
		}
		return EST; 
	}
	
	/**
	 * 
	 * @param records : vector containing the top 100 records 
	 * 
	 */
	private void sort(Vector records)
	{ 
		String [] x ; 
		String [] y ; 
		 for (int i=0; i<records.size()-1; i++)
		      for (int j=records.size()-1; j>i; j--)
		      {
		    	  x = (String[])records.elementAt(j-1); 
		    	  y = (String[])records.elementAt(j); 
		    	  if (Long.parseLong(x[14])< Long.parseLong(y[14]))
		    	  {
		    		  String [] tmp = (String[]) records.elementAt(j-1);
			          records.setElementAt(records.elementAt(j), j-1);
			          records.setElementAt(tmp, j);
		    	  }
		      }
	}
	/**
	 * 
	 * @param records : vector containing the top 100 records 
	 * 			Data: a new data line to be input to the records
	 */
	private void updateTop(Vector records, String[] Data) {
	
		// if there is still less than a 20 , add the record 
		if (records.size() < 100) {
			records.add(Data);
			sort(records); 
		} else {
			// get the least , if we add one that is  <= to the min  ... replace
			String [] minCount = (String[])records.lastElement();
			if (Long.parseLong(Data[14]) > Long.parseLong(minCount[14])) {
				records.remove(minCount);
				records.add(Data);
				sort(records); 
			}
			
			/**
			 * @author ahmad.abushady
			 * The following section was commented to enhance performance
			 * 
			 * There was no need for the below section becuase it added Values to the top 100
			 * the values add were discarded later. Thus there no need to add them anyway.
			 * 
			 * Updated in Dec. 17 2007
			 */
			
			/*
			else if (Long.parseLong(Data[14]) == Long.parseLong(minCount[14])) {
				records.add(Data);
				sort(records); 
			}*/
			
			/**
			 * @author ahmad.abushady
			 * Updated in Dec. 17 2007
			 */
		}
	}
	
	/**
	 * 
	 * @param day : the Date to be converted
	 * @return String : the Date String representation
	 */
	private String writeDate(Date day)
	{
		String date = null; 
		SimpleDateFormat frm = new SimpleDateFormat();
		frm.applyPattern("MM/dd/yyyy HH:mm:ss");
		try
		{
			date = frm.format(day); 
		}	catch (Exception e)
		{
			
		}
		return date; 
	}
	/**
	 * @param dateString -
	 *            the date string to be converted
	 * 
	 * @return String - the date string converted to standard format
	 * @exception InputException
	 *                if ParseException occured
	 */
	private Date updateDateFormat(String dateString) throws InputException {
		SimpleDateFormat frm = new SimpleDateFormat();
		frm.applyPattern("MM/dd/yyyy HH:mm:ss");
		try {
			Date date = frm.parse(dateString);
			return date;
		} catch (ParseException e) {
			throw new InputException("invalid date in input file ");
		}
	}
	/*
	public static void main(String ag[]) {
		try {
			VASPTopRequestsConverter s = new VASPTopRequestsConverter();
			s.path = "c:\\converted";
			File[] input = new File[1];
			input[0]=new File("C:\\fout.11-28.mapped");
			s.convert(input,"VASP");
			System.out.println("finish");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
}
