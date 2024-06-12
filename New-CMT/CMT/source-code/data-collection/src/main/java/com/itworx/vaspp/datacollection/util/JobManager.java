package com.itworx.vaspp.datacollection.util;

import com.itworx.vaspp.datacollection.objects.Mail;
import com.itworx.vaspp.datacollection.scheduler.VDataCollectionJob;
import eg.com.vodafone.model.Job;
import eg.com.vodafone.model.enums.JobExecutionPeriod;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.TimeZone;

/**
 * This utility class allows the following Job functionality:
 * <ol>Add new Job</ol>
 * <ol>Update Existing Job</ol>
 * <ol>Delete Job</ol>
 * <p/>
 * User: Alia.Adel
 * Date: 5/11/13
 * Time: 1:57 PM
 */
public class JobManager {

    public static final String FILE_SEPARATOR = System.getProperty("file.separator");
    public static final String COLLECTOR_JOB_GROUP = "VDataCollectionJob";
    public static final String FORCE_JOB_MAIL_FROM_PROP = "force.job.mail.from";
    public static final String FORCE_JOB_MAIL_SUBJECT_PROP = "force.job.mail.subject";
    public static final String FORCE_JOB_MAIL_BODY_PROP = "force.job.mail.body";
    public static final String FORCE_JOB_SMS_BODY_PROP = "force.job.sms.body";
    private static final String OLD_JOB_GROUP = "collectorJob";
    private static final String COLLECTOR_JOB_CLASS
            = "com.itworx.vaspp.datacollection.scheduler.CollectorJob";
    private static final String HOURLY_COLLECTOR_JOB_CLASS
            = "com.itworx.vaspp.datacollection.scheduler.HourlyCollectorJob";
    private static final String GENERIC_HOURLY_COLLECTOR_JOB_CLASS
            = "com.itworx.vaspp.datacollection.scheduler.GenericHourlyCollectorJob";
    //TODO load from property file
    private static final long ALLOWED_FREE_SERVER_MEMORY_MB = 512;
    //TODO load from property file
    private static final String FREE_SERVER_MEMORY_COMMAND = "/freeMemoryCheck.sh";
    private static final String LOG_CONF = "resources" + FILE_SEPARATOR
            + "configuration" + FILE_SEPARATOR + "log4j.xml";
    private static Logger LOGGER;
    private static Scheduler qrtzScheduler = null;

    private static void init() {
        try {
            DOMConfigurator.configure(LOG_CONF);
            LOGGER = Logger.getLogger("JobManager");

            PropertyReader.init(".", LOGGER);
            /**
             * Setting default time zone
             */
            setAppDefaultTimeZone();


        } catch (Exception e) {
            LOGGER.error("JOBUTIL-1000:JobManager.init() - Error in log4j configuration file"
                    + e.getMessage(), e);
            LOGGER.error(Job.JOB_REMOTE_ERROR_KEYWORD + e.getMessage());
            System.exit(-1);
        }
        StdSchedulerFactory sf = new StdSchedulerFactory();
        try {
            qrtzScheduler = sf.getScheduler();
        } catch (SchedulerException e) {
            LOGGER.error("JOBUTIL-1001:JobManager.init() - Error in getting scheduler file"
                    + e.getMessage(), e);
            LOGGER.error(Job.JOB_REMOTE_ERROR_KEYWORD + e.getMessage());
            System.exit(-1);
        } catch (Exception e) {
            LOGGER.error("General Exception:" + e.getMessage(), e);
            LOGGER.error(Job.JOB_REMOTE_ERROR_KEYWORD + e.getMessage());
            System.exit(-1);
        }
    }

