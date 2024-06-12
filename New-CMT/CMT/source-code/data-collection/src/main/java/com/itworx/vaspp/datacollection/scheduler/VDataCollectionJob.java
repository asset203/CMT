package com.itworx.vaspp.datacollection.scheduler;

import com.itworx.vaspp.datacollection.CollectorScheduler;
import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.common.InputException;
import com.itworx.vaspp.datacollection.util.DataCollectionManager;
import com.itworx.vaspp.datacollection.util.PropertyReader;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.quartz.*;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: basma.alkerm
 * Date: 3/31/13
 * Time: 11:12 AM
 * To change this template use File | Settings | File Templates.
 */
public class VDataCollectionJob implements Job {

    public static final String FILE_SEPARATOR = System.getProperty("file.separator");
    public static final String SYSTEM_NAME_KEY = "SystemName";
    public static final String NODE_NAME_KEY = "NodeName";
    public static final String RETRY_COUNT_KEY = "RetryCount";
    public static final String RETRY_INTERVAL_KEY = "RetryInterval";
    public static final String IS_HOURLY_KEY = "isHourly";
    public static final String TARGET_DATE_KEY = "TargetDate";
    public static final String NUMBER_OF_TRAILS_KEY = "no_of_retrials";
    public static final String IS_HOURLY_KEY_TRUE = "true";
    public static final String IS_HOURLY_KEY_FALSE = "false";
    private static final String LOG_CONF = "resources" + FILE_SEPARATOR
            + "configuration" + FILE_SEPARATOR + "log4j.xml";
    private Logger logger;
    private Date targetDate;
    private String systemName;
    private String nodeName;
    private boolean isHourly;
    private int retryCount;
    private int retryInterval;

