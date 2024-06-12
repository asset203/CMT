package eg.com.vodafone.web.mvc.formbean;

import eg.com.vodafone.model.Job;
import eg.com.vodafone.web.mvc.util.CMTConstants;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Alia
 * Date: 5/13/13
 * Time: 7:33 AM
 * To change this template use File | Settings | File Templates.
 */
public class ForceJobFormBean implements Serializable {

    private static final long serialVersionUID = 1;
    @Valid
    private Job job;
    @NotEmpty
    private String fromDate;
    @NotEmpty
    private String toDate;
    private String fromHour;
    private String toHour;

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getFromHour() {
        return fromHour;
    }

    public void setFromHour(String fromHour) {
        this.fromHour = fromHour;
    }

    public String getToHour() {
        return toHour;
    }

    public void setToHour(String toHour) {
        this.toHour = toHour;
    }
}
