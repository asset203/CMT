package eg.com.vodafone.model;

import eg.com.vodafone.model.enums.LogType;

import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Alia.Adel
 * Date: 3/14/13
 * Time: 9:59 AM
 * To change this template use File | Settings | File Templates.
 */
public class DCLogEntry implements Serializable {

    public static final String ALL_SYSTEMS = "All Systems";
    private static final long serialVersionUID = 1;
    private int id;
    private Date logDate;
    private String systemName;
    private String nodeName;
    private String logType;
    private String errorCode;
    private String logEntryDescription;
    private int retrialCount;

    public DCLogEntry(int id, Date logDate, String systemName,
                      String nodeName, String logType, String errorCode,
                      String logEntryDescription, int retrialCount) {
        this.id = id;
        this.logDate = logDate;
        this.systemName = systemName;
        this.nodeName = nodeName;
        this.logType = logType;
        this.errorCode = errorCode;
        this.logEntryDescription = logEntryDescription;
        this.retrialCount = retrialCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

}
