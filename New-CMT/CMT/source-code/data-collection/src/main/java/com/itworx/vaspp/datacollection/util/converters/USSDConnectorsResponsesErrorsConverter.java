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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;

public class USSDConnectorsResponsesErrorsConverter extends AbstractTextConverter
{


	private Logger logger;

	public USSDConnectorsResponsesErrorsConverter() {
	}


	public String dateString;
	public String connectorName;
	HashMap<String, Set> map = new HashMap<String, Set>();
	HashMap<String, Integer> countMap = new HashMap<String, Integer>();
	ArrayList<myData> mylist=new ArrayList<myData>();
	int cou=1;
	public File[] convert(File[] inputFiles, String systemName)
			throws ApplicationException, InputException {
	
		logger = Logger.getLogger(systemName);
		logger.debug("USSDConnectorsResponsesErrorsConverter.convert() - started converting input files ");
		String outputLine="";
		
		try {
			String path = PropertyReader.getConvertedFilesPath();
			File[] outputFiles = new File[1];
			File outputFile = new File(path, inputFiles[0].getName());
			BufferedWriter outputStream = new BufferedWriter(new FileWriter(
					outputFile));
			BufferedReader inputStream;
			for (int i = 0; i < inputFiles.length; i++) 
			{
				logger.debug("USSDConnectorsResponsesErrorsConverter.convert() - converting file "
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
					}else 
					{
						outputLine=	this.readData(line, outputStream);
						continue;
					}
				}
				
				ArrayList list=new ArrayList<String>();
				int count=0;
				//iterate on map which contains data
				Iterator myVeryOwnIterator = map.keySet().iterator();
				int x=0;
			//	String[] keyArray;
				while(myVeryOwnIterator.hasNext()){ 
					Object key = myVeryOwnIterator.next();
					//set
					Object value = map.get(key);
					Set s=(Set)value;
					Iterator it = s.iterator(); 
					count=count+s.size();
					//loop on set of messages for each error code
					while (it.hasNext()) { 
						// Get element
						Object element = it.next(); 
						list.add(x, key+","+element.toString());
						x++;
					} 
				}

				//iterate on count map which contains occurance number for each error code
				Iterator countMapIterator = countMap.keySet().iterator();
				while(countMapIterator.hasNext()){
					Object key = countMapIterator.next();
					Object value = countMap.get(key);
					
					String v=value.toString();
					String vv=","+v;

					for(int z=0;z<list.size();z++)
					{	
						if(list.get(z).toString().contains(key.toString()))
						{
							//System.out.println("**final***"+list.get(z).toString().concat(vv));
							outputStream.write(list.get(z).toString().concat(vv));
							outputStream.newLine();
							break;
						}
						else{
						
							continue;
						}
					}
				}

				dateString="";				
				connectorName="";
				x=0;
				map.clear();
				countMap.clear();
				list.clear();
				inputStream.close();
			}		
			
			outputStream.close();
			
			outputFiles[0]=outputFile;
			logger.debug("USSDConnectorsResponsesErrorsConverter.convert() - finished converting input files successfully ");
			return outputFiles;
			
		} catch (FileNotFoundException e) {
			logger.error("USSDConnectorsResponsesErrorsConverter.convert() - Input file not found " + e);
			new ApplicationException("" + e);
		} catch (IOException e) {
			logger.error("USSDConnectorsResponsesErrorsConverter.convert() - Couldn't read input file"
					+ e);
			new ApplicationException("" + e);
		}
		return null;
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
	private String readData(String line, BufferedWriter outputStream)
		throws IOException,InputException {
	
		String[] tok;
		String[] t;
		String key="";
		String value="";
		String[] msgArray;
		String[] tokens;
		Date date=new Date();
		String outLineString;
		tokens = line.split(",");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		//parse date string
		try {
			
			date=sdf1.parse(tokens[0]);
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM/dd/yyyy HH:00:00");
		//format date
		dateString=sdf.format(date);
		int zz;
		if(tokens[2].contains("AppResponse")&& tokens[2].contains("ErrCode:")){
			tok= tokens[2].split(":");	
			t=	tok[2].split(" ");
			if(t[0].equals("0")==false){
				//key =date+connector name+error code
				key=dateString+","+connectorName+","+t[0];
				msgArray=tok[3].split("'");
				value=msgArray[1];
	
				if(map.containsKey(key)==false)
				{
					
					Set valueSet = new HashSet(); 
					valueSet.add(value);
					map.put(key, valueSet);
					countMap.put(key+","+value,1);
				}
				else if(map.containsKey(key)==true )
				{
					if(map.get(key).contains(value)){
						
						zz=countMap.get(key+","+value);						
						zz++;					
						countMap.put(key+","+value,zz);
					}
					else
					{
						countMap.put(key+","+value,1);
						 map.get(key).add(value);
					}
				}
		    }
		 }
		outLineString=connectorName;
		return outLineString;

	}
	
	 class myData
	 {
	 	
	 	String key;
	 	String value;
		public String getKey() {
			return key;
		}
		public void setKey(String key) {
			this.key = key;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
	 }

	public static void main(String ag[]) {
		try {
			
			PropertyReader.init("D:\\VASPortalWF5_working\\VASPortalWF5\\Source Code\\DataCollection");
			USSDConnectorsResponsesErrorsConverter s = new USSDConnectorsResponsesErrorsConverter();
			File[] input = new File[2];
			//input[0]=new File("D:\\maha\\ipcconnector_2010010513.log");
			input[0]=new File("D:\\maha\\sara osama\\ipcconnector_2010010600.log");
			input[1]=new File("D:\\maha\\sara osama\\ipcconnector_2010010523.log");
			//input[1]=new File("D:\\maha\\maha_file2.res");
			//input[2]=new File("D:\\maha\\maha_file3.res");
			/*input[3]=new File("D:\\Work\\VFE_VAS_Performance_Portal\\Phase III\\VFE_VAS_Performance_Portal_SS\\Source Code\\DataCollection\\resources\\ftpfolder\\perf_file_waits.res");		
			*/
			s.convert(input,"Maha_Test");
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


}