    /**
     * Add Quartz Job to scheduler
     *
     * @param args
     * @return
     * @throws ParseException
     * @throws SchedulerException
     */
    private static void addJob(String args[]) {
        try {
            if (qrtzScheduler != null) {
                JobDetail jobDetail;
                JobDataMap jobDataMap = new JobDataMap();
                CronTrigger cronTrigger = new CronTrigger();

                if (StringUtils.isNotEmpty(args[1])
                        && StringUtils.isNotEmpty(args[2]) && StringUtils.isNotEmpty(args[3])
                        && StringUtils.isNotEmpty(args[4]) && StringUtils.isNotEmpty(args[5])
                        && StringUtils.isNotEmpty(args[6])) {
                    //Create JobDetails Object
                    jobDetail = new JobDetail(args[1], COLLECTOR_JOB_GROUP, VDataCollectionJob.class);
                    jobDetail.setDescription(args[2]);
                    jobDetail.setRequestsRecovery(true);

                    //Create JobDataMap
                    jobDataMap.put(Job.MAP_SYSTEM_NAME, args[3]);
                    if (args[4] != null && StringUtils.isNotEmpty(args[4])
                            && !args[4].equals(Job.MAP_NO_NODE)) {
                        jobDataMap.put(Job.MAP_NODE_NAME, args[4]);
                    } else {
                        jobDataMap.put(Job.MAP_NODE_NAME, Job.MAP_SYSTEM_SYSTEM);
                    }

                    if (args[6].equals(JobExecutionPeriod.HOURLY.name())) {
                        jobDataMap.put(VDataCollectionJob.IS_HOURLY_KEY,
                                VDataCollectionJob.IS_HOURLY_KEY_TRUE);
                    } else {
                        jobDataMap.put(VDataCollectionJob.IS_HOURLY_KEY, VDataCollectionJob.IS_HOURLY_KEY_FALSE);
                        if (StringUtils.isNotEmpty(args[7]) && StringUtils.isNotEmpty(args[8])) {
                            jobDataMap.put(Job.MAP_RETRY_COUNT, args[7]);
                            jobDataMap.put(Job.MAP_RETRY_INTERVAL, args[8]);
                        } else {
                            LOGGER.error(Job.JOB_REMOTE_ERROR_KEYWORD
                                    + "Retry count or/and retry interval is missing");
                            System.exit(-1);
                        }
                    }

                    jobDataMap.setAllowsTransientData(true);

                    jobDetail.setJobDataMap(jobDataMap);

                    //Create CRON Trigger
                    cronTrigger.setName(args[1].trim() + "_TRIGGER");
                    cronTrigger.setDescription(args[1].trim() + " CRON TRIGGER");
                    cronTrigger.setGroup(COLLECTOR_JOB_GROUP);
                    cronTrigger.setCronExpression(args[5]);
                    cronTrigger.setJobName(args[1]);
                    cronTrigger.setJobGroup(COLLECTOR_JOB_GROUP);
                    cronTrigger.setTimeZone(TimeZone.getDefault());

                    //Add Job to Scheduler
                    qrtzScheduler.scheduleJob(jobDetail, cronTrigger);
                    
                    LOGGER.info("Addition done successfully");
                    System.exit(0);

                } else {
                    LOGGER.error("Some of the values are null or empty");
                    System.out.println("For Job Addition: <<" + Job.JOB_REMOTE_ADD
                            + ">> <<JOB_NAME>> <<DESCRIPTION>> <<SYSTEM_NAME>> <<NODE_NAME>> <<CRON_EXP>> " +
                            "<<EXECUTION_PERIOD(HOURLY/DAILY)>> <<RETRY_COUNT>> <<RETRY_INTERVAL>>");
                    LOGGER.error(Job.JOB_REMOTE_ERROR_KEYWORD + "Some of the values are null or empty");
                    System.exit(-1);
                }
            }
        } catch (ParseException e) {
            LOGGER.error("Parse exception during CRON validation:" + e.getMessage(), e);
            LOGGER.error(Job.JOB_REMOTE_ERROR_KEYWORD + e.getMessage());
            System.exit(-1);

        } catch (SchedulerException e) {
            LOGGER.error("SchedulerException:" + e.getMessage(), e);
            LOGGER.error(Job.JOB_REMOTE_ERROR_KEYWORD + e.getMessage());
            System.exit(-1);
        } catch (Exception e) {
            LOGGER.error("General Exception:" + e.getMessage(), e);
            LOGGER.error(Job.JOB_REMOTE_ERROR_KEYWORD + e.getMessage());
            System.exit(-1);
        }

        LOGGER.error(Job.JOB_REMOTE_ERROR_KEYWORD);
        System.exit(-1);


    }

