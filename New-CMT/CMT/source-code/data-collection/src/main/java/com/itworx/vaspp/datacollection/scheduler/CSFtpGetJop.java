/* 
 * File:       CleanerJob.java
 * Date        Author          Changes
 * 31/05/2006  Nayera Mohamed  Created 
 * 13/12/2007  Eshraq Essam    Updated 
 * 
 * Class implementing Quartz Job to delete files from ftpfolder
 */

package com.itworx.vaspp.datacollection.scheduler;

import java.io.File;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.itworx.vaspp.datacollection.util.PropertyReader;
import com.itworx.vaspp.datacollection.util.Utils;

public class CSFtpGetJop implements Job {

	private Logger logger; 

	/**
	 * called when quartz scheduler triggers CS Ftp Get files Job
   * responsible for clearing all files in the imported file path
	 * @param context -
	 *            Job execution context passed by quartz scheduler when job is
	 *            triggered
	 * @exception JobExecutionException
	 *                returned by quartz
	 */
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		logger = Logger.getLogger("CS");
		try {
			 logger.info("CSFtpGetJop Script Started successfully");
			 Calendar calendar = Calendar.getInstance();
             calendar.add(Calendar.DAY_OF_MONTH, -1);
             int csNodesNo = Integer.parseInt(PropertyReader.getCSNodesNumbers());
             for (int i = 0; i < csNodesNo; i++) {
            	 String destPath = PropertyReader.getImportedFilesPath();
            	 int no = i+1;
                 destPath +="/AIR"+no+1+"/";
                 logger.debug("destPath = "+destPath);
                 String sourcePath=PropertyReader.getAccessClass("cs.node"+no+".remotPath");
                 logger.debug("sourcePath = "+sourcePath);
                 String server= PropertyReader.getAccessClass("cs.node"+no+".remotServer");
                 logger.debug("server = "+server);
                 String user= PropertyReader.getAccessClass("cs.node"+no+".remotUser");
                 logger.debug("user = "+user);
                 String password= PropertyReader.getAccessClass("cs.node"+no+".remotPassword");
                 logger.debug("password = "+password);
                 String scriptsPath = PropertyReader.getScriptsPath();
                 logger.debug("scriptsPath = "+scriptsPath);
                 String fileNamePattern = PropertyReader.getAccessClass("cs.node"+no+".fileNamePattern");
                 logger.debug("fileNamePattern = "+fileNamePattern);
                 fileNamePattern=Utils.resolveName(fileNamePattern, calendar.getTime());
                 logger.debug("fileNamePattern after = "+fileNamePattern);
                 String command = "bash ";
                 command+=scriptsPath+"/ftp_get.sh ";
                 command+=server+" ";
                 command+=user+" ";
                 command+=password+" ";
                 command+=sourcePath+" ";
                 command+=fileNamePattern+" ";
                 command+=destPath+" ";
                 logger.debug("com.itworx.vaspp.datacollection.scheduler.CSFtpGetJop.execute(): Command = ");
                 logger.debug(command);
                 System.out.println(command);
                 Runtime rt = Runtime.getRuntime();
    			 Process proc=rt.exec(command);
    			 proc.waitFor();				
			 }
             logger.info("CSFtpGetJop Script finished successfully");
     
		} catch (Exception ex) {
			logger.error("com.itworx.vaspp.datacollection.scheduler.CSFtpGetJop.execute(): Exception ",ex);
		}
		
		
	}
	
	public static void main(String[] args) {
//		CSFtpGetJop job=new CSFtpGetJop();
//			try {
//				job.execute(null);
//			} catch (JobExecutionException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		
		File dr=new File("D:/SMSC");
		File[] files=dr.listFiles();
		for (int i = 0; i < files.length; i++) {
			System.out.println(files[i].getName());
		}
		
	}

}