package eg.com.vodafone.web.mvc.controller;

import eg.com.vodafone.service.BusinessException;
import eg.com.vodafone.web.exception.GenericException;
import eg.com.vodafone.web.security.UserContextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.util.GregorianCalendar;
import java.util.UUID;

import static eg.com.vodafone.service.BusinessException.NAME_USED_BY_OTHER_DB_OBJECT;

/**
 * @author Radwa Osama
 * @since 2/25/13
 */
@Controller
public abstract class AbstractController {

    private final static org.slf4j.Logger logger = org.slf4j.LoggerFactory
			.getLogger(AbstractController.class);

	@Autowired
	private UserContextService userContextService;

	@ExceptionHandler(AccessDeniedException.class)
	public ModelAndView handleAccessDeniedException(
			AccessDeniedException exception) {
        return logErrorMessage(exception, "/error403");
	}

	@ExceptionHandler(RuntimeException.class)
	public ModelAndView handleRuntimeException(RuntimeException exception) {
        return logErrorMessage(exception, "/error");
	}

	@ExceptionHandler(GenericException.class)
	public ModelAndView handleGenericException(GenericException exception) {
        return logErrorMessage(exception, "/error");
	}

    @ExceptionHandler(SQLException.class)
    public ModelAndView handleSQLException(SQLException exception) {
        throw new GenericException("An error has occurred", exception);
    }


    /**
     * Log error message
     *
     * @param exception
     */
    private ModelAndView logErrorMessage(RuntimeException exception, String modelName){

        String errorCode = UUID.randomUUID().toString();
        String msg=null;
        StringBuffer errorMsg = new StringBuffer(300);
        errorMsg.append("========================================================================\n[").append(errorCode)
                .append("][").append(userContextService.getCurrentUser()).append("][").
                append(new GregorianCalendar().getTime().toString() + ']');

        logger.error(errorMsg.toString(), exception);


        //***************** added to display a certeine message for the case of "ORA-00955" only ,With minimum code change
            if(exception.getCause() instanceof SQLSyntaxErrorException)
            {
                SQLSyntaxErrorException sqlException =(SQLSyntaxErrorException)exception.getCause();
                if(sqlException.getMessage()!= null &&
                        sqlException.getMessage().contains("ORA-00955: name is already used by an existing object")){
                    msg="name is already used by an existing object in database schema";
                }

            }
        //****************

        return new ModelAndView(modelName).
                addObject("errorCode", errorCode)
                .addObject("errorMsg",msg);

    }
}
