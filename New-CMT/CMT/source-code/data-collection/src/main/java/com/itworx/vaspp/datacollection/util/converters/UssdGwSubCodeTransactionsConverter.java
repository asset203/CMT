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
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.Map;

import org.apache.log4j.Logger;
import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.PropertyReader;
import com.itworx.vaspp.datacollection.util.Utils;

public class UssdGwSubCodeTransactionsConverter extends AbstractTextConverter {

	private Logger logger;
	private Map<String, Entry> numOfResReqPerSubCode =  new HashMap<String, Entry>() ;
	private Map<String,String> msisdnVsSubCode =  new HashMap<String, String>() ; 


	public UssdGwSubCodeTransactionsConverter() {

	}
	
	private void printMap(){
	Map<String,Entry> list  = new HashMap<String,Entry>();
		
		for (Map.Entry<String, Entry> entry : numOfResReqPerSubCode
				.entrySet()) {

			String outLineString = entry.getValue().getDate()+
					","
					+ entry.getValue().getSubCode() 
					+ ","
					+ entry.getValue().getNumOfResponsesPerSubCode()
							.intValue()
					+ ","
					+ entry.getValue().getNumOfRequestsPerSubCode()
							.intValue() + "";
		
				//outputStream.write(outLineString);
				//System.out.println(outLineString);
				//outputStream.newLine();
		}
	}

