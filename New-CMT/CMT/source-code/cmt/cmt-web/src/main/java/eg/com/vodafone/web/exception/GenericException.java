package eg.com.vodafone.web.exception;


/**
 * Parent Exception handling class
 * for Capacity Management Tool
 * @author Alia.Adel
 *
 */
public class GenericException extends RuntimeException{
    private static final long serialVersionUID = 1;

    public GenericException() {
    }

    public GenericException(String message) {
        super(message);
    }

    public GenericException(String message, Throwable cause) {
        super(message, cause);
    }

    public GenericException(Throwable cause) {
        super(cause);
    }
}
