package eg.com.vodafone.web.mvc.controller;

import eg.com.vodafone.model.User;
import eg.com.vodafone.service.impl.UserService;
import eg.com.vodafone.web.mvc.formbean.UserFormBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Radwa Osama
 * @since 2/25/13
 */
@Controller
@RequestMapping("/index/*")
public class HomeController extends AbstractController {

	@Autowired
    private UserService userService;
    private final static  Logger logger = LoggerFactory.getLogger(HomeController.class);

	@RequestMapping(value = "home", method = RequestMethod.GET)
	public ModelAndView home() {
		logger.info("entered HomeController");

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findUserByUserName(username);
        UserFormBean userFormBean = UserManagementController.transformModelBeanToFormBean(user);

        if(user.getFirstLogin() == 1){
            ModelAndView firstLoginMV = new ModelAndView("firstLogin");
            firstLoginMV.getModel().put("user", userFormBean);
            return firstLoginMV;
        }
		return new ModelAndView("home");
	}

}
