/* 
 * File: FtpHandler.java
 * 
 * Date        Author          Changes
 * 
 * 24/01/2006  Nayera Mohamed  Created
 * 07/03/2006  Nayera Mohamed  Updated to retrieve list of file, matching regular expression for input name
 * 28/03/2006  Nayera Mohamed  Updated to match names correctly when retreiving files from Solaris 8 & 9
 * 10/05/2006  Nayera Mohamed  Updated to include file prefix
 * 
 * Responsible for Retrieval of input files using apache ftp client
 */

package com.itworx.vaspp.datacollection.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eg.com.vodafone.model.VInput;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.io.CopyStreamException;
import org.apache.log4j.Logger;

import com.itworx.vaspp.datacollection.common.InputException;

public class FtpHandler extends FileHandler {

	private FTPClient ftp;

	private Logger logger;

	protected FtpHandler() {
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
			String password,String nodeName) throws InputException {
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
						.error("-"+ nodeName +"- FH - 2001: FtpHandler.connect() - FTP server refused connection."+ server);
				throw new InputException("FTP server refused connection."+ server);
			}
		} catch (Exception e) {
			logger
					.error("-"+ nodeName +"- FH - 2000: FtpHandler.connect() - Error connecting to ftp server"
							+ server + " " , e);
			throw new InputException("-"+ nodeName +"- Error connecting to ftp server" + server
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
	 *            prefix used to be appended on the file name
	 * @param currentServerPath -
	 *            current used path on the extracted server to ftp files
	 * 
	 * @return File[] - array of input Files for this input
	 * @exception InputException
	 *                if connection to server failed if no files where found
	 *                matching input name
	 */
	private File[] performGetFiles(VInput input,String targetLocation,
			String prefix,String currentServerPath) throws InputException {
		
		logger = Logger.getLogger(input.getSystemName());
		logger.debug("FtpHandler.performGetFiles() - started for file: "
				+ input.getInputName() + " to location: " + targetLocation + " from ftp path : " + currentServerPath);
		try {
			Vector filesList = new Vector();
			ftp = new FTPClient();
		
			this.connect(ftp, input.getServer(), input.getUser(), input
					.getPassword(),input.getNodeName());
			String serverFileName = null;
			ftp.setFileType(FTP.BINARY_FILE_TYPE);
			Pattern p = Pattern.compile(input.getInputName());
			logger.debug("FtpHandler.performGetFiles() - Looking for file "
					+ p.pattern());
			// list file names on the given path
			String[] files = ftp.listNames(currentServerPath);

			if (files == null || files.length == 0) {
				// no files were found on the given path
				logger
						.error("-"+ input.getNodeName() +"- FH - 2002: FtpHandler.performGetFiless() - Couldn't retrieve ftp file: No files found on specified path "+currentServerPath);
				throw new InputException(
						"-"+ input.getNodeName() +"- couldn't retrieve ftp file: No files found on specified path "+currentServerPath);
			}
			int foundCount = 0;
			// loop over file names found on the given path
			for (int i = 0; i < files.length; i++) {

				int index = files[i].lastIndexOf("/"); // index of the name of
				// the file
				// if ( index == -1 ) index = 0;
				logger.debug("file name" + files[i]);
				// make sure serverFileName holds only the name of the file not
				// the whole path
				// as in case of Solaris 8 the whole path is returned
				serverFileName = files[i].substring(index + 1, files[i]
						.length());
				// System.out.println("file name"+files[i]
				// +"updated"+serverFileName);
				// System.out.println(p.pattern());
				Matcher m = p.matcher(serverFileName);
				if (m.matches()) {
					foundCount++;
					// retrieve file matching input name pattern
					// System.out.println("match");
					String localFileName ="";
					String systemName = PropertyReader.getUssdConnectorsSystemName();
					String[] systemNames = Utils.split(systemName,",");
					String c2Name=PropertyReader.getUssdConnC2SystemName();
					
					if(Utils.exists(input.getSystemName(),systemNames)||input.getSystemName().equals(c2Name)){
						String[] filePathTokens = currentServerPath.split("/");
						String connectorName = filePathTokens[filePathTokens.length-1];
						localFileName = prefix + foundCount + "_(" +connectorName+ ")_"	+ serverFileName;						
					} else {
						localFileName = prefix + foundCount + "_"
						+ serverFileName;
					}
					
					File localFile = new File(targetLocation, localFileName);
					localFile.delete();
					FileOutputStream out = new FileOutputStream(localFile);
					String serverFile = currentServerPath + serverFileName;
					boolean success = ftp.retrieveFile(serverFile, out);
					if (!success) {
						ftp.disconnect();
						localFile.delete();
						logger
								.error("-"+ input.getNodeName() +"- FH - 2003 : FtpHandler.performGetFiles() - couldn't retrieve ftp file ("+serverFile+")");
						throw new InputException("-"+ input.getNodeName() +"- couldn't retrieve ftp file file ("+serverFile+")");
					}
					filesList.addElement(localFile);
					out.close();
				}
			}
			ftp.disconnect();
			if (filesList.size() == 0) {
				logger
						.error("-"+ input.getNodeName() +"- FH - 2004 : FtpHandler.performGetFiles() - couldn't retrieve ftp file - no files found matching name for path : "+currentServerPath);
				throw new InputException("-"+ input.getNodeName() +"- couldn't retrieve ftp file for path : "+currentServerPath);			
			}
			logger
					.debug("FtpHandler.performGetFiles() -finished performGetFile for file: "
							+ input.getInputName()
							+ " to location: "
							+ targetLocation
							+ " from ftp path : " 
							+ currentServerPath);
			File[] localFiles = new File[filesList.size()];
			localFiles = (File[]) filesList.toArray(localFiles);
			return localFiles;
		} catch (CopyStreamException ex ){
			logger.error("-"+ input.getNodeName() +"- FH - 2005 - FtpHandler.performGetFiles() - CopyStreamException error getting file "
					, ex);
			throw new InputException(ex);
		} catch (IOException ex) {
			logger.error("-"+ input.getNodeName() +"- FH - 2005 - FtpHandler.performGetFiles() - error getting file "
					, ex);
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
		Vector mainFilesList = new Vector();
		String currentPath = "";
		boolean stopDCProcess = PropertyReader.isStopDCInMultiPathsError();
		
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
							.error("-"+ input.getNodeName() +"- FH - 2006 : FtpHandler.performGetFiles() - couldn't retrieve ftp files in this path ["+currentPath+"]",e);
						e.printStackTrace();
					}
				}
			}
		}
		if (mainFilesList.size() == 0) {
			logger
					.error("-"+ input.getNodeName() +"- FH - 2004 : FtpHandler.performGetFiles() - couldn't retrieve ftp file - no files found matching name");
			throw new InputException("-"+ input.getNodeName() +"- couldn't retrieve ftp file");
		}
		logger
				.debug("FtpHandler.performGetFiles() -finished performGetFile for file: "
						+ input.getInputName()
						+ " to location: "
						+ targetLocation);
		File[] localFiles = new File[mainFilesList.size()];
		localFiles = (File[]) mainFilesList.toArray(localFiles);
		return localFiles;
	}

	public static void main(String[] args) {
		Pattern p = Pattern.compile("CCIVR.\\p{Alpha}+(\\d+)-(\\d+).ucip2010121610.cdr");
		String serverFileName="CCIVR.mpsram2-4.ucip2010121610.cdr";
		Matcher m = p.matcher(serverFileName);
		System.out.println(m.matches());
//		FtpHandler handler= new FtpHandler();
//		
//		String path = "/export/home/nayera/ftptest/KPI";
//		try {
//			FTPClient ftp = new FTPClient();
//			handler.connect(ftp,"blade1", "nayera", "nayera","21");
//			String[] fileNames=ftp.listNames(path);
//			for (int i = 0; i < fileNames.length; i++) {
//				System.out.println(fileNames[i]);
//			}
//		} catch (InputException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
}