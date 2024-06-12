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

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;
import com.itworx.vaspp.datacollection.util.Utils;

public class UserActionsTransactionsConverterNew extends AbstractTextConverter{
	private Logger logger;

	public UserActionsTransactionsConverterNew() {
	}
	public String dateString;
	HashMap<String, Integer> actionCountMap = new HashMap<String, Integer>();
	HashMap<String, List> dateActionMap = new HashMap<String, List>();
	Date start=null;
	Date end=null;
	public File[] convert(File[] inputFiles, String systemName)
	
	
	throws ApplicationException, InputException {
		logger = Logger.getLogger(systemName);
		logger.debug("UserActionsTransactionsConverter.convert() - started converting input files ");
		start=new Date();
		try {
			String dateLine="";
		String path = PropertyReader.getConvertedFilesPath();
		File[] outputFiles = new File[1];
		File outputFile = new File(path, inputFiles[0].getName());
		BufferedWriter outputStream = new BufferedWriter(new FileWriter(
				outputFile));
		BufferedReader inputStream;
		
		for (int i = 0; i < inputFiles.length; i++) 
		{
			logger.debug("UserActionsTransactionsConverter.convert() - converting file "
					+ inputFiles[i].getName());
			inputStream = new BufferedReader(new FileReader(inputFiles[i]));
			String line;
			while (inputStream.ready()) {
				
				line = inputStream.readLine();
				if (line.trim().equals("")) {
					continue;
				}
				
				else 
				{
					String[] tokens= line.split("\\|");
					if(tokens.length==7&&tokens[3]!=null&&tokens[5]!=null&&tokens[4]!=null)
					{
					//00:00:04 02/03/10
					SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
					//sdf1.setTimeZone( TimeZone.getDefault() ); 
					Date date=new Date();
					try {
						//parse date string
							date=sdf1.parse(tokens[3]);
							
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							continue;
						}

						SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM/dd/yy HH:00:00");
						//format date
						dateString=sdf.format(date);
						boolean actionfound=false;
						int numberOfActionsPerHour=1;
						
						if(dateActionMap.containsKey(dateString))
						{
							List actionList=dateActionMap.get(dateString);
							for(int j=0;j<actionList.size();j++)
							{
								String [] actionCounts=actionList.get(j).toString().split("_");
								if(actionCounts[0].equalsIgnoreCase(tokens[5]+","+tokens[6]))
								{ 
									actionfound=true;
									int count=Integer.parseInt(actionCounts[1]);
									count++;
									double sum1=Double.parseDouble(actionCounts[2]);
									double sum2=Double.parseDouble(tokens[4]);
									double sum=sum1+sum2;
									String actionNumber =actionCounts[0]+"_"+count+"_"+sum;
									actionList.remove(actionList.get(j));
									actionList.add(actionNumber);
									break;
								}
							}
							if(!actionfound){
							actionList.add(tokens[5]+","+tokens[6]+"_"+1+"_"+tokens[4]);
							dateActionMap.remove(dateString);
							dateActionMap.put(dateString, actionList);
							}
						}
						else
						{
							List newActions= new ArrayList();
							newActions.add(tokens[5]+","+tokens[6]+"_"+1+"_"+tokens[4]);
							dateActionMap.put(dateString,newActions);
						}
				}//end if
				}//end else
				
				}//end while 
			Iterator myVeryOwnIterator =dateActionMap.keySet().iterator();
			while(myVeryOwnIterator.hasNext()){ 					
				Object key = myVeryOwnIterator.next();
				List actions = dateActionMap.get(key);
				for(int k=0;k<actions.size();k++)
				{   String [] actionsNumber=actions.get(k).toString().split("_");
				   double avg=Double.parseDouble(actionsNumber[2])/Double.parseDouble(actionsNumber[1]);
					outputStream.write(key+","+actionsNumber[0]+","+(long)Integer.parseInt(actionsNumber[1])+","+(long)avg);
					outputStream.newLine();
				}
			
			}
			inputStream.close();
		}
		outputStream.close();
		outputFiles[0]=outputFile;
		
		logger.debug("UserActionsTransactionsConverter.convert() - finished converting input files successfully ");
		end = new Date();
		System.out.println(Utils.getTimeDifferenceinMills(end,start));
		return outputFiles;
		} catch (FileNotFoundException e) {
			logger.error("UserActionsTransactionsConverter.convert() - Input file not found " + e);
			new ApplicationException("" + e);
		} catch (IOException e) {
			logger.error("USSDConnectorsResponseStatusConverter.convert() - Couldn't read input file"
					+ e);
			new ApplicationException("" + e);
		}
		return null;
	}
	

	public static void main(String ag[]) {
		try {
			
			String path="D:\\Projects\\VAS Portal Project\\Phase 7\\VFE_VAS_Performance_Portal_V7\\SourceCode\\DataCollection\\";
			PropertyReader.init(path);
			UserActionsTransactionsConverterNew s = new UserActionsTransactionsConverterNew();
			File[] input = new File[1];
			input[0]=new File(path+"cdr.log-20100724");
			s.convert(input,"Maha_Test");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		/*String dateStr="29-6-2010 17:33:56:436";
		SimpleDateFormat dateFormat= new SimpleDateFormat("dd-MM-yyyy HH:mm:ss:sss");
		Date date=null;
		try {
			date = dateFormat.parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(date);*/

	}
}
