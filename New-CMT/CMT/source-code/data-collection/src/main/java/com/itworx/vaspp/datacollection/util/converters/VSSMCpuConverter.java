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
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Iterator;
import org.apache.log4j.Logger;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;

public class VSSMCpuConverter extends AbstractTextConverter {
	private Logger logger;
	private Map<String , Object> allLists = new HashMap<String, Object>() ;
	private Map<String , String> dateVSMinMaxAvg = new HashMap<String, String>() ;
	public VSSMCpuConverter()
	{}
	public File[] convert(File[] inputFiles, String systemName)
	throws ApplicationException, InputException {
		logger = Logger.getLogger(systemName);
		logger
				.debug("Inside VSSMCpuConverter convert - started converting input files");
		String path = PropertyReader.getConvertedFilesPath();
		File[] outputFiles = new File[1];
		File outputFile = new File(path, inputFiles[0].getName());

		BufferedReader inputStream = null;
		BufferedWriter outputStream;
		try {
			outputStream = new BufferedWriter(new FileWriter(outputFile));
			for (int i = 0; i < inputFiles.length; i++) {
				logger
						.debug("VSSMCpuConverter.convert() - converting file "
								+ inputFiles[i].getName());
				
				inputStream = new BufferedReader(new FileReader(inputFiles[i]));
	           // System.out.println("File [" + i +"]");
				String line;
				String date = "";
				while (inputStream.ready()) {
                 
					line = inputStream.readLine();
					if(!line.contains(":"))//to skip the first tow lines
					{
						continue;
					}
					/*String columns[]=line.split("       ");
					String lastColumns[]=columns[3].split("      ");*/
					/**/
					String columns []=line.split(" ");
					 List real = new ArrayList();
				
					  for(int k=0;k<columns.length;k++)
					  {
						  if(!columns[k].equalsIgnoreCase("") && !columns[k].equalsIgnoreCase(" "))
						  { 
							  real.add(columns[k]); 
							 
						  }
						  
					  }
					/**/
					try
					{
					  date = getDate(real.get(0)+" "+real.get(1));
					}
					catch(ParseException exc) { logger.error(exc) ; continue ;}
					
					if(allLists.containsKey(date))
					{
						myLists clazz=(myLists)allLists.get(date);
						clazz.getUser().add(real.get(2));
						clazz.getSys().add(real.get(3));
						clazz.getWio().add(real.get(4));
						clazz.getIdle().add(real.get(5));
						allLists.remove(date);
						allLists.put(date, clazz);
					}
					else
					{
						myLists clasz= new myLists();
						clasz.getUser().add(real.get(2));
						clasz.getSys().add(real.get(3));
						clasz.getWio().add(real.get(4));
						clasz.getIdle().add(real.get(5));
						allLists.put(date, clasz);
					}
					
				}//while
				
				dateVSMinMaxAvg=getMinMaxAvg(allLists);
			}
			Iterator myVeryOwnIterator =dateVSMinMaxAvg.keySet().iterator();
			while(myVeryOwnIterator.hasNext()){ 					
				Object key = myVeryOwnIterator.next();
				String  maxMinAvg = dateVSMinMaxAvg.get(key);
					outputStream.write(key+","+maxMinAvg);
					outputStream.newLine();
			}
		
inputStream.close();
		
		outputStream.close();
		outputFiles[0]=outputFile;
		logger.debug("VSSMCpuConverter.convert() - finished converting input files successfully ");
	
	}
	catch (FileNotFoundException e) {
			logger
					.error("VSSMCpuConverter.convert() - Input file not found "
							+ e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger
					.error("VSSMCpuConverter.convert() - Couldn't read input file"
							+ e);
			throw new ApplicationException(e);
		}
		logger
				.debug("VSSMCpuConverter.convert() - finished converting input files successfully ");
		return outputFiles;
	}
	public Map getMinMaxAvg(Map map)
	{
		 Map<String , String> dateVSMinMaxAvg = new HashMap<String, String>() ;
		Iterator it=map.keySet().iterator();
		while(it.hasNext())
		{
			Object key=it.next();
			myLists clazz=(myLists)map.get(key);
			double users[]=minMaxAvg(clazz.getUser());
			double systems[]=minMaxAvg(clazz.getSys());
			double wio[]=minMaxAvg(clazz.getWio());
			double idle[]=minMaxAvg(clazz.getIdle());
			dateVSMinMaxAvg.put(key.toString(), users[0]+","+users[1]+","+users[2]+","+systems[0]+","+systems[1]+","+systems[2]+","+wio[0]+","+wio[1]+","+wio[2]+","+idle[0]+","+idle[1]+","+idle[2]);
		}
		
		return dateVSMinMaxAvg;
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
		//System.out.println("Initial Average" + avg+",") ;
		//System.out.println(data.size());
		while(listIterator.hasNext())
		{
			Double element = Double.parseDouble((String)listIterator.next()); 
		//	System.out.println("element: " + ((Long)element).longValue()) ;
			if(element.doubleValue()<min)
			{
				min=element.doubleValue();
			}
			if(element.doubleValue()>max)
			{
				max=element.doubleValue();
			}
			avg = avg + element.doubleValue() ; 
		//	System.out.println(avg + ",") ;
		}
	//	System.out.println();
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
				"dd/MM/yyyy HH:mm:ss");
		SimpleDateFormat outDateFormat = new SimpleDateFormat(
				"MM/dd/yyyy HH:00:00");

		
			date = inDateFormat.parse(line);
		dateString = outDateFormat.format(date);
		return dateString;

	}
	class myLists
	{
		List user= new ArrayList();
		List sys= new ArrayList();
		List wio= new ArrayList();
		List idle= new ArrayList();
		public void setUser(List user)
		{
			this.user=user;
		}
		public List getUser()
		{
			return this.user;
		}
		public void setSys(List sys)
		{
			this.sys=sys;
		}
		public List getSys()
		{
			return this.sys;
		}
		public void setWio(List wio)
		{
			this.wio=wio;
		}
		public List getWio()
		{
			return this.wio;
		}
		public void setIdle(List idle)
		{
			this.idle=idle;
		}
		public List getIdle()
		{
			return this.idle;
		}
	}
	public static void main(String ag[]) {
		try {
			
			PropertyReader.init("D:\\build\\VASPortal\\DataCollection");
			VSSMCpuConverter s = new VSSMCpuConverter();
			File[] input = new File[2];
			input[0]=new File("D:\\build\\VASPortal\\DataCollection\\cpu-snapshot.out_2010-05-27.Thursday");
			input[1]=new File("D:\\build\\VASPortal\\DataCollection\\cpu.Thursday");
			
               s.convert(input,"Maha_Test");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
