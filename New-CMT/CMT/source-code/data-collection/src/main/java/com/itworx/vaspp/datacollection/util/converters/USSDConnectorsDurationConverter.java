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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import sun.java2d.pipe.OutlineTextRenderer;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;

public class USSDConnectorsDurationConverter extends AbstractTextConverter{


	private Logger logger;

	public USSDConnectorsDurationConverter() {
	}

	public String dateString;
	public String connectorName;
	HashMap<String, Integer> countMap = new HashMap<String, Integer>();
	ArrayList<myData> notTakenList=new ArrayList<myData>();
	ArrayList<myData> mylist=new ArrayList<myData>();
	HashMap<String ,List> myMap=new HashMap<String,List>();
	BufferedReader inputStream;
	SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM/dd/yyyy HH:00:00");
	public File[] convert(File[] inputFiles, String systemName)
			throws ApplicationException, InputException {
	
		logger = Logger.getLogger(systemName);
		logger.debug("USSDConnectorsDurationConverter.convert() - started converting input files ");
		String outputLine="";
		long[] statusCount ;
		try {
			String path = PropertyReader.getConvertedFilesPath();
			File[] outputFiles = new File[1];
			File outputFile = new File(path, inputFiles[0].getName());
			BufferedWriter outputStream = new BufferedWriter(new FileWriter(
					outputFile));
			List<File> files = Arrays.asList(inputFiles);
			 Collections.sort(files, new Comparator<File>() {
					
				    private Pattern hour = Pattern.compile("\\d{2}") ;
				    private Pattern dot  = Pattern.compile("\\.") ;    
					public int compare(File f1, File f2) {
				        String file1 = "" ; 
				        String file2 = "" ;
				        Integer hourInt1 = new Integer(0) ;
				        Integer hourInt2 = new Integer(0) ;
				        Matcher m = null ;
		                if (f1.getName().contains("."))
		                {
		                	file1 = dot.split(f1.getName())[0] ;
		                	
		                	int endIndex   = file1.length()  ; int startIndex   = file1.length() - 2 ; 
		                	file1 = file1.substring(startIndex , endIndex) ; 
		                	m = hour.matcher(file1) ; 
		                	if(m.matches())
		                	{
		                		hourInt1 = new Integer(file1) ;
		                	}
		                }
		                
		                if(f2.getName().contains("."))
		                {
		                	file2 = dot.split(f2.getName())[0] ;
		                	int endIndex   = file2.length()  ; int startIndex   = file2.length() - 2 ;
		                	file2 = file2.substring(startIndex , endIndex) ; 
		                	m = hour.matcher(file2) ; 
		                	if(m.matches())
		                	{
		                		hourInt2 = new Integer(file2) ;
		                	}
		                }
				         
				    	
				    	return hourInt1.intValue() - hourInt2.intValue()  ;
				    }});

			inputFiles = (File[]) files.toArray() ;
			for (int i = 0; i < inputFiles.length; i++) 
			{
				logger.debug("USSDConnectorsDurationConverter.convert() - converting file "
						+ inputFiles[i].getName());
				inputStream = new BufferedReader(new FileReader(inputFiles[i]));
				connectorName = inputFiles[i].getName().
									substring(inputFiles[i].getName().indexOf("(")+1, inputFiles[i].getName().lastIndexOf(")"));
		
				String line;
				while (inputStream.ready()) {
					line = inputStream.readLine();
					if (line.trim().equals("")) {
						continue;
					}
					else if(line.contains("AppResponse")== false && line.contains("begin SRV_SUB_INTERFACE")== false){
						continue;
					}		
					else {
						outputLine=	this.readData(line, outputStream);
						continue;
					}
				}
			
				statusCount=new long[mylist.size()];	
				//loop for list that contains data
				for(int m=0;m<mylist.size();m++)
					{ 
						Object key = mylist.get(m);
						myData n=(myData)key;
					//	System.out.println(mylist.get(m));
						if(n.isRequest()==true)
						{
							for(int z=m+1;z<mylist.size();z++)
							{
//								System.out.println("---------" +mylist.get(z));
//								System.out.println(mylist.get(z).isRequest());
//								System.out.println(mylist.get(z).getMsdn());
//								System.out.println(n.getMsdn());
//								System.out.println(mylist.get(z).isTaken());
								if(mylist.get(z).getMsdn().equals(n.getMsdn()) && 
										mylist.get(z).isTaken()==false)
								{
									if(mylist.get(z).isRequest()){
										n.setTaken(true);
										break;
									}else{
									
								//	System.out.println("Found Response ");
								
									if(myMap.containsKey(sdf.format(n.getMydate()).concat(","+connectorName)))
									{
										
										long di=this.getDaysDiff(mylist.get(z).getMydate(),n.getMydate());
										myMap.get(sdf.format(n.getMydate()).concat(","+connectorName)).add(di);		
									
							
									}
									else
									{
										List valueList = new ArrayList(); 
										valueList.add(this.getDaysDiff(mylist.get(z).getMydate(),n.getMydate()));
										myMap.put(sdf.format(n.getMydate()).concat(","+connectorName),valueList);
										
									}
	
									mylist.get(z).setTaken(true);		
									n.setTaken(true);

									break;
									}
								}else{
									continue;
								}
							}
						}else{
							continue;
						}
					}
				
				
				/***/
				//generate filtered list that contains non taken requests and responses
				for(int m=0;m<mylist.size();m++)	
				{
					myData n=(myData)mylist.get(m);
					if(n.isTaken()==false && n.isRequest())
					{
						notTakenList.add(n);	
					}
				}

	
				mylist.clear();
				//add non taken requests and responses to the list that will contain data of the next file
				for(int k=0;k<notTakenList.size();k++)
				{
					mylist.add(notTakenList.get(k));
				}
		
				notTakenList.clear();
				inputStream.close();
			}	
			
			
			
	
			
			//iterate on map that contains data of all files 
			Iterator myVeryOwnIterator = myMap.keySet().iterator();			
			while(myVeryOwnIterator.hasNext()){ 
				Object key = myVeryOwnIterator.next();		
				//set
				Object value = myMap.get(key);
				List s=(List)value;
				long[] array = this.minMaxAvg(s) ; 
				//System.out.println("*********aaaaaaal***"+key.toString()+","+array[0]+","+array[1]+","+array[2]);
				outputStream.write(key.toString()+","+array[0]+","+array[1]+","+array[2]);
				outputStream.newLine();
			}
	
			dateString="";
			connectorName="";
			myMap.clear() ; 
			outputStream.close();
			outputFiles[0]=outputFile;
			logger.debug("USSDConnectorsDurationConverter.convert() - finished converting input files successfully ");
			return outputFiles;
			
		} catch (FileNotFoundException e) {
			logger.error("USSDConnectorsDurationConverter.convert() - Input file not found " + e);
			new ApplicationException("" + e);
		} catch (IOException e) {
			logger.error("USSDConnectorsDurationConverter.convert() - Couldn't read input file"
					+ e);
			new ApplicationException("" + e);
		}
		
		return null;
	}

