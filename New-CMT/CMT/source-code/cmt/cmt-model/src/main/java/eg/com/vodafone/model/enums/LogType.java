package eg.com.vodafone.model.enums;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Alia
 * Date: 3/20/13
 * Time: 3:36 PM
 * To change this template use File | Settings | File Templates.
 */
public enum LogType implements Serializable {
    ALL("All", "All"),
    WARNING("Warning", "Warning"),
    ERROR("Error", "Error");

    private String value;
    private String description;

    private LogType(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