    /**
     * Update Quartz Job
     *
     * @param args
     * @return
     * @throws ParseException
     * @throws SchedulerException
     */
    private static void updateJob(String args[]) {
        try {
            if (qrtzScheduler != null) {
                JobDetail jobDetail;
                JobDataMap jobDataMap;
                CronTrigger cronTrigger = new CronTrigger();

                if (StringUtils.isNotEmpty(args[1])
                        && StringUtils.isNotEmpty(args[2]) && StringUtils.isNotEmpty(args[3])
                        && StringUtils.isNotEmpty(args[4]) && StringUtils.isNotEmpty(args[5])
                        && StringUtils.isNotEmpty(args[6])) {
                    jobDetail = qrtzScheduler.getJobDetail(args[1], COLLECTOR_JOB_GROUP);
                    if (jobDetail != null) {
                        //Update Job Details
                        jobDetail.setDescription(args[2]);
                        jobDetail.setRequestsRecovery(true);

                        //Update JobDataMap
                        jobDataMap = jobDetail.getJobDataMap();
                        jobDataMap.put(Job.MAP_SYSTEM_NAME, args[3]);
                        if (args[4] != null && StringUtils.isNotEmpty(args[4])) {
                            if (!args[4].equals(Job.MAP_NO_NODE)) {
                                jobDataMap.put(Job.MAP_NODE_NAME, args[4]);
                            } else {
                                jobDataMap.put(Job.MAP_NODE_NAME, Job.MAP_SYSTEM_SYSTEM);
                            }
                        }

                        if (args[6].equals(JobExecutionPeriod.HOURLY.name())) {
                            jobDataMap.put(VDataCollectionJob.IS_HOURLY_KEY, VDataCollectionJob.IS_HOURLY_KEY_TRUE);
                        } else {
                            jobDataMap.put(VDataCollectionJob.IS_HOURLY_KEY, VDataCollectionJob.IS_HOURLY_KEY_FALSE);
                            if (StringUtils.isNotEmpty(args[7]) && StringUtils.isNotEmpty(args[8])) {
                                jobDataMap.put(Job.MAP_RETRY_COUNT, args[7]);
                                jobDataMap.put(Job.MAP_RETRY_INTERVAL, args[8]);
                            } else {
                                LOGGER.error(Job.JOB_REMOTE_ERROR_KEYWORD
                                        + "Retry count or/and retry interval is missing");
                                System.exit(-1);
                            }
                        }

                        jobDataMap.setAllowsTransientData(true);

                        jobDetail.setJobDataMap(jobDataMap);
                        //Update CRON Trigger
                        cronTrigger =
                                (CronTrigger) qrtzScheduler.getTrigger(args[1].trim() + "_TRIGGER", COLLECTOR_JOB_GROUP);
                        cronTrigger.setCronExpression(args[5]);
                        cronTrigger.setTimeZone(TimeZone.getDefault());


                        //Update scheduler with the updated Job details
                        qrtzScheduler.addJob(jobDetail, true);
                        Date nextFireTime = qrtzScheduler.rescheduleJob(cronTrigger.getName(), COLLECTOR_JOB_GROUP, cronTrigger);
                        LOGGER.info("Update done successfully. and the next fire time is: "+nextFireTime);
                        System.exit(0);

                    } else {
                        System.out.println("Couldn't find job named: " + args[1]);
                        LOGGER.error(Job.JOB_REMOTE_ERROR_KEYWORD + "Couldn't find job named: " + args[1]);
                        System.exit(-1);
                    }
                } else {
                    LOGGER.error("Some of the values are null or empty");
                    System.out.println("For Job Update: <<" + Job.JOB_REMOTE_UPDATE
                            + ">> <<JOB_NAME>> <<DESCRIPTION>> <<SYSTEM_NAME>> <<NODE_NAME>> <<CRON_EXP>> " +
                            " <<EXECUTION_PERIOD(HOURLY/DAILY)>> <<RETRY_COUNT>> <<RETRY_INTERVAL>>");
                    LOGGER.error(Job.JOB_REMOTE_ERROR_KEYWORD + "Some of the values are null or empty");
                    System.exit(-1);
                }
            }
        } catch (ParseException e) {
            LOGGER.error("Parse exception during CRON validation:" + e.getMessage(), e);
            LOGGER.error(Job.JOB_REMOTE_ERROR_KEYWORD + e.getMessage());
            System.exit(-1);
        } catch (SchedulerException e) {
            LOGGER.error("SchedulerException:" + e.getMessage(), e);
            LOGGER.error(Job.JOB_REMOTE_ERROR_KEYWORD + e.getMessage());
            System.exit(-1);
        } catch (Exception e) {
            LOGGER.error("General Exception:" + e.getMessage(), e);
            LOGGER.error(Job.JOB_REMOTE_ERROR_KEYWORD + e.getMessage());
            System.exit(-1);
        }
        LOGGER.error(Job.JOB_REMOTE_ERROR_KEYWORD);
        System.exit(-1);
    }

