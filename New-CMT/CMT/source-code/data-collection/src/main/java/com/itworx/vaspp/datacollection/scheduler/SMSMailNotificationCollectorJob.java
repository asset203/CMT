package com.itworx.vaspp.datacollection.scheduler;

import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.itworx.vaspp.datacollection.CollectorScheduler;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.SMSMailNotificationManager;

public class SMSMailNotificationCollectorJob implements Job{
	private Date targetDate;
	
	private boolean hourly = false;

	private Logger logger;
	
	private SMSMailNotificationManager notificationManager;

	/**
	 * initialize the instance of SMSMailNotificationCollectorJob
	 * 
	 */
	private void init(JobDataMap jobDataMap) {
		logger
				.debug("SMSMailNotificationCollectorJob.init() - Loading parameters from jobDataMap");

		if(jobDataMap.getString("hourly") != null){
			try{
				hourly = Boolean.parseBoolean(jobDataMap.getString("hourly"));
			} catch (Exception e){
				logger.error("SMSMailNotificationCollectorJob.execute() - error while parsing hourly key ["+jobDataMap.getString("hourly"),e);
			}
		}
		
		// set the targetdate to the day before this job execution time
		java.util.Calendar c = java.util.Calendar.getInstance();
		c.setTime(new Date());
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		if(hourly){
			c.add(java.util.Calendar.HOUR_OF_DAY, -1);
		}else{
			c.add(java.util.Calendar.DAY_OF_MONTH, -1);
			c.add(java.util.Calendar.HOUR_OF_DAY, 0);
		}
		this.setTargetDate(c.getTime());
		notificationManager = new SMSMailNotificationManager();
		
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

		logger = Logger.getLogger("SMSMailNotification");
		JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
		try {
			this.init(jobDataMap);
			logger.info("SMSMailNotificationCollectorJob.execute() - Dispatch Job for SMSMailNotification for Date: "
					+ targetDate +  " , home: "
					+ CollectorScheduler.getHomeDirectory());
			notificationManager.dispatchJob(targetDate,hourly);
			
			logger.info("SMSMailNotificationCollectorJob.execute() - Finish Job for SMSMailNotification for Date: "
					+ targetDate +  " , home: "
					+ CollectorScheduler.getHomeDirectory());
		} catch (InputException ex) {
			logger.error("SMSMailNotificationCollectorJob.execute() Exception- Job for SMSMailNotification for Date: "+targetDate+" ---> "
					,ex);
			
		} catch (Exception ex) {
			logger.error("SMSMailNotificationCollectorJob.execute() Exception- Job for SMSMailNotification for Date: "+targetDate+" ---> "
					,ex);
			
		}
	}
	
	public void setTargetDate(Date targetDate) {
		this.targetDate = targetDate;
	}

	public Date getTargetDate() {
		return targetDate;
	}
}
