package com.itworx.vaspp.datacollection.scheduler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SimpleTrigger;

import com.itworx.vaspp.datacollection.CollectorScheduler;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.logmanager.LogFilesDAO;
import com.itworx.vaspp.datacollection.logmanager.LogManager;
import com.itworx.vaspp.datacollection.util.DataCollectionManager;

public class LogManagerJob implements Job{
	private Date targetDate;

	private String systemName;

	private String nodeName;

	private Logger logger;

	private int retryCount;

	private int retryInterval;

	/**
	 * initialize the instance of CollectorJob with values from JobDataMap
	 * 
	 * @param jobDataMap -
	 *            the jobDataMap holding the parameters
	 */
	private void init(JobDataMap jobDataMap) {
		logger
			.debug("CollectorJob.init() - Loading parameters from jobDataMap");
				
		// set the targetdate to the day before this job execution time
		java.util.Calendar c = java.util.Calendar.getInstance();
		c.setTime(new Date());
		this.setTargetDate(c.getTime());
		try {
			int vretryCount = jobDataMap.getIntFromString("RetryCount");
			this.setRetryCount(vretryCount);
		} catch (Exception e) {
			this.setRetryCount(3);
		}
		try {
			int vretryInterval = jobDataMap.getIntFromString("RetryInterval");
			this.setRetryInterval(vretryInterval);
		} catch (Exception e) {
			this.setRetryInterval(24);
		}
	}

	/**
	 * called when quartz scheduler triggers DataCollection job retrieve
	 * parameters from jobDataMap and pass to DataCollectionManager to dispatch
	 * job if failure occured a recovery job is scheduled
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

		JobDataMap jobDataMap = null;
		logger = Logger.getLogger("DBJobLogger");
			
			try{
				java.util.Calendar c = java.util.Calendar.getInstance();
				c.setTime(new Date());
				this.setTargetDate(c.getTime());
				LogManager.dispatchLogManagerJob(targetDate, CollectorScheduler.getHomeDirectory());
			} catch (FileNotFoundException e1) {
				logger.error("CollectorJob.execute() Exception- Log Manger job - ",e1);
				e1.printStackTrace();
				this.createRecoveryJob(context, e1, "ApplicationRecovery");
			} catch (IOException e) {
				logger.error("CollectorJob.execute() Exception- Log Manger job - ",e);
				this.createRecoveryJob(context, e, "ApplicationRecovery");
			} catch (Exception ex) {
				logger.error("CollectorJob.execute() Exception- Log Manger job - ",ex);
				this.createRecoveryJob(context, ex, "ApplicationRecovery");
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
	private void createRecoveryJob(JobExecutionContext context, Exception ex,
			String groupName) {
		try {
			String jobName = "Recovery" + context.getJobDetail().getName()
					+ "_" + System.currentTimeMillis();
			JobDetail jobDetail = new JobDetail(
					jobName,
					groupName,
					Class
							.forName("com.itworx.vaspp.datacollection.scheduler.RecoveryCollectorJob"));
			JobDataMap dataMap = new JobDataMap();
			dataMap.put("SystemName", this.getSystemName());
			dataMap.put("NodeName", this.getNodeName());
			String msg = ex.getMessage();
			if (msg.length() > 80) {
				msg = msg.substring(0, 80);
			}
			
			String desc = "Sys:" + this.getSystemName() + "_"
					+ this.getNodeName() + "_" + this.getTargetDate() + ":"
					+ msg;
			if (desc.length() > 115) {
				desc = desc.substring(0, 115);
			}
			jobDetail.setDescription(desc);
			SimpleDateFormat f = new SimpleDateFormat();
			StringBuffer s = f.format(this.getTargetDate(), new StringBuffer(),
					new FieldPosition(f.DATE_FIELD));
			dataMap.put("TargetDate", "" + s);

			dataMap.put("RetryCount", "" + this.getRetryCount());
			dataMap.put("RetryInterval", "" + this.getRetryInterval());
			dataMap.put("no_of_retrials", "1");

			jobDetail.setJobDataMap(dataMap);
			java.util.Calendar c = java.util.Calendar.getInstance();
			c.setTime(new Date());
			c.add(java.util.Calendar.MINUTE/* DAY_OF_MONTH */, this
					.getRetryInterval());
			Date runningTime = c.getTime();
			context.getScheduler().addJob(jobDetail, true);
			SimpleTrigger trigger = new SimpleTrigger("Recovery_trigger_"
					+ System.currentTimeMillis(), groupName, runningTime);
			trigger.setJobName(jobName);
			trigger.setJobGroup(groupName);
			String triggerDesc = "Failed " + this.getSystemName() + " "
					+ this.getNodeName() + " trigger - retries left:"
					+ this.getRetryCount();

			if (triggerDesc.length() > 115) {
				triggerDesc = triggerDesc.substring(0, 115);
			}
			
			trigger.setDescription(triggerDesc);
			context.getScheduler().scheduleJob(trigger);
		} catch (Exception e) {
			logger
					.error("CJ-1001: CollectorJob.createRecoveryJob() - Couldn't create Recovery Job for system : "+systemName+" Node : "+nodeName+"  ---> " 
							, e);
		}
	}
	
	public void setTargetDate(Date targetDate) {
		this.targetDate = targetDate;
	}

	public Date getTargetDate() {
		return targetDate;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}

	public int getRetryCount() {
		return retryCount;
	}

	public void setRetryInterval(int retryInterval) {
		this.retryInterval = retryInterval;
	}

	public int getRetryInterval() {
		return retryInterval;
	}

}
