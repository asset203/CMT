/* 
 * File:       TrendAnalysisCollectorJob.java
 * 
 * Class implementing Quartz Job to execute Trend Analysis Job
 */

package com.itworx.vaspp.datacollection.scheduler;

import java.util.Date;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.itworx.vaspp.datacollection.CollectorScheduler;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.TrendAnalysisManager;

public class TrendAnalysisCollectorJob implements Job {

	private Date targetDate;

	private Logger logger;

	/**
	 * initialize the instance of TrendAnalysisCollectorJob
	 * 
	 */
	private void init() {
		logger
				.debug("TrendAnalysisCollectorJob.init() - Loading parameters from jobDataMap");

		// set the targetdate to the day before this job execution time
		java.util.Calendar c = java.util.Calendar.getInstance();
		c.setTime(new Date());
		c.add(java.util.Calendar.DAY_OF_MONTH, -1);
		this.setTargetDate(c.getTime());
		
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

		logger = Logger.getLogger("DBJobLogger");
		try {
			this.init();
			logger.info("TrendAnalysisCollectorJob.execute() - Dispatch Job for Trend Analysis for Date: "
					+ targetDate +  " , home: "
					+ CollectorScheduler.getHomeDirectory());
			
			TrendAnalysisManager.dispatchJob(targetDate);
			
			logger.info("TrendAnalysisCollectorJob.execute() - Finish Job for Trend Analysis for Date: "
					+ targetDate +  " , home: "
					+ CollectorScheduler.getHomeDirectory());
		} catch (InputException ex) {
			logger.error("TrendAnalysisCollectorJob.execute() Exception- Job for Trend Analysis for Date: "+targetDate+" ---> "
					,ex);
			
		} catch (Exception ex) {
			logger.error("TrendAnalysisCollectorJob.execute() Exception- Job for Trend Analysis for Date: "+targetDate+" ---> "
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