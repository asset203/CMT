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
import com.itworx.vaspp.datacollection.util.converters.VSSMMemoryConverter.Memory;

public class HuaweiIVRCPUStatusConverter extends AbstractTextConverter{
	private Logger logger;
	private Map  <String ,CpuStatus> dateVSCPUStatus=new HashMap<String,CpuStatus>() ;
	public File[] convert(File[] inputFiles, String systemName)
	throws ApplicationException, InputException {
		logger = Logger.getLogger(systemName);
		logger
				.debug("Inside HuaweiIVRCPUStatusConverter convert - started converting input files");
		String path = PropertyReader.getConvertedFilesPath();
		File[] outputFiles = new File[1];
		File outputFile = new File(path, inputFiles[0].getName());

		BufferedReader inputStream = null;
		BufferedWriter outputStream;
		try {
			outputStream = new BufferedWriter(new FileWriter(outputFile));
			for (int i = 0; i < inputFiles.length; i++) {
				logger
						.debug("HuaweiIVRCPUStatusConverter.convert() - converting file "
								+ inputFiles[i].getName());
				
				inputStream = new BufferedReader(new FileReader(inputFiles[i]));
	            System.out.println("File [" + i +"]");
				String line;
				String date = "";
				String key="";
				String sys="";
				String usr="";
				String idle="";
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
					else if(line.contains("CPU") &&line.contains("USER") &&line.contains("SYS")&&line.contains("IDLE"))
					{
						line = inputStream.readLine();
						
						while(!line.contains("---   "))
						{
							if(line.contains("    ")&&line.contains("   "))
							{
							key=date+","+line.split("    ")[0].trim();
						if(line.contains("%"))		
						{		
							   sys=line.split("%")[2].trim();
							   usr=line.split("%")[0].split(" ")[line.split("%")[0].split(" ").length-1];
							   idle=line.split("%")[3].trim();
								if(dateVSCPUStatus.containsKey(key))
								{
									CpuStatus cpuStatus=dateVSCPUStatus.get(key);
									cpuStatus.getUser().add(usr);
									cpuStatus.getSyst().add(sys);
									cpuStatus.getIdle().add(idle);
									dateVSCPUStatus.remove(cpuStatus);
									dateVSCPUStatus.put(key, cpuStatus);
								
								}
								else
								{
									CpuStatus cpuStatus = new CpuStatus();
									cpuStatus.getUser().add(usr);
									cpuStatus.getSyst().add(sys);
									cpuStatus.getIdle().add(idle);
									dateVSCPUStatus.put(key, cpuStatus);
									
								}
								
						}
						}
							line = inputStream.readLine();
						}
						
						
					}
				}
			}
			inputStream.close();
			Iterator it=dateVSCPUStatus.keySet().iterator();
			while(it.hasNext())
			{
				 Object key=it.next();
				 CpuStatus cpuStatus=(CpuStatus)dateVSCPUStatus.get(key);
				 double [] minMaxAvgIdle=minMaxAvg(cpuStatus.getIdle());
				 double [] minMaxAvgSys=minMaxAvg(cpuStatus.getSyst());
		    	 double [] minMaxAvgUser=minMaxAvg(cpuStatus.getUser());
		    	 outputStream.write(key+","+minMaxAvgUser[0]+","+minMaxAvgUser[1]+","+minMaxAvgUser[2]+","+minMaxAvgSys[0]+","+minMaxAvgSys[1]+","+minMaxAvgSys[2]+","+minMaxAvgIdle[0]+","+minMaxAvgIdle[1]+","+minMaxAvgIdle[2]);
				 outputStream.newLine();
			}

			
			outputStream.close();
			outputFiles[0]=outputFile;
			logger.debug("HuaweiIVRCPUStatusConverter.convert() - finished converting input files successfully ");
		
		}
		catch (FileNotFoundException e) {
				logger
						.error("HuaweiIVRCPUStatusConverter.convert() - Input file not found "
								+ e);
				throw new ApplicationException(e);
			} catch (IOException e) {
				logger
						.error("HuaweiIVRCPUStatusConverter.convert() - Couldn't read input file"
								+ e);
				throw new ApplicationException(e);
			}
			logger
					.debug("HuaweiIVRCPUStatusConverter.convert() - finished converting input files successfully ");
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
			HuaweiIVRCPUStatusConverter s = new HuaweiIVRCPUStatusConverter();
			File[] input = new File[1];
			input[0]=new File("D:\\build\\phase9\\phase9Builds\\DataCollection\\output_final.txt");
			   s.convert(input,"Maha_Test");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	class CpuStatus
	{
		List user = new ArrayList();
		public List getUser() {
			return user;
		}
		public void setUser(List user) {
			this.user = user;
		}
		public List getSyst() {
			return syst;
		}
		public void setSyst(List syst) {
			this.syst = syst;
		}
		public List getIdle() {
			return idle;
		}
		public void setIdle(List idle) {
			this.idle = idle;
		}
		List syst = new ArrayList();
		List idle = new ArrayList();
		
	}
}
