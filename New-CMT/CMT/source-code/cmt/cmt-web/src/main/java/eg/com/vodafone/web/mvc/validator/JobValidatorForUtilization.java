/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eg.com.vodafone.web.mvc.validator;

import eg.com.vodafone.service.impl.JobExecutionService;
import eg.com.vodafone.web.mvc.formbean.JobModel;
import java.util.List;
import org.quartz.CronExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 *
 * @author mahmoud.awad
 */
@Component
public class JobValidatorForUtilization  implements Validator{

    
     private final static Logger LOGGER = LoggerFactory.getLogger(JobValidatorForUtilization.class);
     
     
    @Autowired
    private JobExecutionService jobExecutionService;
    
    @Override
    public boolean supports(Class<?> type) {
        return JobModel.class.isAssignableFrom(type);
    }

    @Override
    public void validate(Object jobModelObject, Errors errors) {
        
        JobModel jobModel = (JobModel) jobModelObject;
        
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
    }
    
}
