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
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import com.itworx.vaspp.datacollection.util.Utils;
import org.apache.log4j.Logger;
import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;


public class USSDGWShortCodesDurationConverter extends AbstractTextConverter
{

	private Logger logger;

	public USSDGWShortCodesDurationConverter() {
	}


	public String dateString;
	HashMap<String, List> map = new HashMap<String, List>();
	BufferedReader inputStream;
	SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM/dd/yyyy HH:00:00");
	SimpleDateFormat sdff = new java.text.SimpleDateFormat("MM/dd/yyyy 00:00:00");
	int x=0;
	int count=0;
	ArrayList<myData> mylist=new ArrayList<myData>();
	ArrayList<myData> notTakenList=new ArrayList<myData>();

	
	public File[] convert(File[] inputFiles, String systemName)
			throws ApplicationException, InputException {
		String outputLine="";
		long[] statusCount ;
		logger = Logger.getLogger(systemName);
		logger.debug("USSDGWShortCodesDurationConverter.convert() - started converting input files ");


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
       

		try {
			String path = PropertyReader.getConvertedFilesPath();
			File[] outputFiles = new File[1];
			File outputFile = new File(path, inputFiles[0].getName());
			
			for (int i = 0; i < inputFiles.length; i++) 
			{
				//inputStream = new BufferedReader(new FileReader(inputFiles[i]));
                inputStream = Utils.getGZIPAwareBufferdReader(inputFiles[i]);
				String line;
				logger.debug("USSDGWShortCodesDurationConverter.convert() - converting file "
						+ inputFiles[i].getName());


				while (inputStream.ready()) 
				{
					line = inputStream.readLine();
					if (line.trim().equals("")) {

						continue;
					}else if(line.contains("New Mobile Initiated Dialogue")== false && line.contains("Close ussd dialogue")== false){

						continue;
					}else {

						outputLine=	this.readData(line);

						continue;
					}
				}

					for(int m=0;m<mylist.size();m++)
					{ 

						Object key = mylist.get(m);

						myData n=(myData)key;
						if(n.isRequest()==true)
						{
							for(int z=m+1;z<mylist.size();z++)
							{
								if(mylist.get(z).isRequest()==false && mylist.get(z).getMsdn().equals(n.getMsdn()) &&mylist.get(z).getSC().equals(n.getSC()) && mylist.get(z).isTaken()==false)
								{
									if(map.containsKey(sdf.format(n.getMydate()).concat(","+n.getSC()))==false)
									{
										List valueList = new ArrayList(); 
										valueList.add(this.getDaysDiff(mylist.get(z).getMydate(),n.getMydate()));
										map.put(sdf.format(n.getMydate()).concat(","+n.getSC()), valueList);
									}
									else if(map.containsKey(sdf.format(n.getMydate()).concat(","+n.getSC()))==true )
									{
										long di=this.getDaysDiff(mylist.get(z).getMydate(),n.getMydate());
										map.get(sdf.format(n.getMydate()).concat(","+n.getSC())).add(di);		
									}
									
									mylist.get(z).setTaken(true);		
									n.setTaken(true);

									break;											
								}
								else
								{
									continue;
								}						
							}
						}else
						{
							continue;
						}
					}
			
					//generate filtered list that contains non taken requests and responses
					for(int m=0;m<mylist.size();m++)	
					{
						myData n=(myData)mylist.get(m);
						if(n.isTaken()==false && n.isRequest())
						{
							//System.out.println("nottakenleeh"+n.getSC());
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
			/***/
		

		
			Iterator mapIterator = map.keySet().iterator();
			int x=0;
			String[] arr;
			BufferedWriter outputStream = new BufferedWriter(new FileWriter(
					outputFile));
			while(mapIterator.hasNext())
				{ 
					Object key = mapIterator.next();
					arr=key.toString().split(",");
	
					//set
					Object value = map.get(key);
					List s=(List)value;		
					long[] array = this.minMaxAvg(s) ; 
					
					//System.out.println("**********outlineall*******"+key.toString()+","+array[0]+","+array[1]+","+array[2]);
					outputStream.write(key.toString()+","+array[0]+","+array[1]+","+array[2]);
					outputStream.newLine();	
				}

			dateString="";	
			map.clear();
			/***/
			outputStream.close();
         	outputFiles[0]=outputFile;
			logger.info("USSDGWShortCodesDurationConverter.convert() - finished converting input files successfully ");
			logger.debug("USSDGWShortCodesDurationConverter.convert() - finished converting input files successfully ");
			return outputFiles;
			
		} catch (FileNotFoundException e) {
			logger.error("USSDGWShortCodesDurationConverter.convert() - Input file not found " + e);
			new ApplicationException("" + e);
			
		} catch (IOException e) {
			logger.error("USSDGWShortCodesDurationConverter.convert() - Couldn't read input file"
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
	
	

	
	private long getDaysDiff(Date date1, Date date2) { 
		Calendar cal = Calendar.getInstance();
	//	System.out.println(cal.getTimeZone()) ; 
		Calendar cal2=Calendar.getInstance(); 
	//	System.out.println(cal2.getTimeZone()) ; 
		cal.setTime(date1);
		cal2.setTime(date2);
		long diff=cal.getTimeInMillis()-cal2.getTimeInMillis();
		return diff;
	}

	
	/**
	 * extract data.
	 * 
	 * @param inputStream -
	 *            the input file
	 * @param lines -
	 *            the arrays of lines to concatenate data
	 * 
	 * @exception InputException
	 *                if format of date string was invalid
	 * @exception IOException
	 *                if error occured while reading file
	 */
	private String readData(String line)
		throws IOException,InputException {
	
	
		String[] tokens;
		Date date=new Date();
		String outLineString;
		String[] leftTokens;
		tokens = line.split(",");
		String[] scToken;
		String sc;
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
		String msdn;
	
	
			leftTokens=tokens[2].split(":");
			scToken=tokens[3].split(":");
			sc=scToken[1];	
			msdn=leftTokens[2];

			if(tokens[6].contains("New Mobile Initiated Dialogue"))
			{
             	request=true;
			}
			else if(tokens[6].contains("Close ussd dialogue"))
			{
            	request=false;
			}

			myData mydata=new myData();
			mydata.setMsdn(msdn);
			mydata.setSC(sc);
			mydata.setMydate(date);
			mydata.setRequest(request);
			mydata.setTaken(false);		
			mylist.add(mydata);
	
		outLineString=dateString;
		return outLineString;
	}
	
	public static void main(String ag[]) 
	{
		try {
			
			PropertyReader.init("D:\\Deployment WorkSpace\\DataCollection");
			USSDGWShortCodesDurationConverter s = new USSDGWShortCodesDurationConverter();
			File[] input = new File[4];
			
			input[0]=new File("D:\\Deployment WorkSpace\\DataCollection\\Negative\\trace2010022718.log");
			input[2]=new File("D:\\Deployment WorkSpace\\DataCollection\\Negative\\trace2010022719.log");
			input[1]=new File("D:\\Deployment WorkSpace\\DataCollection\\Negative\\trace2010022720.log");
			input[3]=new File("D:\\Deployment WorkSpace\\DataCollection\\Negative\\trace2010022721.log");

			s.convert(input,"USSD_GW");
			
			
			
			// trace$date(yyyymmdd)$\d{2}.log
		/*	 File[] input = new File[4];
			input[0]=new File("D:\\Deployment WorkSpace\\DataCollection\\Negative\\trace2010022718.log");
			input[2]=new File("D:\\Deployment WorkSpace\\DataCollection\\Negative\\trace2010022719.log");
			input[1]=new File("D:\\Deployment WorkSpace\\DataCollection\\Negative\\2010022720.log");
			input[3]=new File("D:\\Deployment WorkSpace\\DataCollection\\Negative\\trace2010022721.log");
			
			

			List<File> files = Arrays.asList(input);

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
                	System.out.println(file1) ; 
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
                	System.out.println(file2) ; 
                	m = hour.matcher(file2) ; 
                	if(m.matches())
                	{
                		hourInt2 = new Integer(file2) ;
                	}
                }
		         
		    	
		    	return hourInt1.intValue() - hourInt2.intValue()  ;
		    }});
			for (File f : files)
			    System.out.println(f.getName());*/

			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	
	private class myData
	 {
	 	
	 	String msdn;
	 	String SC;
	 	Date mydate;
	 	boolean request;
	 	boolean taken;


		public String getSC() {
			return SC;
		}
		public void setSC(String sC) {
			SC = sC;
		}
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
	
	 }

}

