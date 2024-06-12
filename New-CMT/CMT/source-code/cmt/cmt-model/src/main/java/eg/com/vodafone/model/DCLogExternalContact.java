package eg.com.vodafone.model;

import eg.com.vodafone.model.constants.CMTConstants;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Alia.Adel
 * Date: 3/18/13
 * Time: 4:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class DCLogExternalContact implements Serializable {
    private static final long serialVersionUID = 1;
    private int id;
    @NotEmpty
    @Pattern(regexp = CMTConstants.USERNAME_PATTERN)
    private String userName;
    private String eMail;
    private String mobileNum;

    public DCLogExternalContact() {
    }

    public DCLogExternalContact(int id, String userName, String eMail, String mobileNum) {
        this.id = id;
        this.userName = userName;
        this.eMail = eMail;
        this.mobileNum = mobileNum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getMobileNum() {
        return mobileNum;
    }

    public void setMobileNum(String mobileNum) {
        this.mobileNum = mobileNum;
    }
}
