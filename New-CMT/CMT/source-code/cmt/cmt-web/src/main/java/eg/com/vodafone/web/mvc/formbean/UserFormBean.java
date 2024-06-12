package eg.com.vodafone.web.mvc.formbean;

import eg.com.vodafone.web.mvc.util.CMTConstants;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Samaa.ElKomy
 * Date: 3/7/13
 * Time: 10:38 AM
 * To change this template use File | Settings | File Templates.
 */
public class UserFormBean implements Serializable {
    private int id;
    @NotEmpty
    @Pattern(regexp = CMTConstants.USERNAME_PATTERN)
    private String username;
    private String password;
    private int firstLogin;
    private String email;
    @Pattern(regexp = CMTConstants.MOBILE_PATTERN)
    private String mobile;
    @NotEmpty
    private String authority;
    private String rePassword;
    private String oldPassword;
    
    private String appsToAccess;

    public UserFormBean() {
    }

    public UserFormBean(int id, String username, String password, int firstLogin, String email, String mobile) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.firstLogin = firstLogin;
        this.email = email;
        this.mobile = mobile;

    }

    public UserFormBean(int id, String username, String email, String mobile) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.mobile = mobile;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getFirstLogin() {
        return firstLogin;
    }

    public void setFirstLogin(int firstLogin) {
        this.firstLogin = firstLogin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getRePassword() {
        return rePassword;
    }

    public void setRePassword(String rePassword) {
        this.rePassword = rePassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getAppsToAccess() {
        return appsToAccess;
    }

    public void setAppsToAccess(String appsToAccess) {
        this.appsToAccess = appsToAccess;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", firstLogin=" + firstLogin +
                ", email='" + email + '\'' +
                ", appsToAccess ='" + appsToAccess + '\'' +
                ", role=" + authority +
                '}';
    }
}
