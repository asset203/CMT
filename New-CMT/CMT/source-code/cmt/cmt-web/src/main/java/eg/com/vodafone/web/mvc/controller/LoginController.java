package eg.com.vodafone.web.mvc.controller;


import org.slf4j.LoggerFactory;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;


/**
 * @author Radwa Osama
 * @since 2/25/13
 */
@Controller
public class LoginController extends AbstractController {

	final org.slf4j.Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	@RequestMapping("login")
	public ModelAndView login(HttpServletRequest request) {

		Object securityErrorMessage = request.getSession().getAttribute(
                WebAttributes.AUTHENTICATION_EXCEPTION);

		ModelAndView loginView = new ModelAndView("login");

		if (securityErrorMessage != null) {
            logger.info(securityErrorMessage.toString());
			loginView.addObject("exception",
                    ((Exception) securityErrorMessage).getMessage());
            if(((Exception) securityErrorMessage).getMessage().equals("Bad credentials")){
                loginView.addObject("errorLogin",
                        ((Exception) securityErrorMessage).getMessage());
            }
		}

		return loginView;
	}
}
