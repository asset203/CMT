package eg.com.vodafone.model;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Alia.Adel
 * Date: 5/15/13
 * Time: 11:40 AM
 */
public class SSHResult implements Serializable {
    private static final long serialVersionUID = 1;
    private boolean isSuccess;
    private String errorMessage;

    public SSHResult() {
    }

    public SSHResult(boolean success, String errorMessage) {
        isSuccess = success;
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
