package eg.com.vodafone.web.mvc.formbean;

import eg.com.vodafone.model.DCLogExternalContact;
import eg.com.vodafone.model.DCLogNotificationList;
import eg.com.vodafone.model.User;
import eg.com.vodafone.model.enums.LogType;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Alia
 * Date: 4/6/13
 * Time: 2:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class DCLogNotificationForm implements Serializable {
    private static final long serialVersionUID = 1;
    private String systemName;
    private LogType logType;
    private int errorCodeID;
    private DCLogNotificationList dcLogNotificationList;
    @Valid
    private User registeredUser;
    @Valid
    private User updatedRegisteredUser;
    @Valid
    private DCLogExternalContact externalUser;
    @Valid
    private DCLogExternalContact updatedExternalUser;
    @NotEmpty
    private String notificationType;
    private String userIDToDelete;
    private String contactListID;
    private String isRegisteredUserAction;
    private boolean noUpdate;

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

    public int getErrorCodeID() {
        return errorCodeID;
    }

    public void setErrorCodeID(int errorCodeID) {
        this.errorCodeID = errorCodeID;
    }

    public DCLogNotificationList getDcLogNotificationList() {
        return dcLogNotificationList;
    }

    public void setDcLogNotificationList(DCLogNotificationList dcLogNotificationList) {
        this.dcLogNotificationList = dcLogNotificationList;
    }

    public User getRegisteredUser() {
        return registeredUser;
    }

    public void setRegisteredUser(User registeredUser) {
        this.registeredUser = registeredUser;
    }

    public DCLogExternalContact getExternalUser() {
        return externalUser;
    }

    public void setExternalUser(DCLogExternalContact externalUser) {
        this.externalUser = externalUser;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public String getUserIDToDelete() {
        return userIDToDelete;
    }

    public void setUserIDToDelete(String userIDToDelete) {
        this.userIDToDelete = userIDToDelete;
    }

    public String getContactListID() {
        return contactListID;
    }

    public void setContactListID(String contactListID) {
        this.contactListID = contactListID;
    }

    public String getRegisteredUserAction() {
        return isRegisteredUserAction;
    }

    public void setRegisteredUserAction(String registeredUserAction) {
        isRegisteredUserAction = registeredUserAction;
    }

    public User getUpdatedRegisteredUser() {
        return updatedRegisteredUser;
    }

    public void setUpdatedRegisteredUser(User updatedRegisteredUser) {
        this.updatedRegisteredUser = updatedRegisteredUser;
    }

    public DCLogExternalContact getUpdatedExternalUser() {
        return updatedExternalUser;
    }

    public void setUpdatedExternalUser(DCLogExternalContact updatedExternalUser) {
        this.updatedExternalUser = updatedExternalUser;
    }

    public boolean isNoUpdate() {
        return noUpdate;
    }

    public void setNoUpdate(boolean noUpdate) {
        this.noUpdate = noUpdate;
    }
}
