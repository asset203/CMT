/* 
 * File:       CleanerJob.java
 * Date        Author          Changes
 * 31/05/2006  Nayera Mohamed  Created 
 * 
 * Class implementing Quartz Job to delete files from ftpfolder
 */

package com.itworx.vaspp.datacollection.scheduler;

import com.itworx.vaspp.datacollection.util.PropertyReader;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.File;

public class CleanerJob implements Job {

	private Logger logger;

	/**
	 * called when quartz scheduler triggers Cleaning job
	 * responsible for clearing all files in the imported file path
	 * @param context -
	 *            Job execution context passed by quartz scheduler when job is
	 *            triggered
	 * @exception JobExecutionException
	 *                returned by quartz
	 */
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		logger = Logger.getLogger("DBJobLogger");
        logger.info("CleanerJob.execute() started");
		try {
			String filesPath = PropertyReader.getImportedFilesPath();
            logger.info("CleanerJob.execute()- Imported Files path ["+filesPath+"]");
			File importFolder = new File(filesPath);
			deletedFolder(importFolder);
            logger.info("CleanerJob.execute()- Imported Files Deleted successfully");
			String convertedFilesPath = PropertyReader.getConvertedFilesPath();
            logger.info("CleanerJob.execute()- converted Files path ["+convertedFilesPath+"]");
			File covertedFolder = new File(convertedFilesPath);
			deletedFolder(covertedFolder);
            logger.info("CleanerJob.execute()- converted Files Deleted successfully");
		} catch (Exception ex) {
            logger.error(ex);
			logger.error("CLR-1000: CleanerJob.execute() Exception- message:"
					+ ex.getMessage());
		}
	}

	private void deletedFolder(File folder) {
		if (folder == null)
			return;
		if (folder.isFile()) {
			folder.delete();
			return;
		}
		File[] folderFiles = folder.listFiles();

		for (int i = 0; i < folderFiles.length; i++) {
			deletedFolder(folderFiles[i]);
		}
	}

}