    private static Date parseDateString(String dateString) {
        if (dateString.split(" ")[1].length() == 1) {
            dateString = dateString.split(" ")[0] + " 0" + dateString.split(" ")[1];
        }
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH");
        try {
            Date today = df.parse(dateString);
            return today;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Logger getLogger() {
        return Logger.getLogger("VDataCollectionJobLogger");
    }

    public void execute(JobExecutionContext context) throws JobExecutionException {

        logger = getLogger();
        //  try{
        DOMConfigurator.configure(LOG_CONF);
        JobDataMap jobDataMap = null;
        jobDataMap = context.getJobDetail().getJobDataMap();
        logger.debug("VDataCollectionJob - Loading parameters from jobDataMap for job:"
                + context.getJobDetail().getName());
        systemName = jobDataMap.getString(SYSTEM_NAME_KEY);
        nodeName = jobDataMap.getString(NODE_NAME_KEY);

        try {
            retryCount = jobDataMap.getIntFromString(RETRY_COUNT_KEY);

        } catch (Exception e) {
            retryCount = 3;
        }
        try {
            retryInterval = jobDataMap.getIntFromString(RETRY_INTERVAL_KEY);

        } catch (Exception e) {
            retryInterval = 24;
        }
        try {
            isHourly = IS_HOURLY_KEY_TRUE.equals(jobDataMap.getString(IS_HOURLY_KEY));
        } catch (Exception e) {
            isHourly = false;
        }

        if (systemName == null) {
            logger.error("VCJ-1000: VDataCollectionJob.execute() - Job Parameters are not complete missing system name - aborting job - no retry");
            return;
        }
        if (jobDataMap.get(eg.com.vodafone.model.Job.MAP_DATE) != null) {
            try {
                targetDate = (Date) jobDataMap.get(eg.com.vodafone.model.Job.MAP_DATE);
                logger.info("received target date is:" + targetDate);
            } catch (Exception ex) {
                logger.error("Exception has occurred while getting target date from map", ex);
            }

        }

        logger.debug("received system name:" + systemName + ", node name:" + nodeName
                + ", target date:" + targetDate + ", retry count:" + retryCount + ", retry interval:" + retryInterval);

        if (isHourly) {
            executeHourlyJob();
        } else//if != true || or not exist (null)
        {
            executeDailyJob(context);
        }
        logger.info("VDataCollectionJob.execute() - Finish Job for system: "
                + systemName + ", node: " + nodeName + ", home: "
                + CollectorScheduler.getHomeDirectory());
          /* }catch(Exception e){
               logger.error("VDataCollectionJob.execute() - Failed: Exception Occurred ",e);
               logger.error("==== Job Thread wil Exit ===");
               System.exit(1);
           } */
    }

    private void executeDailyJob(JobExecutionContext context) {
        if (nodeName == null) {
            logger.error("VCJ-1000: VDataCollectionJob.executeDailyJob - Job Parameters are not complete : missing node name - aborting job - no retry");
            return;
        }

        logger.info("VDataCollectionJob.executeDailyJob() - Dispatch Job for system: "
                + systemName + ", node: " + nodeName + ", home: "
                + CollectorScheduler.getHomeDirectory());

        setTargetDateForDailyJobs();

        try {
            DataCollectionManager.dispatchJob(systemName, nodeName, targetDate);
        } catch (InputException ex) {
            logger.error("VDataCollectionJob.executeDailyJob() Exception- System :"
                    + systemName + "  Node : " + nodeName + " ---> ", ex);
            createRecoveryJob(context, ex, "InputRecovery");

        } catch (ApplicationException e) {
            logger.error("VDataCollectionJob.executeDailyJob() Exception- System :" + systemName + "  Node : " + nodeName + " ---> "
                    , e);
            createRecoveryJob(context, e, "ApplicationRecovery");
        }
    }

    private void setTargetDateForHourlyJob() {
        logger.debug("VDataCollectionJob() - setTargetDateForHourlyJob");
        // set the target date to the hour before this job execution time
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(new Date());
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        int lag = PropertyReader.getHourlyDataCollectionLagRange();
        c.add(java.util.Calendar.HOUR_OF_DAY, lag);
        targetDate = c.getTime();
    }

    private void executeHourlyJob() {
        try {
            setTargetDateForHourlyJob();
            SimpleDateFormat simpleDF = new SimpleDateFormat("MM/dd/yyyy HH");
            String formattedDate = simpleDF.format(targetDate);
            int hour = Integer.parseInt(formattedDate.split(" ")[1]);
            Map namesVsStructures = DataCollectionManager.getNodesSystemsStructures(systemName);
            try {
                DataCollectionManager.dispatchHourlyJob(systemName,
                        targetDate);
            } catch (Exception e) {
                logger.error("VDataCollectionJob.executeHourlyJob() Exception- System : " + systemName + " ", e);
            }
            hour = hour - 1;
            while (hour >= 0) {
                Iterator it = namesVsStructures.keySet().iterator();
                while (it.hasNext()) {
                    Object key = it.next();
                    List structures = (List) namesVsStructures.get(key);
                    List dbList = DataCollectionManager.getHourlyEvent(systemName, formattedDate.split(" ")[0] + " " + hour, key.toString());
                    if (dbList.size() < structures.size()) {
                        logger.debug("inside the deleted hour : " + hour);
                        try {
                            DataCollectionManager.dispatchHourlyJobForNodes(systemName, parseDateString(formattedDate.split(" ")[0] + " " + hour), key.toString());
                        } catch (Exception e) {
                            logger.error("VDataCollectionJob.executeHourlyJob() Exception- System : " + systemName + " ", e);
                        }
                    }
                }
                hour--;
            }
        } catch (Exception ex) {
            logger.error("VDataCollectionJob.executeHourlyJob() Exception- System : " + systemName + " ", ex);
        }
    }

    private void setTargetDateForDailyJobs() {
        logger.debug("VDataCollectionJob() - set target date for daily job");
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(new Date());
        c.add(java.util.Calendar.DAY_OF_MONTH, -1);
        targetDate = c.getTime();
    }

    private void createRecoveryJob(JobExecutionContext context, Exception ex, String groupName) {
        try {
            String jobName = "Recovery" + context.getJobDetail().getName()
                    + "_" + System.currentTimeMillis();
            JobDetail jobDetail = new JobDetail(
                    jobName,
                    groupName,
                    Class
                            .forName("com.itworx.vaspp.datacollection.scheduler.RecoveryCollectorJob"));
            JobDataMap dataMap = new JobDataMap();
            dataMap.put(SYSTEM_NAME_KEY, systemName);
            dataMap.put(NODE_NAME_KEY, nodeName);
            String msg = ex.getMessage();
            if (msg.length() > 80) {
                msg = msg.substring(0, 80);
            }

            String desc = "Sys:" + systemName + "_"
                    + nodeName + "_" + targetDate + ":"
                    + msg;
            if (desc.length() > 115) {
                desc = desc.substring(0, 115);
            }
            jobDetail.setDescription(desc);
            SimpleDateFormat f = new SimpleDateFormat();
            StringBuffer s = f.format(targetDate, new StringBuffer(),
                    new FieldPosition(f.DATE_FIELD));
            dataMap.put(TARGET_DATE_KEY, "" + s);

            dataMap.put(RETRY_COUNT_KEY, "" + retryCount);
            dataMap.put(RETRY_INTERVAL_KEY, "" + retryInterval);
            dataMap.put(NUMBER_OF_TRAILS_KEY, "1");

            jobDetail.setJobDataMap(dataMap);
            java.util.Calendar c = java.util.Calendar.getInstance();
            c.setTime(new Date());
            c.add(java.util.Calendar.MINUTE/* DAY_OF_MONTH */, retryInterval);
            Date runningTime = c.getTime();
            context.getScheduler().addJob(jobDetail, true);
            SimpleTrigger trigger = new SimpleTrigger("Recovery_trigger_"
                    + System.currentTimeMillis(), groupName, runningTime);
            trigger.setJobName(jobName);
            trigger.setJobGroup(groupName);
            String triggerDesc = "Failed " + systemName + " "
                    + nodeName + " trigger - retries left:"
                    + retryCount;

            if (triggerDesc.length() > 115) {
                triggerDesc = triggerDesc.substring(0, 115);
            }

            trigger.setDescription(triggerDesc);
            context.getScheduler().scheduleJob(trigger);
        } catch (Exception e) {
            logger.error("VCJ-1001: VDataCollectionJob.createRecoveryJob() - Couldn't create Recovery Job for system : "
                    + systemName + " Node : " + nodeName + "  ---> ", e);
        }
    }

}
