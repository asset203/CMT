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
import com.itworx.vaspp.datacollection.util.converters.VSSMDiskConverter.Device;

public class VSSMInterfaceConverter  extends AbstractTextConverter{
	private Logger logger;
	private Map  <String ,List> dateVSInterface=new HashMap<String,List>() ;
	public VSSMInterfaceConverter()
	{
	
	}
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
							  { real.add(columns[k]);  }
							  
						  }
						  try
						  {
							 date= getDate(real.get(0).toString()+" "+real.get(1));
						  }
						  catch(ParseException exc){ logger.error(exc) ; continue ;}
						  boolean found =false;
							  if(dateVSInterface.containsKey(date))
								  
							  {
								  List interfaces =(List)dateVSInterface.get(date);
									for(int k=0;k<interfaces.size();k++){
									if(((Interface)interfaces.get(k)).getName().equalsIgnoreCase(real.get(2).toString()))
									{
										((Interface)interfaces.get(k)).getRkbList().add(real.get(3));
										((Interface)interfaces.get(k)).getWkbList().add(real.get(4));
										((Interface)interfaces.get(k)).getPerUtilizeList().add(real.get(9));
										((Interface)interfaces.get(k)).getSatList().add(real.get(10));
										
										found= true;
										dateVSInterface.remove(date);
										dateVSInterface.put(date, interfaces);
										break;
									}
									
									
									}
									if(!found)
									{
										Interface interfac= new Interface();
										interfac.setName(real.get(2).toString());
										interfac.getRkbList().add(real.get(3));
										interfac.getWkbList().add(real.get(4));
										interfac.getPerUtilizeList().add(real.get(9));
										interfac.getSatList().add(real.get(10));
										interfaces.add(interfac);
										dateVSInterface.remove(date);
										dateVSInterface.put(date, interfaces);
										
										
									}
							  }
							  else
							  {
								  
								  Interface interfac= new Interface();
									interfac.setName(real.get(2).toString());
									interfac.getRkbList().add(real.get(3));
									interfac.getWkbList().add(real.get(4));
									interfac.getPerUtilizeList().add(real.get(9));
									interfac.getSatList().add(real.get(10));
								   
									List interfList = new ArrayList();
									
									interfList.add(interfac);
									dateVSInterface.put(date,interfList);
							  }
						  
					}
				}//end of the file 
				Iterator it =dateVSInterface.keySet().iterator();
				while (it.hasNext())
				{
					Object key=it.next();
					List interfaces=(List)dateVSInterface.get(key);
				     for (int h=0;h<interfaces.size();h++)
				     {
				    	 Interface interfac=(Interface)interfaces.get(h);
				    	 double[] minMaxAvgRKB=minMaxAvg(interfac.getRkbList());
				    	 double [] minMaxAvgWKB=minMaxAvg(interfac.getWkbList());
				    	 double [] minMaxAvgUTIL=minMaxAvg(interfac.getPerUtilizeList());
				    	 double [] minMaxAvgAvgSAT=minMaxAvg(interfac.getSatList());
				    	 outputStream.write(key+","+interfac.getName()+","+minMaxAvgRKB[0]+","+minMaxAvgRKB[1]+","+minMaxAvgRKB[2]+","+minMaxAvgWKB[0]+","+minMaxAvgWKB[1]+","+minMaxAvgWKB[2]+","+minMaxAvgUTIL[0]+","+minMaxAvgUTIL[1]+","+minMaxAvgUTIL[2]+","+minMaxAvgAvgSAT[0]+","+minMaxAvgAvgSAT[1]+","+minMaxAvgAvgSAT[2]);
						outputStream.newLine();
				     }
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
			long  another =Math.round((avg / data.size())*1000);
			double anotherone=(Math.round((avg / data.size())*1000))/1000;
			minMaxAvgArray[2] = avg / data.size();
			
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
				VSSMInterfaceConverter s = new VSSMInterfaceConverter();
				File[] input = new File[1];
				input[0]=new File("D:\\build\\VASPortal\\DataCollection\\nic-snapshot.out_2010-05-27.Thursday");
				   s.convert(input,"Maha_Test");
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		class Interface
		{
			public String name;
			public List rkbList= new ArrayList();	
			public List wkbList= new ArrayList();
			public List perUtilizeList= new ArrayList();
			public List satList= new ArrayList();
			public String getName() {
				return name;
			}
			public void setName(String name) {
				this.name = name;
			}
			public List getRkbList() {
				return rkbList;
			}
			public void setRkbList(List rkbList) {
				this.rkbList = rkbList;
			}
			public List getWkbList() {
				return wkbList;
			}
			public void setWkbList(List wkbList) {
				this.wkbList = wkbList;
			}
			public List getPerUtilizeList() {
				return perUtilizeList;
			}
			public void setPerUtilizeList(List perUtilizeList) {
				this.perUtilizeList = perUtilizeList;
			}
			public List getSatList() {
				return satList;
			}
			public void setSatList(List satList) {
				this.satList = satList;
			}
			
		}
}