	private long[] minMaxAvg(List data)
	{
		long min,max,avg;
		
		long[] minMaxAvgArray = new long[3];
		Iterator listIterator = data.iterator(); 
	    
		avg =((Long)listIterator.next()).longValue();
		max = avg ;
		min = avg ;
		//System.out.println("Initial Average" + avg+",") ;
		//System.out.println(data.size());
		while(listIterator.hasNext())
		{
			Object element = listIterator.next(); 
		//	System.out.println("element: " + ((Long)element).longValue()) ;
			if((Long)element<min)
			{
				min=(Long)element;
			}
			if((Long)element>max)
			{
				max=(Long)element;
			}
			avg = avg + ((Long)element).longValue() ; 
		//	System.out.println(avg + ",") ;
		}
	//	System.out.println();
		minMaxAvgArray[0] = min ; 
		minMaxAvgArray[1] = max ;
		minMaxAvgArray[2] = avg / data.size() ;
		return minMaxAvgArray;
	}
	private long min(Set data)
	{
		long min;
		Iterator setIterator = data.iterator(); 
		
		min=(Long)setIterator.next();
		while(setIterator.hasNext())
		{
			Object element = setIterator.next(); 
			if((Long)element<min)
			{
				min=(Long)element;
			}
		}
		return min;
	}
	
	
	private long max(Set data)
	{
		long max;
		Iterator setIterator = data.iterator(); 
		
		max=(Long)setIterator.next();;
		while(setIterator.hasNext())
		{
			Object element = setIterator.next(); 
			if((Long)element>max)
			{
				max=(Long)element;
			}
		}
		return max;
	}
	
/*	
	private long avg(Long count,Set data)
	{
		System.out.println("count"+count);
		long sum=0;
		long avg=0;
		Iterator setIterator = data.iterator(); 
		while(setIterator.hasNext())
		{
			Object element = setIterator.next();
			System.out.println("element"+element);
			sum=sum+(Long)element;
		}
		System.out.println("sum"+sum);
		avg=sum/count;
		return avg;
	}*/

