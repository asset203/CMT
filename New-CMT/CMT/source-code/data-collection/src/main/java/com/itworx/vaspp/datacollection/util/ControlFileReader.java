package com.itworx.vaspp.datacollection.util;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.objects.ExtractionField;
import com.itworx.vaspp.datacollection.objects.GenericInputStructure;
import com.itworx.vaspp.datacollection.objects.GenericMapping;
import com.itworx.vaspp.datacollection.util.generic.XMLUtils;

public class ControlFileReader {
	
	private org.jdom.Document document;
	private String controlFileName;

	private static final String systemSeparator = System
			.getProperty("file.separator");

	private Logger logger;
	/**
	 * intiating ControlFileReader Loading and validating XML File
	 * 
	 * @param logger -
	 *            the name of the logger for the targeted system
	 * @param controlFileName -
	 *            the name of the logger for the targeted system
	 * 
	 * @exception ApplicationException
	 *                if an error occured while reading XML File if XML File
	 *                wasn't valid
	 */
	public ControlFileReader(String controlFileName,Logger logger) throws ApplicationException {
		try {
			this.logger = logger;
			logger
					.debug("ControlFileReader.ControlFileReader() : initating ControlFileReader: reading control file xml["+controlFileName+"]");
			String xmlPath = PropertyReader.getControlFilesPath();
			String xmlName = controlFileName + ".xml";
			String schemaPath = PropertyReader.getControlFilesSchemaPath();
			String schemaURL = new File(schemaPath).getAbsolutePath();
			String fileURL = xmlPath + systemSeparator + xmlName;
			File controlFile = new File(fileURL);
			SAXBuilder builder = new SAXBuilder(
					"org.apache.xerces.parsers.SAXParser");
			XMLUtils.validateDocument(fileURL, schemaURL);
			document = builder.build(controlFile);
			this.controlFileName = controlFileName;
		} catch (IOException e) {
			logger.error("ControlFileReader.ControlFileReader() : couldn't open control file xml [" + controlFileName + "] " + e);
			throw new ApplicationException(e);
		} catch (JDOMException e) {
			logger.error("ControlFileReader.ControlFileReader() : Invalid Control XML " + controlFileName + " " + e);
			throw new ApplicationException("Invalid Control XML " + controlFileName + " " + e, 1001);
		}
		logger
			.debug("ControlFileReader.ControlFileReader() : end initating ControlFileReader with file ["+controlFileName+"]");

	}
	
