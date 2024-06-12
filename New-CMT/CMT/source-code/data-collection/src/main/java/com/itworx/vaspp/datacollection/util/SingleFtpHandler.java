/* 
 * File: SingleFtpHandler.java
 * 
 * Date        Author          Changes
 * 
 * 25/05/2006  Nayera Mohamed  Created
 * 
 * Responsible for Retrieval of input files using apache ftp client
 */

package com.itworx.vaspp.datacollection.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Vector;

import eg.com.vodafone.model.VInput;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

import com.itworx.vaspp.datacollection.common.InputException;

public class SingleFtpHandler extends FileHandler {

	private FTPClient ftp;

	private Logger logger;

	protected SingleFtpHandler() {
	}

	/**
	 * connect to ftp server
	 * 
	 * @param ftp -
	 *            the apache ftp client
	 * @param server -
	 *            the name of ftp server
	 * @param user -
	 *            user name for ftp
	 * @param password -
	 *            password for ftp
	 * 
	 * @exception InputException
	 *                if FTP server refused connection if FTP server couldn't be
	 *                reached
	 */
	private void connect(FTPClient ftp, String server, String user,
			String password) throws InputException {
		String response;
		int reply;
		try {
			ftp.connect(server);
			response = ftp.getReplyString();
			ftp.login(user, password);
			response = ftp.getReplyString();
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				logger
						.error("FH - 2001:FtpHandler.connect() - FTP server refused connection.");
				throw new InputException("FTP server refused connection.");
			}
		} catch (IOException e) {
			logger
					.error("FH - 2000:FtpHandler.connect() - Error connecting to ftp server"
							+ server + " " + e);
			throw new InputException("Error connecting to ftp server" + server
					+ " " + e);
		}
	}

	/**
	 * connect to ftp server and retrieve all files matching the regular
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
			String prefix,String currentServerPath) throws InputException {
		logger = Logger.getLogger(input.getSystemName());
		logger.debug("SingleFtpHandler.performGetFiles() - started for file: "
				+ input.getInputName() + " to location: " + targetLocation + " from ftp path : " + currentServerPath);
		try {
			Vector filesList = new Vector();
			ftp = new FTPClient();
			this.connect(ftp, input.getServer(), input.getUser(), input
					.getPassword());
			ftp.setFileType(FTP.BINARY_FILE_TYPE);
			String localFileName = prefix+ "_"+ input.getInputName();
			File localFile = new File(targetLocation, localFileName);
			localFile.delete();
			FileOutputStream out = new FileOutputStream(localFile);
			String serverFile = currentServerPath + "/"+ input.getInputName();
			boolean success = ftp.retrieveFile(serverFile, out);
			if (!success) {
				ftp.disconnect();
				localFile.delete();
				logger
					.error("-"+ input.getNodeName() +"- FH - 4002: SingleFtpHandler.performGetFiles() - couldn't retrieve ftp file");
				throw new InputException("couldn't retrieve ftp file");
			}
			out.close();
			ftp.disconnect();
			logger
					.debug("SingleFtpHandler.performGetFiles() -finished performGetFile for file: "
							+ input.getInputName()
							+ " to location: "
							+ targetLocation
							+ " from ftp path : "
							+ currentServerPath);
			File[] localFiles = new File[1];
			localFiles[0] = localFile;
			return localFiles;
		} catch (IOException ex) {
			logger.error("-"+ input.getNodeName() +"- FH - 4004 - SingleFtpHandler.performGetFiles() - error getting file "
					+ ex);
			throw new InputException(ex);
		}
	}
	
	/**
	 * connect to ftp server and retrieve all files matching the regular
	 * expression supplied in input name property
	 * 
	 * @param input -
	 *            the input object for targeted system node data
	 * @param targetLocation -
	 *            the local location where the files will be saved
	 * @param prefix -
	 *            prefix used to be appended on the file name
	 *            
	 * @return File[] - array of input Files for this input
	 * @exception InputException
	 *                if connection to server failed if no files where found
	 *                matching input name
	 */
	public File[] performGetFiles(VInput input, String targetLocation,
			String prefix) throws InputException {
		logger = Logger.getLogger(input.getSystemName());
		logger.debug("SingleFtpHandler.performGetFiles() - started for file: "
				+ input.getInputName() + " to location: " + targetLocation);
		
		Vector mainFilesList = new Vector();
		String currentPath = "";
		String pathPrfx = "";
		boolean pathManipulated = isPathManipulated(input);
		for(int i = 0;i<input.getPaths().length;i++)
		{
			currentPath = input.getPaths()[i];
			if(pathManipulated){
				pathPrfx = "("+currentPath.replaceAll("\\\\|/", "+")+")";
			}
			File[] localFiles = performGetFiles(input,targetLocation,prefix + pathPrfx,currentPath);
			mainFilesList.addAll(Arrays.asList(localFiles));
		}
		if (mainFilesList.size() == 0) {
			logger
					.error("-"+ input.getNodeName() +"- FH - 2004 : SingleFtpHandler.performGetFiles() - couldn't retrieve ftp file - no files found matching name");
			throw new InputException("-"+ input.getNodeName() +"- couldn't retrieve ftp file");
		}
		logger
				.debug("SingleFtpHandler.performGetFiles() -finished performGetFile for file: "
						+ input.getInputName()
						+ " to location: "
						+ targetLocation);
		File[] localFiles = new File[mainFilesList.size()];
		localFiles = (File[]) mainFilesList.toArray(localFiles);
		return localFiles;
	}
	

	// for testing
	public static void main(String arg[]) {
		SingleFtpHandler f = new SingleFtpHandler();

		try {
			// logger = Logger.getLogger("ddd");
			VInput input = new VInput();
			input.setSystemName("nayera");
			input.setInputName("sar_*");
			input.setServer("blade1");
			input.setUser("nayera");
			input.setPassword("nayera");
			input.setPaths(new String[]{"/export/home/nayera/ftptest/"});
			f.performGetFiles(input, "c:\\", "dd");
		} catch (InputException e) {
			e.printStackTrace();
		}
	}
}