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


public class SDPNewLoadRegConverter  extends AbstractTextConverter{
	private Logger logger;
	private Map  <String ,LoadRegulation> dateVssumm =new HashMap<String,LoadRegulation>() ;
	private Map  <String ,List> dateVsTotal =new HashMap<String,List>() ;
	private Map  <String ,List> dateVsFirst =new HashMap<String,List>() ;
	private Map  <String ,List> dateVsInter =new HashMap<String,List>() ;
	private Map  <String ,List> dateVsFinal =new HashMap<String,List>() ;
	
public SDPNewLoadRegConverter()
{
	
}
public File[] convert(File[] inputFiles, String systemName)
throws ApplicationException, InputException {
	logger = Logger.getLogger(systemName);	
	logger
			.debug("Inside SDPNewLoadRegConverter convert - started converting input files");
	String path = PropertyReader.getConvertedFilesPath();
	File[] outputFiles = new File[1];
	File outputFile = new File(path, inputFiles[0].getName());

	BufferedReader inputStream = null;
	BufferedWriter outputStream;
	try {
		outputStream = new BufferedWriter(new FileWriter(outputFile));
		for (int i = 0; i < inputFiles.length; i++) {
			logger
					.debug("SDPNewLoadRegConverter.convert() - converting file "
							+ inputFiles[i].getName());
			
			inputStream = new BufferedReader(new FileReader(inputFiles[i]));
            
			String line;
			String part1 = "";
			String part2 = "";
			String date =null;
			long totalSucc=0;
			long totalFail=0;
			long totalthruput=0;
			long firstSucc=0;
			long firstFail=0;
			long firstthruput=0;
			long interSucc=0;
			long interFail=0;
			long interthruput=0;
			long finalSucc=0;
			long finalFail=0;
			long finalthruput=0;
			long regSucc=0;
			long regFail=0;
			while (inputStream.ready()) {
				line = inputStream.readLine();
				try
				{
				if(line.contains("Name")&&line.contains("Succ")&&line.contains("Response"))
				{
					part1=line.split("Name")[0].trim();
					
				}
				else if (line.contains("Total")&&line.contains(" "))
				{
					String columns []=line.split(" ");
					  List real = new ArrayList();
					
						  for(int k=0;k<columns.length;k++)
						  {
							  if(!columns[k].equalsIgnoreCase("") && !columns[k].equalsIgnoreCase(" "))
							  { 
								  real.add(columns[k]);  
								 
							  }							  
						  }
						  part2=real.get(0).toString();					
						  date=getDate(part1+part2);
						  totalSucc= Long.parseLong(real.get(2).toString()!=null? real.get(2).toString():"0");
						  totalFail= Long.parseLong(real.get(3).toString()!=null? real.get(3).toString():"0");
						  totalthruput= Long.parseLong(real.get(4).toString()!=null? real.get(4).toString():"0");
								if(date!=null)
								{
						  if(dateVssumm.containsKey(date))
								{
									LoadRegulation obj=(LoadRegulation)dateVssumm.get(date);
									obj.setTotalInterrogationSucc(obj.getTotalInterrogationSucc()+totalSucc);
									obj.setTotalInterrogationFail(obj.getTotalInterrogationFail()+totalFail);
									dateVssumm.put(date, obj);
								}
								else
								{   
									LoadRegulation obj= new LoadRegulation();
									obj.setTotalInterrogationSucc(totalSucc);
									obj.setTotalInterrogationFail(totalFail);
									dateVssumm.put(date, obj);
								}
								if(dateVsTotal.containsKey(date))
								{
									List list=dateVsTotal.get(date);
									list.add(totalthruput);
									dateVsTotal.put(date, list);
								}else
								{
									List list= new ArrayList();
									list.add(totalthruput);
									dateVsTotal.put(date, list);
								}
								}
				}
				else if (line.contains("   First")&&line.contains(" "))
				{
					String columns []=line.split(" ");
					  List real = new ArrayList();
					
						  for(int k=0;k<columns.length;k++)
						  {
							  if(!columns[k].equalsIgnoreCase("") && !columns[k].equalsIgnoreCase(" "))
							  { 
								  real.add(columns[k]);  
								 
							  }							  
						  }
						 
						  firstSucc= Long.parseLong(real.get(1).toString()!=null? real.get(1).toString():"0");						  
						  firstFail= Long.parseLong(real.get(2).toString()!=null? real.get(2).toString():"0");						  
						  firstthruput= Long.parseLong(real.get(3).toString()!=null? real.get(3).toString():"0");
						  
						  if(date!=null)
							{
					         if(dateVssumm.containsKey(date))
					           { 
					        	 LoadRegulation obj =(LoadRegulation)dateVssumm.get(date);
					        	 obj.setFirstInterrogationSucc(obj.getFirstInterrogationSucc()+firstSucc);
					        	 obj.setFirstInterrogationFail(obj.getFirstInterrogationFail()+firstFail);
					        	 dateVssumm.put(date, obj);
							   }
					         else
					         {
					        	 LoadRegulation obj= new LoadRegulation();
					        	 obj.setFirstInterrogationSucc(obj.getFirstInterrogationSucc()+firstSucc);
					        	 obj.setFirstInterrogationFail(obj.getFirstInterrogationFail()+firstFail);
					        	 dateVssumm.put(date, obj);
					         }
					         if(dateVsFirst.containsKey(date))
					         {
					        	 List list=dateVsFirst.get(date);
					        	 list.add(firstthruput);
					        	 dateVsFirst.put(date, list);
					        	 
					         }else
					         {
					        	 List list=new ArrayList();
					        	 list.add(firstthruput);
					        	 dateVsFirst.put(date, list);
					         }
				          }
						
				}
				else if (line.contains("Intermediate")&&line.contains(" "))
				{
					String columns []=line.split(" ");
					  List real = new ArrayList();
					
						  for(int k=0;k<columns.length;k++)
						  {
							  if(!columns[k].equalsIgnoreCase("") && !columns[k].equalsIgnoreCase(" "))
							  { 
								  real.add(columns[k]);  
								 
							  }							  
						  }
						 
						  interSucc= Long.parseLong(real.get(1).toString()!=null? real.get(1).toString():"0");
						  interFail= Long.parseLong(real.get(2).toString()!=null? real.get(2).toString():"0");						  
						  interthruput= Long.parseLong(real.get(3).toString()!=null? real.get(3).toString():"0");
						  
						  if(date!=null)
							{
					         if(dateVssumm.containsKey(date))
					           { 
					        	 LoadRegulation obj =(LoadRegulation)dateVssumm.get(date);
					        	 obj.setInterInterrogationSucc(obj.getInterInterrogationSucc()+interSucc);
					        	 obj.setInterInterrogationFail(obj.getInterInterrogationFail()+interFail);
					        	 dateVssumm.put(date, obj);
							   }
					         else
					         {
					        	 LoadRegulation obj= new LoadRegulation();
					        	 obj.setInterInterrogationSucc(obj.getInterInterrogationSucc()+interSucc);
					        	 obj.setInterInterrogationFail(obj.getInterInterrogationFail()+interFail);
					        	 dateVssumm.put(date, obj);
					         }
					         if(dateVsInter.containsKey(date))
					         {
					        	 List list=dateVsInter.get(date);
					        	 list.add(interthruput);
					        	 dateVsInter.put(date, list);
					        	 
					         }else
					         {
					        	 List list=new ArrayList();
					        	 list.add(interthruput);
					        	 dateVsInter.put(date, list);
					         }
				          }
						
				}
				else if (line.contains("Final")&&line.contains(" "))
				{
					
					String columns []=line.split(" ");
					  List real = new ArrayList();
					
						  for(int k=0;k<columns.length;k++)
						  {
							  if(!columns[k].equalsIgnoreCase("") && !columns[k].equalsIgnoreCase(" "))
							  { 
								  real.add(columns[k]);  
								 
							  }							  
						  }
						 
						  finalSucc= Long.parseLong(real.get(1).toString()!=null? real.get(1).toString():"0");
						  finalFail= Long.parseLong(real.get(2).toString()!=null? real.get(2).toString():"0");						  
						  finalthruput= Long.parseLong(real.get(3).toString()!=null? real.get(3).toString():"0");
						  
						  if(date!=null)
							{
					         if(dateVssumm.containsKey(date))
					           { 
					        	 LoadRegulation obj =(LoadRegulation)dateVssumm.get(date);
					        	 obj.setFinalInterrogationSucc(obj.getFinalInterrogationSucc()+finalSucc);
					        	 obj.setFinalInterrogationFail(obj.getFinalInterrogationFail()+finalFail);
					        	 dateVssumm.put(date, obj);
							   }
					         else
					         {
					        	 LoadRegulation obj= new LoadRegulation();
					        	 obj.setFinalInterrogationSucc(obj.getFinalInterrogationSucc()+finalSucc);
					        	 obj.setFinalInterrogationFail(obj.getFinalInterrogationFail()+finalFail);
					        	 dateVssumm.put(date, obj);
					         }
					         if(dateVsFinal.containsKey(date))
					         {
					        	 List list=dateVsFinal.get(date);
					        	 list.add(finalthruput);
					        	 dateVsFinal.put(date, list);
					        	 
					         }else
					         {
					        	 List list=new ArrayList();
					        	 list.add(finalthruput);
					        	 dateVsFinal.put(date, list);
					         }
				          }
						
				}
				else if (line.contains("Regulated First")&&line.contains(" "))
				{
					
					String columns []=line.split(" ");
					  List real = new ArrayList();
					
						  for(int k=0;k<columns.length;k++)
						  {
							  if(!columns[k].equalsIgnoreCase("") && !columns[k].equalsIgnoreCase(" "))
							  { 
								  real.add(columns[k]);  
								 
							  }							  
						  }
						 
						  regSucc= Long.parseLong(real.get(2).toString()!=null? real.get(2).toString():"0");
						  regFail= Long.parseLong(real.get(3).toString()!=null? real.get(3).toString():"0");						  
						 
						  
						  if(date!=null)
							{
					         if(dateVssumm.containsKey(date))
					           { 
					        	 LoadRegulation obj =(LoadRegulation)dateVssumm.get(date);
					        	 obj.setRegInterrogationSucc(obj.getRegInterrogationSucc()+regSucc);
					        	 obj.setRegInterrogationFail(obj.getRegInterrogationFail()+regFail);
					        	 dateVssumm.put(date, obj);
							   }
					         else
					         {
					        	 LoadRegulation obj= new LoadRegulation();
					        	 obj.setRegInterrogationSucc(obj.getRegInterrogationSucc()+regSucc);
					        	 obj.setRegInterrogationFail(obj.getRegInterrogationFail()+regFail);
					        	 dateVssumm.put(date, obj);
					         }
					        
				          }
						
				}
				}catch(ParseException exc){ logger.error(exc) ; continue ;}
				catch(NumberFormatException exc){ logger.error(exc) ; continue ;}
				
			}
		}//end of files
		Iterator it=dateVssumm.keySet().iterator();
		while (it.hasNext())
		{
			Object key=it.next();
			LoadRegulation obj=(LoadRegulation)dateVssumm.get(key);
			long totalMax=getMax((List)dateVsTotal.get(key));
			long firstMax=getMax((List)dateVsFirst.get(key));
			long interMax=getMax((List)dateVsInter.get(key));
			long finalMax=getMax((List)dateVsFinal.get(key));
			outputStream.write(key.toString()+","+obj.getTotalInterrogationSucc()+","+obj.getTotalInterrogationFail()+","+
					obj.getFirstInterrogationSucc()+","+obj.getFirstInterrogationFail()+","+
					obj.getInterInterrogationSucc()+","+obj.getInterInterrogationFail()+","+
					obj.getFinalInterrogationSucc()+","+obj.getFinalInterrogationFail()+","+
					obj.getRegInterrogationSucc()+","+obj.getRegInterrogationFail()+","+
					totalMax+","+firstMax+","+interMax+","+finalMax);
			outputStream.newLine();
		}
	inputStream.close();
				
				outputStream.close();
				outputFiles[0]=outputFile;
				logger.debug("SDPNewLoadRegConverter.convert() - finished converting input files successfully ");
			
			}
			catch (FileNotFoundException e) {
					logger
							.error("SDPNewLoadRegConverter.convert() - Input file not found "
									+ e);
					throw new ApplicationException(e);
				} catch (IOException e) {
					logger
							.error("SDPNewLoadRegConverter.convert() - Couldn't read input file"
									+ e);
					throw new ApplicationException(e);
				}
				logger
						.debug("SDPNewLoadRegConverter.convert() - finished converting input files successfully ");
				return outputFiles;
			}
		private long getMax(List data)
		{
			long min,max;
			long any;			
			Iterator listIterator = data.iterator(); 		
			any=(Long) listIterator.next();			
			max = any ;
			min = any ;			
			while(listIterator.hasNext())
			{
				long element = (Long) listIterator.next(); 			
				if(element<min)		
				{
					min=element;
				}
				if(element>max)
				{
					max=element;
				}
		     }
			return max;
		}
		private String getDate(String line) throws ParseException {
			String[] tokens = null;
			Date date = new Date();
			String dateString;
			SimpleDateFormat inDateFormat = new SimpleDateFormat(
					"yy-MM-ddHH:mm:ss");
			SimpleDateFormat outDateFormat = new SimpleDateFormat(
					"MM/dd/yyyy HH:00:00");

			
				date = inDateFormat.parse(line);
			dateString = outDateFormat.format(date);
			return dateString;

		}
		public static void main(String ag[]) {
			try {
				
				PropertyReader.init("D:\\build\\phase10\\DataCollection");
				SDPNewLoadRegConverter s = new SDPNewLoadRegConverter();
				File[] input = new File[1];
				input[0]=new File("D:\\build\\phase10\\DataCollection\\PSC-SDPInapHD_1.0_A_1.stat.0");
				   s.convert(input,"Maha_Test");
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		class LoadRegulation
		{
		public long totalInterrogationFail=0;
		public long totalInterrogationSucc=0;
		public long firstInterrogationFail=0;
		public long firstInterrogationSucc=0;
		public long getFirstInterrogationSucc() {
			return firstInterrogationSucc;
		}
		public void setFirstInterrogationSucc(long firstInterrogationSucc) {
			this.firstInterrogationSucc = firstInterrogationSucc;
		}
		public long interInterrogationFail=0;
		public long interInterrogationSucc=0;
		public long FinalInterrogationFail=0;
		public long FinalInterrogationSucc=0;
		public long RegInterrogationFail=0;
		public long RegInterrogationSucc=0;
		public long getTotalInterrogationFail() {
			return totalInterrogationFail;
		}
		public void setTotalInterrogationFail(long totalInterrogationFail) {
			this.totalInterrogationFail = totalInterrogationFail;
		}
		public long getTotalInterrogationSucc() {
			return totalInterrogationSucc;
		}
		public void setTotalInterrogationSucc(long totalInterrogationSucc) {
			this.totalInterrogationSucc = totalInterrogationSucc;
		}
		public long getFirstInterrogationFail() {
			return firstInterrogationFail;
		}
		public void setFirstInterrogationFail(long firstInterrogationFail) {
			this.firstInterrogationFail = firstInterrogationFail;
		}
		
		public long getInterInterrogationFail() {
			return interInterrogationFail;
		}
		public void setInterInterrogationFail(long interInterrogationFail) {
			this.interInterrogationFail = interInterrogationFail;
		}
		public long getInterInterrogationSucc() {
			return interInterrogationSucc;
		}
		public void setInterInterrogationSucc(long interInterrogationSucc) {
			this.interInterrogationSucc = interInterrogationSucc;
		}
		public long getFinalInterrogationFail() {
			return FinalInterrogationFail;
		}
		public void setFinalInterrogationFail(long finalInterrogationFail) {
			FinalInterrogationFail = finalInterrogationFail;
		}
		public long getFinalInterrogationSucc() {
			return FinalInterrogationSucc;
		}
		public void setFinalInterrogationSucc(long finalInterrogationSucc) {
			FinalInterrogationSucc = finalInterrogationSucc;
		}
		public long getRegInterrogationFail() {
			return RegInterrogationFail;
		}
		public void setRegInterrogationFail(long regInterrogationFail) {
			RegInterrogationFail = regInterrogationFail;
		}
		public long getRegInterrogationSucc() {
			return RegInterrogationSucc;
		}
		public void setRegInterrogationSucc(long regInterrogationSucc) {
			RegInterrogationSucc = regInterrogationSucc;
		}
		
		}
}
