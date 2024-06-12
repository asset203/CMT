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
import com.itworx.vaspp.datacollection.util.converters.HuaweiIVRCPUStatusConverter.CpuStatus;

public class HuaweiIVRMemoryStatusConverter  extends AbstractTextConverter{
	private Logger logger;
	private Map  <String ,MemoryStatus> dateVSMemoryStatus=new HashMap<String,MemoryStatus>() ;
	public File[] convert(File[] inputFiles, String systemName)
	throws ApplicationException, InputException {
		logger = Logger.getLogger(systemName);
		logger
				.debug("Inside HuaweiIVRMemoryStatusConverter convert - started converting input files");
		String path = PropertyReader.getConvertedFilesPath();
		File[] outputFiles = new File[1];
		File outputFile = new File(path, inputFiles[0].getName());

		BufferedReader inputStream = null;
		BufferedWriter outputStream;
		try {
			outputStream = new BufferedWriter(new FileWriter(outputFile));
			for (int i = 0; i < inputFiles.length; i++) {
				logger
						.debug("HuaweiIVRMemoryStatusConverter.convert() - converting file "
								+ inputFiles[i].getName());
				
				inputStream = new BufferedReader(new FileReader(inputFiles[i]));
	            System.out.println("File [" + i +"]");
				String line;
				String date = "";
				String key="";
				String real="";
				String virtual="";
				String free="";
				while (inputStream.ready()) {
					line = inputStream.readLine();
					if(line.contains("HSystem")&&line.split("HSystem")[1].contains("["))
					{
						date=((line.split("HSystem")[1]).split("\\[")[1].trim()).substring(3);
						try 
						{
							date=getDate(date);
							
							
						} catch(ParseException exc){ logger.error(exc) ; continue ;}
					}
					else if (line.contains("Memory")&&line.contains("real") &&line.contains("virtual")&&line.contains("free"))
					{
						if(line.contains(","))
						{
							if(line.split(",")[0].contains("K"))
								real=Utils.stringBetween(line.split(",")[0], "Memory:", "K").trim();
							if(line.split(",")[1].contains("K"))
								virtual=line.split(",")[1].split("K")[0].trim();
							if(line.split(",")[2].contains("K"))
								free=(line.split(",")[2]).split("K")[0].trim();
							
						
							if(dateVSMemoryStatus.containsKey(date))
							{
								MemoryStatus mem=dateVSMemoryStatus.get(date);
								if(!real.equalsIgnoreCase(""))
								mem.getReal().add(real);
								if(!virtual.equalsIgnoreCase(""))
									mem.getVirtual().add(virtual);
								if (!free.equalsIgnoreCase(""))
									mem.getFree().add(free);
								dateVSMemoryStatus.remove(date);
								dateVSMemoryStatus.put(date, mem);
							}else
							{
								MemoryStatus mem= new MemoryStatus();
								if(!real.equalsIgnoreCase(""))
									mem.getReal().add(real);
									if(!virtual.equalsIgnoreCase(""))
										mem.getVirtual().add(virtual);
									if (!free.equalsIgnoreCase(""))
										mem.getFree().add(free);
									dateVSMemoryStatus.put(date, mem);
							}
							
						}
					}
					else 
						{continue;}
				}
			}
			
		inputStream.close();
		Iterator it=dateVSMemoryStatus.keySet().iterator();
	while(it.hasNext())
		{
			 Object key=it.next();
			 MemoryStatus mem=(MemoryStatus)dateVSMemoryStatus.get(key);
			 double [] minMaxAvgReal=minMaxAvg(mem.getReal());
			 double [] minMaxAvgVirtual=minMaxAvg(mem.getVirtual());
	    	 double [] minMaxAvgFree=minMaxAvg(mem.getFree());
	    	 outputStream.write(key+","+minMaxAvgReal[0]+","+minMaxAvgReal[1]+","+minMaxAvgReal[2]+","+minMaxAvgVirtual[0]+","+minMaxAvgVirtual[1]+","+minMaxAvgVirtual[2]+","+minMaxAvgFree[0]+","+minMaxAvgFree[1]+","+minMaxAvgFree[2]);
			 outputStream.newLine();
		}
		outputStream.close();
		outputFiles[0]=outputFile;
		logger.debug("HuaweiIVRMemoryStatusConverter.convert() - finished converting input files successfully ");
	
	}
	catch (FileNotFoundException e) {
			logger
					.error("HuaweiIVRMemoryStatusConverter.convert() - Input file not found "
							+ e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger
					.error("HuaweiIVRMemoryStatusConverter.convert() - Couldn't read input file"
							+ e);
			throw new ApplicationException(e);
		}
		logger
				.debug("HuaweiIVRMemoryStatusConverter.convert() - finished converting input files successfully ");
		return outputFiles;
		
}
	private double[] minMaxAvg(List data)
	{
		double min,max,avg;
		Double any;
		double[] minMaxAvgArray = new double[3];
		Iterator listIterator = data.iterator(); 
		any=Double.parseDouble((String)listIterator.next());
		avg =any.doubleValue();
		max = avg ;
		min = avg ;	
		while(listIterator.hasNext())
		{
			Double element = Double.parseDouble((String)listIterator.next()); 
		
			if(element.doubleValue()<min)
			{
				min=element.doubleValue();
			}
			if(element.doubleValue()>max)
			{
				max=element.doubleValue();
			}
			avg = avg + element.doubleValue() ; 
		
		}
	
		minMaxAvgArray[0] = min ; 
		minMaxAvgArray[1] = max ;
		minMaxAvgArray[2] = avg / data.size() ;
		return minMaxAvgArray;
	}
	private String getDate(String line) throws ParseException {
		String[] tokens = null;
		Date date = new Date();
		String dateString;
		SimpleDateFormat inDateFormat = new SimpleDateFormat(
				"E MMM dd HH:mm:ss yyyy");
		SimpleDateFormat outDateFormat = new SimpleDateFormat(
				"MM/dd/yyyy HH:00:00");

		
			date = inDateFormat.parse(line);
		dateString = outDateFormat.format(date);
		return dateString;

	}
	public static void main(String ag[]) {
		try {
			
			PropertyReader.init("D:\\build\\phase9\\phase9Builds\\DataCollection");
			HuaweiIVRMemoryStatusConverter s = new HuaweiIVRMemoryStatusConverter();
			File[] input = new File[1];
			input[0]=new File("D:\\build\\phase9\\phase9Builds\\DataCollection\\output_final.txt");
			   s.convert(input,"Maha_Test");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	class MemoryStatus
	{
		List real = new ArrayList();
		public List getReal() {
			return real;
		}
		public void setReal(List real) {
			this.real = real;
		}
		public List getVirtual() {
			return virtual;
		}
		public void setVirtual(List virtual) {
			this.virtual = virtual;
		}
		public List getFree() {
			return free;
		}
		public void setFree(List free) {
			this.free = free;
		}
		List virtual = new ArrayList();
		List free = new ArrayList();
	}
}