    /**
     * Delete Job/s
     *
     * @param jobName
     * @return
     */
    private static void deleteJob(String jobName) {
        try {
            if (qrtzScheduler != null && StringUtils.isNotEmpty(jobName)) {
                String[] jobNames = null;
                JobDetail jobDetail;

                if (jobName.startsWith("[")
                        && jobName.endsWith("]")) {
                    jobName = jobName.replaceFirst("\\[", "");
                    jobName = jobName.replace("]", "");
                    LOGGER.info("job name after removing [ & ]:" + jobName);
                }
                if (jobName.contains(",")) {
                    jobNames = jobName.split(",");
                }

                LOGGER.info("Job names'' size after split:" + ((jobNames != null) ? jobNames.length : null));

                if (jobNames != null && jobNames.length > 0) {
                    for (String job : jobNames) {
                        job = job.trim();
                        jobDetail
                                = qrtzScheduler.getJobDetail(job, COLLECTOR_JOB_GROUP);
                        if (jobDetail != null) {
                            qrtzScheduler.getCurrentlyExecutingJobs();
                            qrtzScheduler.deleteJob(job, COLLECTOR_JOB_GROUP);
                            LOGGER.info("Deletion done successfully");
                        } else {
                            System.out.println("Couldn't find job named: " + job);
                            LOGGER.error(Job.JOB_REMOTE_ERROR_KEYWORD + "Couldn't find job named: " + job);
                        }
                    }
                    System.exit(0);
                } else {
                    jobDetail
                            = qrtzScheduler.getJobDetail(jobName, COLLECTOR_JOB_GROUP);
                    if (jobDetail != null) {
                        qrtzScheduler.getCurrentlyExecutingJobs();
                        qrtzScheduler.deleteJob(jobName, COLLECTOR_JOB_GROUP);
                        LOGGER.info("Deletion done successfully");
                        System.exit(0);
                    } else {
                        System.out.println("Couldn't find job named: " + jobName);
                        LOGGER.error(Job.JOB_REMOTE_ERROR_KEYWORD + "Couldn't find job named: " + jobName);
                        System.exit(-1);
                    }
                }
            } else {
                System.out.println("For Job Deletion: <<" + Job.JOB_REMOTE_DELETE + ">> <<JOB_NAME>>");
                LOGGER.error(Job.JOB_REMOTE_ERROR_KEYWORD);
                System.exit(-1);
            }
        } catch (SchedulerException e) {
            LOGGER.error("SchedulerException:" + e.getMessage(), e);
            LOGGER.error(Job.JOB_REMOTE_ERROR_KEYWORD);
            System.exit(-1);
        } catch (Exception e) {
            LOGGER.error("General Exception:" + e.getMessage(), e);
            LOGGER.error(Job.JOB_REMOTE_ERROR_KEYWORD + e.getMessage());
            System.exit(-1);
        }
        LOGGER.error(Job.JOB_REMOTE_ERROR_KEYWORD);
        System.exit(-1);
    }

