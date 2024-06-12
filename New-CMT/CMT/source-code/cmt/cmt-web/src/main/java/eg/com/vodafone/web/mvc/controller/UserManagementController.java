package eg.com.vodafone.web.mvc.controller;

import eg.com.vodafone.model.Role;
import eg.com.vodafone.model.User;
import eg.com.vodafone.service.impl.RoleService;
import eg.com.vodafone.service.impl.UserService;
import eg.com.vodafone.web.exception.GenericException;
import eg.com.vodafone.web.mvc.formbean.UserFormBean;
import eg.com.vodafone.web.mvc.model.ApplicationsToAccess;
import eg.com.vodafone.web.mvc.util.CMTConstants;
import eg.com.vodafone.web.mvc.validator.FirstLoginValidator;
import eg.com.vodafone.web.mvc.validator.UserValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Alia.Adel
 * @author Samaa.Elkomy
 * @since 03/03/2013
 */
@Controller
@RequestMapping("/userManagement/*")
public class UserManagementController extends AbstractController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserValidator userValidator;

    @Autowired
    private FirstLoginValidator firstLoginValidator;

    private final static Logger logger = LoggerFactory.getLogger(UserManagementController.class);

    private ModelAndView enrichUserModelAndView(String viewName, UserFormBean userFormBean) {
        ModelAndView userModelAndView = new ModelAndView(viewName);

        List<Role> roles = roleService.getRolesList();

        //Added By Awad
        //CMT_DashBoard Configuration
        List<ApplicationsToAccess> apps = new ArrayList<ApplicationsToAccess>();
        apps.addAll(Arrays.asList(ApplicationsToAccess.values()));
        //End Awad

        userModelAndView.getModel().put("roles", roles);
        userModelAndView.getModel().put("apps", apps);
        userModelAndView.getModel().put("user", userFormBean);
        return userModelAndView;
    }

    private ModelAndView enrichUsersModelAndView(String viewName, List<User> users) {
        ModelAndView userManagementMV = new ModelAndView(viewName);
        if (users == null) {
            users = userService.getUserList();
        }
        List<UserFormBean> userFormBeans = new ArrayList<UserFormBean>(users.size());
        for (User user : users) {
            userFormBeans.add(transformModelBeanToFormBean(user));
        }

        userManagementMV.getModel().put("users", userFormBeans);
        return userManagementMV;
    }

    private boolean isCurrentUserRoleChanged(UserFormBean user) {
        boolean isCurrentUserRoleChanged = false;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUser = null;
        if (authentication != null) {
            loggedInUser = authentication.getName();
            if (loggedInUser.equalsIgnoreCase(user.getUsername())) {
                String oldRole = userService.getUserRole(loggedInUser);
                if (!oldRole.equalsIgnoreCase(user.getAuthority())) {
                    isCurrentUserRoleChanged = true;
                }
            }
        }
        return isCurrentUserRoleChanged;
    }

    @RequestMapping(value = "userManagement", method = RequestMethod.GET)
    public ModelAndView getUserManagement() {
        logger.debug("entered UserManagementController: User Management");

        return enrichUsersModelAndView("userManagement", userService.getUserList());
    }

    @RequestMapping(value = "addUser", method = RequestMethod.GET)
    public ModelAndView addUser() {
        logger.debug("entered UserManagementController: Add User");
        return enrichUserModelAndView("addUser", new UserFormBean());
    }

    @RequestMapping(value = "editUser/{userId}", method = RequestMethod.GET)
    public ModelAndView editUser(@PathVariable String userId) {
        logger.debug("entered UserManagementController: Edit User");
        return enrichUserModelAndView("editUser",
                transformModelBeanToFormBean(userService.findUserById(Integer.parseInt(userId))));
    }

    @RequestMapping(value = "deleteUser/{userId}", method = RequestMethod.GET)
    public ModelAndView deleteUser(@PathVariable String userId) throws GenericException {
        logger.debug("Delete User");
        User user = userService.findUserById(Integer.parseInt(userId));
        String loggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        if (user.getUsername().equals(loggedInUsername)) {
            logger.error("User us trying to delete himself");
            ModelAndView modelAndView = getUserManagement();
            modelAndView.addObject(CMTConstants.ERROR_MSG_KEY,
                    "<div class=\"ErrorMsg\">You cannot delete yourself !</div>");
            return modelAndView;
        }

        int result = userService.deleteUser(user);
        ModelAndView modelAndView = getUserManagement();

        if (result == 1) {
            modelAndView.addObject(CMTConstants.SUCCESS_MSG_KEY,
                    "<div class=\"SuccessMsg\">User has been deleted successfully</div>");
        } else {
            modelAndView.addObject(CMTConstants.ERROR_MSG_KEY,
                    "<div class=\"ErrorMsg\">The action has not been performed</div>");
        }

        return modelAndView;
    }

    @RequestMapping(value = "applyAddUser", method = RequestMethod.POST)
    public ModelAndView applyAddUser(@Valid @ModelAttribute("user") UserFormBean user, BindingResult errors) {

        logger.debug("Apply Add User, received user object: {}\nerrors:{}", user, errors);

        ModelAndView modelAndView;
        userValidator.validate(user, errors);
        if (errors.hasErrors()) {
            modelAndView = enrichUserModelAndView("addUser", user);
            return modelAndView;
        }

        List<Integer> resultValList = userService.addUser(transformFormBeanToModelBean(user));

        modelAndView = enrichUsersModelAndView("userManagement", userService.getUserList());

        boolean errorExist = false;
        for (int i = 0; i < resultValList.size(); i++) {
            if (!resultValList.get(i).equals(1)) {
                errorExist = true;
                break;
            }
        }

        if (!errorExist) {
            modelAndView.addObject(CMTConstants.SUCCESS_MSG_KEY,
                    "<div class=\"SuccessMsg\">User has been added successfully</div>");
        } else {
            modelAndView.addObject(CMTConstants.ERROR_MSG_KEY,
                    "<div class=\"ErrorMsg\">The action has not been performed</div>");
        }

        return modelAndView;
    }

    @RequestMapping(value = "applyEditUser", method = RequestMethod.POST)
    public ModelAndView applyEditUser(@Valid @ModelAttribute("user") UserFormBean user, BindingResult errors,
            HttpServletResponse response) {
        logger.debug("Apply Edit User, received user object: {}", user);

        ModelAndView modelAndView;
        userValidator.validate(user, errors);
        if (errors.hasErrors()) {
            modelAndView = enrichUserModelAndView("editUser", user);
            return modelAndView;
        }
        boolean isCurrentUserRoleChanged = isCurrentUserRoleChanged(user);

        List<Integer> resultValList = userService.updateUser(transformFormBeanToModelBean(user));

        modelAndView = enrichUsersModelAndView("userManagement", userService.getUserList());

        boolean errorExist = false;
        for (int i = 0; i < resultValList.size(); i++) {
            if (!resultValList.get(i).equals(1)) {
                errorExist = true;
                break;
            }
        }

        if (!errorExist) {
            modelAndView.addObject(CMTConstants.SUCCESS_MSG_KEY,
                    "<div class=\"SuccessMsg\">User has been updated successfully</div>");

        } else {
            modelAndView.addObject(CMTConstants.ERROR_MSG_KEY,
                    "<div class=\"ErrorMsg\">The action has not been performed</div>");
        }
        modelAndView.addObject("isCurrentUserRoleChanged", isCurrentUserRoleChanged);
        modelAndView.addObject("response", response);
        return modelAndView;
    }

    @RequestMapping(value = "changePassword", method = RequestMethod.POST)
    public ModelAndView changePasswordAndFistLogin(@ModelAttribute("user") UserFormBean userFormBean, BindingResult errors) {

        logger.debug("Change Password First Login, form bean is {}", userFormBean.toString());
        User oldUser = userService.findUserById(userFormBean.getId());

        userFormBean.setId(oldUser.getId());
        userFormBean.setUsername(oldUser.getUsername());
        userFormBean.setEmail(oldUser.getEmail());
        userFormBean.setMobile(oldUser.getMobile());
        userFormBean.setAuthority(oldUser.getAuthority());
        
        // Added By Awad
        // CMT_DashBoard Configuration
        userFormBean.setAppsToAccess(oldUser.getAppsToAccess());
        // End

        ModelAndView modelAndView;
        firstLoginValidator.validate(userFormBean, errors);
        if (errors.hasErrors()) {
            modelAndView = enrichUserModelAndView("firstLogin", userFormBean);
            return modelAndView;
        }
        List<Integer> resultValList = userService.updateUserForFirstLogin(transformFormBeanToModelBean(userFormBean));

        modelAndView = new ModelAndView("home");

        boolean errorExist = false;
        for (int i = 0; i < resultValList.size(); i++) {
            if (!resultValList.get(i).equals(1)) {
                errorExist = true;
                break;
            }
        }

        if (!errorExist) {
            modelAndView.addObject(CMTConstants.SUCCESS_MSG_KEY,
                    "<div class=\"SuccessMsg\">User password has been updated successfully</div>");
        } else {
            modelAndView.addObject(CMTConstants.ERROR_MSG_KEY,
                    "<div class=\"ErrorMsg\">The action has not been performed</div>");
        }

        return modelAndView;
    }

    public static User transformFormBeanToModelBean(UserFormBean userFormBean) {
        User user = new User();
        user.setId(userFormBean.getId());
        user.setUsername(userFormBean.getUsername());
        user.setPassword(userFormBean.getPassword());
        user.setEmail(userFormBean.getEmail());
        user.setMobile(userFormBean.getMobile());
        user.setAuthority(userFormBean.getAuthority());
        user.setFirstLogin(userFormBean.getFirstLogin());
        //added by Awad
        //CMT_DashBoard Configuration 
        user.setAppsToAccess(userFormBean.getAppsToAccess());
        // End 
        return user;
    }

    public static UserFormBean transformModelBeanToFormBean(User user) {
        UserFormBean userFormBean = new UserFormBean();
        userFormBean.setId(user.getId());
        userFormBean.setUsername(user.getUsername());
        userFormBean.setPassword(user.getPassword());
        userFormBean.setEmail(user.getEmail());
        userFormBean.setMobile(user.getMobile());
        userFormBean.setAuthority(user.getAuthority());
        userFormBean.setFirstLogin(user.getFirstLogin());
        //added by Awad
        //CMT_DashBoard Configuration 
        userFormBean.setAppsToAccess(user.getAppsToAccess());
        // End 
        return userFormBean;
    }

}
