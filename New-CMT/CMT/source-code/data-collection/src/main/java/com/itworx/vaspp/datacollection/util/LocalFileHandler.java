/* 
 * File: LocalFileHandler.java
 * 
 * Date        Author          Changes
 * 
 * 21/03/2006  Nayera Mohamed  Created
 * 
 * Responsible for Retrieval of input files from local machine
 */

package com.itworx.vaspp.datacollection.util;

import java.io.File;
import java.util.Arrays;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eg.com.vodafone.model.VInput;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.log4j.Logger;

import com.itworx.vaspp.datacollection.common.InputException;

public class LocalFileHandler extends FileHandler {

	private FTPClient ftp;

	private Logger logger;

	protected LocalFileHandler() {

	}
	/**
	 * connect to local server and retrieve all files matching the regular
	 * expression supplied in input name property and for current ftped path
	 * 
	 * @param input -
	 *            the input object for targeted system node data
	 * @param targetLocation -
	 *            the local location where the files will be saved
	 * @param prefix -
	 *            random prefix used to be appended on the file name
	 * @param currentServerPath -
	 *            current used path on the extracted server to ftp files
	 * 
	 * @return File[] - array of input Files for this input
	 * @exception InputException
	 *                if connection to server failed if no files where found
	 *                matching input name
	 */
	private File[] performGetFiles(VInput input, String targetLocation,
			String prefix ,String currentServerPath) throws InputException {
		logger = Logger.getLogger(input.getSystemName());
		logger
				.debug("LocalFileHandler.performGetFiles() - Started performGetFile for file: "
						+ input.getInputName()
						+ " to location: "
						+ targetLocation
						+ " from path : " 
						+ currentServerPath);

		File folder = new File(currentServerPath);
		Vector filesList = new Vector();
		if (!folder.exists()) {
			logger
					.error("-"+ input.getNodeName() +"- FH - 3000 :LocalFileHandler.performGetFiles() - Couldn't find path on local machine"
							+ currentServerPath);
			throw new InputException("-"+ input.getNodeName() +"- Couldn't find path on local machine"
					+ currentServerPath);
		}
		File[] files = folder.listFiles();
		Pattern p = Pattern.compile(input.getInputName());
		if (files == null) { // no files found on specified path
			logger
					.error("-"+ input.getNodeName() +"- FH - 3002 : LocalFileHandler.performGetFiles() - No files found on local path "
							+ currentServerPath);
			throw new InputException("-"+ input.getNodeName() +"- No files found on local path "
					+ currentServerPath);
		}
		for (int i = 0; i < files.length; i++) {
			Matcher m = p.matcher(files[i].getName());
			if (m.matches()) {
				filesList.addElement(files[i]);
			}
		}

		if (filesList.size() == 0) { // no files found matching input name
			logger
					.error("-"+ input.getNodeName() +"- FH - 3003 : LocalFileHandler.performGetFiles() - No files matching name "
							+ input.getInputName());
			throw new InputException("-"+ input.getNodeName() +"- No files matching name "
					+ input.getInputName());
		}

		logger
				.debug("-"+ input.getNodeName() +"- LocalFileHandler.performGetFiles() - finished performGetFile for file: "
						+ input.getInputName()
						+ " to location: "
						+ targetLocation
						+ " from path : " 
						+ currentServerPath);
		File[] localFiles = new File[filesList.size()];
		localFiles = (File[]) filesList.toArray(localFiles);
		return localFiles;
	}
	
	/**
	 * connect to local server and retrieve all files matching the regular
	 * expression supplied in input name property
	 * 
	 * @param input -
	 *            the input object for targeted system node data
	 * @param targetLocation -
	 *            the location where the files are exist
	 * @param prefix -
	 *            the prefix appended to local file
	 * 
	 * @return File[] - array of input Files for this input
	 * @exception InputException
	 *                if connection to server failed if no files where found
	 *                matching input name
	 */
	public File[] performGetFiles(VInput input, String targetLocation,
			String prefix) throws InputException {
		logger = Logger.getLogger(input.getSystemName());
		logger.debug("FtpHandler.performGetFiles() - started for file: "
				+ input.getInputName() + " to location: " + targetLocation);
		boolean stopDCProcess = PropertyReader.isStopDCInMultiPathsError();
		Vector mainFilesList = new Vector();
		String currentPath = "";
		
		String pathPrfx = "";
		boolean pathManipulated = isPathManipulated(input);
		for(int i = 0;i<input.getPaths().length;i++) 
		{
			currentPath = input.getPaths()[i];
			try{
				if(pathManipulated){
					pathPrfx = "("+currentPath.replaceAll("\\\\|/", "+")+")";
				}
			File[] localFiles = performGetFiles(input,targetLocation,prefix + pathPrfx,currentPath);
			mainFilesList.addAll(Arrays.asList(localFiles));
			}catch(InputException e){
				if(input.getPaths().length == 1)
					throw e;
				else{
					if(stopDCProcess){
						throw e;
					}else{
						logger
							.error("-"+ input.getNodeName() +"- FH - 2006 : LocalFileHandler.performGetFiles() - couldn't retrieve ftp files in this path ["+currentPath+"]",e);
						e.printStackTrace();
					}
				}
			}
		}
		if (mainFilesList.size() == 0) {
			logger
					.error("-"+ input.getNodeName() +"- FH - 2004 : LocalFileHandler.performGetFiles() - couldn't retrieve ftp file - no files found matching name");
			throw new InputException("-"+ input.getNodeName() +"- couldn't retrieve ftp file");
		}
		logger
				.debug("LocalFileHandler.performGetFiles() -finished performGetFile for file: "
						+ input.getInputName()
						+ " to location: "
						+ targetLocation);
		File[] localFiles = new File[mainFilesList.size()];
		localFiles = (File[]) mainFilesList.toArray(localFiles);
		return localFiles;
	}

}