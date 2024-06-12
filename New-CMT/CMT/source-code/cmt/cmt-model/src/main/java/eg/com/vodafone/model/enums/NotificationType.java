package eg.com.vodafone.model.enums;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Alia
 * Date: 3/20/13
 * Time: 3:36 PM
 * To change this template use File | Settings | File Templates.
 */
public enum NotificationType implements Serializable {
    EMAIL(1, "Email"),
    MOBILE(2, "SMS");

    private int notificationType;
    private String notificationTypeStr;

    private NotificationType(
            int notificationType, String notificationTypeStr) {
        this.notificationType = notificationType;
        this.notificationTypeStr = notificationTypeStr;
    }

    public int getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(int notificationType) {
        this.notificationType = notificationType;
    }

    public String getNotificationTypeStr() {
        return notificationTypeStr;
    }

    public void setNotificationTypeStr(String notificationTypeStr) {
        this.notificationTypeStr = notificationTypeStr;
    }

}