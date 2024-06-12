package eg.com.vodafone.model;

/**
 * Created with IntelliJ IDEA.
 * User: tarek.moustafa
 * Date: 4/10/13
 * Time: 12:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class KPI {
    private int systemNodeID;
    private String propertyName;
    private String grain;
    private long value;
    private String trafficTableName;
    private String notificationThreshold;

    public KPI(){}
    public KPI(int systemNodeID, String propertyName, String grain,  long value, String trafficTableName, String notificationThreshold)
    {
        this.systemNodeID = systemNodeID;
        this.propertyName = propertyName;
        this.grain = grain;
        this.value = value;
        this.trafficTableName = trafficTableName;
        this.notificationThreshold = notificationThreshold;
    }

    public int getSystemNodeID() {
        return systemNodeID;
    }

    public void setSystemNodeID(int systemNodeID) {
        this.systemNodeID = systemNodeID;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String PropertyName) {
        this.propertyName = PropertyName;
    }

    public String getGrain() {
        return grain;
    }

    public void setGrain(String grain) {
        this.grain = grain;
    }


    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public String getTrafficTableName() {
        return trafficTableName;
    }

    public void setTrafficTableName(String trafficTableName) {
        this.trafficTableName = trafficTableName;
    }

    public String getNotificationThreshold() {
        return notificationThreshold;
    }

    public void setNotificationThreshold(String notificationThreshold) {
        this.notificationThreshold = notificationThreshold;
    }

    @Override
    public String toString() {
        return "User{" +
                "systemNodeID=" + systemNodeID +
                ", propertyName='" + propertyName + '\'' +
                ", grain='" + grain + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

}
