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
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import java.util.Map;
import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;

public class VSSMFileSystemConverter extends AbstractTextConverter {
	private Logger logger;
	private Map  <String ,List> dateVSFileSys=new HashMap<String,List>() ;
	
	public VSSMFileSystemConverter()
	{}
	public File[] convert(File[] inputFiles, String systemName)
	throws ApplicationException, InputException {
		logger = Logger.getLogger(systemName);
		logger
				.debug("Inside VSSMFileSystemConverter convert - started converting input files");
		String path = PropertyReader.getConvertedFilesPath();
		File[] outputFiles = new File[1];
		File outputFile = new File(path, inputFiles[0].getName());

		BufferedReader inputStream = null;
		BufferedWriter outputStream;
		try {
			outputStream = new BufferedWriter(new FileWriter(outputFile));
			for (int i = 0; i < inputFiles.length; i++) {
				logger
						.debug("VSSMFileSystemConverter.convert() - converting file "
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
					String colums[]=line.split("\\s\\s");
					try
					{
					  date = getDate(colums[0]);
					}
					catch(ParseException exc) { logger.error(exc) ; continue ;}
					boolean found =false;
					if(dateVSFileSys.containsKey(date))
					{
						List sys =(List)dateVSFileSys.get(date);
						for(int k=0;k<sys.size();k++){
						if(((FileSystem)sys.get(k)).getName().equalsIgnoreCase(colums[1].trim()))
						{
							((FileSystem)sys.get(k)).getUtiList().add(colums[2].trim().split("%")[0]);
							found= true;
							dateVSFileSys.remove(date);
							dateVSFileSys.put(date, sys);
							break;
						}
						
						
						}
						if(!found)
						{
							FileSystem newSys= new FileSystem();
							newSys.setName(colums[1].trim());
							newSys.getUtiList().add(colums[2].trim().split("%")[0]);
							sys.add(newSys);
							dateVSFileSys.remove(date);
							dateVSFileSys.put(date, sys);
							
							
						}
					}
					else
					{
						FileSystem sys= new FileSystem();
						List sysList = new ArrayList();
						sys.setName(colums[1].trim());
						sys.getUtiList().add(colums[2].trim().split("%")[0]);
						sysList.add(sys);
						dateVSFileSys.put(date,sysList);
					}
				}//end of the file
				
				Iterator it =dateVSFileSys.keySet().iterator();
				while (it.hasNext())
				{
					Object key=it.next();
					List fileSystems=(List)dateVSFileSys.get(key);
				     for (int h=0;h<fileSystems.size();h++)
				     {
				    	 FileSystem sys=(FileSystem)fileSystems.get(h);
				    	 double[] minMaxAvg=minMaxAvg(sys.getUtiList());
				    	 outputStream.write(key+","+sys.getName()+","+minMaxAvg[0]+","+minMaxAvg[1]+","+minMaxAvg[2]);
						outputStream.newLine();
				     }
				}
			}
			inputStream.close();
			
			outputStream.close();
			outputFiles[0]=outputFile;
			logger.debug("VSSMFileSystemConverter.convert() - finished converting input files successfully ");
		
		}
		catch (FileNotFoundException e) {
				logger
						.error("VSSMFileSystemConverter.convert() - Input file not found "
								+ e);
				throw new ApplicationException(e);
			} catch (IOException e) {
				logger
						.error("VSSMFileSystemConverter.convert() - Couldn't read input file"
								+ e);
				throw new ApplicationException(e);
			}
			logger
					.debug("VSSMFileSystemConverter.convert() - finished converting input files successfully ");
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
			VSSMFileSystemConverter s = new VSSMFileSystemConverter();
			File[] input = new File[1];
			input[0]=new File("D:\\build\\VASPortal\\DataCollection\\fs-snapshot.out_2010-05-27.Thursday");
			   s.convert(input,"Maha_Test");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	class FileSystem
	{
		public String name;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public List getUtiList() {
			return utiList;
		}
		public void setUtiList(List utiList) {
			this.utiList = utiList;
		}
		public List utiList = new ArrayList();
	
	}

}
