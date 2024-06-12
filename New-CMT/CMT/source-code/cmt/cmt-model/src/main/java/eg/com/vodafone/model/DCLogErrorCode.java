package eg.com.vodafone.model;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Alia.Adel
 * Date: 3/18/13
 * Time: 4:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class DCLogErrorCode implements Serializable {
    private static final long serialVersionUID = 1;
    private int id;
    private String errorCode;
    private String description;
    private String shortDesc;

    public DCLogErrorCode() {
    }

    public DCLogErrorCode(int id, String errorCode, String description, String shortDesc) {
        this.id = id;
        this.errorCode = errorCode;
        this.description = description;
        this.shortDesc = shortDesc;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }
}
