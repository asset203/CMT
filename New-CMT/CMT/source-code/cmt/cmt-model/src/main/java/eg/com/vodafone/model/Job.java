package eg.com.vodafone.model;

import eg.com.vodafone.model.constants.CMTConstants;
import eg.com.vodafone.model.enums.JobExecutionPeriod;
import org.hibernate.validator.constraints.NotEmpty;
import org.quartz.JobDataMap;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author marwa.goda
 * @since 4/22/13
 */
public class Job implements Serializable {

    public static final String MAP_SYSTEM_NAME = "SystemName";
    public static final String MAP_NODE_NAME = "NodeName";
    public static final String MAP_RETRY_COUNT = "RetryCount";
    public static final String MAP_RETRY_INTERVAL = "RetryInterval";
    public static final String MAP_PERIOD = "isHourly";
    public static final String MAP_DATE = "targetDate";
    public static final String MAP_NO_NODE = "NONE";
    public static final String MAP_SYSTEM_NODE = "system_nodes";
    public static final String MAP_SYSTEM_SYSTEM = "system";
    public static final String JOB_REMOTE_ADD = "ADD_JOB";
    public static final String JOB_REMOTE_UPDATE = "UPDATE_JOB";
    public static final String JOB_REMOTE_DELETE = "DELETE_JOB";
    public static final String JOB_REMOTE_FORCE = "JOB_FORCE";
    public static final String JOB_REMOTE_ERROR_KEYWORD = "FAILED_OPERATION";
    public static final String NO_MEMORY_KEYWORD = "NO_ENOUGH_JVM";
    @NotEmpty
    @Size(min = 1, max = 50)
    @Pattern(regexp = CMTConstants.JOB_NAME_REGEX)
    private String jobName;
    private String jobDescription;
    @NotEmpty
    private String systemName;
    private String systemNode;
    private String cronExpression;
    @NotNull
    private int zone;
    private JobExecutionPeriod executionPeriod;
    private String retryCount;
    private String retryInterval;

    public Job() {

    }

    public Job(String jobName, String jobDescription, String cronExpression, JobDataMap jobDataMap, int zone) {
        this.jobName = jobName;
        this.jobDescription = jobDescription;
        this.cronExpression = cronExpression;
        this.zone = zone;
        if (jobDataMap != null && !jobDataMap.isEmpty()) {
            if (jobDataMap.get(MAP_SYSTEM_NAME) != null) {
                this.systemName = jobDataMap.get(MAP_SYSTEM_NAME).toString();
            }
            if (jobDataMap.get(MAP_NODE_NAME) != null) {
                if (jobDataMap.get(MAP_NODE_NAME).equals(Job.MAP_SYSTEM_SYSTEM)) {
                    this.systemNode = null;
                } else {
                    this.systemNode = jobDataMap.get(MAP_NODE_NAME).toString();
                }
            }
            if (jobDataMap.get(MAP_PERIOD) != null
                    && jobDataMap.get(MAP_PERIOD).equals("true")) {
                this.executionPeriod = JobExecutionPeriod.HOURLY;
            } else {
                this.executionPeriod = JobExecutionPeriod.DAILY;
            }

            if (jobDataMap.get(MAP_RETRY_COUNT) != null) {
                this.retryCount = jobDataMap.get(MAP_RETRY_COUNT).toString();
            }
            if (jobDataMap.get(MAP_RETRY_INTERVAL) != null) {
                this.retryInterval = jobDataMap.get(MAP_RETRY_INTERVAL).toString();
            }
        }
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getSystemNode() {
        return systemNode;
    }

    public void setSystemNode(String systemNode) {
        this.systemNode = systemNode;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public int getZone() {
        return zone;
    }

    public void setZone(int zone) {
        this.zone = zone;
    }

    public JobExecutionPeriod getExecutionPeriod() {
        return executionPeriod;
    }

    public void setExecutionPeriod(JobExecutionPeriod executionPeriod) {
        this.executionPeriod = executionPeriod;
    }

    public String getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(String retryCount) {
        this.retryCount = retryCount;
    }

    public String getRetryInterval() {
        return retryInterval;
    }

    public void setRetryInterval(String retryInterval) {
        this.retryInterval = retryInterval;
    }
}
