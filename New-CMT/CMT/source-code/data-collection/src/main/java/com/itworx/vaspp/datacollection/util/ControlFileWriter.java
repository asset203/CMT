package com.itworx.vaspp.datacollection.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Properties;


import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.xml.sax.InputSource;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.objects.GenericInputStructure;
import com.itworx.vaspp.datacollection.util.generic.XMLUtils;

public class ControlFileWriter {
	private String controlFileName;
	private GenericInputStructure inputStructure;
	private Logger logger;

	public ControlFileWriter(GenericInputStructure inputStructure,String controlFileName,Logger logger){
		XMLUtils.init(logger);
		this.controlFileName = controlFileName;
		this.inputStructure = inputStructure;
		this.logger = logger;
	}
	public void writeControlFile() throws ApplicationException{
		logger.debug("ControlFileWriter.writeControlFile() - start");
        try {

        	String controlFilesPath = PropertyReader.getControlFilesPath();
			String controlFileURL = new File(controlFilesPath + File.separator + controlFileName).getAbsolutePath()+".xml";
			
	        File outFile = new File(controlFileURL);
	        if(outFile.exists()){
	        	throw new Exception("file with name : "+controlFileName+" already exists");
	        }
			
        	Properties p = new Properties();
            p.setProperty("file.resource.loader.path",PropertyReader.getVelocityTemplatesPath());
            Velocity.init(p);

	        VelocityContext context = new VelocityContext();
	        context.put("inputStructure", inputStructure);
	        
	        String extractionMethodName = "Delimiter";
			if(inputStructure.getExtractionMethod() == GenericInputStructure.DELIMITER_EXTRACTOR)
				extractionMethodName = "Delimiter";
			else if(inputStructure.getExtractionMethod() == GenericInputStructure.REGULAR_EXP_EXTRACTOR)
				extractionMethodName = "RegularExp";
			else if(inputStructure.getExtractionMethod() == GenericInputStructure.EXCEL_EXTRACTOR)
				extractionMethodName = "Excel"; 
			else if(inputStructure.getExtractionMethod() == GenericInputStructure.DATABASE_EXTRACTOR)
				extractionMethodName = "Database"; 
			else if(inputStructure.getExtractionMethod() == GenericInputStructure.JAVA_CODE_EXTRACTOR)
				extractionMethodName = "JavaCode";
		
			context.put("extractionMethodName",extractionMethodName);
		
	        StringWriter w = new StringWriter();
	        
	        String fileName = "controlfile.vm";
	
	        Velocity.mergeTemplate(fileName, "UTF-8",context, w );
	        String xmlString = w.toString();
	        InputSource validationSource = new InputSource();
	        validationSource.setCharacterStream(new StringReader(xmlString));
	        
	        
//	        XMLUtils.validateDocument(validationSource, schemaURL);

	        FileWriter out = new FileWriter(outFile);
            out.write(xmlString);
            out.close();
            
            
		} catch (ResourceNotFoundException e) {
			logger
			.error("CFW - 1000: ControlFileWriter.writeControlFile() - could not read control template file "
					+ e);
			throw new ApplicationException("could not read control template file");
		} catch (ParseErrorException e) {
			logger
			.error("CFW - 1001: ControlFileWriter.writeControlFile() - control template file has invalid format "
					+ e);
			throw new ApplicationException("control template file has invalid format");
		} catch (MethodInvocationException e) {
			logger
			.error("CFW - 1002: ControlFileWriter.writeControlFile() - could not read application properties file "
					+ e);
			throw new ApplicationException(e);
		} catch (IOException e) {
			logger
			.error("CFW - 1003: ControlFileWriter.writeControlFile() - I/O error couldn't write control file"
					+ e);
			throw new ApplicationException("I/O error couldn't write control file");
		} 
		catch (Exception e) {
			logger
			.error("CFW - 1004: ControlFileWriter.writeControlFile() - couldn't write control file"
					+ e);
			throw new ApplicationException("couldn't write control file ["+e.getMessage()+"]");
		}
        logger.debug("ControlFileWriter.writeControlFile() - end");
	}

	public static void main(String[] arg) {
		try {
			Logger logger = Logger.getLogger("SchedulerLogger");
			PropertyReader.init("D:\\Work\\My Eclipse Projects\\VASPortalWF\\Source Code\\DataCollection",logger);
			
			ControlFileReader cr = new ControlFileReader("test_generic_data_dist",logger);
			GenericInputStructure genericInputStructure =  cr.getGenericInputStructure();
			ControlFileWriter cw = new ControlFileWriter(genericInputStructure, "basemtest",logger);
			cw.writeControlFile();
			
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
