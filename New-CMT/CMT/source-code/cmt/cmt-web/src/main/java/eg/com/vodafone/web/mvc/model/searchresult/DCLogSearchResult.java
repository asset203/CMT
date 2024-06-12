package eg.com.vodafone.web.mvc.model.searchresult;

import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Alia.Adel
 * Date: 3/28/13
 * Time: 2:34 PM
 */
public class DCLogSearchResult extends SearchResult implements Serializable {

    private String id;
    private Date logDate;
    private String systemName;
    private String nodeName;
    private String logType;
    private String errorCode;
    private String logEntryDescription;
    private int retrialCount;
    // Selection Reason
    private String selectionReason;
    // Selection status
    private boolean selected;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getLogDate() {
        return logDate;
    }

    public void setLogDate(Date logDate) {
        this.logDate = logDate;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getLogEntryDescription() {
        return logEntryDescription;
    }

    public void setLogEntryDescription(String logEntryDescription) {
        this.logEntryDescription = logEntryDescription;
    }

    public int getRetrialCount() {
        return retrialCount;
    }

    public void setRetrialCount(int retrialCount) {
        this.retrialCount = retrialCount;
    }

    public String getSelectionReason() {
        return selectionReason;
    }

    public void setSelectionReason(String selectionReason) {
        this.selectionReason = selectionReason;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
