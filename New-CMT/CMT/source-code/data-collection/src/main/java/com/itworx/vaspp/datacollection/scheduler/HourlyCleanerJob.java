/* 
 * File:       CleanerJob.java
 * Date        Author          Changes
 * 31/05/2006  Nayera Mohamed  Created 
 * 
 * Class implementing Quartz Job to delete files from ftpfolder
 */

package com.itworx.vaspp.datacollection.scheduler;

import com.itworx.vaspp.datacollection.util.PropertyReader;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class HourlyCleanerJob implements Job {

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
		try {
			String filesPath = PropertyReader.getImportedFilesPath();
			File importFolder = new File(filesPath);
			Calendar currentHour=Calendar.getInstance();
			currentHour.setTime(new Date());
			currentHour.add(Calendar.HOUR,-1);
			currentHour.set(Calendar.MINUTE, 0);
			
			deletedFolder(importFolder,currentHour);
			
			String convertedFilesPath = PropertyReader.getConvertedFilesPath();
			File covertedFolder = new File(convertedFilesPath);
			deletedFolder(covertedFolder,currentHour);
		} catch (Exception ex) {
			logger.error("CLR-1000: CleanerJob.execute() Exception- message:"
					+ ex.getMessage());
		}
	}

	private void deletedFolder(File folder,Calendar currentHour) {
		if (folder == null)
			return;

		if (folder.isFile()) {
			Date lastModifiedDate=new Date(folder.lastModified());
			Calendar lastModifiedDateCal=Calendar.getInstance();
			lastModifiedDateCal.setTime(lastModifiedDate);
			if(lastModifiedDateCal.before(currentHour))		
				folder.delete();
			return;
		}
		File[] folderFiles = folder.listFiles();

		for (int i = 0; i < folderFiles.length; i++) {
			deletedFolder(folderFiles[i],currentHour);
		}
	}
	
	
}