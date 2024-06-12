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
import com.itworx.vaspp.datacollection.util.converters.GGSNTempConverter.TacDetails;

public class SDPHWMonitoringConverter extends AbstractTextConverter{
	private Logger logger;
	private Map  <String ,Monitoring> keyVsMem=new HashMap<String,Monitoring>() ;
	public SDPHWMonitoringConverter()
	{}
	public File[] convert(File[] inputFiles, String systemName)
	throws ApplicationException, InputException {
		logger = Logger.getLogger(systemName);
		logger
				.debug("Inside SDPHWMonitoringConverter - started converting input files");
		String path = PropertyReader.getConvertedFilesPath();
		File[] outputFiles = new File[1];
		File outputFile = new File(path, inputFiles[0].getName());

		BufferedReader inputStream = null;
		BufferedWriter outputStream;
		try {
			
			outputStream = new BufferedWriter(new FileWriter(outputFile));
			for (int i = 0; i < inputFiles.length; i++) {
				logger
						.debug("SDPHWMonitoringConverter.convert() - converting file "
								+ inputFiles[i].getName());
			
				
				inputStream = new BufferedReader(new FileReader(inputFiles[i]));
	           
				String line;
				String key="";
				String date = "";
				String temp[] ;
				String result[];
				double mem=0.0;
				double cpu=0.0;
				while (inputStream.ready()) {
					
					line = inputStream.readLine();
					if(line.contains("+++++++++++++++++")&& line.contains(" "))
					{
						try{
						date=getDate(line.split(" ")[1].trim());
						
						} catch(ParseException exc){logger.error(exc) ; continue ;}
						  catch(NumberFormatException exc){logger.error(exc) ; continue ;}
					}
					else if(line.contains("NPROC") &&line.contains("MEMORY") && line.contains("CPU"))
					{
						line = inputStream.readLine();
						while(!line.contains("Total:"))
						{
							temp=line.split(" ");
							result= new String[7];
							int index=0;
							/*this loop to escape the spaces in the line */
							for(int t=0;t<temp.length;t++)
							{
								if(!temp[t].equals("")&&(temp[t]!=null))
								{
									result[index]=temp[t];
									index++;
								}
							}
							mem=Double.parseDouble(result[4].split("%")[0]);
							cpu=Double.parseDouble(result[6].split("%")[0]);
							if(keyVsMem.containsKey(date))
							{
								Monitoring mon=keyVsMem.get(date);
								mon.getMem().add(mem);
								mon.getCpu().add(cpu);
								keyVsMem.remove(mon);
								keyVsMem.put(date, mon);
							}
							else
							{
								Monitoring mon= new Monitoring ();
								mon.getMem().add(mem);
								mon.getCpu().add(cpu);
								keyVsMem.put(date, mon);
							}
							line = inputStream.readLine();
						}
					}
				}
			}
			inputStream.close();
			Iterator it1 = keyVsMem.keySet().iterator();
		
			while ( it1.hasNext()) {
				Object key = it1.next();
				 double [] minMaxAvgMem=minMaxAvg(((Monitoring)keyVsMem.get(key)).getMem());
				 double [] minMaxAvgCpu=minMaxAvg(((Monitoring)keyVsMem.get(key)).getCpu());
				outputStream.write(key + "," + minMaxAvgMem[0]+ ","+minMaxAvgMem[1]+","+minMaxAvgMem[2]+","+minMaxAvgCpu[0]+","+minMaxAvgCpu[1]+","+minMaxAvgCpu[2]);

				outputStream.newLine();
			}
			
			outputStream.close();
			outputFiles[0]=outputFile;
			logger.debug("SDPHWMonitoringConverter.convert() - finished converting input files successfully ");
		
		
	}
		catch (FileNotFoundException e) {
				logger
						.error("SDPHWMonitoringConverter.convert() - Input file not found "
								+ e);
				throw new ApplicationException(e);
			} catch (IOException e) {
				logger
						.error("SDPHWMonitoringConverter.convert() - Couldn't read input file"
								+ e);
				throw new ApplicationException(e);
			}
			
			logger
					.debug("SDPHWMonitoringConverter.convert() - finished converting input files successfully ");
			return outputFiles;
	}
	public static void main(String ag[]) {
		try {
			
			PropertyReader.init("D:\\build\\phase9\\phase9Builds\\DataCollection");
			SDPHWMonitoringConverter s = new SDPHWMonitoringConverter();
			File[] input = new File[1];
			input[0]=new File("D:\\build\\phase9\\phase9Builds\\DataCollection\\CPU-Memory_2011.02.10.log");
			   s.convert(input,"Maha_Test");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	private String getDate(String line) throws ParseException {
		Date date = new Date();
		String dateString;

		SimpleDateFormat inDateFormat = new SimpleDateFormat("yyyy.MM.dd_HH.mm");
		SimpleDateFormat outDateFormat = new SimpleDateFormat(
				"MM/dd/yyyy HH:00:00");

		date = inDateFormat.parse(line);
		dateString = outDateFormat.format(date);
		return dateString;

	}
	class Monitoring 
	{
	 List mem= new ArrayList();
	 public List getMem() {
		return mem;
	}
	public void setMem(List mem) {
		this.mem = mem;
	}
	public List getCpu() {
		return cpu;
	}
	public void setCpu(List cpu) {
		this.cpu = cpu;
	}
	List cpu = new ArrayList();
	} 
	private double[] minMaxAvg(List data)
	{
		double min,max,avg;
		Double any;
		double[] minMaxAvgArray = new double[3];
		Iterator listIterator = data.iterator(); 
		any=(Double)listIterator.next();
		avg =any.doubleValue();
		max = avg ;
		min = avg ;	
		while(listIterator.hasNext())
		{
			Double element = (Double)listIterator.next(); 
		
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
}
