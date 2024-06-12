package eg.com.vodafone.web.mvc.formbean;

import eg.com.vodafone.model.DCLogExternalContact;
import eg.com.vodafone.model.KPINotificationList;
import eg.com.vodafone.model.User;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Alia
 * Date: 4/27/13
 * Time: 12:07 PM
 */

public class KPINotificationForm implements Serializable {
    private static final long serialVersionUID = 1;
    @NotEmpty
    private String systemName;
    @Min(value = 1)
    private int nodeID;
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
    private KPINotificationList kpiNotificationList;
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

    public int getNodeID() {
        return nodeID;
    }

    public void setNodeID(int nodeID) {
        this.nodeID = nodeID;
    }

    public User getRegisteredUser() {
        return registeredUser;
    }

    public void setRegisteredUser(User registeredUser) {
        this.registeredUser = registeredUser;
    }

    public User getUpdatedRegisteredUser() {
        return updatedRegisteredUser;
    }

    public void setUpdatedRegisteredUser(User updatedRegisteredUser) {
        this.updatedRegisteredUser = updatedRegisteredUser;
    }

    public DCLogExternalContact getExternalUser() {
        return externalUser;
    }

    public void setExternalUser(DCLogExternalContact externalUser) {
        this.externalUser = externalUser;
    }

    public DCLogExternalContact getUpdatedExternalUser() {
        return updatedExternalUser;
    }

    public void setUpdatedExternalUser(DCLogExternalContact updatedExternalUser) {
        this.updatedExternalUser = updatedExternalUser;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public KPINotificationList getKpiNotificationList() {
        return kpiNotificationList;
    }

    public void setKpiNotificationList(KPINotificationList kpiNotificationList) {
        this.kpiNotificationList = kpiNotificationList;
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

    public boolean isNoUpdate() {
        return noUpdate;
    }

    public void setNoUpdate(boolean noUpdate) {
        this.noUpdate = noUpdate;
    }
}
