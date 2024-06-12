import com.itworx.vaspp.datacollection.common.ApplicationException;
import com.itworx.vaspp.datacollection.scheduler.VCleanerJob;
import com.itworx.vaspp.datacollection.scheduler.VDataCollectionJob;
import com.itworx.vaspp.datacollection.util.DataCollectionManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import static  com.itworx.vaspp.datacollection.scheduler.VDataCollectionJob.*;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: basma.alkerm
 * Date: 4/4/13
 * Time: 12:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestQuartzJobExecution {
    private static Logger logger = Logger.getLogger(TestQuartzJobExecution.class);
    public static void main(String[]args){

        try {
            DOMConfigurator.configure("D:\\Projects src\\SourceCode\\vodafone\\etc\\resources\\resources\\configuration\\log4j.xml");
            DataCollectionManager.init("D:\\Projects src\\SourceCode\\vodafone\\etc\\resources");
        } catch (ApplicationException e) {
            logger.error("failed to initiate Data Collection Manager");

            e.printStackTrace();
        }
        StdSchedulerFactory sf = new StdSchedulerFactory();
        try {
            Scheduler sched = sf.getScheduler();

            // Define job instance
            //1- daily job
            JobDetail dailyJob = new JobDetail("job1", "group1", VDataCollectionJob.class);
            dailyJob.getJobDataMap().put(SYSTEM_NAME_KEY,"EOCN_DATA_CCN");
            dailyJob.getJobDataMap().put(NODE_NAME_KEY,"system_nodes");
            dailyJob.getJobDataMap().put(RETRY_COUNT_KEY,3);
            dailyJob.getJobDataMap().put(RETRY_INTERVAL_KEY,3);
            dailyJob.getJobDataMap().put(IS_HOURLY_KEY,"false");

            //2-hourly job
            JobDetail hourlyJob = new JobDetail("job2", "group1", VDataCollectionJob.class);
            hourlyJob.getJobDataMap().put(SYSTEM_NAME_KEY,"PCM_SMS");
            hourlyJob.getJobDataMap().put(NODE_NAME_KEY,"system_nodes");   //PMC 1
            hourlyJob.getJobDataMap().put(RETRY_COUNT_KEY,3);
            hourlyJob.getJobDataMap().put(RETRY_INTERVAL_KEY,3);
            hourlyJob.getJobDataMap().put(IS_HOURLY_KEY,"true");

            //3-cleaner job
            JobDetail cleanerJob = new JobDetail("job3","group1",VCleanerJob.class);
            cleanerJob.getJobDataMap().put(IS_HOURLY_KEY,"false");

            // Define a Trigger that will fire "now"
            Trigger trigger = new SimpleTrigger("trigger1", "group1", new Date());

            // Schedule the job with the trigger
            sched.start();
            sched.scheduleJob(cleanerJob, trigger);

            logger.debug("quartz started !!");

            try {
                Thread.sleep(30L * 1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            sched.shutdown(true);

        } catch (SchedulerException e) {
            e.printStackTrace();
        }

    }
}
