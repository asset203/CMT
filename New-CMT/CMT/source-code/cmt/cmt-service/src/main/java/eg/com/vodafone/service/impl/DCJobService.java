package eg.com.vodafone.service.impl;

import eg.com.vodafone.dao.DCJobDao;
import eg.com.vodafone.model.Job;
import eg.com.vodafone.model.JobExecutionZone;
import eg.com.vodafone.model.ZoneUtilization;
import eg.com.vodafone.model.SSHResult;
import eg.com.vodafone.model.enums.JobExecutionPeriod;
import eg.com.vodafone.service.BusinessException;
import eg.com.vodafone.service.utils.SSHUtilityService;
import java.text.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import org.quartz.CronExpression;

/**
 * Created with IntelliJ IDEA. User: Alia.Adel Date: 5/10/13 Time: 7:33 PM
 */
@Service
@Transactional(readOnly = true)
public class DCJobService {

    private final static Logger LOGGER = LoggerFactory.getLogger(DCJobService.class);
    private final static String JOB_SHELL_SCRIPT_NAME = "JobManager.sh";
    private static Map<String, JobExecutionZone> jobExecutionZones;
    @Autowired
    private DCJobDao dcJobDao;
    @Autowired
    private JobExecutionService jobExecutionService;

    /**
     * Retrieve all Job Names from Quartz "QRTZ_JOB_DETAILS" table Sorted
     *
     * @param sortType
     * @param startIndex
     * @param endIndex
     * @return
     */
    public List<String> getAllJobNameList(String keyWord, String sortType, int startIndex, int endIndex) {
        return dcJobDao.getJobsByIndex(
                jobExecutionService.getZonesQuartzPrefixes(), keyWord, sortType, startIndex, endIndex);
    }

    /**
     * Get All jobs' count
     *
     * @return
     */
    public int getAllJobNameCount(String keyWord) {
        return dcJobDao.getAllJobNameCount(
                jobExecutionService.getZonesQuartzPrefixes(), keyWord);
    }

    /**
     * Search for a job by a search key
     *
     * @param searchKey search key
     * @return List of jobs returned from search query
     */
    public List<String> searchForJobByName(String searchKey) {
        return dcJobDao.searchForJobByName(
                jobExecutionService.getZonesQuartzPrefixes(), searchKey);
    }

    /**
     * Load job data from Quartz tables using its job name
     *
     * @param jobName job name to search for
     * @return full job Object
     */
    public Job getJobByName(String jobName) {
        return dcJobDao.getJobByName(
                jobExecutionService.getZonesQuartzPrefixes(), jobName);
    }

    /**
     * Add a new job to Quartz scheduler via SSH remote connection
     *
     * @param job Job details to be added
     * @return operation result
     */
    public SSHResult addJob(Job job) {
        SSHResult sshResult;
        JobExecutionZone jobExecutionZone;

        //TODO Cache these values
        if (jobExecutionZones == null || jobExecutionZones.isEmpty()) {
            jobExecutionZones = jobExecutionService.getExecutionZones();
        }
        if (job != null) {
            LOGGER.info("Job details:\nName:{}\nDesc:{}\nSystem:{}\nNode:{}\nCron:{}\nRetry:{}\nInterval:{}\nperiod:{}",
                    new Object[]{job.getJobName(), job.getJobDescription(), job.getSystemName(), job.getSystemNode(),
                        job.getCronExpression(), job.getRetryCount(), job.getRetryInterval(),
                        job.getExecutionPeriod().name()});

            if (!StringUtils.hasText(job.getSystemNode())) {
                job.setSystemNode(Job.MAP_NO_NODE);
            }

            if (!StringUtils.hasText(job.getRetryInterval())) {
                job.setRetryInterval("0");
            }

            jobExecutionZone = jobExecutionZones.get(String.valueOf(job.getZone()));

            sshResult = executeFutureTask(jobExecutionZone.getIp(),
                    String.valueOf(jobExecutionZone.getPort()),
                    jobExecutionZone.getUserName(), jobExecutionZone.getPassword(),
                    jobExecutionZone.getDcPath(),
                    JOB_SHELL_SCRIPT_NAME,
                    Job.JOB_REMOTE_ADD,
                    job.getJobName(), job.getJobDescription(), job.getSystemName(), job.getSystemNode(),
                    job.getCronExpression(), job.getExecutionPeriod().name(), job.getRetryCount(),
                    job.getRetryInterval());

        } else {
            throw new BusinessException("Job object is null");
        }

        return sshResult;
    }

