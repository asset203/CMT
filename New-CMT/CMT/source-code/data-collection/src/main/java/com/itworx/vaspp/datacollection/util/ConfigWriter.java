package com.itworx.vaspp.datacollection.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


import org.apache.commons.transaction.file.FileResourceManager;
import org.apache.commons.transaction.util.FileHelper;

import org.apache.log4j.Logger;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.xml.sax.InputSource;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.objects.GenericInputStructure;
import com.itworx.vaspp.datacollection.objects.VNode;
import com.itworx.vaspp.datacollection.objects.VSystem;
import com.itworx.vaspp.datacollection.util.generic.FileTransactionUtils;
import com.itworx.vaspp.datacollection.util.generic.XMLUtils;


public class ConfigWriter {
	private org.jdom.Document document;
	
	private static final String systemSeparator = System.getProperty("file.separator");
	private static String propertiesPath; 

	private Logger logger;
	
	public ConfigWriter(Logger logger) throws ApplicationException{
		try {
			this.logger = logger;
			logger.debug("initating ConfigWriter: reading system data from xml");
			propertiesPath = PropertyReader.getInputConfigFilePath();

			String xmlPath = PropertyReader.getInputConfigFilePath();
			String xmlName = PropertyReader.getInputConfigFileName();
			String fileURL = xmlPath + systemSeparator + xmlName;
			File configFile = new File(fileURL);
			SAXBuilder builder = new SAXBuilder(
					"org.apache.xerces.parsers.SAXParser");
			document = builder.build(configFile);
		} catch (IOException e) {
			logger.error("couldn't open xml input file  " + e);
			throw new ApplicationException(e);
		} catch (JDOMException e) {
			logger.error("Invalid Input XML" + e);
			throw new ApplicationException("Invalid Input XML" + e, 1001);
		}
	}

	public void addGenericInputStructure(GenericInputStructure inputStructure,String controlFileName) throws ApplicationException{
		logger.debug("ConfigWriter.addGenericInputStructure() - started addGenericInputStructure for control file" + controlFileName);
		backupConfiguration();
		String inputStructName = controlFileName+"_input_struct";
		Element root = document.getRootElement();
		Element inputStructuresElement = root.getChild("input_structures");
		List inputStructures = inputStructuresElement.getChildren("input_structure");
		for (Iterator i = inputStructures.iterator(); i.hasNext();) {
			Element inputStructureElement = (Element) i.next();
			String structureID = inputStructureElement.getAttribute("id").getValue();
			if(structureID.equals(inputStructName)){
				throw new ApplicationException("input structure with name ["+structureID+"] already exists in input_config.xml");
			}
		}
		if(ControlFileReader.isControlFileExist(controlFileName)){
			throw new ApplicationException("Control file with name ["+controlFileName+"] already exists");
		}
		try{
			XMLUtils.init(logger);
			Element newInpStruct = XMLUtils.createXMLElement("genericinputstructure.vm", "controlFileName", controlFileName);
			inputStructuresElement.addContent("\n\t");
			inputStructuresElement.addContent(newInpStruct);
			inputStructuresElement.addContent("\n");
			XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
			String inputConfigURL = propertiesPath+File.separator+PropertyReader.getInputConfigFileName();
			File inputConfigFile = new File(inputConfigURL);
			BufferedOutputStream inputConfigFileStream = new BufferedOutputStream(new FileOutputStream(inputConfigFile));
			outputter.output(document, inputConfigFileStream);
			ControlFileWriter controlWriter = new ControlFileWriter(inputStructure,controlFileName,logger);
			controlWriter.writeControlFile();
			inputConfigFileStream.close();
		}catch(IOException e){
			throw new ApplicationException("an error occured while writing input_config.xml" + e);
		}
		logger
				.debug("ConfigWriter.addGenericInputStructure() - finished addGenericInputStructure for control file" + controlFileName);
	}

