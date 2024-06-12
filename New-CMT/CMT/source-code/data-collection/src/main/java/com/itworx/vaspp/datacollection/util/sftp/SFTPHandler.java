package com.itworx.vaspp.datacollection.util.sftp;

import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.FileHandler;
import com.itworx.vaspp.datacollection.util.PropertyReader;
import com.itworx.vaspp.datacollection.util.Utils;
import com.jcraft.jsch.*;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import eg.com.vodafone.model.VInput;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SFTPHandler extends FileHandler {
	
	
	private Logger logger;
	
	private int port=22;
	public static class MyProgressMonitor implements SftpProgressMonitor
	{
		long count = 0;
		long max = 0;
		//ProgressMonitor monitor;
		private long percent = -1;
		public boolean count(long count)
		{
			this.count += count;
//			if (percent >= this.count * 100 / max)
//			{
//				return true;
//			}
//			percent = this.count * 100 / max;
//			System.out
//					.println("Completed "
//					+ this.count
//					+ "("
//					+ percent
//					+ "%) out of "
//					+ max
//					+ ".");
			/*monitor.setNote(
				"Completed "
					+ this.count
					+ "("
					+ percent
					+ "%) out of "
					+ max
					+ ".");
			monitor.setProgress((int)this.count);
			return !(monitor.isCanceled());*/
			return true;
		}
		public void end()
		{
			//monitor.close();
		}
		public void init(int op, String src, String dest, long max)
		{
			this.max = max;
			/*monitor =
				new ProgressMonitor(
					null,
					((op == SftpProgressMonitor.PUT) ? "put" : "get")
						+ ": "
						+ src,
					"",
					0,
					(int)max);
			count = 0;
			monitor.setProgress((int)this.count);
			monitor.setMillisToDecideToPopup(1000);*/
		}
	}
	public static class MyUserInfo implements UserInfo,UIKeyboardInteractive 
	{
		private String password;
		public MyUserInfo(String password) {
			this.password=password;
		}
		
		public String getPassphrase()
		{
			return null;
		}
		public String getPassword()
		{
			return this.password;
		}
		public boolean promptPassphrase(String message)
		{
			return true;
		}
		public boolean promptPassword(String message)
		{
			return true;
		}
		public boolean promptYesNo(String str)
		{
			return true;
		}
		public void showMessage(String message)
		{
		}

		public String[] promptKeyboardInteractive(String destination,
				String name, String instruction, String[] prompt, boolean[] echo) {
			String[] response=new String[prompt.length];
    		for(int i=0; i<prompt.length; i++){
    			response[i]=this.password;
    		}
    		return response;
		}
	}
	private String password = null;
	private Channel channel = null;
	private Session session = null;
	private ChannelSftp sftpChannel = null;
	public SFTPHandler()
	{
		String methodName = "SftpConnection:\t";
		java.security.Provider[] p1 = java.security.Security.getProviders();
		/*for (int i = 0; i < p1.length; i++) {
			System.out.println("Provider: " + i + "\t" + p1[i].getName());
		}*/
		String[] providers =
			{
				"sun.security.provider.Sun",
				"com.sun.crypto.provider.SunJCE",
				"com.ibm.crypto.provider.IBMJCE" };
		try
		{
			ProviderManager.AddProvider(providers);
//			Provider[] p = ProviderManager.getProviders();
////			for (int i = 0; i < p.length; i++)
////			{
////				System.out.println(
////					"\n\nProvider: "
////						+ i
////						+ "\t"
////						+ p[i].getName());
////				//Debug.print("Provider: " + i + "\t" + p[i].getInfo(), -1);
////			}
		}
		catch (InstantiationException e)
		{
			//
		}
		catch (IllegalAccessException e)
		{
						System.out.println(
							methodName
								+ "Can't Access Additional Security Provider Files ...");
		/*	Debug.print(
				methodName
					+ "Can't Access Additional Security Provider Files ...",
				-1);*/
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
						System.out.println(
							methodName
								+ "Can't Find Additional Security Provider Files ...");
		/*	Debug.print(
				methodName
					+ "Can't Find Additional Security Provider Files ...",
				-1);*/
		}
	}
	/* (non-Javadoc)
	 * @see ISftpConnection#deleteFile(java.lang.String)
	 */
	public boolean deleteFile(String filePath)
	{
		try
		{
			sftpChannel.rm(filePath);
			return true;
		}
		catch (Exception e)
		{
			//e.printStackTrace();
			return false;
		}
	}
	/* (non-Javadoc)
	 * @see ISftpConnection#disconnect()
	 */
	public void disconnect()
	{
		if (sftpChannel != null)
			sftpChannel.disconnect();
		if (session != null)
			session.disconnect();
	}
	
	
	
	
	private String[] listFileNames(String path) throws SftpException  {
		String[] result=null;
		if(this.sftpChannel!=null&&this.sftpChannel.isConnected()) {
			Vector lsEntries=sftpChannel.ls(path);
			result=new String[lsEntries.size()];
			for (int i = 0; i < lsEntries.size(); i++) {
				LsEntry entry=(LsEntry)lsEntries.get(i);
				result[i]=entry.getFilename();
			}
		}
		return result;
	}
	
	
	
	
	
	/**
	 * connect to ftp server
	 * 
	 * @param //ftp -
	 *            the apache ftp client
	 * @param //server -
	 *            the name of ftp server
	 * @param//user -
	 *            user name for ftp
	 * @param password -
	 *            password for ftp
	 * 
	 * @exception InputException
	 *                if FTP server refused connection if FTP server couldn't be
	 *                reached
	 */
	private boolean connect(
			String host,
			String userName,
			String password,
			int port) throws InputException {
			try
			{
				JSch jsch = new JSch();
				session = jsch.getSession( userName, host, port);
				this.password = password;
				UserInfo ui = new MyUserInfo(password);
				session.setUserInfo(ui);
				session.connect();
				channel = session.openChannel("sftp");
				channel.connect();
				sftpChannel = (ChannelSftp)channel;
				return true;
			}
			catch (Exception e){
                throw new InputException(e);
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
		logger.debug("SFTPHandler.performGetFiles() - started for file: "
				+ input.getInputName() + " to location: " + targetLocation);
		try {
			Vector filesList = new Vector();
			this.connect(input.getServer(), input.getUser(), input
					.getPassword(),port);
			String serverFileName = null;
			//ftp.setFileType(FTP.BINARY_FILE_TYPE);
			Pattern p = Pattern.compile(input.getInputName());
			logger.debug("SFTPHandler.performGetFiles() - Looking for file "
					+ p.pattern());
			// list file names on the given path
			String[] files = this.listFileNames(currentServerPath);

			if (files == null || files.length == 0) {
				// no files were found on the given path
				logger
						.error("-"+ input.getNodeName() +"- FH - 2002: SFtpHandler.performGetFiless() - Couldn't retrieve ftp file: No files found on specified path "+currentServerPath);
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
					if(Utils.exists(input.getSystemName(), systemNames)){
						String[] filePathTokens = currentServerPath.split("/");
						String connectorName = filePathTokens[filePathTokens.length-1];
						localFileName = prefix + foundCount + "_(" +connectorName+ ")_"
						+ serverFileName;
					
					}else{
						localFileName = prefix + foundCount + "_"
						+ serverFileName;
					}
					
					
					//File localFile = new File(targetLocation, localFileName);
					//localFile.delete();
					//FileOutputStream out = new FileOutputStream(localFile);
					String serverFile = currentServerPath + serverFileName;
//					System.out
//							.println("com.itworx.vaspp.datacollection.util.sftp.SFTPHandler.performGetFiles() : Source = "+serverFile);
//					System.out
//					.println("com.itworx.vaspp.datacollection.util.sftp.SFTPHandler.performGetFiles() : Dest = "+localFileName);
					localFileName=targetLocation+"/"+localFileName;
					boolean success = this.downloadFile(serverFile, localFileName);
					File localFile=new File(localFileName);
					if (!success) {
						this.disconnect();
						localFile.deleteOnExit();
						logger
								.error("-"+ input.getNodeName() +"- FH - 2003 : SFtpHandler.performGetFiles() - couldn't retrieve ftp file");
						throw new InputException("-"+ input.getNodeName() +"- couldn't retrieve ftp file");
					}
					filesList.addElement(localFile);
				}
			}
			this.disconnect();
			if (filesList.size() == 0) {
				logger
						.error("-"+ input.getNodeName() +"- FH - 2004 : SFTPHandler.performGetFiles() - couldn't retrieve ftp file - no files found matching name for path : "+currentServerPath);
				throw new InputException("-"+ input.getNodeName() +"- couldn't retrieve ftp file for path : "+currentServerPath);
			}
			logger
					.debug("FtpHandler.performGetFiles() -finished performGetFile for file: "
							+ input.getInputName()
							+ " to location: "
							+ targetLocation);
			File[] localFiles = new File[filesList.size()];
			localFiles = (File[]) filesList.toArray(localFiles);
			return localFiles;
		} catch (Exception ex) {
			logger.error("-"+ input.getNodeName() +"- FH - 2005 - SFTPHandler.performGetFiles() - error getting file "
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
		logger.debug("SFTPHandler.performGetFiles() - started for file: "
				+ input.getInputName() + " to location: " + targetLocation);
		
		Vector mainFilesList = new Vector();
		String currentPath = "";
		boolean stopDCProcess = PropertyReader.isStopDCInMultiPathsError();
		String pathPrfx = "";
		boolean pathManipulated = isPathManipulated(input);
		for(int i = 0;i<input.getPaths().length;i++)
		{
			currentPath = input.getPaths()[i];
			if(pathManipulated){
				pathPrfx = "("+currentPath.replaceAll("\\\\|/", "+")+")";
			}
			try{
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
							.error("-"+ input.getNodeName() +"- FH - 2006 : SFTPHandler.performGetFiles() - couldn't retrieve ftp files in this path ["+currentPath+"]",e);
						e.printStackTrace();
					}
				}
			}
		}
		if (mainFilesList.size() == 0) {
			logger
					.error("-"+ input.getNodeName() +"- FH - 2004 : SFTPHandler.performGetFiles() - couldn't retrieve ftp file - no files found matching name");
			throw new InputException("-"+ input.getNodeName() +"- couldn't retrieve ftp file");
		}
		logger
				.debug("SFTPHandler.performGetFiles() -finished performGetFile for file: "
						+ input.getInputName()
						+ " to location: "
						+ targetLocation);
		File[] localFiles = new File[mainFilesList.size()];
		localFiles = (File[]) mainFilesList.toArray(localFiles);
		return localFiles;
	}
	
	/* (non-Javadoc)
	 * @see ISftpConnection#downloadFile(java.lang.String)
	 */
	public boolean downloadFile(String remoteFilePath, String localFilePath)
	{
		String methodName = "SftpConnection:downloadFile()\t";
		try
		{
	//		System.out.println("Download Started ..............");
		/*	Debug.print(
				methodName + "File(s) download has been started ..............",
				-1);*/
			SftpProgressMonitor monitor = new MyProgressMonitor();
			int mode = ChannelSftp.OVERWRITE;
			String fileSep = System.getProperty("file.separator");
			if (System.getProperty("file.separator").equalsIgnoreCase("\\"))
				fileSep = "\\";
			else
				fileSep = "/";
			StringTokenizer tokenizer = new StringTokenizer(localFilePath, "/");
			String folder = "";
			String pathToImage = "";
			File file = null;
			while (tokenizer.hasMoreTokens())
			{
				folder = tokenizer.nextToken();
				//				
				//				if(!op)
				//				{
				//					System.out.println("Can't Create Directory ===>"+ pathToImage);
				//				}
				//				else
				//				{
				//					System.out.println(" Success Create Directory ===>"+ pathToImage);
				//				}
				//				try
				//				{
				//					System.out.println("Before Creating Directory");
				//					System.out.println("Directory Path @ local === >"+pathToImage);
				//					sftpChannel.mkdir(pathToImage + fileSep.charAt(0));
				//					System.out.println("After Creating Directory");
				//					
				//					sftpChannel.cd(pathToImage);
				//				}
				//				catch (Exception e)
				//				{
				//					e.printStackTrace();
				//					//sftpChannel.cd(pathToImage);
				//				}
			}
			pathToImage = pathToImage + "/" + folder; // + "/";
			file = new File(localFilePath);
			boolean op = file.createNewFile();
			//sftpChannel.cd("/");
			//remoteFilePath = remoteFilePath.replace('/', fileSep.charAt(0));
			//remoteFilePath = remoteFilePath.replace('\\', fileSep.charAt(0));
			//System.out.println("After Creating Directory ==>" + remoteFilePath);
			sftpChannel.get(remoteFilePath, localFilePath, monitor, mode);
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	public static void main(String[] args) {
		SFTPHandler handler= new SFTPHandler();
		
		String path = "/opt/telorb/axe/tsp/NM/PMF/reporterLogs/PlatformMeasures/";
		try {
			handler.connect("172.21.171.71", "telorb", "telorb",21);
			String[] fileNames=handler.listFileNames(path);
			for (int i = 0; i < fileNames.length; i++) {
				System.out.println(fileNames[i]);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
