/*
 * File:       CollectorJob.java
 * Date        Author          Changes
 * 29/01/2006  Tamer Shawky    Created
 * 20/02/2006  Nayera Mohamed  Updated to fix reload
 * 27/02/2006  Nayera Mohamed  Updated to include failure handling
 *
 * Scheduler for running DataCollection jobs using quartz
 */

package com.itworx.vaspp.datacollection;

import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.dao.CMTDBConnectionPool;
import com.itworx.vaspp.datacollection.util.DataCollectionManager;
import com.itworx.vaspp.datacollection.util.JobManager;
import com.itworx.vaspp.datacollection.util.SMSMailNotificationManager;
import com.itworx.vaspp.datacollection.util.SMSUtility;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import sun.misc.Signal;
import sun.misc.SignalHandler;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class CollectorScheduler extends Thread {

    private static String homeDirectory = null;
    public final String FILE_SEPARATOR = System.getProperty("file.separator");
    private final String LOG_CONF = "resources" + FILE_SEPARATOR
            + "configuration" + FILE_SEPARATOR + "log4j.xml";
    Map<String, JobDetail> jobDetailMap;
    Map<String, CronTrigger> jobTriggers;
    private boolean shutdownMode = true; // true: wait current job to
    private boolean go = true;
    private Logger logger = null;
    private DataCollectionManager collectionManager;
    private Scheduler qrtzScheduler = null;

    public static String getHomeDirectory() {
        return homeDirectory;
    }

    public static void setHomeDirectory(String home) {
        homeDirectory = home;
    }

    /**
     * Initiate the Application get the command parameters, start the scheduler
     * thread and start the signal handler
     */
    public static void main(String[] args) {
        CollectorScheduler scheduler = new CollectorScheduler();
        scheduler.setHomeDirectory(System.getProperty("user.dir"));
        if (args.length >= 1) {
            scheduler.setHomeDirectory(args[0]);
            try {
                System.setProperty("user.dir", scheduler.getHomeDirectory());
            } catch (Exception e) {
                scheduler.logger.error("CollectorScheduler.main() - "
                        + e.getMessage());
            }
        }
        if (args.length == 2) {
            try {
                scheduler.shutdownMode = Boolean.valueOf(args[1])
                        .booleanValue();
            } catch (Exception e) {
                scheduler.shutdownMode = true;
                scheduler.logger.warn("CollectorScheduler.main() - "
                        + e.getMessage());
            }
        }
        try {
            DOMConfigurator.configure(scheduler.LOG_CONF);
        } catch (Exception e) {
            scheduler.logger.error("SC-1001:CollectorScheduler.main() - Error in log4j configuration file"
                    + e.getMessage());
        }

        // initiate the signal handler and register the signals
        MySignalHandler handler = new MySignalHandler(scheduler);
        Signal.handle(new Signal("INT"), handler);
        Signal.handle(new Signal("TERM"), handler);
        Signal.handle(new Signal("ALRM"), handler);
        Signal.handle(new Signal("USR2"), handler);
        Signal.handle(new Signal("CHLD"), handler);
        Signal.handle(new Signal("TTOU"), handler);

        scheduler.logger = scheduler.getLogger();
        try {
            // initiate DataCollectionManager
            scheduler.logger
                    .info("CollectorScheduler.main() - initiaing DataCollectionManager");
            scheduler.collectionManager.init(CollectorScheduler.getHomeDirectory());

            // initiate SMSManager
            scheduler.logger.info("CollectorScheduler.main() - initiaing SMSMailNotificationManager");
            SMSMailNotificationManager.init(CollectorScheduler.getHomeDirectory(),
                    scheduler.collectionManager.getPersistenceManager());

            //initiate SMS Service
            SMSUtility.init();

        } catch (ApplicationException e) {
            scheduler.logger
                    .error("CollectorScheduler.main() - Couldn't initiate DataCollection "
                            + e);
            System.exit(2);
        }
        scheduler.start();
    }

    public Map<String, JobDetail> getJobDetailMap() {
        return jobDetailMap;
    }

    public void setJobDetailMap(Map<String, JobDetail> jobDetailMap) {
        this.jobDetailMap = jobDetailMap;
    }

    public Map<String, CronTrigger> getJobTriggers() {
        return jobTriggers;
    }

    public void setJobTriggers(Map<String, CronTrigger> jobTriggers) {
        this.jobTriggers = jobTriggers;
    }

    public Logger getLogger() {
        return Logger.getLogger("SchedulerLogger");
    }

    /**
     * stop the current running scheduler clear quartz DB tables from all jobs
     * notify all threads to stop the application
     */
    synchronized public void stopScheduler() {
        try {
            logger = getLogger();
            /**
             * Added by Alia.Adel on 2013.07.23
             * to refresh stuck jobs before scheduler shutdown
             */
            qrtzScheduler.pauseAll();
            clearQRTZ_FIRED_TRIGGERS(qrtzScheduler);
            logger.info("Fired triggers cleared");
            qrtzScheduler.resumeAll();
            removeAllNonDCJobs(qrtzScheduler);

            if (qrtzScheduler != null && !qrtzScheduler.isShutdown()) {
                logger.info("Starting scheduler shutdown process....");
                qrtzScheduler.shutdown(true);
                logger.info("scheduler is now shutdown.");
                CMTDBConnectionPool.closeConnection();
            }
            notifyAll();
            System.exit(0);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is used to interrupt the currently executing jobs
     * Jobs must implement InterruptableJob interface
     *
     * @param qrtzScheduler
     */
    private void interruptExecutingJobs(Scheduler qrtzScheduler) {
        logger = getLogger();
        try {
            if (qrtzScheduler != null && !qrtzScheduler.isShutdown()) {
                JobDetail job;
                JobExecutionContext jobExecutionContext;
                List<Object> executingJobs = qrtzScheduler.getCurrentlyExecutingJobs();
                logger.info("Size of currently executing jobs is :" + executingJobs.size());

                for (Object obj : executingJobs) {
                    if (obj instanceof JobExecutionContext) {
                        jobExecutionContext = (JobExecutionContext) obj;
                        job = jobExecutionContext.getJobDetail();
                        logger.info("Job to interrupt:" + job.getName() + ", Group:" + job.getGroup());
                        if (job.getGroup().equals(JobManager.COLLECTOR_JOB_GROUP)) {
                            qrtzScheduler.interrupt(job.getName(), JobManager.COLLECTOR_JOB_GROUP);
                        }
                    }
                }

            }
        } catch (SchedulerException e) {
            logger.error("Error while trying to interrupt jobs" + e);
        }
    }

    /**
     * Performs backup for all jobs associated with VDataCollectionJob Group
     *
     * @param qrtzScheduler
     * @return List<Object> list of currently executing jobs
     */
    private List<Object> backupCurrentlyExecutingJobs(Scheduler qrtzScheduler) {
        List<Object> executingJobs = null;
        try {
            setJobDetailMap(new HashMap<String, JobDetail>());
            setJobTriggers(new HashMap<String, CronTrigger>());

            if (qrtzScheduler != null && !qrtzScheduler.isShutdown()) {
                logger = getLogger();
                /**
                 * Added by Alia.Adel on 2013.07.23 to backup all VDataCollectionJob jobs
                 * before deletion then reschedule them before shutting down the scheduler
                 */
                System.out.println("Please wait till we backup DataCollection Jobs for cleanup");
                JobExecutionContext jobExecutionContext;
                JobDetail jobDetail;
                Trigger[] allJobCRONTrig;
                executingJobs = qrtzScheduler.getCurrentlyExecutingJobs();
                logger.info("Size of currently executing jobs is :" + executingJobs.size());

                for (Object obj : executingJobs) {
                    jobExecutionContext = (JobExecutionContext) obj;
                    jobDetail = jobExecutionContext.getJobDetail();
                    logger.info("Job to backup:" + jobDetail.getName() + ", Group:" + jobDetail.getGroup());
                    if (jobDetail.getGroup().equals(JobManager.COLLECTOR_JOB_GROUP)) {
                        logger.info("backing up job with name:" + jobDetail.getName());
                        getJobDetailMap().put(jobDetail.getName(), jobDetail);
                        allJobCRONTrig
                                = qrtzScheduler.getTriggersOfJob(
                                jobDetail.getName(), JobManager.COLLECTOR_JOB_GROUP);
                        for (Trigger jobTrigger : allJobCRONTrig) {
                            if (jobTrigger instanceof CronTrigger) {
                                getJobTriggers().put(jobDetail.getName(), (CronTrigger) jobTrigger);
                                break;
                            }
                        }
                        jobDetail = new JobDetail();
                    }
                }
            }
        } catch (Exception ex) {
            logger.error("SC-1006: Exception thrown while trying to backup jobs", ex);
        }

        logger.info("Jobs Backup completed with size:" + getJobDetailMap().size() + ", & Trigger size: "
                + getJobTriggers().size()
                + ", now performing job deletion");

        return executingJobs;
    }

    /**
     * Add backed up Jobs to scheduler
     *
     * @param qrtzScheduler
     * @param jobDetailMap
     * @param jobTriggers
     */
    private void restoreJobs(Scheduler qrtzScheduler, Map<String, JobDetail> jobDetailMap,
                             Map<String, CronTrigger> jobTriggers) {
        logger = getLogger();
        try {
            if (qrtzScheduler != null) {
                for (Map.Entry<String, JobDetail> jobMapEntry : jobDetailMap.entrySet()) {
                    logger.info("restoring job with name:" + jobMapEntry.getKey());
                    if (jobTriggers.containsKey(jobMapEntry.getKey())) {
                        logger.info("scheduling job...");
                        qrtzScheduler.scheduleJob(jobMapEntry.getValue(),
                                jobTriggers.get(jobMapEntry.getKey()));
                        logger.info("job scheduled");
                    }
                }
            }
        } catch (Exception ex) {
            logger.error("SC-1008: Error while trying to restore jobs", ex);
        }
    }

    /**
     * Remove currently executing jobs
     *
     * @param scheduler
     * @param executingJobs
     * @param removeAll     will remove all currently executing jobs else only those with VDataCollectionGroup
     */
    private void removeCurrentlyExecutingJobs(Scheduler scheduler, List<Object> executingJobs, boolean removeAll) {
        logger = getLogger();
        try {
            JobExecutionContext jobExecutionContext;
            JobDetail jobDetail;

            for (Object obj : executingJobs) {
                jobExecutionContext = (JobExecutionContext) obj;
                jobDetail = jobExecutionContext.getJobDetail();
                if (removeAll) {
                    scheduler.deleteJob(jobDetail.getName(), jobDetail.getGroup());
                    logger.info("Job deleted with Name: " + jobDetail.getName()
                            + " & group name: " + jobDetail.getGroup());
                } else if (jobDetail.getGroup().equals(JobManager.COLLECTOR_JOB_GROUP)) {
                    scheduler.deleteJob(jobDetail.getName(), jobDetail.getGroup());
                    logger.info("Job deleted with Name: " + jobDetail.getName()
                            + " & group name: " + jobDetail.getGroup());
                }
            }

        } catch (Exception e) {
            logger.error("SC-1007: Error while trying to remove currently executing jobs", e);
        }
    }

    /**
     * Manually delete records from All QRTZX_FIRED_TRIGGERS table
     *
     * @param scheduler
     */
    private void clearQRTZ_FIRED_TRIGGERS(Scheduler scheduler) {
        System.out.println("Starting to clear QRTZ_FIRED_TRIGGERS tables");
        logger = getLogger();
        logger.info("Starting to clear QRTZ_FIRED_TRIGGERS tables");
        Statement stmt = null;
        try {
            if (scheduler != null) {

                Properties prop = new Properties();
                prop.load(new FileInputStream("quartz.properties"));
                Object tablePrefix = prop.getProperty("org.quartz.jobStore.tablePrefix");
                logger.info("Quartz table prefix is:" + tablePrefix);
                if (tablePrefix != null) {
                    String sqlStmt = "DELETE " + tablePrefix.toString() + "FIRED_TRIGGERS";
                    logger.info("clear sql stmt: "
                            + sqlStmt);
                    Connection conn = CMTDBConnectionPool.getConnection();
                    stmt = conn.createStatement();
                    int result = stmt.executeUpdate(sqlStmt);
                    logger.info("Statement executed with result: " + result);
                    stmt.close();
                }
            }
        } catch (Exception ex) {
            logger.error("Exception occurred while trying to clear Quartz fired triggers:  "
                    + ex.getMessage(), ex);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    logger.error(e);
                }
            }
        }
    }

    /**
     * clear quartz DB tables from all jobs
     *
     * @param removeAll -
     *                  boolean to specify if all jobs or execulde recovery jobs
     */
    private void removeJobs(Scheduler scheduler, boolean removeAll) {
        try {
            if (removeAll) {
                String[] groupNames = scheduler.getJobGroupNames();
                // loop over all groups and delete all jobs
                for (int i = 0; i < groupNames.length; i++) {
                    String[] jobNames = scheduler.getJobNames(groupNames[i]);
                    for (int j = 0; j < jobNames.length; j++) {
                        scheduler.getCurrentlyExecutingJobs();
                        scheduler.deleteJob(jobNames[j], groupNames[i]);
                    }
                }
            } else {
                String[] groupNames = scheduler.getJobGroupNames();
                // loop over all groups
                for (int i = 0; i < groupNames.length; i++) { // skip deleting
                    // the job if it
                    // belonged to
                    // any of the
                    // recovery
                    // groups
                    if (groupNames[i].equals("ApplicationRecovery")
                            || groupNames[i].equals("InputRecovery")) {
                        continue;
                    }
                    String[] jobNames = scheduler.getJobNames(groupNames[i]);
                    for (int j = 0; j < jobNames.length; j++) {
                        scheduler.getCurrentlyExecutingJobs();
                        scheduler.deleteJob(jobNames[j], groupNames[i]);
                    }
                }
            }
        } catch (SchedulerException e) {
            logger.error("SC-1003: CollectorScheduler.removeJobs() - Couldn't remove old jobs " + e);
        }
    }

    /**
     * stop the current running scheduler call removeJobs then intiliaze and
     * start a new scheduler
     *
     * @param removeAll -
     *                  boolean to specify to clear all jobs or execulde recovery jobs
     */
    public void restartScheduler(boolean removeAll) throws SchedulerException {
        logger = getLogger();
        if (qrtzScheduler != null) {
            logger
                    .info("CollectorScheduler.restartScheduler() - Shutdown old Scheduler...");

            /**
             * Added by Alia.Adel on 2013.07.23
             * to refresh stuck jobs before scheduler shutdown
             */
            qrtzScheduler.pauseAll();
            clearQRTZ_FIRED_TRIGGERS(qrtzScheduler);
            logger.info("Fired triggers cleared");
            qrtzScheduler.resumeAll();
            removeAllNonDCJobs(qrtzScheduler);
            if (qrtzScheduler != null && !qrtzScheduler.isShutdown()) {
                logger.info("Starting scheduler shutdown process....");
                qrtzScheduler.shutdown(shutdownMode);
                logger.info("scheduler is now shutdown");
                CMTDBConnectionPool.closeConnection();
            }
            StdSchedulerFactory sf = new StdSchedulerFactory();
            sf.initialize();
            qrtzScheduler = sf.getScheduler();
            qrtzScheduler.start();
            logger
                    .info("CollectorScheduler.restartScheduler() - new Scheduler started ...");
        }
    }

    /**
     * Remove all recovery jobs
     *
     * @param qrtzScheduler
     */
    public void removeAllNonDCJobs(Scheduler qrtzScheduler) {
        logger = getLogger();
        try {
            if (qrtzScheduler != null) {
                String[] jobGroupNames
                        = qrtzScheduler.getJobGroupNames();
                for (String jobGroup : jobGroupNames) {
                    if (!jobGroup.equals(JobManager.COLLECTOR_JOB_GROUP)) {
                        String[] jobNames = qrtzScheduler.getJobNames(jobGroup);
                        for (String jobName : jobNames) {
                            logger.info("Removing job with name: "
                                    + jobName + ", job group: " + jobGroup);
                            qrtzScheduler.deleteJob(jobName, jobGroup);
                        }
                    }
                }
            }
        } catch (SchedulerException e) {
            logger.error(e);
        }

    }

    /**
     * Delete recovery jobs from groups ApplicationRecovery and InputRecovery
     * only if their original jobs are deleted
     */
    public void removeOldFailed() {
        try {
            logger = getLogger();
            logger.info("Remove Old failed recovery jobs");
            String[] newJobNames = qrtzScheduler.getJobNames(JobManager.COLLECTOR_JOB_GROUP);
            String[] appfailJobNames = qrtzScheduler
                    .getJobNames("ApplicationRecovery");
            String[] infailJobNames = qrtzScheduler
                    .getJobNames("InputRecovery");
            outer:
            for (int i = 0; i < appfailJobNames.length; i++) {
                for (int j = 0; j < newJobNames.length; j++) { /*
                                                                 * extract the
																 * name of the
																 * original job
																 * from the
																 * recovery job
																 * name any
																 * recovery job
																 * name is
																 * "Recovery"+original
																 * job
																 * name+timestamp
																 */
                    String originalJob = appfailJobNames[i].substring(0,
                            appfailJobNames[i].lastIndexOf("_"));
                    // if the original job is found in the new jobs then the
                    // recovery job is not deleted
                    if (originalJob.equals("Recovery" + newJobNames[j])) {
                        continue outer;
                    }
                }
                logger.info("deleting recovery job with name:" + appfailJobNames[i]);
                // if reached then the original job names is not found, the
                // recovery job is then deleted
                qrtzScheduler.deleteJob(appfailJobNames[i],
                        "ApplicationRecovery");
            }
            outer2:
            for (int i = 0; i < infailJobNames.length; i++) {
                for (int j = 0; j < newJobNames.length; j++) {
                    String originalJob = infailJobNames[i].substring(0,
                            infailJobNames[i].lastIndexOf("_"));
                    if (originalJob.equals("Recovery" + newJobNames[j])) {
                        continue outer2;
                    }
                }
                qrtzScheduler.deleteJob(infailJobNames[i], "InputRecovery");
            }
        } catch (SchedulerException e) {
            logger
                    .error("SC-1004: CollectorScheduler.removeOldFailed() - Couldn't delete old failed jobs "
                            + e);
        }
    }

    /**
     * Trigger all jobs in group ApplicationRecovery
     */
    public void runApplicationFailedJobs() {
        try {
            String[] jobNames = qrtzScheduler
                    .getJobNames("ApplicationRecovery");
            for (int j = 0; j < jobNames.length; j++) {
                qrtzScheduler.triggerJob(jobNames[j], "ApplicationRecovery");
            }
        } catch (SchedulerException e) {
            logger
                    .error("SC-1005: CollectorScheduler.runApplicationFailedJobs() - Couldn't trigger failed jobs ...");
        }
    }

    /**
     * Run the scheduler intiliaze the scheduler factory initiate first
     * scheduler thread then wait
     */

    private void saveScheduledJobs(final Scheduler scheduler) {
        try {
            String[] groupNames = scheduler.getJobGroupNames();
            String systemName, nodeName;
            if (groupNames == null)
                return;
            String filePath = "/resources/configuration/ScheduledJobs.txt";
            String homeDirectory = this.getHomeDirectory();
            logger.debug("File Payj : " + this.getHomeDirectory() + filePath);
            File file = new File(homeDirectory + filePath);
            file.deleteOnExit();
            file.createNewFile();
            BufferedWriter outputStream = new BufferedWriter(new FileWriter(file));
            List jobNamesList = new ArrayList();
            for (int i = 0; i < groupNames.length; i++) {
                //logger.debug("Group Name : "+groupNames[i]);
                String[] jobNames = scheduler.getJobNames(groupNames[i]);
                if (jobNames == null)
                    continue;
                for (int j = 0; j < jobNames.length; j++) {
                    //logger.debug("Job Name : "+jobNames[j]);
                    JobDetail jobDetail = scheduler.getJobDetail(jobNames[j], groupNames[i]);
                    //logger.debug("Job Description : "+jobDetail.getDescription());
                    if (jobDetail == null)
                        continue;
                    JobDataMap dataMap = jobDetail.getJobDataMap();
                    if (dataMap == null)
                        continue;
                    systemName = dataMap.getString("SystemName");
                    if (systemName == null)
                        continue;
                    nodeName = dataMap.getString("NodeName");
                    if (nodeName == null)
                        nodeName = "system";
                    outputStream.write(systemName + "," + nodeName);
                    outputStream.newLine();
                }
            }
            outputStream.close();
        } catch (SchedulerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    synchronized public void run() {
        try {

            StdSchedulerFactory sf = new StdSchedulerFactory();
            qrtzScheduler = sf.getScheduler();
            logger.info("Starting scheduler");
            qrtzScheduler.start();
            try{
                qrtzScheduler.resumeAll();
            }catch (Exception ex){
                logger.error("Exception thrown while trying to resumeAll Jobs", ex);
            }
            saveScheduledJobs(qrtzScheduler);
            logger.info("CollectorScheduler.run() - Scheduler started");
            // keep the application running untill notified by method
            // stopscheduler
            wait();
            logger.info("CollectorScheduler.run() - Scheduler stopped");
        } catch (Exception ex) {
            logger.error("SC-1000: CollectorScheduler.run() - Error starting scheduler" + ex);
            System.exit(1);
            // ex.printStackTrace();
        }
    }

}

class MySignalHandler implements SignalHandler {

    CollectorScheduler scheduler;

    MySignalHandler(CollectorScheduler s) {
        scheduler = s;
    }

    public void handle(Signal signal) {
        Logger logger = scheduler.getLogger();
        if (logger.isDebugEnabled()) {
            logger.debug("MySignalHandler.handle() - Signal received "
                    + signal.getName());
        }
        if ("INT".equals(signal.getName()) | "TERM".equals(signal.getName())) {
            scheduler.stopScheduler();
            CMTDBConnectionPool.closeConnection();
        } else {
            File f = new File(signal.getName() + ".txt");
            if (f.exists()) {
                if ("ALRM".equals(signal.getName())) {
                    try {
                        scheduler.restartScheduler(true);
                        logger.info("Scheduler Updated .......... ");
                    } catch (SchedulerException e) {
                        logger.error("SC-1002: MySignalHandler.handle() - Couldn't restart scheduler"
                                + e.getMessage());
                    }
                } else if ("USR2".equals(signal.getName())) {
                    try {
                        scheduler.restartScheduler(false);
                        logger.info("Scheduler Updated .......... ");
                    } catch (SchedulerException e) {
                        logger.error("SC-1002: MySignalHandler.handle() - Couldn't restart scheduler"
                                + e.getMessage());
                    }
                } else if ("TTOU".equals(signal.getName())) {
                    try {
                        scheduler.restartScheduler(false);
                        scheduler.removeOldFailed();
                        logger.info("Scheduler Updated .......... ");
                    } catch (SchedulerException e) {
                        logger.error("SC-1002: MySignalHandler.handle() - Couldn't restart scheduler"
                                + e.getMessage());
                    }
                } else if ("CHLD".equals(signal.getName())) {
                    scheduler.runApplicationFailedJobs();
                    logger.info("Finished running failed jobs .......... ");
                }
            }
            f.delete();
        }

    }

}