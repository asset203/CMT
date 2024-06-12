/**
 * 
 */
package com.itworx.vaspp.datacollection.util.converters;


import java.io.File;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;
import com.itworx.vaspp.datacollection.util.Utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.log4j.Logger;


/**
 * @author ahmad.abushady
 *
 */
public class SDPNewLoadRegulationConverter extends AbstractTextConverter implements Comparator {
	
	

	private Logger logger;
	
	private int lastRejected = 0;
	
	
	public SDPNewLoadRegulationConverter(){}

	public File[] convert(File[] inputFiles, String systemName)
			throws ApplicationException, InputException {
		
		
		logger = Logger.getLogger(systemName);
		
		logger.debug("SDPNewLoadRegulationConverter - Starting Converting SDP Load Regulation");
		
		String path = PropertyReader.getConvertedFilesPath();
		File[] outputFiles = new File[inputFiles.length];


		
		try{
			for (int i = 0; i < inputFiles.length; i++) {
				logger.debug( "SDPNewLoadRegulationConverter.convert() - converting file "
				+ inputFiles[i].getName() );
				Vector Columns = new Vector();
				//File output = new File(path, inputFiles[i].getName());
				File output = new File(path, inputFiles[i].getName()+".txt");
				BufferedReader inputStream = new BufferedReader(new FileReader(
						inputFiles[i]));
				BufferedWriter outputStream = new BufferedWriter(
						new FileWriter(output));


				String Line = "";
				if(inputStream.ready())
					Line = inputStream.readLine();
				
				while(Line != null){
					Line = FillLine(Columns, Line, inputStream);
					
				}
				
				if(Columns.size() == 0)
					Columns.add(Utils.getYesterdaysDate() + " 00:00:00,0,0");
				
				//sort data according to date
				Object [] col = Columns.toArray();				
				Arrays.sort(col, this);				
				
				
				Columns = ProccessData(col);
				
				
				
				//sort data again
				/*col = Columns.toArray();				
				Arrays.sort(col, this);				
				Columns = new Vector(Arrays.asList(col));*/
				
				
				//write data to output files
				for (int j=0; j < Columns.size(); j++){
					if(Columns.elementAt(j) == null)
						break;
					// System.out.println((String)Columns.elementAt(j));
					outputStream.write((String)Columns.elementAt(j));
					outputStream.newLine();
				}
				
				
				outputStream.close();
				inputStream.close();
				outputFiles[i] = output;
				
				logger.debug( "SDPNewLoadRegulationConverter.convert() - "
						+ inputFiles[i].getName() + " Successfully Converted");
			}
			
			
			logger.debug( "SDPNewLoadRegulationConverter.convert() - Finished converting all files.");
			
			
			return outputFiles;
		}
		catch (FileNotFoundException e) {
			logger.error("SDPNewLoadRegulationConverter.convert() - Input file not found " + e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger.error("SDPNewLoadRegulationConverter.convert() - Couldn't read input file"
					+ e);
			throw new ApplicationException(e);
		} catch (ParseException e) {
			logger.error("SDPNewLoadRegulationConverter.convert() - error parsing date" + e);
			throw new InputException("invalid date in input file " + e);
		}
	}
	
	
	public int compare(Object o1, Object o2) {
		try {
			String a = (String) o1;
			String b = (String) o2;
			String [] temp = a.split(",");
			a = temp[0];
			temp = b.split(",");
			b = temp[0];
			
			Date date = Utils.convertToDate(a, "MM/dd/yyyy HH");
			Date firstDate = Utils.convertToDate(b, "MM/dd/yyyy HH");
			return date.compareTo(firstDate);
			
			
		} catch (InputException e) {
			logger.error("SDPNewLoadRegulationConverter.convert() - error parsing date" + e);
		}
		return 0;
	}
	
	private static String getDate(String date) throws ParseException{
		SimpleDateFormat dateFormat = new SimpleDateFormat();
		dateFormat.applyPattern("yyyyMMdd HH");
		Date d = dateFormat.parse(date);
		dateFormat.applyPattern("MM/dd/yyyy HH:mm:ss");
		
		return dateFormat.format(d);
	}
	
	private Vector ProccessData(Object [] col) throws ParseException{
		Vector Columns = new Vector(Arrays.asList(col));
		
		if (Columns.size() <= 0)
			return Columns;
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		
		String [] temp = ((String) col[0]).split(",");
			
		Date currentDate = dateFormat.parse(temp[0]);
		
		
		temp = ((String) col[col.length - 1]).split(",");
		
		int i = 0;
		
		SimpleDateFormat smpl = new SimpleDateFormat("MM/dd/yyyy HH");
		
		while(i < col.length){
		
			Date x = smpl.parse((String) col[i]);
			int comp = currentDate.compareTo(x);
			/*if(smpl.format(currentDate).equals("11/27/2006 20")){
			System.out.println("x:" + smpl.format(x));
			}*/
			
			if(comp == 0){
				i++;
				currentDate = new Date(currentDate.getTime() + 3600000);
				continue;
			}
			else {
				Columns.add(smpl.format(currentDate)+ ":00:00,0,0");
				currentDate = new Date(currentDate.getTime() + 3600000);
				continue;
			}
		}
		
		Date todayDate = getTodayDate();
		
		while(todayDate.compareTo(currentDate) > 0){
			Columns.add(smpl.format(currentDate)+ ":00:00,0,0");
			currentDate = new Date(currentDate.getTime() + 3600000);
		}
		
		return Columns;
	}

	private String FillLine(Vector col,String Line, BufferedReader inputStream) throws ParseException, IOException{
		int rejected = 0;
		int lastRej = 0;
		int totalRej = 0;
		int rejectionEvent = 0;
		String time ="";
		boolean flag = false;
		int rejectedIdx = -1;
		boolean firstRound = true;
		SimpleDateFormat smpl = new SimpleDateFormat("MM/dd/yyyy HH");
		do{
			rejectedIdx = Line.lastIndexOf("Rejected:");
			if(rejectedIdx != -1 && (Line.lastIndexOf("CPU:") > rejectedIdx || Line.lastIndexOf("Calls:") > rejectedIdx) ){
				flag = true;
				StringTokenizer st = new StringTokenizer(Line.substring(rejectedIdx + 10));
				rejected = Integer.parseInt(st.nextToken());
				
				if(firstRound){
					lastRejected = 0;
					if(rejected != 0){
						totalRej += (rejected - lastRejected);
						rejectionEvent++;
					}
					lastRej = rejected;
					lastRejected = lastRej;
					firstRound = false;
				}else{				
					if(rejected != 0){
						totalRej += (rejected - lastRej);
						lastRej = rejected;
						lastRejected = lastRej;
						rejectionEvent++;
					}
					else{
						lastRej = 0;
						lastRejected = 0;
					}
				}
				
				time = Line.substring(14, 25);
				Line = inputStream.readLine();
				
			}
			else{
				Line = inputStream.readLine();
			}
			
			if(Line != null && !Line.equals("")){
				while(Line.length() < 26 || !Line.matches("^[0-9]{10}\\.[0-9]{2}.*$")){
					Line = inputStream.readLine();
					if(Line == null)
						break;
				}
			}
		} while(!(Line == null || Line.equals("") || !Line.substring(14, 25).equals(time)));
		/*if(flag)
			col.add(getDate(time) + "," + totalRej + "," + rejectionEvent);*/
		
		
		if(flag){
			// added to fix infinite loop issue that happened because error in the file structure
			if(col.size()>0){
				String [] temp = ((String)col.get(col.size()-1)).split(",");
				Date oldDate = smpl.parse((String) temp[0]);
				Date newDate = smpl.parse((String) getDate(time));
				if(oldDate.compareTo(newDate)>=0){
					logger.info( "SDPNewLoadRegulationConverter.convert() - ERROR - there is a problem with date "
							+ newDate );
					System.out.println("error");
				}else{
					col.add(getDate(time) + "," + totalRej + "," + rejectionEvent);
				}
			}else
				col.add(getDate(time) + "," + totalRej + "," + rejectionEvent);
		}
		return Line;
	}
	
	private Date getTodayDate() throws ParseException
	{	
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	    String currentDate = sdf.format(new Date());
	    
	    return sdf.parse(currentDate);
	}
	
	
	/*
	 * for testing
	 *
	public static void main(String ag[]) {
		try {
			SDPNewLoadRegulationConverter s = new SDPNewLoadRegulationConverter();
			s.path = "c:\\converted";//
			File[] files = new File[1];
			files[0] = new File("C:\\sdp_loadreg_1200480442038__cFSCInapContainer.log.0");
			s.convert(files,"");
			//String Line = "1164581265.79[20061127 00:47:45] INAP Converter , myCPUThreshold:  1164581265.79[20061127 00:47:45] EventSender [FSC-Inap/7.3/A/1] - All events registered";
			//System.out.println(Line.matches("^[0-9]{10}\\.[0-9]{2}.*$"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
	
	public static void main(String[] args) throws Exception
	{
		try {
			
			String path = "D:\\Projects\\VAS Portal Project\\Phase 7\\VFE_VAS_Performance_Portal_V7\\SourceCode\\DataCollection\\";
			PropertyReader.init(path);
			SDPNewLoadRegulationConverter s = new SDPNewLoadRegulationConverter();
			File[] input = new File[1];
			input[0]=new File(path+"cFSCInapContainer.log.0_41");
			//input[0]=new File("D:\\Work\\VFE_VAS_Performance_Portal\\Phase IV\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection\\resources\\ftpfolder\\capacity_reg.txt");
			s.convert(input,"Database");
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
}