	public File[] convert(File[] inputFiles, String systemName)
			throws ApplicationException, InputException {

		logger = Logger.getLogger(systemName);
		logger
				.debug("Inside UssdGwSubCodeTransactionsConverter convert - started converting input files");
		
		String path = PropertyReader.getConvertedFilesPath();
		File[] outputFiles = new File[1];
		//System.out.println("the file name "+inputFiles[0].getName());
		File outputFile = new File(path, inputFiles[0].getName());
	
		BufferedReader inputStream = null;
		BufferedWriter outputStream;
        Pattern subCodeHashPattern = Pattern.compile("[0-9]+#");
        Pattern subCodeAstrixPattern = Pattern.compile("[0-9]+\\*");
        String wholeSubCode=PropertyReader.getUssdGWSubCodes();
        String []subCodes=wholeSubCode.split(",");
        List convertedSubCode=new ArrayList();
       // Map reqTypesNumber=new HashMap();
        for(int i=0;i<subCodes.length;i++)
        {
        	convertedSubCode.add(subCodes[i]);
        
        }

        int reqCount=0;
        int respCount =0;
		try {
			outputStream = new BufferedWriter(new FileWriter(outputFile));
			for (int i = 0; i < inputFiles.length; i++) {
				
				logger
						.debug("UssdGwSubCodeTransactionsConverter.convert() - converting file "
								+ inputFiles[i].getName());
                printMap();
				//inputStream = new BufferedReader(new FileReader(inputFiles[i]));
                inputStream = Utils.getGZIPAwareBufferdReader(inputFiles[i]);

				String line;
				
			//	numOfResReqPerSubCode = new HashMap<String, Entry>();
			//	msisdnVsSubCode         = new HashMap<String,String>();
				String date = "";
				String msisdn=null;
				boolean validPairs=false;
				while (inputStream.ready()) {

					line = inputStream.readLine();
					
					String subCode = null;
					
					if (line.contains("SC:868")) {
						String key = "" ;
						
						try
						{
						  date = getDate(line);
						}
						
						catch(ParseException exc) { logger.error(exc) ; continue ;}
						// request
						 	/* [Code Review]
						 	 * Elsayed Hassan
						 	 * You already checked for the condition line.contains("SC:868")
						 	 * in the most outer if statement , there is no need to recheck it again in 
						 	 * the inner if statements  
						 	 */
							String[] lineParts=line.split(",");
							
							 if (//line.contains("SC:868")&& 
									line.contains("PSSR Indication:")) {
								
								 reqCount++;
								 /* [Code Review]
								  * Elsayed Hassan
								  * in the below line there is a possibility for null pointer 
								  * or index out of range exceptions
								  * Please first ensure that the line is in the expected format
								  * before splitting it  
								  */

								 	
								 	int pssrIndex = -1;
								 	
								 	for(int z = 0; z < lineParts.length;z++){
								 		if(lineParts[z].contains("PSSR Indication:")){
								 			pssrIndex = z;
								 			break;
								 		}
								 	}

								 	if(pssrIndex == -1)
								 		continue;
								 	
								 	subCode = lineParts[pssrIndex].split(":")[1];
								    subCode = subCode.replaceAll("\'", "").trim();
									Matcher hashMatcher = subCodeHashPattern.matcher(subCode);
									Matcher astrikMatcher = subCodeAstrixPattern.matcher(subCode);
								if(hashMatcher.lookingAt() || astrikMatcher.lookingAt()){
									if(hashMatcher.lookingAt())
									{
										subCode = subCode.split("#")[0] ; 
									}
									else if(astrikMatcher.lookingAt())
								    {
										subCode  = subCode.split("\\*")[0] ;
								    }
									
									/*for(int k=0;k<subCodes.length;k++)
									{
										if(subCodes[k].equalsIgnoreCase(subCode))
											{
											found=true;
										    break;
										    }
									}*/
									
									if(convertedSubCode.contains(subCode)){
										/* [Code Review]
										  * Elsayed Hassan
										  * I see that this map is not use , so this is misuse for the working memory 
										  */
										//reqTypesNumber.put(new Integer(reqCount),"submitted");
										validPairs=false;
										
									key = date.trim() +"," + subCode.trim() +",Pairs="+validPairs;
									 /* [Code Review]
									  * Elsayed Hassan
									  * Same  comment for subCode variable
									  */
									msisdn=lineParts[2].split("MSISDN:")[1];
									//msisdnVsSubCode.put(line.split(",")[2].split("MSISDN:")[1],key) ;
									msisdnVsSubCode.put(msisdn,key) ;
								if (numOfResReqPerSubCode
										.containsKey(key) == true) {
									Entry e = (Entry) numOfResReqPerSubCode
											.get(key);
									int newNumRequests = (e.getNumOfRequestsPerSubCode() != null) ? 
											(e.getNumOfRequestsPerSubCode().intValue() + 1)
											: new Integer(1);
									e.setNumOfRequestsPerSubCode(new Integer(
											newNumRequests));
									e.setDate(date);
								    e.setSubCode(subCode);
								    e.hasResponse = false ;
								} else {
								//	System.out.println("RequestSubcode" + subCode);
									numOfResReqPerSubCode.put(key,
											new Entry(subCode,date,
													new Integer(1),
													new Integer(0),false));
								}}else{
									/* [Code Review]
									  * Elsayed Hassan
									  * I see that this map is not use , so this is misuse for the working memory 
									  */
									//reqTypesNumber.put(new Integer(reqCount),"rejected");
									continue ;
									}
							}}

							// Responses Per Sub Code
							 else if (line.contains("SC:868")
									&& line.contains("Close ussd dialogue.")) {
								 if(lineParts.length<3)
									 continue;
								 msisdn=lineParts[2].split("MSISDN:")[1];
								 respCount++;
                               //subCode =  msisdnVsSubCode.get(line.split(",")[2].split("MSISDN:")[1]) ;
								 subCode =  msisdnVsSubCode.get(msisdn) ;
                               key =  subCode;	
                              
                              
                                if(subCode != null&&subCode.split("Pairs=")[1].equalsIgnoreCase("false"))
                               {  
                                	validPairs=true;
                                	//msisdnVsSubCode.remove(line.split(",")[2].split("MSISDN:")[1]);	
                                	msisdnVsSubCode.remove(msisdn);
                                    msisdnVsSubCode.put(msisdn,key.split("Pairs=")[0]+"Pairs="+validPairs);
                                	if (numOfResReqPerSubCode
										.containsKey(key) == true) {
									Entry e = (Entry) numOfResReqPerSubCode
											.get(key);
									int newNumResponses = (e
											.getNumOfResponsesPerSubCode() != null) ? (e
											.getNumOfResponsesPerSubCode()
											.intValue() + 1)
											: new Integer(1);
									e.setNumOfResponsesPerSubCode(new Integer(
													newNumResponses));
									e.setDate(date);
									e.hasResponse = true ;

								} else {
									//System.out.println("ResponseSubcode" + subCode);
									numOfResReqPerSubCode.put(key,
											new Entry(subCode.split(",")[1],date,
													new Integer(0),
													new Integer(1),true));
								 }
                                }else{continue ;}

							}else {continue ;}
						
						}
					    else {
						continue;
					}
				}
				inputStream.close();
				
				//System.out.println(inputFiles[0].getName());
				generateLines(outputStream);
				
			}

			outputStream.close();
			outputFiles[0] = outputFile;

		} catch (FileNotFoundException e) {
			logger.error("UssdGwSubCodeTransactionsConverter.convert() - Input file not found ", e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger.error("UssdGwSubCodeTransactionsConverter.convert() - Couldn't read input file", e);
			throw new ApplicationException(e);
		}
		logger.debug("UssdGwSubCodeTransactionsConverter.convert() - finished converting input files successfully ");
		return outputFiles;
	}

	/*****************************************************
	 * generate the lines in the file
	 * 
	 * @param outputStream
	 * @throws ApplicationException 
	 *****************************************************/
	private void generateLines(BufferedWriter outputStream) throws ApplicationException {
       
		Map<String,Entry> list  = new HashMap<String,Entry>();
		
		for (Map.Entry<String, Entry> entry : numOfResReqPerSubCode
				.entrySet()) {

			String outLineString = entry.getValue().getDate()+
					","
					+ entry.getValue().getSubCode() 
					+ ","
					+ entry.getValue().getNumOfResponsesPerSubCode()
							.intValue()
					+ ","
					+ entry.getValue().getNumOfRequestsPerSubCode()
							.intValue() + "";
			try {
				outputStream.write(outLineString);
				//System.out.println(outLineString);
				outputStream.newLine();
			} catch (IOException exc) {
				logger.debug("Error while writing to outputStream" + exc);
				logger
				.error("UssdGwSubCodeTransactionsConverter.convert() - Input file not found "
						+ exc);
		         throw new ApplicationException(exc);
			}
			if (!entry.getValue().isHasResponse())
			{
				//System.out.println("Entry with no responses "+ entry.getKey());
				entry.getValue().setNumOfRequestsPerSubCode(new Integer(0));
				entry.getValue().setNumOfResponsesPerSubCode(new Integer(0));
				list.put(entry.getKey(), entry.getValue());
			}
		}
		numOfResReqPerSubCode.clear() ; 
		numOfResReqPerSubCode.putAll(list);

	}

	/**
	 * get date
	 * 
	 * @param line
	 *            - the line to extract date
	 * @throws ParseException 
	 */
	private String getDate(String line) throws ParseException {
		String[] tokens = null;
		Date date = new Date();
		String dateString;
		SimpleDateFormat inDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss.mss");
		SimpleDateFormat outDateFormat = new SimpleDateFormat(
				"MM/dd/yyyy HH:00:00");

		if (line != null)
			tokens = line.split(",");
		
			date = inDateFormat.parse(tokens[0]);
		

		dateString = outDateFormat.format(date);

		return dateString;

	}

	private class Entry {
		Integer numOfRequestsPerSubCode;
		Integer numOfResponsesPerSubCode;
		String subCode ; 
		public boolean isHasResponse() {
			return hasResponse;
		}

		public void setHasResponse(boolean hasResponse) {
			this.hasResponse = hasResponse;
		}

		boolean hasResponse ;
		public String getSubCode() {
			return subCode;
		}

		public void setSubCode(String subCode) {
			this.subCode = subCode;
		}

		String date;

		public Entry(String subCode , String date, Integer numOfRequestsPerSubCode,
				Integer numOfResponsesPerSubCode,boolean hasResponse) {
			this.subCode = subCode ;
			this.date = date;
			this.numOfRequestsPerSubCode = numOfRequestsPerSubCode;
			this.numOfResponsesPerSubCode = numOfResponsesPerSubCode;
			this.hasResponse = hasResponse ;
		}

		public Integer getNumOfRequestsPerSubCode() {
			return numOfRequestsPerSubCode;
		}

		public void setNumOfRequestsPerSubCode(
				Integer numOfRequestsPerSubCode) {
			this.numOfRequestsPerSubCode = numOfRequestsPerSubCode;
		}

		public Integer getNumOfResponsesPerSubCode() {
			return numOfResponsesPerSubCode;
		}

		public void setNumOfResponsesPerSubCode(
				Integer numOfResponsesPerSubCode) {
			this.numOfResponsesPerSubCode = numOfResponsesPerSubCode;
		}

		public String getDate() {
			return date;
		}

		public void setDate(String date) {
			this.date = date;
		}
	}

	public static void main(String ag[]) {
		
		
		
		try {
			String path = "D:\\Projects\\ITWorx\\Teleco\\VNPP\\SourceCode_\\DataCollection\\";
			PropertyReader.init(path);
			PropertyReader.getUssdGWSubCodes();
			UssdGwSubCodeTransactionsConverter s = new UssdGwSubCodeTransactionsConverter();
			File[] input = new File[6];
			input[0]= new File("C:\\Documents and Settings\\Basem.Deiaa\\Desktop\\VNPP Deployments\\20120214_update_ussd_gw_subcode_transactions\\trace2012021405.log") ;
			input[1]= new File("C:\\Documents and Settings\\Basem.Deiaa\\Desktop\\VNPP Deployments\\20120214_update_ussd_gw_subcode_transactions\\trace2012021406.log") ;
			input[2]= new File("C:\\Documents and Settings\\Basem.Deiaa\\Desktop\\VNPP Deployments\\20120214_update_ussd_gw_subcode_transactions\\trace2012021407.log") ;
			input[3]= new File("C:\\Documents and Settings\\Basem.Deiaa\\Desktop\\VNPP Deployments\\20120214_update_ussd_gw_subcode_transactions\\trace2012021408.log") ;
			input[4]= new File("C:\\Documents and Settings\\Basem.Deiaa\\Desktop\\VNPP Deployments\\20120214_update_ussd_gw_subcode_transactions\\trace2012021409.log") ;
			input[5]= new File("C:\\Documents and Settings\\Basem.Deiaa\\Desktop\\VNPP Deployments\\20120214_update_ussd_gw_subcode_transactions\\trace2012021404.log") ;
			
			//input[1]= new File("D:\\build\\DataCollection\\trace2010022720.log") ;
			//input[1] = new File("D:\\Deployment WorkSpace\\DataCollection\\trace2010011404.log");
			s.convert(input, "Maha_Test");

		} catch (Exception e) {
			e.printStackTrace();
		}
	/*	
		String line = "2*0104942038*50#" ; 
		Pattern subCodeAstrixPattern = Pattern.compile("[0-9]+\\*");
		Matcher m = subCodeAstrixPattern.matcher(line);
		System.out.println(m.lookingAt()) ;
		System.out.println(line.split("\\*")[0]);
		
		line = "2#" ; 
		Pattern subCodeHashPattern = Pattern.compile("[0-9]+#");
		Matcher m2 = subCodeHashPattern.matcher(line);
		System.out.println(m2.lookingAt() || m.lookingAt());*/

	}

}