    /**
     * Force Job by calling DataCollectionManager directly
     * using batch shell script
     *
     * @param path
     * @param jobName
     * @param startDate
     * @param endDate
     */
    private static void forceJobWithNoQuartz(String path, String jobName, String startDate, String endDate) {
        try {
            if (qrtzScheduler != null
                    && StringUtils.isNotEmpty(path)
                    && StringUtils.isNotEmpty(jobName)
                    && StringUtils.isNotEmpty(startDate)
                    && StringUtils.isNotEmpty(endDate)) {
                JobDataMap jobDataMap;
                boolean isHourly = false;
                String systemName = "";
                String commandResult = "";
                int freeMemory = 0;
                JobDetail jobDetail = qrtzScheduler.getJobDetail(jobName, COLLECTOR_JOB_GROUP);
                LOGGER.info("checking available memory on server");
                try {
                    commandResult
                            = executeCommand(path + FREE_SERVER_MEMORY_COMMAND);
                    freeMemory =
                            Integer.parseInt(commandResult.trim());
                    if (freeMemory <= ALLOWED_FREE_SERVER_MEMORY_MB) {
                        LOGGER.info("No enough memory on server to execute job for date:"
                                + startDate + " to " + endDate);
                        LOGGER.error(Job.JOB_REMOTE_ERROR_KEYWORD + Job.NO_MEMORY_KEYWORD);
                        System.exit(-1);
                    }
                } catch (NumberFormatException e) {
                    LOGGER.error("Number format exception for memory check:" + commandResult, e);
                } catch (Exception e) {
                    LOGGER.error("Exception thrown while trying to check server memory", e);
                }

                if (jobDetail != null) {
                    jobDataMap = jobDetail.getJobDataMap();
                    if (jobDataMap.get(Job.MAP_PERIOD) != null) {
                        isHourly = Boolean.valueOf(jobDataMap.get(Job.MAP_PERIOD).toString());
                    }
                    if (jobDataMap.get(Job.MAP_SYSTEM_NAME) != null) {
                        systemName = jobDataMap.get(Job.MAP_SYSTEM_NAME).toString();
                    }
                    if (StringUtils.isNotEmpty(systemName)) {
                        if (jobDataMap.get(Job.MAP_NODE_NAME) != null
                                && !jobDataMap.get(Job.MAP_NODE_NAME).equals(Job.MAP_SYSTEM_NODE) && !isHourly) {
                            systemName += '(' + jobDataMap.get(Job.MAP_NODE_NAME).toString() + ')';
                        }

                        String[] collectorArgs = {systemName, startDate, endDate, path};
                        LOGGER.info("Arguments sent to collector:");
                        for (String arg : collectorArgs) {
                            LOGGER.info(arg);
                        }
                        if (isHourly) {
                            HourlyBatchCollector.main(collectorArgs);
                        } else {
                            BatchCollector.main(collectorArgs);
                        }
                        LOGGER.info("job force completed.");

                        //Send notification after completion
                        try {
                            DCNotificationManager.sendNotificationForJobCompletion(systemName,
                                    composeForceJobNotificationMail(systemName, startDate, endDate),
                                    composeForceJobNotificationSMS(systemName, startDate, endDate));
                        } catch (Exception ex) {
                            LOGGER.error("Exception thrown while trying to send notification", ex);
                        }

                        System.exit(0);
                    }
                }
            } else {
                System.out.println("For Job Force: <<" + Job.JOB_REMOTE_FORCE
                        + ">> <<PATH>> <<JOB_NAME>> <<START_DATE(dd/MM/yyyy) or (dd/MM/yyyy:HH)>> <<END_DATE(dd/MM/yyyy) or (dd/MM/yyyy:HH)>>");
                LOGGER.error("For Job Force: <<" + Job.JOB_REMOTE_FORCE
                        + ">> <<PATH>> <<JOB_NAME>> <<START_DATE(dd/MM/yyyy) or (dd/MM/yyyy:HH)>> <<END_DATE(dd/MM/yyyy) or (dd/MM/yyyy:HH)>>");
            }
        } catch (SchedulerException e) {
            LOGGER.error("SchedulerException:" + e.getMessage(), e);
            LOGGER.error(Job.JOB_REMOTE_ERROR_KEYWORD + e.getMessage());
            System.exit(-1);
        } catch (Exception e) {
            LOGGER.error("General Exception:" + e.getMessage(), e);
            LOGGER.error(Job.JOB_REMOTE_ERROR_KEYWORD + e.getMessage());
            System.exit(-1);
        }
        LOGGER.error(Job.JOB_REMOTE_ERROR_KEYWORD);
        System.exit(-1);
    }

