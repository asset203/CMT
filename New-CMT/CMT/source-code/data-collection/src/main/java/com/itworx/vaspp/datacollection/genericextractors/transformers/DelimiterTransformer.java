package com.itworx.vaspp.datacollection.genericextractors.transformers;


import com.itworx.vaspp.datacollection.common.ApplicationException;
import eg.com.vodafone.model.ExtractionField;
import eg.com.vodafone.model.GenericInputStructure;
import org.apache.log4j.Logger;

import java.io.*;
import java.sql.SQLException;

public class DelimiterTransformer extends InputFilesTransformer{

    public void trasformFiles(File[] inputFiles ,GenericInputStructure inputStructure ,Logger logger) throws ApplicationException {
		logger
				.debug("DelimiterTransformer.trasformFiles() - started transforming input files ");
		try {
			
			long dataInserted = 0;
			long notValidData = 0;
			String sampleLine="";
			String line;
			String[] rawData = null;
			for (int i = 0; i < inputFiles.length; i++) {
				BufferedReader inputStream = new BufferedReader(new FileReader(inputFiles[i]));
				tmpDbTableOperator.beginTransaction();
				tmpDbTableOperator.prepareInsertStatement();
				
				//Ignore the specified count of lines
				int ignoredLines = inputStructure.getIgnoredLinesCount();
				if(ignoredLines > 0){
					int index = 1;
					while(inputStream.ready() && index != ignoredLines){
						inputStream.readLine();
						index++;
					}
				}
				//Ignore Headers
				if(inputStream.ready() && inputStructure.isUseHeaders())
					inputStream.readLine();
				while (inputStream.ready()) {
					line=inputStream.readLine();
					if(!"".equals(line)){
						String delimiter = inputStructure.getExtractionParameter().toString() + 
												inputStructure.getExtractionParameter().toString();
						while(line.indexOf(delimiter)!=-1){
							String newDelimiter = inputStructure.getExtractionParameter().toString() +" "
													+ inputStructure.getExtractionParameter().toString();
							line=line.replaceFirst(delimiter, newDelimiter);
						}
						if(line.endsWith(",")){
							line = line+" ";
						}
						
						rawData = line.split(inputStructure.getExtractionParameter().toString());
						if(rawData.length >= inputStructure.getExtractionFields().length){
							
							for(int idx = 0 ; idx < inputStructure.getExtractionFields().length ; idx++)
							{
								ExtractionField field = inputStructure.getExtractionFields()[idx];
								rawData[idx] = rawData[idx].trim(); 
								if("".equals(rawData[idx]) && field.isActive()){
									rawData[idx] = (field.getDefaultValue() != null) ? field.getDefaultValue() : null;
								}
							}
							try{
								//copy the needed elements and insert it (to avoid file structure)
								String[] validData = new String[inputStructure.getExtractionFields().length];
								System.arraycopy(rawData, 0, validData, 0, validData.length);
								tmpDbTableOperator.insertData(validData);
								dataInserted++;
							}
							catch(Exception e){
								logger.error(e);
								sampleLine = line;
								notValidData++;
							}
						}else{
							sampleLine = line;
							notValidData++;
						}
							
					}
				}
				inputStream.close();
				tmpDbTableOperator.commitTransaction();
				logger.debug("DelimiterTransformer.trasformFiles() - "
						+ inputFiles[i].getName() + " transformed");
			}
			if(notValidData > 0){
				logger.warn("DelimiterTransformer.trasformFiles() - failed to insert "+notValidData+" lines with sample structure["+sampleLine+"] for inputStructure ["+inputStructure.getId()+"] in file into temp database : ");
			}
			if(dataInserted == 0){
				ApplicationException e = new ApplicationException("No data found in files");
				logger.error("DelimiterTransformer.trasformFiles() - error ["+e+"]");
				throw e;
			}
			logger
					.debug("DelimiterTransformer.trasformFiles() - finished transforming input files successfully ");
		} 
		catch (FileNotFoundException e) {
			logger.error("DelimiterTransformer.trasformFiles() - Input file not found " , e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger.error("DelimiterTransformer.trasformFiles() - Couldn't read input file"
					, e);
			throw new ApplicationException(e);
		} catch (SQLException e) {
			logger.error("DelimiterTransformer.trasformFiles() - Couldn't insert data in table"
					, e);
			throw new ApplicationException( e);
		} catch (ApplicationException e){
			throw e;
		}
		catch (Exception e) {
			logger.error("DelimiterTransformer.trasformFiles() - Couldn't insert data in table"
				, e);
			throw new ApplicationException( e);
		}
    }
	public static void main(String[] args){
		/*String line = "22/07/2008 08:01:50.254 ||  INFO || AppID || EBPROMO || ShortCode || 4466 || MSISDN || 20101713233 || ShortMessage ||  || LangID || 0 || Msg_Type || 0";
		String[] lineArr = line.split("\\|\\|");
		String line2= "20091015,090210,301,001,0900134   ,REL,106098122           ,a-welcome-hotline-20090305    ,                    ,                    ,                    ,15    ,0      ,                    , ,                                                                                                             ";
		String[] lineArr21 = line2.split(",");
		String[] lineArr22 = line2.split(",",3);
		
		
		for(int i=0; i< lineArr.length;i++){
			System.out.println(lineArr[i]);
		}*/
		String line ="10,354206033605069,20104702317,20100317000547,0,Set Combined Settings (OTA),ctest / ctest,VFE,Nokia,3110Evolve,602022012350515,FAILED,10006,111947577,,125,800,8,907,,,";
		
		System.out.println(line);
		while(line.indexOf(",,")!=-1){
			line=line.replaceFirst(",,", ", ,");
		}

		System.out.println(line);
		System.out.println(line.split(",").length);
		
	}
}
