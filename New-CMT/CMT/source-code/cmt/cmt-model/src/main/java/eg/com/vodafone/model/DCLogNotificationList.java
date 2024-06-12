package eg.com.vodafone.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Alia.Adel
 * Date: 3/18/13
 * Time: 4:00 PM
 */
public class DCLogNotificationList implements Serializable {

    private static final long serialVersionUID = 1;

    private int id;
    private String systemName;
    private DCLogErrorCode errorCode;
    private List<DCLogContacts> contactsList;
    private String logType;

    public DCLogNotificationList() {
    }

    public DCLogNotificationList(int id, String systemName
                                    , String logType
                                    , DCLogErrorCode errorCode) {
        this.errorCode = errorCode;
        this.id = id;
        this.logType = logType;
        this.systemName = systemName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public DCLogErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(DCLogErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public List<DCLogContacts> getContactsList() {
        return contactsList;
    }

    public void setContactsList(List<DCLogContacts> contactsList) {
        this.contactsList = contactsList;
    }

}