    /**
     * Update job in Quartz scheduler via SSH remote connection
     *
     * @param job Job details to be updated
     * @return operation result
     */
    public SSHResult updateJob(Job job) {
        SSHResult sshResult;
        JobExecutionZone jobExecutionZone;

        //TODO Cache these values
        if (jobExecutionZones == null || jobExecutionZones.isEmpty()) {
            jobExecutionZones = jobExecutionService.getExecutionZones();
        }
        if (job != null) {
            LOGGER.info("Job details:\nName:{}\nDesc:{}\nSystem:{}\nNode:{}\nCron:{}\nRetry:{}\nInterval:{}\nperiod:{}",
                    new Object[]{job.getJobName(), job.getJobDescription(), job.getSystemName(), job.getSystemNode(),
                        job.getCronExpression(), job.getRetryCount(), job.getRetryInterval(),
                        job.getExecutionPeriod().name()});

            if (!StringUtils.hasText(job.getSystemNode())) {
                job.setSystemNode(Job.MAP_NO_NODE);
            }

            if (!StringUtils.hasText(job.getRetryInterval())) {
                job.setRetryInterval("0");
            }

            jobExecutionZone = jobExecutionZones.get(String.valueOf(job.getZone()));

            sshResult = executeFutureTask(jobExecutionZone.getIp(),
                    String.valueOf(jobExecutionZone.getPort()),
                    jobExecutionZone.getUserName(), jobExecutionZone.getPassword(),
                    jobExecutionZone.getDcPath(),
                    JOB_SHELL_SCRIPT_NAME,
                    Job.JOB_REMOTE_UPDATE,
                    job.getJobName(), job.getJobDescription(), job.getSystemName(), job.getSystemNode(),
                    job.getCronExpression(), job.getExecutionPeriod().name(), job.getRetryCount(),
                    job.getRetryInterval());

        } else {
            throw new BusinessException("Job object is null");
        }

        return sshResult;
    }

    /**
     * Remove job from Quartz scheduler
     *
     * @param jobName job name to be removed from Quartz scheduler
     * @return operation result
     */
    public SSHResult deleteJob(String jobName) {

        SSHResult sshResult;
        JobExecutionZone jobExecutionZone;
        Job job = dcJobDao.getJobByName(jobExecutionService.getZonesQuartzPrefixes(),
                jobName);

        //TODO Cache these values
        if (jobExecutionZones == null || jobExecutionZones.isEmpty()) {
            jobExecutionZones = jobExecutionService.getExecutionZones();
        }

        if (job != null) {
            LOGGER.info("Job details:\nName:{}\nDesc:{}\nSystem:{}\nNode:{}\nCron:{}\nRetry:{}\nInterval:{}\nperiod:{}",
                    new Object[]{job.getJobName(), job.getJobDescription(), job.getSystemName(), job.getSystemNode(),
                        job.getCronExpression(), job.getRetryCount(), job.getRetryInterval(),
                        job.getExecutionPeriod().name()});

            jobExecutionZone = jobExecutionZones.get(String.valueOf(job.getZone()));

            sshResult = executeFutureTask(jobExecutionZone.getIp(),
                    String.valueOf(jobExecutionZone.getPort()),
                    jobExecutionZone.getUserName(), jobExecutionZone.getPassword(),
                    jobExecutionZone.getDcPath(),
                    JOB_SHELL_SCRIPT_NAME,
                    Job.JOB_REMOTE_DELETE,
                    job.getJobName());
        } else {
            throw new BusinessException("Job object is null");
        }

        return sshResult;
    }

    /**
     * Delete Multiple Jobs on a specific Zone
     *
     * @param jobList
     * @param jobExecutionZone
     * @return
     */
    private SSHResult executeAllJobsDeletion(List<Job> jobList, JobExecutionZone jobExecutionZone) {
        SSHResult sshResult = new SSHResult();
        sshResult.setSuccess(false);
        sshResult.setErrorMessage(" ");
        if (jobList != null && !jobList.isEmpty() && jobExecutionZone != null) {
            LOGGER.info("List of Jobs to delete on Zone:{}", jobExecutionZone.getIp());
            List<String> jobNameList = new ArrayList<String>();
            for (Job job : jobList) {
                jobNameList.add(job.getJobName());
                LOGGER.info("Job details:\nName:{}\nDesc:{}\nSystem:{}\nNode:{}\nCron:{}\nRetry:{}\nInterval:{}\nperiod:{}",
                        new Object[]{job.getJobName(), job.getJobDescription(), job.getSystemName(), job.getSystemNode(),
                            job.getCronExpression(), job.getRetryCount(), job.getRetryInterval(),
                            job.getExecutionPeriod().name()});
            }

            sshResult = executeFutureTask(jobExecutionZone.getIp(),
                    String.valueOf(jobExecutionZone.getPort()),
                    jobExecutionZone.getUserName(), jobExecutionZone.getPassword(),
                    jobExecutionZone.getDcPath(),
                    JOB_SHELL_SCRIPT_NAME,
                    Job.JOB_REMOTE_DELETE,
                    jobNameList.toString());
        } else {
            throw new BusinessException("JobList is empty or job execution zone");
        }
        return sshResult;
    }

