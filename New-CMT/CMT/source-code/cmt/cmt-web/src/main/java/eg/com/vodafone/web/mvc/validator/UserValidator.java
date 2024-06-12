package eg.com.vodafone.web.mvc.validator;

import eg.com.vodafone.model.User;
import eg.com.vodafone.service.impl.UserService;
import eg.com.vodafone.web.mvc.formbean.UserFormBean;
import eg.com.vodafone.web.mvc.util.CMTConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @author samaa.elkomy
 * @since Mar-13, 2013
 */
@Component
public class UserValidator implements Validator {

    @Autowired
    private RegexValidator regexValidator;
    @Autowired
    private UserService userService;

    public UserValidator() {
    }

    public boolean supports(Class<?> clazz) {
        return UserFormBean.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object modelBean, Errors errors) {
        UserFormBean user = (UserFormBean) modelBean;

        //If new user or changing password for the first login, check password is not empty
        if (user.getId() == 0 || user.getFirstLogin() == 1) {
            if (!StringUtils.hasLength(user.getPassword())) {
                errors.rejectValue("password", "field.required");
            }
            if (!StringUtils.hasLength(user.getRePassword())) {
                errors.rejectValue("rePassword", "field.required");
            }
        } else {
            if (StringUtils.hasLength(user.getPassword()) && !StringUtils.hasLength(user.getRePassword())) {
                errors.rejectValue("rePassword", "retype.password.required", "<label class='error'>Please retype password.</label>");
            }
        }


        // Validate that user name is not used in case of adding new user
        if (StringUtils.hasLength(user.getUsername())) {

            User queryUser = userService.findUserByUserName(user.getUsername());
            if (queryUser != null && queryUser.getUsername() != null && user.getId() != queryUser.getId()) {
                errors.rejectValue("username", "user.duplicate.username",
                        "<label class='error'>This user name already exists.</label>");
            }
        }

        if (StringUtils.hasText(user.getEmail())) {
            if(user.getEmail().length() > 50){
                errors.rejectValue("email", CMTConstants.EMAIL_FIELD_SIZE_INVALID);
            }

            regexValidator.validate(user.getEmail(), errors,
                    "email", CMTConstants.EMAIL_PATTERN);

        }

        // validate that user mobile is not used before
        if (StringUtils.hasLength(user.getMobile())) {
            User queryUser = userService.findUserByMobile(user.getMobile());
            if (queryUser != null && queryUser.getMobile() != null && user.getId() != queryUser.getId()) {
                errors.rejectValue("mobile", "user.duplicate.mobile",
                        "<label class='error'>This mobile number is used before.</label>");
            }
        }

        if (StringUtils.hasLength(user.getPassword())) {
            regexValidator.validate(user.getPassword(), errors, "password", CMTConstants.PASSWORD_PATTERN);

            if (user.getRePassword() != null && !user.getRePassword().equals(user.getPassword())) {
                errors.rejectValue("rePassword", "retype.password.doesnotmatch", "<label class='error'>Password doesn't match.</label>");
            }
        }


        if (user.getAuthority().equals("-1")) {
            errors.rejectValue("authority", "field.required");
        }
        
        if(user.getAppsToAccess().equals("-1")){
            errors.rejectValue("appsToAccess", "field.required");
        }
    }
}