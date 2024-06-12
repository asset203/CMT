package com.itworx.vaspp.datacollection.scheduler;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.util.DataCollectionManager;

public class SystemMonitorJob implements Job{

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		try {
			DataCollectionManager.logCurrentSystemState();
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