    /**
     * Retrieve all jobs associated to a system
     *
     * @param systemName
     * @return
     */
    public List<Job> getJobsBySystemName(String systemName) {
        List<Job> systemJobs = new ArrayList<Job>();
        List<Job> jobList
                = dcJobDao.getAllJobs(jobExecutionService.getZonesQuartzPrefixes());
        LOGGER.debug("retrieved all available jobs");
        if (jobList != null && !jobList.isEmpty()) {
            LOGGER.debug("All Quartz job count:{}", jobList.size());
            for (Job job : jobList) {
                if (StringUtils.hasText(job.getSystemName())
                        && job.getSystemName().equals(systemName)) {
                    systemJobs.add(job);
                }
            }
        }

        return systemJobs;
    }

    /**
     * Retrieve all jobs associated with a system node
     *
     * @param systemName
     * @param nodeName
     * @return
     */
    public List<Job> getJobsBySystemNode(String systemName, String nodeName) {
        List<Job> systemNodeJobs = new ArrayList<Job>();
        List<Job> jobList
                = dcJobDao.getAllJobs(jobExecutionService.getZonesQuartzPrefixes());
        LOGGER.debug("retrieved all available jobs");
        if (jobList != null && !jobList.isEmpty()) {
            LOGGER.debug("All Quartz job count:{}", jobList.size());
            for (Job job : jobList) {
                if (StringUtils.hasText(job.getSystemName())
                        && job.getSystemName().equals(systemName)
                        && StringUtils.hasText(nodeName)
                        && job.getSystemNode().equals(nodeName)) {
                    systemNodeJobs.add(job);
                }
            }
        }

        return systemNodeJobs;
    }

    /**
     * Filter the job list according to the available zones & send the delete
     * request accordingly
     *
     * @param jobList
     * @return
     */
    private List<Integer> deleteJobsByZone(List<Job> jobList) {

        List<Job> filteredJobList = new ArrayList<Job>();
        List<Integer> result = new ArrayList<Integer>();
        SSHResult sshResult;

        //TODO Cache these values
        if (jobExecutionZones == null || jobExecutionZones.isEmpty()) {
            jobExecutionZones = jobExecutionService.getExecutionZones();
        }

        for (Map.Entry<String, JobExecutionZone> entry : jobExecutionZones.entrySet()) {
            int zone = Integer.parseInt(entry.getKey());
            LOGGER.debug("Collecting list for zone:{} ", zone);

            for (Job job : jobList) {
                if (job.getZone() == zone) {
                    filteredJobList.add(job);
                }
            }
            if (filteredJobList != null && !filteredJobList.isEmpty()) {
                sshResult = executeAllJobsDeletion(filteredJobList, entry.getValue());
                if (sshResult != null) {
                    if (sshResult.isSuccess()) {
                        result.add(1);
                    } else {
                        result.add(0);
                    }
                    LOGGER.info("Deletion result for zone:{} is '{}' with error message: {}",
                            new Object[]{zone, sshResult.isSuccess(), sshResult.getErrorMessage()});
                }
            }

            filteredJobList = new ArrayList<Job>();
        }

        return result;

    }

    /**
     * get Zone Utilization for specific Zone
     *
     * @param jobModel
     * @return
     */
    public List<ZoneUtilization> getZoneUtilization(String cronExpression) {

        List<ZoneUtilization> zonesUtilization = new ArrayList<ZoneUtilization>();

        try {

            if (jobExecutionZones == null || jobExecutionZones.isEmpty()) {
                jobExecutionZones = jobExecutionService.getExecutionZones();
            }

            CronExpression cron = new CronExpression(cronExpression);

            Long nextValidTimeAfter = cron.getNextValidTimeAfter(new Date()).getTime();

            zonesUtilization = dcJobDao.getZoneUtilization(jobExecutionZones ,nextValidTimeAfter);

        } catch (ParseException pEx) {
            LOGGER.error("Parse Exception in Cron Excepression ", pEx);
            LOGGER.debug("Parse Exception in Cron Excepression ", pEx);
            throw new BusinessException("Parse Exception in Cron Excepression", pEx);
        } catch (Exception ex) {
            LOGGER.error(" Exception Occured in getZoneUtilization  ", ex);
            LOGGER.debug(" Exception Occured in getZoneUtilization ", ex);
            throw new BusinessException(" Exception Occured in getZoneUtilization ", ex);
        }
        
        return zonesUtilization;

    }

