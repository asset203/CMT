package eg.com.vodafone.model;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Alia.Adel
 * Date: 3/18/13
 * Time: 4:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class DCLogContacts implements Serializable {
    private static final long serialVersionUID = 1;
    private int id;
    private DCLogExternalContact externalContact;
    private User registeredContact;
    private String notificationType;
    private boolean isRegisteredUser;
    private int notificationListID;


    public DCLogContacts() {
    }

    public DCLogContacts(int id, String notificationType, boolean registeredUser,
                         DCLogExternalContact externalContact, User registeredContact,
                         int notificationListID) {

        this.id = id;
        this.externalContact = externalContact;
        this.registeredContact = registeredContact;
        this.notificationType = notificationType;
        this.isRegisteredUser = registeredUser;
        this.notificationListID =notificationListID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public DCLogExternalContact getExternalContact() {
        return externalContact;
    }

    public void setExternalContact(DCLogExternalContact externalContact) {
        this.externalContact = externalContact;
    }

    public User getRegisteredContact() {
        return registeredContact;
    }

    public void setRegisteredContact(User registeredContact) {
        this.registeredContact = registeredContact;
    }

    public boolean isRegisteredUser() {
        return isRegisteredUser;
    }

    public void setRegisteredUser(boolean registeredUser) {
        isRegisteredUser = registeredUser;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public int getNotificationListID() {
        return notificationListID;
    }

    public void setNotificationListID(int notificationListID) {
        this.notificationListID = notificationListID;
    }
}