    /**
     * Updates jobs with CollectorJob Group with
     * the new VDataCollectionJob Group
     * in case their classes where "CollectorJob", "HourlyCollectorJob"
     * or "GenericHourlyCollectorJob"
     * In case the job class is either "HourlyCollectorJob" or "GenericHourlyCollectorJob"
     * then their "Hourly" job data Map entry will be equal true.
     *
     * @param jobName
     */
    private static void updateJobGroup(String jobName) {
        if (qrtzScheduler != null && StringUtils.isNotEmpty(jobName)) {
            JobDetail jobDetail;
            try {
                if (jobName.equalsIgnoreCase("All")) {

                    String[] jobDetailsNames = qrtzScheduler.getJobNames(OLD_JOB_GROUP);
                    for (String jobNameStr : jobDetailsNames) {
                        jobDetail = qrtzScheduler.getJobDetail(
                                jobNameStr, OLD_JOB_GROUP);
                        updateOldJob(jobDetail);
                    }
                } else {
                    jobDetail = qrtzScheduler.getJobDetail(jobName, OLD_JOB_GROUP);
                    updateOldJob(jobDetail);
                }
            } catch (SchedulerException e) {
                LOGGER.error("SchedulerException:" + e.getMessage(), e);
                System.exit(-1);
            } catch (Exception e) {
                LOGGER.error("General Exception:" + e.getMessage(), e);
                System.exit(-1);
            } finally {
                System.exit(-1);
            }
        } else {
            System.out.println("For Job Group update: UPDATE_JOB_GROUP <<JOB_NAME or All>> " +
                    "<<OLD_JOB_NAME>>");
            System.exit(-1);
        }
    }

    /**
     * Execute the given unix command and return the result as text
     *
     * @param command
     * @return
     * @throws Exception
     */
    public static String executeCommand(String command) throws InterruptedException, IOException {
        LOGGER.info("Executing Command: " + command);
        Runtime run = Runtime.getRuntime();
        Process pr = run.exec(command);
        pr.waitFor();

        String successMessage = getStreamMessage(pr.getInputStream());
        String commandOutput = StringUtils.isNotBlank(successMessage) ?
                successMessage : getStreamMessage(pr.getErrorStream());
        LOGGER.info("Command Output: " + commandOutput);
        return commandOutput;
    }

    private static void showRunningJobs() {
        try {
            LOGGER.info("Job running count:" + qrtzScheduler.getCurrentlyExecutingJobs().size());
            JobExecutionContext jobExecutionContext;
            for (Object obj : qrtzScheduler.getCurrentlyExecutingJobs()) {
                jobExecutionContext = (JobExecutionContext) obj;
                JobDetail jobDetail = jobExecutionContext.getJobDetail();
                LOGGER.debug("jobName:" + jobDetail.getName());
                JobDataMap dataMap = jobDetail.getJobDataMap();
                LOGGER.debug("System Name:" + dataMap.get(Job.MAP_SYSTEM_NAME));
            }
        } catch (SchedulerException e) {
            LOGGER.error("", e);
        }
    }

