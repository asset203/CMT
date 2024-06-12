package com.itworx.vaspp.datacollection.scheduler;

import com.itworx.vaspp.datacollection.util.PropertyReader;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import static  com.itworx.vaspp.datacollection.scheduler.VDataCollectionJob.*;

/**
 * Created with IntelliJ IDEA.
 * User: basma.alkerm
 * Date: 4/4/13
 * Time: 1:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class VCleanerJob  implements Job {

    private Logger logger;
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger = Logger.getLogger(VCleanerJob.class);
       // try{
                JobDataMap jobDataMap = null;
                jobDataMap = context.getJobDetail().getJobDataMap();
                logger.debug("VCleanerJob - Loading parameters from jobDataMap");
                boolean isHourly;
                try{
                    isHourly = IS_HOURLY_KEY_TRUE.equals(jobDataMap.getString(IS_HOURLY_KEY));
                }catch (Exception e){
                    isHourly =false;
                }
                if(isHourly){
                    cleanHourlyJobs();
                }
                else
                {
                    cleanDailyJobs();
                }
                logger.debug("VCleanerJob.execute() finished");
      /* } catch(Exception e){
            logger.error("VCleanerJob.execute() - Failed: Exception Occurred ",e);
            logger.error("==== Job Thread wil Exit ===");
            System.exit(1);
       } */
    }

    private void cleanDailyJobs(){

        try {
            logger.debug("VCleanerJob.cleanDailyJobs() start");
            String filesPath = PropertyReader.getImportedFilesPath();
            File importFolder = new File(filesPath);
            deletedFolder(importFolder);
            String convertedFilesPath = PropertyReader.getConvertedFilesPath();
            File covertedFolder = new File(convertedFilesPath);
            deletedFolder(covertedFolder);
        } catch (Exception ex) {
            logger.error("CLR-1000: VCleanerJob.cleanDailyJobs() Exception- message:"
                    + ex.getMessage());
        }
        logger.debug("VCleanerJob.cleanDailyJobs() finish");
    }

    private void cleanHourlyJobs(){

        try {
            logger.debug("VCleanerJob.cleanHourlyJobs() start");
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
            logger.error("CLR-1000: VCleanerJob.cleanHourlyJobs() Exception- message:"
                    + ex.getMessage());
        }
        logger.debug("VCleanerJob.cleanHourlyJobs() finish");
    }


    private void deletedFolder(File folder) {
        if (folder == null)
            return;
        if (folder.isFile()) {
            folder.delete();
            return;
        }
        File[] folderFiles = folder.listFiles();

        for (int i = 0; i < folderFiles.length; i++) {
            deletedFolder(folderFiles[i]);
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
