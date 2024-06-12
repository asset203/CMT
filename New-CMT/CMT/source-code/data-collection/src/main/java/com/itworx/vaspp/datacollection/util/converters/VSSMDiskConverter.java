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
import com.itworx.vaspp.datacollection.util.converters.VSSMFileSystemConverter.FileSystem;

public class VSSMDiskConverter  extends AbstractTextConverter{
	private Logger logger;
	private Map  <String ,List> dateVSDecice=new HashMap<String,List>() ;
	public VSSMDiskConverter()
	{}
	
	public File[] convert(File[] inputFiles, String systemName)
	throws ApplicationException, InputException {
		logger = Logger.getLogger(systemName);
		logger
				.debug("Inside VSSMDiskConverter convert - started converting input files");
		String path = PropertyReader.getConvertedFilesPath();
		File[] outputFiles = new File[1];
		File outputFile = new File(path, inputFiles[0].getName());

		BufferedReader inputStream = null;
		BufferedWriter outputStream;
		try {
			outputStream = new BufferedWriter(new FileWriter(outputFile));
			for (int i = 0; i < inputFiles.length; i++) {
				logger
						.debug("VSSMDiskConverter.convert() - converting file "
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
					//String dateline[]=line.split("     ");
					try
					{
					  /*date = getDate(dateline[0]);
					  String columns[]=dateline[1].split(" ");
					 List real = new ArrayList();
					
					  for(int k=0;k<columns.length;k++)
					  {
						  if(!columns[k].equalsIgnoreCase("") && !columns[k].equalsIgnoreCase(" "))
						  {
							  real.add(columns[k]);  
						  }
						  
					  }*/
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
						  date =getDate(real.get(0)+" "+real.get(1));
					  /**/
					  boolean found =false;
					  if(dateVSDecice.containsKey(date))
					  
					  {
						  List devices =(List)dateVSDecice.get(date);
							for(int k=0;k<devices.size();k++){
							if(((Device)devices.get(k)).getDeviceName().equalsIgnoreCase(real.get(12).toString()))
							{
								((Device)devices.get(k)).getBusy().add(real.get(11));
								((Device)devices.get(k)).getWait().add(real.get(10));
								((Device)devices.get(k)).getAvgService().add(real.get(9));
								((Device)devices.get(k)).getAvgWait().add(real.get(8));
								((Device)devices.get(k)).getAvgNo().add(real.get(7));
								((Device)devices.get(k)).getKw().add(real.get(5));
								((Device)devices.get(k)).getKr().add(real.get(4));
								found= true;
								dateVSDecice.remove(date);
								dateVSDecice.put(date, devices);
								break;
							}
							
							
							}
							if(!found)
							{
								Device device= new Device();
								device.setDeviceName(real.get(12).toString());
								device.getBusy().add(real.get(11));
								device.getWait().add(real.get(10));
							    device.getAvgService().add(real.get(9));
								device.getAvgWait().add(real.get(8));
								device.getAvgNo().add(real.get(7));
								device.getKw().add(real.get(5));
								device.getKr().add(real.get(4));
								
								devices.add(device);
								dateVSDecice.remove(date);
								dateVSDecice.put(date, devices);
								
								
							}
					  }
					  else
					  {
						  
						   Device device= new Device();
							device.setDeviceName(real.get(12).toString());
							device.getBusy().add(real.get(11));
							device.getWait().add(real.get(10));
						    device.getAvgService().add(real.get(9));
							device.getAvgWait().add(real.get(8));
							device.getAvgNo().add(real.get(7));
							device.getKw().add(real.get(5));
							device.getKr().add(real.get(4));
						   
							List deviceList = new ArrayList();
							
							deviceList.add(device);
							dateVSDecice.put(date,deviceList);
					  }
					
					}
					catch(ParseException exc){ logger.error(exc) ; continue ;}
					
				}
			}//end of the file 
			
			Iterator it =dateVSDecice.keySet().iterator();
			while (it.hasNext())
			{
				Object key=it.next();
				List devices=(List)dateVSDecice.get(key);
			     for (int h=0;h<devices.size();h++)
			     {
			    	 Device device=(Device)devices.get(h);
			    	 double[] minMaxAvgBusy=minMaxAvg(device.getBusy());
			    	 double [] minMaxAvgWait=minMaxAvg(device.getWait());
			    	 double [] minMaxAvgService=minMaxAvg(device.getAvgService());
			    	 double [] minMaxAvgAvgW=minMaxAvg(device.getAvgWait());
			    	 double [] minMaxAvgNo=minMaxAvg(device.getAvgNo());
			    	 double [] minMaxAvgKw=minMaxAvg(device.getKw());
			    	 double [] minMaxAvgKr=minMaxAvg(device.getKr());
			    	 outputStream.write(key+","+device.getDeviceName().replace(",", "_")+","+minMaxAvgBusy[0]+","+minMaxAvgBusy[1]+","+minMaxAvgBusy[2]+","+minMaxAvgWait[0]+","+minMaxAvgWait[1]+","+minMaxAvgWait[2]+","+minMaxAvgService[0]+","+minMaxAvgService[1]+","+minMaxAvgService[2]+","+minMaxAvgAvgW[0]+","+minMaxAvgAvgW[1]+","+minMaxAvgAvgW[2]+","+minMaxAvgNo[0]+","+minMaxAvgNo[1]+","+minMaxAvgNo[2]+","+minMaxAvgKw[0]+","+minMaxAvgKw[1]+","+minMaxAvgKw[2]+","+minMaxAvgKr[0]+","+minMaxAvgKr[1]+","+minMaxAvgKr[2]);
					outputStream.newLine();
			     }
			}
inputStream.close();
			
			outputStream.close();
			outputFiles[0]=outputFile;
			logger.debug("VSSMDiskConverter.convert() - finished converting input files successfully ");
		
		}
		catch (FileNotFoundException e) {
				logger
						.error("VSSMDiskConverter.convert() - Input file not found "
								+ e);
				throw new ApplicationException(e);
			} catch (IOException e) {
				logger
						.error("VSSMDiskConverter.convert() - Couldn't read input file"
								+ e);
				throw new ApplicationException(e);
			}
			logger
					.debug("VSSMDiskConverter.convert() - finished converting input files successfully ");
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
	class Device
	{
		public String deviceName;
		public List busy = new ArrayList();
		public List wait = new ArrayList();
		public List avgService = new ArrayList();
		public List avgWait = new ArrayList();
		public List avgNo = new ArrayList();
		public List kw = new ArrayList();
		public List kr= new ArrayList();
		public String getDeviceName() {
			return deviceName;
		}
		public void setDeviceName(String deviceName) {
			this.deviceName = deviceName;
		}
		public List getBusy() {
			return busy;
		}
		public void setBusy(List busy) {
			this.busy = busy;
		}
		public List getWait() {
			return wait;
		}
		public void setWait(List wait) {
			this.wait = wait;
		}
		public List getAvgService() {
			return avgService;
		}
		public void setAvgService(List avgService) {
			this.avgService = avgService;
		}
		public List getAvgWait() {
			return avgWait;
		}
		public void setAvgWait(List avgWait) {
			this.avgWait = avgWait;
		}
		public List getAvgNo() {
			return avgNo;
		}
		public void setAvgNo(List avgNo) {
			this.avgNo = avgNo;
		}
		public List getKw() {
			return kw;
		}
		public void setKw(List kw) {
			this.kw = kw;
		}
		public List getKr() {
			return kr;
		}
		public void setKr(List kr) {
			this.kr = kr;
		}
		
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
			VSSMDiskConverter s = new VSSMDiskConverter();
			File[] input = new File[1];
			input[0]=new File("D:\\build\\VASPortal\\DataCollection\\dsk-snapshot.out_2010-05-27.Thursday");
			   s.convert(input,"Maha_Test");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
