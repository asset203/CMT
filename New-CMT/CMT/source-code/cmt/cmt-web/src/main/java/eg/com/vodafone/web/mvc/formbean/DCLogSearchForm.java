package eg.com.vodafone.web.mvc.formbean;


import eg.com.vodafone.model.enums.LogType;
import eg.com.vodafone.web.mvc.model.searchcriteria.SearchCriteria;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Alia
 * Date: 3/16/13
 * Time: 3:01 PM
 */
public class DCLogSearchForm extends SearchCriteria implements Serializable {

    private static final long serialVersionUID = 1;
    @NotEmpty
    private String systemName;
    @NotNull
    private LogType logType;
    @NotEmpty
    private String fromDate;
    @NotEmpty
    private String toDate;

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public LogType getLogType() {
        return logType;
    }

    public void setLogType(LogType logType) {
        this.logType = logType;
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
}
