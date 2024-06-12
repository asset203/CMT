/*
 * File: VMSFtpHandler.java
 *
 * Date        Author          Changes
 *
 * 25/05/2006  Nayera Mohamed  Created
 *
 * Responsible for Retrieval of input files using apache ftp client
 */

package com.itworx.vaspp.datacollection.util;

import com.itworx.vaspp.datacollection.common.*;
import com.itworx.vaspp.datacollection.objects.VInput;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

public class VMSFtpHandler extends FileHandler {

	private FTPClient ftp;

	private Logger logger;

	protected VMSFtpHandler() {
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
						.error("FH - 5001: VMSFtpHandler.connect() - FTP server refused connection.");
				throw new InputException("FTP server refused connection.");
			}
		} catch (IOException e) {
			logger
					.error("FH - 5001: VMSFtpHandler.connect() - Error connecting to ftp server"
							+ server + " " + e);
			throw new InputException("Error connecting to ftp server" + server
					+ " " + e);
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
		logger.debug("VMSFtpHandler.performGetFiles() - started for file: "
				+ input.getInputName() + " to location: " + targetLocation);
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
			String serverFile = input.getInputName();
			boolean success = ftp.retrieveFile(serverFile, out);
			if (!success) {
				ftp.disconnect();
				localFile.delete();
				logger
					.error("VMSFtpHandler.performGetFiles() - couldn't retrieve ftp file");
				throw new InputException("couldn't retrieve ftp file");
			}
			out.close();
			ftp.disconnect();
			logger
					.debug("VMSFtpHandler.performGetFiles() -finished performGetFile for file: "
							+ input.getInputName()
							+ " to location: "
							+ targetLocation);
			File[] localFiles = new File[1];
			localFiles[0] = localFile;
			return localFiles;
		} catch (IOException ex) {
			logger.error("VMSFtpHandler.performGetFiles() - error getting file "
					+ ex);
			throw new InputException(ex);
		}
	}

	// for testing
	public static void main(String arg[]) {
		VMSFtpHandler f = new VMSFtpHandler();

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