	private long avg(Long count,Long sum)
	{
		long avg=0;
	
		avg=sum/count;

		return avg;
	}
	private long getDaysDiff(Date date1, Date date2) { 
		Calendar cal = Calendar.getInstance(); 
		Calendar cal2=Calendar.getInstance(); 
		cal.setTime(date1);
		cal2.setTime(date2);
		long diff=cal.getTimeInMillis()-cal2.getTimeInMillis();
		return diff;
	}

	private String readData(String line, BufferedWriter outputStream)
		throws IOException,InputException {
	
		//System.out.println(line);
		String[] tokens;
		Date date=new Date();
		String outLineString="";
		tokens = line.split(",");
		String[] leftTokens;
		String[] rightTokens;
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		//parse date string
		try {
			date=sdf1.parse(tokens[0]);
		
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		//format date
		dateString=sdf.format(date);
		Date redate;
		boolean request=true;
		String msdn="";
		if(tokens[2].contains("begin SRV_SUB_INTERFACE"))
		{
			leftTokens=tokens[2].split("\\(");
			rightTokens=leftTokens[1].split(",");
			msdn=rightTokens[0];
			msdn=msdn.replaceAll("'", "");
		}else if(tokens[2].contains("AppResponse"))
		{
		leftTokens=tokens[2].split("\\[");
		rightTokens=leftTokens[1].split("\\]");
		msdn=rightTokens[0];
		}
		
		redate=	date;
		if(tokens[2].contains("begin SRV_SUB_INTERFACE")){
				request=true;
		}else if(tokens[2].contains("AppResponse")){
			//System.out.println(line);
				request=false;
		}

		myData mydata=new myData();
		mydata.setMsdn(msdn);
		mydata.setMydate(redate);
		mydata.setRequest(request);
		mydata.setTaken(false);
		mylist.add(mydata);
		outLineString=dateString+","+connectorName;
		return outLineString;
	}

	public static void main(String ag[]) {
		try {
			
			String ws= "D:\\Projects\\VAS Portal Project\\VFE_VAS_Portal_2010\\SourceCode\\DataCollection\\";
			//PropertyReader.init("D:\\build\\VASPortal\\DataCollection");
			PropertyReader.init(ws);
			USSDConnectorsDurationConverter s = new USSDConnectorsDurationConverter();
			File[] input = new File[1];
			//input[0]=new File("D:\\build\\VASPortal\\DataCollection\\ipcconnector_2010010513.log");
			input[0]=new File("D:\\Projects\\VAS Portal Project\\VFE_VAS_Portal_2010\\Internal Builds\\Temp\\_USSD_Connectors_1283774098854_2_(RASEED_EXTRA_3)_ipcconnector_2010082912.log");
			/*input[2]=new File("D:\\Deployment WorkSpace\\DataCollection\\ipcconnector_2010010513.log");
			input[0]=new File("D:\\Deployment WorkSpace\\DataCollection\\ipcconnector_2010010523.log");*/
			s.convert(input,"Maha_Test");
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	 class myData
	 {
	 	
	 	String msdn;
	 	Date mydate;
	 	boolean request;
	 	boolean taken;

	

		public boolean isTaken() {
			return taken;
		}
		public void setTaken(boolean taken) {
			this.taken = taken;
		}
		public Date getMydate() {
			return mydate;
		}
		public void setMydate(Date mydate) {
			this.mydate = mydate;
		}
		public String getMsdn() {
			return msdn;
		}
		public void setMsdn(String msdn) {
			this.msdn = msdn;
		}

		public boolean isRequest() {
			return request;
		}
		public void setRequest(boolean request) {
			this.request = request;
		}
	 	
		public String toString(){
			return mydate+","+msdn+","+request+","+taken;
		}
	 	
	 	
	 	
	 }


}


