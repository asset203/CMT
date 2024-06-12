package eg.com.vodafone.web.mvc.validator;

import eg.com.vodafone.model.Job;
import eg.com.vodafone.model.enums.JobExecutionPeriod;
import eg.com.vodafone.service.impl.DCJobService;
import eg.com.vodafone.service.impl.JobExecutionService;
import eg.com.vodafone.web.mvc.formbean.JobModel;
import eg.com.vodafone.web.mvc.model.JobAction;
import eg.com.vodafone.web.mvc.model.JobLevel;
import eg.com.vodafone.web.mvc.util.CMTConstants;
import org.quartz.CronExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

/**
 * @author Alia.Adel
 */
@Component
public class JobValidator implements Validator {

    private final static Logger LOGGER = LoggerFactory.getLogger(JobValidator.class);
    @Autowired
    private DCJobService dcJobService;
    @Autowired
    private JobExecutionService jobExecutionService;
    @Autowired
    private RegexValidator regexValidator;

    @Override
    public boolean supports(Class<?> aClass) {
        return JobModel.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object jobModelObject, Errors errors) {

        JobModel jobModel = (JobModel) jobModelObject;

        if (StringUtils.hasText(jobModel.getJob().getJobName())) {
            //Check if another job is added with the same name or if no updates were done
            Job job = dcJobService.getJobByName(jobModel.getJob().getJobName());
            if (job != null && StringUtils.hasText(job.getJobName())) {
                if (jobModel.getJobAction().equals(JobAction.SAVE_JOB.toString())
                        && job.getJobName().equalsIgnoreCase(jobModel.getJob().getJobName())) {
                    errors.rejectValue("job.jobName", "job.duplicate.jobName");
                } else if (jobModel.getJobAction().equals(JobAction.UPDATE_JOB.toString())
                        && job.getJobName().equals(jobModel.getJob().getJobName())
                        && job.getJobDescription().equals(jobModel.getJob().getJobDescription())
                        && job.getSystemName().equals(jobModel.getJob().getSystemName())
                        && job.getExecutionPeriod().equals(jobModel.getJob().getExecutionPeriod())
                        && job.getCronExpression().equals(jobModel.getJob().getCronExpression())
                        && ((jobModel.getJob().getExecutionPeriod().equals(JobExecutionPeriod.DAILY)
                            && job.getRetryCount().equals(jobModel.getJob().getRetryCount())
                            && job.getRetryInterval().equals(jobModel.getJob().getRetryInterval()))
                        || jobModel.getJob().getExecutionPeriod().equals(JobExecutionPeriod.HOURLY))
                        && (((jobModel.getJobLevel().equals(JobLevel.NODE_LEVEL.name())
                        && StringUtils.hasText(jobModel.getJob().getSystemNode())
                        && StringUtils.hasText(job.getSystemNode())
                        && job.getSystemNode().equals(jobModel.getJob().getSystemNode())))
                        || (jobModel.getJobLevel().equals(JobLevel.SYSTEM_LEVEL.name()))
                        && (!StringUtils.hasText(job.getSystemNode())
                        || job.getSystemNode().equals(Job.MAP_SYSTEM_SYSTEM)) )) {

                    errors.rejectValue("noUpdate", "job.no.change");

                }
            }
        }


        //Validate on mandatory Job description
        if (!StringUtils.hasText(jobModel.getJob().getJobDescription())) {
            errors.rejectValue("job.jobDescription", "field.required");
        } else {
            if (jobModel.getJob().getJobDescription().length() > 120) {
                errors.rejectValue("job.jobDescription", "Size.job.jobDescription");
            }
            regexValidator.validate(jobModel.getJob().getJobDescription(),
                    errors, "job.jobDescription",
                    eg.com.vodafone.model.constants.CMTConstants.JOB_DESCRIPTION_REGEX);
        }


        //Validate on System & nodes
        if (!StringUtils.hasText(jobModel.getJob().getSystemName())
                || (StringUtils.hasText(jobModel.getJob().getSystemName()))
                && jobModel.getJob().getSystemName().equals(CMTConstants.PLEASE_SELECT)) {
            errors.rejectValue("job.systemName", "field.required");
        } else {
            List<Job> jobList
                    = dcJobService.getJobsBySystemName(jobModel.getJob().getSystemName());
            if (jobList != null && !jobList.isEmpty()) {
                if (jobModel.getJobLevel().equals(JobLevel.SYSTEM_LEVEL.name())) {
                    for (int i = 0; i < jobList.size(); i++) {
                        if (!jobList.get(i).getJobName().equalsIgnoreCase(jobModel.getJob().getJobName())
                                && jobList.get(i).getSystemName().equalsIgnoreCase(jobModel.getJob().getSystemName())
                                && (jobList.get(i).getSystemNode() == null
                                || (jobList.get(i).getSystemNode() != null
                                && (jobList.get(i).getSystemNode().equals(Job.MAP_SYSTEM_SYSTEM)
                                || jobList.get(i).getSystemNode().equals(Job.MAP_SYSTEM_NODE))))) {

                            errors.rejectValue("job.systemName", "error.duplicate.system.job",
                                    new Object[]{jobList.get(i).getJobName()},
                                    "<label class='error'>Another job already exists for this system by the name {0}</label>");
                        }
                    }
                } else if (jobModel.getJobLevel().equals(JobLevel.NODE_LEVEL.name())) {
                    if (!StringUtils.hasText(jobModel.getJob().getSystemNode())) {
                        errors.rejectValue("job.systemNode", "field.required");
                    } else {
                        for (int i = 0; i < jobList.size(); i++) {
                            if (!jobList.get(i).getJobName().equalsIgnoreCase(jobModel.getJob().getJobName())
                                    && jobList.get(i).getSystemName().equalsIgnoreCase(jobModel.getJob().getSystemName())) {
                                if(jobModel.getJob().getSystemNode().equals(Job.MAP_SYSTEM_NODE)
                                        && jobList.get(i).getSystemNode() == null){
                                    errors.rejectValue("job.systemNode", "error.available.system.system.job",
                                            new Object[]{jobList.get(i).getJobName()},
                                            "<label class='error'>Another job already exists for this system with system level by the name {0}.</label>");
                                }else if(jobList.get(i).getSystemNode() != null){

                                    if(jobModel.getJob().getSystemNode().equals(Job.MAP_SYSTEM_NODE)
                                            && jobList.get(i).getSystemNode().equalsIgnoreCase(Job.MAP_SYSTEM_SYSTEM)){
                                        errors.rejectValue("job.systemNode", "error.available.system.system.job",
                                                new Object[]{jobList.get(i).getJobName()},
                                                "<label class='error'>Another job already exists for this system with system level by the name {0}.</label>");
                                    }else if ((jobList.get(i).getSystemNode().equalsIgnoreCase(jobModel.getJob().getSystemNode()))) {
                                        errors.rejectValue("job.systemNode", "error.duplicate.node.job",
                                                new Object[]{jobList.get(i).getJobName()},
                                                "<label class='error'>Another job already exists for this system by the name {0}</label>");
                                    }else if (jobList.get(i).getSystemNode().equalsIgnoreCase(Job.MAP_SYSTEM_NODE)){
                                        errors.rejectValue("job.systemNode", "error.all.node.already.defined",
                                                new Object[]{jobList.get(i).getJobName()},
                                                "<label class='error'>Another job already exists for this system by the name {0}</label>");
                                    }else if(jobModel.getJob().getSystemNode().equals(Job.MAP_SYSTEM_NODE)
                                            && StringUtils.hasText(jobList.get(i).getSystemNode())){
                                        errors.rejectValue("job.systemNode", "error.available.node.job",
                                                new Object[]{jobList.get(i).getJobName()},
                                                "<label class='error'>Another job already exists for this system with an individual node by the name {0}.</label>");
                                    }
                                }

                            }

                        }
                    }
                }
            }
        }


        //Validate on Zones
        if (jobModel.getJob().getZone() == 0) {
            errors.rejectValue("job.zone", "field.required");
        } else {
            List<String> zoneIDs = jobExecutionService.getZonesID();
            boolean validZone = false;
            for (String zone : zoneIDs) {
                try {
                    if (jobModel.getJob().getZone() == Integer.parseInt(zone)) {
                        validZone = true;
                        break;
                    }
                } catch (NumberFormatException e) {
                    LOGGER.error("failed to parse zone: " + zone, e);
                }
            }
            if (!validZone) {
                errors.rejectValue("job.zone", "field.invalid");
            }
        }
        //Validate on CRON Expression
        if (!StringUtils.hasText(jobModel.getJob().getCronExpression())) {
            errors.rejectValue("job.cronExpression", "field.required");
        } else {
            if (jobModel.getJob().getCronExpression().length() > 80) {
                errors.rejectValue("job.cronExpression", "Size.job.cronExpression");
            }

            if (!CronExpression.isValidExpression(jobModel.getJob().getCronExpression())) {
                errors.rejectValue("job.cronExpression", "invalid.format");
            }
        }


        //validate on Retry count
        if(jobModel.getJob().getExecutionPeriod() != null
                && jobModel.getJob().getExecutionPeriod().equals(JobExecutionPeriod.DAILY)){

            if (!StringUtils.hasText(jobModel.getJob().getRetryCount())) {
                errors.rejectValue("job.retryCount", "field.required");
            } else {
                int retryCount = 0;
                try {
                    retryCount = Integer.valueOf(jobModel.getJob().getRetryCount());
                    if (retryCount < 2 || retryCount > 100) {
                        errors.rejectValue("job.retryCount", "invalid.retryCount.range");
                    }

                    if (!StringUtils.hasText(jobModel.getJob().getRetryInterval())) {
                        errors.rejectValue("job.retryInterval", "field.required");
                    } else {
                        long retryInterval = 0;
                        try {
                            retryInterval = Long.valueOf(jobModel.getJob().getRetryInterval());
                            if (retryInterval < 0) {
                                errors.rejectValue("job.retryInterval", "field.invalid");
                            }else if(retryInterval < 1  || retryInterval > 1440){
                                errors.rejectValue("job.retryInterval", "invalid.retryInterval.range");
                            }
                        } catch (NumberFormatException e) {
                            LOGGER.error("Invalid retry interval", e);
                            errors.rejectValue("job.retryInterval", "field.invalid");
                        }
                    }

                } catch (NumberFormatException e) {
                    LOGGER.error("Invalid retry count", e);
                    errors.rejectValue("job.retryCount", "field.invalid");
                }
            }
        }


    }


}
