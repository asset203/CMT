package eg.com.vodafone.model;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Alia
 * Date: 4/27/13
 * Time: 2:58 PM
 */
public class KPIContact implements Serializable {
    private static final long serialVersionUID = 1;
    private int id;
    private DCLogExternalContact externalContact;
    private User registeredContact;
    private String notificationType;
    private boolean isRegisteredUser;

    public KPIContact() {
    }

    public KPIContact(int id, DCLogExternalContact externalContact,
                      User registeredContact, String notificationType,
                      boolean registeredUser) {
        this.id = id;
        this.externalContact = externalContact;
        this.registeredContact = registeredContact;
        this.notificationType = notificationType;
        isRegisteredUser = registeredUser;
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

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public boolean isRegisteredUser() {
        return isRegisteredUser;
    }

    public void setRegisteredUser(boolean registeredUser) {
        isRegisteredUser = registeredUser;
    }
}