	public GenericInputStructure getGenericInputStructure() throws ApplicationException{
		logger.debug("ControlFileReader.getGenericInputStructure() - started getGenericInputStructure() for input structure "
				+ controlFileName );
		GenericInputStructure inputStructure = new GenericInputStructure();
		Element root = document.getRootElement();
		Element ignoredLinesElement = root.getChild("ignordLines");
		if(ignoredLinesElement != null){
			int ignoredLinesCount = 0;
			try
			{
				ignoredLinesCount = Integer.parseInt(ignoredLinesElement.getValue());
				inputStructure.setIgnoredLinesCount(ignoredLinesCount);
			}catch(Exception e){
				logger.error("ControlFileReader.getGenericInputStructure() : ignored lines element is invalid in " + controlFileName + " file " + e);
			}
			inputStructure.setIgnoredLinesCount(ignoredLinesCount);
		}

		Element useHeadersElement = root.getChild("useHeaders");
		if(useHeadersElement != null)
			if("true".equals(useHeadersElement.getValue()))
				inputStructure.setUseHeaders(true);
			else
				inputStructure.setUseHeaders(false);
		Element extractorElement = root.getChild("extractor");
		Element extractorMethodElement = extractorElement.getChild("method");
		
		inputStructure.setControlFileName(controlFileName);
		
		String extractionMethod = extractorMethodElement.getValue();
		if(extractionMethod.equals("Delimiter")){
			inputStructure.setExtractionMethod(GenericInputStructure.DELIMITER_EXTRACTOR);
		}else if (extractionMethod.equals("RegularExp")){
			inputStructure.setExtractionMethod(GenericInputStructure.REGULAR_EXP_EXTRACTOR);
		}else if (extractionMethod.equals("Excel")){
			inputStructure.setExtractionMethod(GenericInputStructure.EXCEL_EXTRACTOR);
		}else if (extractionMethod.equals("Database")){
			inputStructure.setExtractionMethod(GenericInputStructure.DATABASE_EXTRACTOR);
		}else if (extractionMethod.equals("JavaCode")){
			inputStructure.setExtractionMethod(GenericInputStructure.JAVA_CODE_EXTRACTOR);
		}
		//may be updated in case of other types of parameters
		inputStructure.setExtractionParameter(extractorElement.getChild("parameter").getValue());
		
		if(inputStructure.getExtractionMethod() != GenericInputStructure.DATABASE_EXTRACTOR)
		{
			Element extractionFieldsElement = extractorElement.getChild("extractionFields");
			List fieldElements = extractionFieldsElement.getChildren("field");
			ExtractionField[] extractionFields = new ExtractionField[fieldElements
					.size()];
			for (int i = 0; i < fieldElements.size(); i++) {
					Element fieldElement = ((Element) fieldElements.get(i));
					extractionFields[i] = new ExtractionField(); 
					extractionFields[i].setIndex(Integer.parseInt(fieldElement.getAttribute("index").getValue()));
					extractionFields[i].setIdentifier(fieldElement.getAttribute("identifier").getValue());
					extractionFields[i].setType(fieldElement.getAttribute("type").getValue());
					extractionFields[i].setActive(Boolean.parseBoolean(fieldElement.getAttribute("active").getValue()));
					if(fieldElement.getAttribute("default") != null)
						extractionFields[i].setDefaultValue(fieldElement.getAttribute("default").getValue());
					if(extractionFields[i].getType().equals("date"))
						extractionFields[i].setDateFormat(fieldElement.getAttribute("format").getValue());
			}
			inputStructure.setExtractionFields(extractionFields);
		}
		
		Element sqlRetrievalElement = extractorElement.getChild("sqlRetrieval");
		
		inputStructure.setExtractionSQL(sqlRetrievalElement.getChild("sql").getValue());
		
		GenericMapping outputMapping = new GenericMapping();

		Element mappingElement = root.getChild("mapping");
		outputMapping.setTableName(mappingElement.getChild("tableName").getValue());
		Element mappedColumnsElement = mappingElement.getChild("mappedColumns");
		List mappedColumnsElements = mappedColumnsElement.getChildren("column");
		for (int i = 0; i < mappedColumnsElements.size(); i++) {
				Element columnElement = ((Element) mappedColumnsElements.get(i));
				String resultFieldIdentifier = columnElement.getAttribute("resultField").getValue();
				String outputColumnName = columnElement.getValue();
				outputMapping.createAssoc(resultFieldIdentifier,outputColumnName);
		}
		if(mappingElement.getChild("nodeColumn") != null && mappingElement.getChild("nodeColumn").getValue() != ""){
			outputMapping.setNodeColumnName(mappingElement.getChild("nodeColumn").getValue());
		}
		
		if(mappingElement.getChild("dateColumn") != null && mappingElement.getChild("dateColumn").getValue() != ""){
			outputMapping.setDateColumnName(mappingElement.getChild("dateColumn").getValue());
		}
		
		inputStructure.setOutputMapping(outputMapping);
		logger.debug("ControlFileReader.getGenericInputStructure() - finished getGenericInputStructure() for control file "
				+ controlFileName );
		
		return inputStructure;
	}
	
	public static boolean isControlFileExist(String controlFileName){
		String xmlPath = PropertyReader.getControlFilesPath();
		String xmlName = controlFileName + ".xml";
		String fileURL = xmlPath + systemSeparator + xmlName;
		File controlFile = new File(fileURL);
		return controlFile.exists();
	}

}