	public void saveSystem(VSystem system) throws ApplicationException{
		logger.debug("ConfigWriter.saveSystem() - start to writing system ["+system.getName()+"] contents");
		backupConfiguration();
		FileTransactionUtils.init(logger);
		FileResourceManager fileResourceManager = FileTransactionUtils.getFileResourceManager(PropertyReader.getTransactionStorePath(), PropertyReader.getTransactionWorkingPath());
		String transactionId = FileTransactionUtils.beginTransaction(fileResourceManager);
		try{
			//perform saving operation of the system
			InputStream cnfInpStream = FileTransactionUtils.getResourceInputStream(fileResourceManager, transactionId, PropertyReader.getInputConfigFileRelativeURL());
			InputStream jobsInpStream = FileTransactionUtils.getResourceInputStream(fileResourceManager, transactionId, PropertyReader.getJobsFileRelativeURL());
			InputStream logsInpStream = FileTransactionUtils.getResourceInputStream(fileResourceManager, transactionId, PropertyReader.getLogsFileRelativeURL());
			
			Document inputConfigDoc = XMLUtils.getXMLDocument(cnfInpStream);
			Document jobsDoc = XMLUtils.getXMLDocument(jobsInpStream);
			Document logsDoc = XMLUtils.getXMLDocument(logsInpStream);

			cnfInpStream.close();
			jobsInpStream.close();
			logsInpStream.close();

			saveSystem(system, jobsDoc, logsDoc, inputConfigDoc);
		
			OutputStream cnfOutStream = FileTransactionUtils.getResourceOutputStream(fileResourceManager, transactionId, PropertyReader.getInputConfigFileRelativeURL());
			OutputStream jobsOutStream = FileTransactionUtils.getResourceOutputStream(fileResourceManager, transactionId, PropertyReader.getJobsFileRelativeURL());
			OutputStream logsOutStream = FileTransactionUtils.getResourceOutputStream(fileResourceManager, transactionId, PropertyReader.getLogsFileRelativeURL());

			XMLUtils.writeXMLDocument(inputConfigDoc, cnfOutStream);
			XMLUtils.writeXMLDocument(jobsDoc, jobsOutStream);
			XMLUtils.writeXMLDocument(logsDoc, logsOutStream);
			
			cnfOutStream.close();
			jobsOutStream.close();
			logsOutStream.close();
			
			String schemaPath = PropertyReader.getInputConfigSchemaPath();
			String schemaURL = new File(schemaPath).getAbsolutePath();

			XMLUtils.validateDocument(inputConfigDoc, schemaURL);
			
			FileTransactionUtils.commitTransaction(fileResourceManager, transactionId);
		}catch(Exception e){
			FileTransactionUtils.rollbackTransaction(fileResourceManager, transactionId);
			logger.error("ConfigWriter.saveOrUpdateSystem() - error while saving system ["+system.getName()+"] :" + e);
			throw new ApplicationException("error while saving system ["+system.getName()+"] :" + e);
		}
		logger.debug("ConfigWriter.saveSystem() - finish writing system ["+system.getName()+"] contents");
	}

	public void deleteSystem(VSystem system) throws ApplicationException{
		logger.debug("ConfigWriter.deleteSystem() - start writing system ["+system.getName()+"] contents");
		backupConfiguration();
		FileTransactionUtils.init(logger);
		FileResourceManager fileResourceManager = FileTransactionUtils.getFileResourceManager(PropertyReader.getTransactionStorePath(), PropertyReader.getTransactionWorkingPath());
		String transactionId = FileTransactionUtils.beginTransaction(fileResourceManager);
		try{
			//perform deletion operation of the system
			InputStream cnfInpStream = FileTransactionUtils.getResourceInputStream(fileResourceManager, transactionId, PropertyReader.getInputConfigFileRelativeURL());
			InputStream jobsInpStream = FileTransactionUtils.getResourceInputStream(fileResourceManager, transactionId, PropertyReader.getJobsFileRelativeURL());
			InputStream logsInpStream = FileTransactionUtils.getResourceInputStream(fileResourceManager, transactionId, PropertyReader.getLogsFileRelativeURL());
			
			Document inputConfigDoc = XMLUtils.getXMLDocument(cnfInpStream);
			Document jobsDoc = XMLUtils.getXMLDocument(jobsInpStream);
			Document logsDoc = XMLUtils.getXMLDocument(logsInpStream);

			cnfInpStream.close();
			jobsInpStream.close();
			logsInpStream.close();

			deleteSystem(system, jobsDoc, logsDoc, inputConfigDoc);
		
			
			OutputStream cnfOutStream = FileTransactionUtils.getResourceOutputStream(fileResourceManager, transactionId, PropertyReader.getInputConfigFileRelativeURL());
			OutputStream jobsOutStream = FileTransactionUtils.getResourceOutputStream(fileResourceManager, transactionId, PropertyReader.getJobsFileRelativeURL());
			OutputStream logsOutStream = FileTransactionUtils.getResourceOutputStream(fileResourceManager, transactionId, PropertyReader.getLogsFileRelativeURL());

			XMLUtils.writeXMLDocument(inputConfigDoc, cnfOutStream);
			XMLUtils.writeXMLDocument(jobsDoc, jobsOutStream);
			XMLUtils.writeXMLDocument(logsDoc, logsOutStream);
			
			cnfOutStream.close();
			jobsOutStream.close();
			logsOutStream.close();
			
			String schemaPath = PropertyReader.getInputConfigSchemaPath();
			String schemaURL = new File(schemaPath).getAbsolutePath();

			XMLUtils.validateDocument(inputConfigDoc, schemaURL);
			
			FileTransactionUtils.commitTransaction(fileResourceManager, transactionId);
		}catch(Exception e){
			FileTransactionUtils.rollbackTransaction(fileResourceManager, transactionId);
			logger.error("ConfigWriter.deleteSystem() - error while saving system ["+system.getName()+"] :" + e);
			throw new ApplicationException("error while deleting system ["+system.getName()+"] :" + e);
		}
		logger.debug("ConfigWriter.deleteSystem() - finish writing system ["+system.getName()+"] contents");
	}
	