    /**
     * Delete All jobs associated with the given System name
     *
     * @param systemName
     * @return
     */
    public List<Integer> deleteAllSystemJobs(String systemName) {
        List<Job> jobList = getJobsBySystemName(systemName);
        return deleteJobsByZone(jobList);
    }

    /**
     * Delete All jobs associated with the given System & node
     *
     * @param systemName
     * @param nodeName
     * @return
     */
    public List<Integer> deleteAllSystemNodeJobs(String systemName, String nodeName) {
        List<Job> jobList = getJobsBySystemNode(systemName, nodeName);
        return deleteJobsByZone(jobList);
    }

    /**
     * Force job execution by adding a simple trigger in quartz with Now date.
     *
     * @param job
     * @param fromDate
     * @param toDate
     * @return
     */
    public SSHResult forceJobExecution(Job job, String fromDate,
            String toDate) {
        SSHResult sshResult = new SSHResult();
        JobExecutionZone jobExecutionZone;

        //TODO Cache these values
        if (jobExecutionZones == null || jobExecutionZones.isEmpty()) {
            jobExecutionZones = jobExecutionService.getExecutionZones();
        }

        if (job != null) {
            jobExecutionZone = jobExecutionZones.get(String.valueOf(job.getZone()));
            LOGGER.info("Forcing job for job with name:{} and parameters, FromDate:{}, ToDate:{}, FromHr:{}, ToHr:{}",
                    new Object[]{job.getJobName(), fromDate, toDate});
            LOGGER.debug("Zone details:\nZone Name:{}\nIP:{}:{}\nuser:{}\npassword:{}\npath:{}\nQuartz prefix:{}",
                    new Object[]{jobExecutionZone.getName(), jobExecutionZone.getIp(), jobExecutionZone.getPort(),
                        jobExecutionZone.getUserName(), jobExecutionZone.getPassword(), jobExecutionZone.getDcPath(),
                        jobExecutionZone.getQrtzTblPrefix()});

            sshResult = executeFutureTask(jobExecutionZone.getIp(),
                    String.valueOf(jobExecutionZone.getPort()),
                    jobExecutionZone.getUserName(), jobExecutionZone.getPassword(),
                    jobExecutionZone.getDcPath(),
                    JOB_SHELL_SCRIPT_NAME,
                    Job.JOB_REMOTE_FORCE, jobExecutionZone.getDcPath(), job.getJobName(), fromDate, toDate);

        }

        return sshResult;

    }

    /**
     * Executes Future Task
     *
     * @param macIP
     * @param port
     * @param userName
     * @param password
     * @param shellScriptPath
     * @param shellScriptName
     * @param params
     * @return
     */
    private SSHResult executeFutureTask(String macIP, String port, String userName,
            String password, String shellScriptPath,
            String shellScriptName, String... params) {
        SSHResult sshResult = new SSHResult();
        ExecutorService executor = Executors.newFixedThreadPool(1);
        FutureTask<SSHResult> futureTask = new FutureTask<SSHResult>(new SSHUtilityService(
                macIP, port, userName, password,
                shellScriptPath, shellScriptName, params
        ));
        boolean exitExecution = false;
        try {
            executor.execute(futureTask);
            try {
                futureTask.get(SSHUtilityService.TIMEOUT,
                        TimeUnit.MILLISECONDS);
            } catch (TimeoutException e) {
                LOGGER.error("Task took more than than the configured timeout:" + SSHUtilityService.TIMEOUT, e);
                sshResult.setSuccess(false);
                sshResult.setErrorMessage(SSHUtilityService.TIMEOUT_MESSAGE);
                exitExecution = true;
            }

            if (futureTask.isDone()) {
                sshResult = futureTask.get();
                LOGGER.debug("Task on machine {} with parameters {} is now completed.", macIP, params);
            }
        } catch (InterruptedException e) {
            LOGGER.error("InterruptedException", e);
            throw new BusinessException("InterruptedException", e);
        } catch (ExecutionException e) {
            LOGGER.error("ExecutionException", e);
            throw new BusinessException("ExecutionException", e);
        } finally {
            executor.shutdownNow();
        }

        //Close thread
        if (exitExecution) {
            futureTask.cancel(true);
        }

        return sshResult;
    }

}
