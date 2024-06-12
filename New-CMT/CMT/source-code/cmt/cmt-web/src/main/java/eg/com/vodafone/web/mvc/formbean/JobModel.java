package eg.com.vodafone.web.mvc.formbean;

import eg.com.vodafone.model.Job;
import eg.com.vodafone.web.mvc.model.searchcriteria.SearchCriteria;
import eg.com.vodafone.web.mvc.util.CMTConstants;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * @Author Alia.Adel
 */
public class JobModel extends SearchCriteria implements Serializable {

    @Valid
    private Job job;
    private String jobAction;
    private String jobLevel;
    private boolean noUpdate;


    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public String getJobAction() {
        return jobAction;
    }

    public void setJobAction(String jobAction) {
        this.jobAction = jobAction;
    }

    public String getJobLevel() {
        return jobLevel;
    }

    public void setJobLevel(String jobLevel) {
        this.jobLevel = jobLevel;
    }

    public boolean isNoUpdate() {
        return noUpdate;
    }

    public void setNoUpdate(boolean noUpdate) {
        this.noUpdate = noUpdate;
    }
}