    /**
     * Get the text message inside the input stream
     *
     * @param is
     * @return
     * @throws IOException
     */
    private static String getStreamMessage(InputStream is) throws IOException {

        BufferedReader buf = new BufferedReader(new InputStreamReader(is));
        String line = "";
        StringBuffer commandOutput = new StringBuffer();
        while ((line = buf.readLine()) != null) {
            commandOutput.append(line);
            commandOutput.append('\n');
        }
        return commandOutput.toString();
    }

    /**
     * Update old CollectorJob jobs with VDataCollectionJob
     *
     * @param jobDetail
     * @throws SchedulerException
     */
    private static void updateOldJob(JobDetail jobDetail) throws SchedulerException {
        JobDataMap jobDataMap;
        CronTrigger cronTrigger;
        Trigger[] triggers;
        if (jobDetail != null && jobDetail.getJobClass().toString().contains(COLLECTOR_JOB_CLASS)
                || jobDetail.getJobClass().toString().contains(HOURLY_COLLECTOR_JOB_CLASS)
                || jobDetail.getJobClass().toString().contains(GENERIC_HOURLY_COLLECTOR_JOB_CLASS)) {

            LOGGER.debug("Updating job with name:" + jobDetail.getName()
                    + ", Job Group:" + jobDetail.getGroup()
                    + ", Job Class:" + jobDetail.getJobClass());

            jobDataMap = jobDetail.getJobDataMap();
            if (jobDetail.getJobClass().toString().contains(HOURLY_COLLECTOR_JOB_CLASS)
                    || jobDetail.getJobClass().toString().contains(GENERIC_HOURLY_COLLECTOR_JOB_CLASS)) {
                jobDataMap.put(VDataCollectionJob.IS_HOURLY_KEY,
                        VDataCollectionJob.IS_HOURLY_KEY_TRUE);
            } else {
                jobDataMap.put(VDataCollectionJob.IS_HOURLY_KEY,
                        VDataCollectionJob.IS_HOURLY_KEY_FALSE);
            }

            jobDetail.setJobDataMap(jobDataMap);
            jobDetail.setGroup(COLLECTOR_JOB_GROUP);
            jobDetail.setJobClass(VDataCollectionJob.class);

            triggers = qrtzScheduler.getTriggersOfJob(jobDetail.getName(), OLD_JOB_GROUP);

            qrtzScheduler.deleteJob(jobDetail.getName(), OLD_JOB_GROUP);
            for (Trigger trigger : triggers) {
                LOGGER.debug("Trigger name:" + trigger.getName());
                if (trigger.getJobGroup().equals(OLD_JOB_GROUP)) {
                    LOGGER.debug("Trigger for job:" + jobDetail.getName() + ", Trigger name:" + trigger.getName()
                            + ", CRON expression:" + ((CronTrigger) trigger).getCronExpression());
                    trigger.setGroup(COLLECTOR_JOB_GROUP);
                    trigger.setJobGroup(COLLECTOR_JOB_GROUP);
                    cronTrigger = (CronTrigger) trigger;
                    cronTrigger.setName(jobDetail.getName() + "_TRIGGER");
                    qrtzScheduler.scheduleJob(jobDetail, cronTrigger);
                    break;
                }
            }
        }
    }

    /**
     * Set Default time zone according to the value added in "app.timezone"
     * in "app_config.properties" file
     */
    public static void setAppDefaultTimeZone() {
        //Egypt time zone
        String timeZone = "GMT+2";
        try {
            timeZone = PropertyReader.getPropertyValue("app.timezone");
        } catch (Exception ex) {
            System.out.println("Exception thrown while trying to get default time zone (GMT+2 will be used now):"
                    + ex.getMessage());
        }
        TimeZone.setDefault(TimeZone.getTimeZone(timeZone));
    }

