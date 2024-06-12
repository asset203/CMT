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
import com.itworx.vaspp.datacollection.util.converters.VSSMInterfaceConverter.Interface;

public class VSSMMemoryConverter  extends AbstractTextConverter{
	private Logger logger;
	private Map  <String ,Memory> dateVSMemUtilization=new HashMap<String,Memory>() ;
	public VSSMMemoryConverter()
	{}
	
	public File[] convert(File[] inputFiles, String systemName)
	throws ApplicationException, InputException {
		logger = Logger.getLogger(systemName);
		logger
				.debug("Inside VSSMNetInitConverter convert - started converting input files");
		String path = PropertyReader.getConvertedFilesPath();
		File[] outputFiles = new File[1];
		File outputFile = new File(path, inputFiles[0].getName());

		BufferedReader inputStream = null;
		BufferedWriter outputStream;
		try {
			outputStream = new BufferedWriter(new FileWriter(outputFile));
			for (int i = 0; i < inputFiles.length; i++) {
				logger
						.debug("VSSMNetInitConverter.convert() - converting file "
								+ inputFiles[i].getName());
				
				inputStream = new BufferedReader(new FileReader(inputFiles[i]));
	            System.out.println("File [" + i +"]");
				String line;
				String date = "";
				while (inputStream.ready()) {
					line = inputStream.readLine();
					if(!line.contains(":"))//to skip the first tow lines
					{
						continue;
					}
					String columns []=line.split(" ");
					 List real = new ArrayList();
				
					  for(int k=0;k<columns.length;k++)
					  {
						  if(!columns[k].equalsIgnoreCase("") && !columns[k].equalsIgnoreCase(" "))
						  { 
							  real.add(columns[k]); 
							 
						  }
						  
					  }
					  try
					  {
						 date= getDate(real.get(0).toString()+" "+real.get(1));
						
					  }
					  catch(ParseException exc){ logger.error(exc) ; continue ;}
					  
					  if(dateVSMemUtilization.containsKey(date))
					  {
						  
						  Double totalmem= Double .parseDouble(real.get(4).toString());
						  Double freemem = Double .parseDouble(real.get(2).toString());
						  Double  totalswap= Double .parseDouble(real.get(5).toString());
						  Double  freeswap = Double .parseDouble(real.get(3).toString());
						  Memory mem =(Memory)dateVSMemUtilization.get(date);
						  //System.out.println("the memory value :"+(totalmem-freemem)/totalmem+"the swap:      :"+(totalswap-freeswap)/totalswap);
				
						  mem.getMemUtilizeList().add(((totalmem-freemem)/totalmem)+"");
						  mem.getSwapUtilizationList().add(((totalswap-freeswap)/totalswap)+"");
						  
						  dateVSMemUtilization.remove(date);
                          dateVSMemUtilization.put(date, mem);
					  }
					  else
					  {
						  Memory mem= new Memory();
						  Double totalmem= Double .parseDouble(real.get(4).toString());
						  Double freemem = Double .parseDouble(real.get(2).toString());
						  Double  totalswap= Double .parseDouble(real.get(5).toString());
						  Double  freeswap = Double .parseDouble(real.get(3).toString());
						 
						  mem.getMemUtilizeList().add(((totalmem-freemem)/totalmem)+"");
						  mem.getSwapUtilizationList().add(((totalswap-freeswap)/totalswap)+"");
						 // System.out.println("the memory value :"+(totalmem-freemem)/totalmem+"the swap:      :"+(totalswap-freeswap)/totalswap);
                          dateVSMemUtilization.put(date, mem);
						  
					  }
				}
			}//end of the file
			Iterator it=dateVSMemUtilization.keySet().iterator();
			while(it.hasNext())
			{
				 Object key=it.next();
				 Memory mem=(Memory)dateVSMemUtilization.get(key);
				 double [] minMaxAvgMemUti=minMaxAvg(mem.getMemUtilizeList());
		    	 double [] minMaxAvgSWAPUtil=minMaxAvg(mem.getSwapUtilizationList());
		    	 outputStream.write(key+","+minMaxAvgMemUti[0]+","+minMaxAvgMemUti[1]+","+minMaxAvgMemUti[2]+","+minMaxAvgSWAPUtil[0]+","+minMaxAvgSWAPUtil[1]+","+minMaxAvgSWAPUtil[2]);
				 outputStream.newLine();
			}
			inputStream.close();
			
			outputStream.close();
			outputFiles[0]=outputFile;
			logger.debug("VSSMNetInitConverter.convert() - finished converting input files successfully ");
		
		}
		catch (FileNotFoundException e) {
				logger
						.error("VSSMNetInitConverter.convert() - Input file not found "
								+ e);
				throw new ApplicationException(e);
			} catch (IOException e) {
				logger
						.error("VSSMNetInitConverter.convert() - Couldn't read input file"
								+ e);
				throw new ApplicationException(e);
			}
			logger
					.debug("VSSMNetInitConverter.convert() - finished converting input files successfully ");
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
	public static void main(String ag[]) {
		try {
			
			PropertyReader.init("D:\\build\\VASPortal\\DataCollection");
			VSSMMemoryConverter s = new VSSMMemoryConverter();
			File[] input = new File[1];
			input[0]=new File("D:\\build\\VASPortal\\DataCollection\\mem-snapshot.out_2010-06-03.Thursday");
			   s.convert(input,"Maha_Test");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	class Memory 
	{
		List memUtilizeList = new ArrayList();
		public List getMemUtilizeList() {
			return memUtilizeList;
		}
		public void setMemUtilizeList(List memUtilizeList) {
			this.memUtilizeList = memUtilizeList;
		}
		public List getSwapUtilizationList() {
			return swapUtilizationList;
		}
		public void setSwapUtilizationList(List swapUtilizationList) {
			this.swapUtilizationList = swapUtilizationList;
		}
		List swapUtilizationList = new ArrayList();
	}

}