	private void saveSystem(VSystem system ,Document jobsDoc ,Document logsDoc ,Document inputConfigDoc) throws ApplicationException{
		deleteSystem(system, jobsDoc, logsDoc, inputConfigDoc);
		addSystem(system,jobsDoc,logsDoc,inputConfigDoc);
	}
	
	public void deleteSystem(VSystem system ,Document jobsDoc ,Document logsDoc ,Document inputConfigDoc) throws ApplicationException{
		ConfigReader reader = new ConfigReader(logger);
		if(reader.isDCSystemsExist(system.getName())){
			VSystem tmpSystem = reader.getSystem(system.getName());
			VNode[] nodes = tmpSystem.getNodes();
			if(nodes != null){
				for(int i = 0; i<nodes.length;i++){
					deleteNode(nodes[i], jobsDoc, inputConfigDoc);
				}
			}
			XMLUtils.removeAppenderElement(logsDoc, tmpSystem.getName());
			XMLUtils.removeLoggerElement(logsDoc, tmpSystem.getName());
			XMLUtils.removeSystemElement(inputConfigDoc, tmpSystem.getName());
		}
	}
	
	private void addSystem(VSystem system, Document jobsDoc, Document logsDoc,	Document inputConfigDoc) throws ApplicationException {
		XMLUtils.addAppenderElement(logsDoc, system);
		XMLUtils.addLoggerElement(logsDoc, system);
		XMLUtils.addSystemElement(inputConfigDoc, system);
		VNode[] nodes = system.getNodes();
		
		if(nodes != null){
			for(int i = 0; i<nodes.length;i++){
				addNode(nodes[i], jobsDoc, inputConfigDoc);
			}
		}
	}

	private void deleteNode(VNode node, Document jobsDoc , Document inputConfigDoc){
		XMLUtils.removeJobElement(jobsDoc, node.getSystemName(), node.getName());
		XMLUtils.removeNodeElement(inputConfigDoc, node.getSystemName(), node.getName());
	}
	
	private void addNode(VNode node, Document jobsDoc, Document inputConfigDoc) throws ApplicationException {
		XMLUtils.addJobElement(jobsDoc, node);
		XMLUtils.addNodeElement(inputConfigDoc, node);
	}

	private void backupConfiguration(){
		logger.debug("ConfigWriter.backupConfiguration() - start backup configuration");
		Date d = new Date();
		String backupFolderName = d.getTime()+"";
		
		String sourcePath = PropertyReader.getTransactionStorePath();
		String backupPath = PropertyReader.getConfigurationBackupPath()+File.separator+backupFolderName;
		
		String inputConfig = "input_config.xml";
		String logs = "log4j.xml";
		String jobs = "jobs.xml";

		File output = new File(backupPath);
		output.mkdir();
		
		File outInputConfigFile = new File(backupPath+File.separator+inputConfig);
		File outLogsFile = new File(backupPath+File.separator+logs);
		File outJobsFile = new File(backupPath+File.separator+jobs);

		File srcInputConfigFile = new File(sourcePath + File.separator+"properties" + File.separator+inputConfig);
		File srcLogsFile = new File(sourcePath + File.separator+"configuration" + File.separator+logs);
		File srcJobsFile = new File(sourcePath + File.separator+"configuration" + File.separator+jobs);
		
		try {
			FileHelper.copy(srcInputConfigFile, outInputConfigFile);
			FileHelper.copy(srcLogsFile, outLogsFile);
			FileHelper.copy(srcJobsFile, outJobsFile);
		} catch (IOException e) {
			logger.error("ConfigWriter.backupConfiguration() - error while backup confgurations :" + e);
			e.printStackTrace();
		}
		logger.debug("ConfigWriter.backupConfiguration() - finish backup configuration");
	}

	public static void main(String args[]){
		//initAddGenericInputStructure();
		//performFileTransaction();
		
		
		try {
//			performFileTransaction();
			PropertyReader.init("D:/Work/My Eclipse Projects/ProofOfConcepts/WebRoot");
			XMLUtils.init(DataCollectionManager.getLogger());
			
			ConfigWriter writer = new ConfigWriter(DataCollectionManager.getLogger());
			
			VSystem system = getSampleSystem();
			
			writer.backupConfiguration();
			writer.saveSystem(system);
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static VSystem getSampleSystem(){
		VSystem sys = null;
		try {
			ConfigReader cr = new ConfigReader(DataCollectionManager.getLogger());
			sys = cr.getSystem("USSDShort");
//			sys.setDescription("sdghjhsdgjhsdgj\nkjsdhjkhsdkjhjds\tsdhgjsdh");
//			sys.setGeneric(true);
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return sys;
	}
}