    public static void main(String[] args) {
        
        args = new String[5];
        
        args[0] = "JOB_FORCE";
        args[1] = "D:\\Work\\CMT";
        args[2] = "CPMON_Risk";
        args[3] = "22-11-2017";
        args[4] = "22-11-207";
        
        
        if (args != null && args.length > 0) {

            //Initialize LOGGER & get scheduler
            init();

            StringBuffer argStr = new StringBuffer(400);
            for (String arg : args) {
                argStr.append(' ').append(arg);
            }
            LOGGER.info("received parameters are:" + argStr);
            String actionType = args[0];


            if (actionType.equals(Job.JOB_REMOTE_ADD)) {
                addJob(args);
            } else if (actionType.equals(Job.JOB_REMOTE_UPDATE)) {
                updateJob(args);
            } else if (actionType.equals(Job.JOB_REMOTE_DELETE)) {
                deleteJob(args[1]);
            } else if (actionType.equals(Job.JOB_REMOTE_FORCE)) {
                forceJobWithNoQuartz(args[1], args[2], args[3], args[4]);
            } else if (actionType.equals("SHOW_JOBS")) {
                showRunningJobs();
            } else if (actionType.equals("UPDATE_JOB_GROUP")) {
                updateJobGroup(args[1]);
            }
        } else {
            System.out.println("Invalid argument count.");
            System.out.println("For Job Addition: <<" + Job.JOB_REMOTE_ADD
                    + ">> <<JOB_NAME>> <<DESCRIPTION>> <<SYSTEM_NAME>> <<NODE_NAME>> <<CRON_EXP>> " +
                    "<<EXECUTION_PERIOD(HOURLY/DAILY)>> <<RETRY_COUNT>> <<RETRY_INTERVAL>>");
            System.out.println("For Job Update: <<" + Job.JOB_REMOTE_UPDATE
                    + ">> <<JOB_NAME>> <<DESCRIPTION>> <<SYSTEM_NAME>> <<NODE_NAME>> <<CRON_EXP>> " +
                    " <<EXECUTION_PERIOD(HOURLY/DAILY)>> <<RETRY_COUNT>> <<RETRY_INTERVAL>>");
            System.out.println("For Job Deletion: <<" + Job.JOB_REMOTE_DELETE + ">> <<JOB_NAME>>");
            System.out.println("For Job Force: <<" + Job.JOB_REMOTE_FORCE
                    + ">> <<PATH>> <<JOB_NAME>> <<START_DATE(dd/MM/yyyy) or (dd/MM/yyyy:HH)>> <<END_DATE(dd/MM/yyyy) or (dd/MM/yyyy:HH)>>");

            System.out.println("For Job Deletion:  <<" + Job.JOB_REMOTE_DELETE + ">> <<JOB_NAME>>");
        }
        System.exit(-1);

    }

    /**
     * Construct the mail format used to notify user/s
     * that the force job has completed
     *
     * @param systemName
     * @param fromDate
     * @param toDate
     * @return
     */
    private static Mail composeForceJobNotificationMail(String systemName, String fromDate, String toDate) {
        String mailContent = PropertyReader.getPropertyValue(FORCE_JOB_MAIL_BODY_PROP);
        mailContent = MessageFormat.format(mailContent, systemName, fromDate, toDate);
        Mail mail = new Mail();
        mail.setFromRecipient(PropertyReader.getPropertyValue(FORCE_JOB_MAIL_FROM_PROP));
        mail.setSubject(PropertyReader.getPropertyValue(FORCE_JOB_MAIL_SUBJECT_PROP));
        mail.setContent(mailContent);

        return mail;
    }

    /**
     * Construct the SMS format used to notify user/s
     * that the force job has completed
     *
     * @param systemName
     * @param fromDate
     * @param toDate
     * @return
     */
    private static String composeForceJobNotificationSMS(String systemName, String fromDate, String toDate) {
        SMSUtility.init();
        String smsContent = PropertyReader.getPropertyValue(FORCE_JOB_SMS_BODY_PROP);
        smsContent = MessageFormat.format(smsContent, systemName, fromDate, toDate);
        return smsContent;
    }


}
