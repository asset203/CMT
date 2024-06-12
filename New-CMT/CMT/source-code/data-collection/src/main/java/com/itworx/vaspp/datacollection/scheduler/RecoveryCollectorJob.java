/*
 * File:       RecoveryCollectorJob.java
 * Date        Author          Changes
 * 26/02/2006  Nayera Mohamed  Created
 *
 * Class implementing Quartz Job to execute Recovery of DataCollection Job
 */
package com.itworx.vaspp.datacollection.scheduler;

import com.itworx.vaspp.datacollection.CollectorScheduler;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.logmanager.LogManager;
import com.itworx.vaspp.datacollection.util.*;

import java.text.SimpleDateFormat;

import java.util.Date;

import org.apache.log4j.Logger;

import org.quartz.*;

public class RecoveryCollectorJob implements StatefulJob {

	private Logger logger;

	/**
	 * called when quartz scheduler triggers Recovery job retrieve parameters
	 * from jobDataMap and pass to DataCollectionManager to dispatch job if
	 * failure occured another recovery job is scheduled
	 * 
	 * @param context -
	 *            Job execution context passed by quartz scheduler when job is
	 *            triggered
	 * 
	 * 
	 * @exception JobExecutionException
	 *                returned by quartz
	 */
	public void execute(JobExecutionContext context)
			throws JobExecutionException {

		logger = Logger.getLogger("DBJobLogger");
		JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
		try {
			String systemName = jobDataMap.getString("SystemName");
			String nodeName = jobDataMap.getString("NodeName");
			SimpleDateFormat f = new SimpleDateFormat();
			Date targetDate = null;
			targetDate = new Date(jobDataMap.getString("TargetDate"));

			logger
					.info("RecoveryCollectorJob.execute() - Dispatch Job for system: "
							+ systemName
							+ ", node: "
							+ nodeName
							+ ", home: "
							+ CollectorScheduler.getHomeDirectory());
			DataCollectionManager.dispatchJob(systemName, nodeName, targetDate);
			
			/**
             * @author ahmad.abushady
             * the following block was modified in Dec. 13 2007
             */
			
			SimpleDateFormat smpl = new SimpleDateFormat("dd/MM/yyyy");
			//LogManager.ParseLogs(CollectorScheduler.getHomeDirectory(), systemName, nodeName, smpl.format(new Date()));		
			
			/**
             * @author ahmad.abushady
             * the following block was modified in Dec. 13 2007
             */
			
		} catch (InputException ex) {
			logger.error("RecoveryCollectorJob.execute() - message:"
					+ ex.getMessage());
			this.createRecoveryJob(context, jobDataMap, ex, "InputRecovery");
		} catch (Exception ex) {
			logger.error("RecoveryCollectorJob.execute() - message:"
					+ ex.getMessage());
			this.createRecoveryJob(context, jobDataMap, ex,
					"ApplicationRecovery");
		}
	}

	/**
	 * create new recovery job with the same jobdatamap number of retrials
	 * incremented running time set to creation time + retry interval
	 * description holds the new failure reason
	 * 
	 * @param context -
	 *            Job execution context
	 * @param jobDataMap -
	 *            JobDataMap of the original job
	 * @param ex -
	 *            the exception that caused the original job failure
	 * @param groupName -
	 *            either ApplicationRecovery or InputRecovery
	 * 
	 */
	private void createRecoveryJob(JobExecutionContext context,
			JobDataMap jobDataMap, Exception ex, String groupName) {
		int noOfRetrials = 0;
		int retryCount = 3;
		int retryInterval = 24;
		retryCount = jobDataMap.getIntFromString("RetryCount");
		retryInterval = jobDataMap.getIntFromString("RetryInterval");
		noOfRetrials = jobDataMap.getIntFromString("no_of_retrials");
		noOfRetrials++;
		// if no of retrials are all exhausted delete the job entirely
		if (noOfRetrials == retryCount) {
			try {
				context.getScheduler().deleteJob(
						context.getJobDetail().getName(),
						context.getJobDetail().getGroup());
			} catch (SchedulerException e) {
				logger
						.error("RJ-1000: RecoveryCollectorJob.createRecoveryJob() - Couldn't delete job after reaching max retrials "
								+ e);
			}
			/**
             * @author ahmad.abushady
             * the following block was modified in Dec. 13 2007
             */
			
			finally{
				SimpleDateFormat smpl = new SimpleDateFormat("dd/MM/yyyy");
				String systemName = jobDataMap.getString("SystemName");
				String nodeName = jobDataMap.getString("NodeName");
				//LogManager.ParseLogs(CollectorScheduler.getHomeDirectory(), systemName, nodeName, smpl.format(new Date()));		
			}
			
			/**
             * @author ahmad.abushady
             * the following block was modified in Dec. 13 2007
             */
			return;
		}
		java.util.Calendar c = java.util.Calendar.getInstance();
		c.setTime(new Date());
		c.add(java.util.Calendar.MINUTE/* DAY_OF_MONTH */, retryInterval);
		JobDetail oldDetail = context.getJobDetail();
		JobDataMap oldDataMap = oldDetail.getJobDataMap();
		Trigger oldTrigger = context.getTrigger();
		try {
			String jobName = context.getJobDetail().getName() + noOfRetrials;
			JobDetail jobDetail = new JobDetail(
					jobName,
					groupName,
					Class
							.forName("com.itworx.vaspp.datacollection.scheduler.RecoveryCollectorJob"));
			JobDataMap dataMap = oldDataMap;
			dataMap.put("no_of_retrials", "" + noOfRetrials);
			jobDetail.setJobDataMap(dataMap);
			String msg = ex.getMessage();
			if (msg.length() > 80) {
				msg = msg.substring(0, 80);
			}
			String desc = "Sys:" + oldDataMap.getString("SystemName") + "_"
					+ oldDataMap.getString("NodeName") + "_"
					+ oldDataMap.getString("TargetDate") + ":" + msg;
			// make sure the size of the description does not exceed the number
			// of characters in DB field
			if (desc.length() > 115) {
				desc = desc.substring(0, 115);
			}
			jobDetail.setDescription(desc);
			jobDetail.setGroup(groupName);
			String triggerDesc = "Failed " + oldDataMap.getString("SystemName")
					+ " " + oldDataMap.getString("NodeName")
					+ " trigger - retries left:" + (retryCount - noOfRetrials);
			// make sure the size of the description does not exceed the number
			// of characters in DB field
			if (triggerDesc.length() > 115) {
				triggerDesc = triggerDesc.substring(0, 115);
			}
			String triggerName = oldTrigger.getName() + noOfRetrials;
			String oldGroup = oldDetail.getGroup();
			context.getScheduler().addJob(jobDetail, true);
			SimpleTrigger newTrigger = new SimpleTrigger(triggerName,
					"InputRecovery", c.getTime());
			newTrigger.setJobName(jobName);
			newTrigger.setDescription(triggerDesc);
			newTrigger.setJobGroup(groupName);
			context.getScheduler().scheduleJob(newTrigger);
			context.getScheduler().deleteJob(oldDetail.getName(), oldGroup);
		} catch (Exception e) {
			logger
					.error("RJ-1001:RecoveryCollectorJob.createRecoveryJob() - Couldn't reschedule failure recovery "
							+ e);
		}
	}
}