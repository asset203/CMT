/* 
 * File:       FileHandler.java
 * 
 * Date        Author          Changes
 * 
 * 19/01/2006  Nayera Mohamed  Created
 * 07/03/2006  Nayera Mohamed  Updated to retrieve list of files
 * 10/05/2006  Nayera Mohamed  Updated to include file prefix
 * 
 * Class responsible for managing retrieval of input files,
 * extended by handlers of different access methods
 */
package com.itworx.vaspp.datacollection.util;

import java.io.File;
import java.util.Date;

import eg.com.vodafone.model.VInput;
import org.apache.log4j.Logger;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;


public class FileHandler {
	private static PropertyReader propertyReader = new PropertyReader();

	private Logger logger;

	public FileHandler() {
	}

	/**
	 * create an instance of the approriate handler class for the specified
	 * access method and initiate the files retrieval
	 * 
	 * @param input -
	 *            the input object for targeted system node data
	 * 
	 * @return File[] - array of input Files for this input
	 * @exception InputException
	 *                Input file couldn't be retrieved
	 * @exception ApplicationException
	 *                if access method hanlder class is not found if access
	 *                method hanlder class couldn't be instansiated
	 */
	public File[] getFiles(VInput input) throws ApplicationException,
			InputException {
		logger = Logger.getLogger(input.getSystemName());
		logger.debug("FileHandler.getFile() - started getFile for input file:"
				+ input.getInputName());
		String handlerClassName = propertyReader.getAccessClass(input
				.getAccessMethod());
		String targetLocation = propertyReader.getImportedFilesPath();
		try {
			FileHandler accessMethodHandler = (FileHandler) Class.forName(
					handlerClassName).newInstance();
			long timeStamp = new Date().getTime();
			
			String node = "";
			
			String nodeManipulateSystems = PropertyReader.getNodeManipulateSystems();
			if(!Utils.isEmpty(nodeManipulateSystems)){
				nodeManipulateSystems = nodeManipulateSystems.toLowerCase();
				String[] systems = nodeManipulateSystems.split(",");
				if(Utils.exists(input.getSystemName().toLowerCase(), systems)){
					node = "_("+input.getNodeName()+")";
				}
			}
			
			String prefix = input.getSystemName() +node+ "_" + timeStamp + "_";
			File[] inputFiles = accessMethodHandler.performGetFiles(input,
					targetLocation, prefix);
			logger
					.debug("FileHandler.getFile() - finished getFile for input file:"
							+ input.getInputName());
			return inputFiles;
		} catch (ClassNotFoundException e) {
			logger
					.error("-"+ input.getNodeName() +"- FH - 1000 : FileHandler.getFile() - couldn't find accessMethodHandler class for accessMethod:"
							+ input.getAccessMethod() + e);
			throw new ApplicationException(
					"-"+ input.getNodeName() +"- error: access method handler class not found: "
							+ handlerClassName + " " + e);
		} catch (InstantiationException e) {
			logger
					.error("-"+ input.getNodeName() +"- FH - 1001 :FileHandler.getFile() - couldn't instatntiate accessMethodHandler class: "
							+ e);
			throw new ApplicationException(e);
		} catch (IllegalAccessException e) {
			logger
					.error("-"+ input.getNodeName() +"- FH - 1002 : FileHandler.getFile() - couldn't acces accessMethodHandler class: "
							+ e);
			throw new ApplicationException(e);
		}
	}

	// Dummy method overriden in subclasses
	public File[] performGetFiles(VInput input, String targetLocation,
			String prefix) throws InputException {
		return null;
	}
	
	public boolean isPathManipulated(VInput input) {
		boolean pathManipulated = false;
		String pathManipulateSystems = PropertyReader.getPathManipulateSystems();
		if(!Utils.isEmpty(pathManipulateSystems)){
			pathManipulateSystems = pathManipulateSystems.toLowerCase();
			String[] systems = pathManipulateSystems.split(",");
			if(Utils.exists(input.getSystemName().toLowerCase(), systems)){
				pathManipulated = true;
			}
		}
		return pathManipulated;
	}
}