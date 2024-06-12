package com.itworx.vaspp.datacollection.scheduler;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.itworx.vaspp.datacollection.CollectorScheduler;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.DataCollectionManager;
import com.itworx.vaspp.datacollection.util.PropertyReader;


public class GenericHourlyCollectorJob  implements Job {

	private Date targetDate;

	private String systemName;

	private Logger logger;

	/**
	 * initialize the instance of CollectorJob with values from JobDataMap
	 * 
	 * @param jobDataMap -
	 *            the jobDataMap holding the parameters
	 */
	private void init(JobDataMap jobDataMap) {
		logger
				.debug("HourlyCollectorJob() - Loading parameters from jobDataMap");
		this.setSystemName(jobDataMap.getString("SystemName"));
		// set the target date to the hour before this job execution time
		java.util.Calendar c = java.util.Calendar.getInstance();
		c.setTime(new Date());
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		int lag = PropertyReader.getHourlyDataCollectionLagRange();
		c.add(java.util.Calendar.HOUR_OF_DAY, lag);
		this.setTargetDate(c.getTime());
		
	}
	
	/**
	 * called when quartz scheduler triggers HourlyDataCollection job retrieve
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
	
	private static Date parseDate(String dateString) {
		try {
			SimpleDateFormat frm = new SimpleDateFormat();
			frm.applyPattern("dd/MM/yyyy:HH");
			Date date = frm.parse(dateString);
			return date;
		} catch (ParseException e) {
			System.out
					.println("Invalid Date Format, Format should be dd/MM/yyyy hh");
			System.exit(1);
		}
		return null;
	}
	public void execute(JobExecutionContext context)
			throws JobExecutionException {

		JobDataMap jobDataMap = null;
		logger = Logger.getLogger("DBJobLogger");
		try {
			jobDataMap = context.getJobDetail().getJobDataMap();
			this.init(jobDataMap);
			if (systemName == null) {
				logger
						.error("HCJ-1000: HourlyCollectorJob.execute() - Job Parameters are not complete - aborting job - no retry");
				return;
			}
			logger.info("HourlyCollectorJob.execute() - Dispatch Job for system: "
					+ systemName + ", home: "
					+ CollectorScheduler.getHomeDirectory());
			/*new code*/
//			Calendar cal=Calendar.getInstance();	
			SimpleDateFormat simpleDF=new SimpleDateFormat("MM/dd/yyyy HH");
			String formattedDate=simpleDF.format(targetDate);
			int hour =Integer.parseInt(formattedDate.split(" ")[1]);
			Map namesVsStructures=DataCollectionManager.getNodesSystemsStructures(systemName);
				try {
					DataCollectionManager.dispatchHourlyJob(systemName,
							targetDate);
				} catch (Exception e) {
					logger.error("HourlyCollectorJob.execute() Exception- System : "+systemName+" ",e);
				}
			
			hour =hour -1;
			while(hour>=0)
			{
			
			Iterator it=namesVsStructures.keySet().iterator();
			while(it.hasNext())
			{
				 Object key=it.next();
				 List structures=(List)namesVsStructures.get(key);
				 List dbList=DataCollectionManager.getHourlyEvent(systemName ,formattedDate.split(" ")[0]+" "+hour,key.toString());
				 if(dbList.size()<structures.size())
				 {
					 System.out.println("inside the deleted hour : " +hour);
						try {
					DataCollectionManager.dispatchHourlyJobForNodes(systemName, parseDateString(formattedDate.split(" ")[0]+" "+hour),key.toString());
						}catch (Exception e) {
							logger.error("HourlyCollectorJob.execute() Exception- System : "+systemName+" ",e);
						}
				 }
			}
			hour--;
			}
			
			
			
		} catch (InputException ex) {
			logger.error("HourlyCollectorJob.execute() Exception- System : "+systemName+" ",ex);
		} catch (Exception ex) {
			logger.error("HourlyCollectorJob.execute() Exception- System : "+systemName+" ",ex);
		}
	}
	private static Date parseDateString(String dateString) {
		if(dateString.split(" ")[1].length()==1)
		{
			dateString=dateString.split(" ")[0]+" 0"+dateString.split(" ")[1];
		}
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH");
		try
		{
			Date today = df.parse(dateString);
			return today;
			
		} catch (ParseException e){e.printStackTrace();}
		return null;
	}
	public Date getTargetDate() {
		return targetDate;
	}

	public void setTargetDate(Date targetDate) {
		this.targetDate = targetDate;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}
	
	public static void main(String[] args){
		/*SimpleDateFormat s = new SimpleDateFormat("MM/dd/yyyy HH");
		Date d1 = new Date();
		Date d2 = new Date();
		Date d3 = new Date();
		Date d4 = new Date();
		Date d5 = new Date();
		try{
			d1 = s.parse("10/25/2009 00");
			d2 = s.parse("10/25/2009 01");
			d3 = s.parse("10/25/2009 19");
			d4 = s.parse("10/25/2009 23");
			d5 = s.parse("10/25/2009 26");
			
			java.util.Calendar c = java.util.Calendar.getInstance();
			
			c.setTime(d1);
			c.add(java.util.Calendar.HOUR_OF_DAY, -1);
			System.out.println(c.getTime());

			c.setTime(d2);
			c.add(java.util.Calendar.HOUR_OF_DAY, -1);
			System.out.println(c.getTime());
			
			c.setTime(d3);
			c.add(java.util.Calendar.HOUR_OF_DAY, -1);
			System.out.println(c.getTime());
			
			c.setTime(d4);
			c.add(java.util.Calendar.HOUR_OF_DAY, -1);
			System.out.println(c.getTime());
			
			c.setTime(d5);
			c.add(java.util.Calendar.HOUR_OF_DAY, -1);
			System.out.println(c.getTime());
			
		}catch(Exception e){}*/
		
		Date t= GenericHourlyCollectorJob.parseDateString("01/13/2010 13");
		System.out.println(t);
	}
	
}
