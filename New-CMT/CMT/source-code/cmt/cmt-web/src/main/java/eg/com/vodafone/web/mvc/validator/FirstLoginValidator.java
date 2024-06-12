package eg.com.vodafone.web.mvc.validator;

import eg.com.vodafone.model.User;
import eg.com.vodafone.service.impl.UserService;
import eg.com.vodafone.web.mvc.formbean.UserFormBean;
import eg.com.vodafone.web.mvc.util.CMTConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @author samaa.elkomy
 * @since Mar-13, 2013
 */
@Component
public class FirstLoginValidator implements Validator {
  @Autowired
  private UserService userService;
  @Autowired
  private StandardPasswordEncoder encoder;
  @Autowired
  private RegexValidator regexValidator;

  public FirstLoginValidator() {
  }

  public boolean supports(Class<?> clazz) {
    return UserFormBean.class.isAssignableFrom(clazz);
  }

  public void validate(Object modelBean, Errors errors) {
    UserFormBean user = (UserFormBean) modelBean;

    if (!StringUtils.hasLength(user.getRePassword())) {
      errors.rejectValue("rePassword", "field.required", "<label class='error'>This field is required.</label>");
    }

    if (!StringUtils.hasLength(user.getPassword())) {
      errors.rejectValue("password", "field.required", "<label class='error'>This field is required.</label>");
    }

    if (!StringUtils.hasLength(user.getOldPassword())) {
      errors.rejectValue("oldPassword", "field.required", "<label class='error'>This field is required.</label>");
    }

    if (StringUtils.hasLength(user.getRePassword()) && !user.getRePassword().equals(user.getPassword())) {
      errors.rejectValue("rePassword", "retype.password.doesnotmatch", "<label class='error'>Password doesn't match.</label>");
    }

    if (StringUtils.hasLength(user.getPassword())) {
      regexValidator.validate(user.getPassword(), errors, "password", CMTConstants.PASSWORD_PATTERN);
    }

    if (StringUtils.hasLength(user.getOldPassword())) {
      User queryUser = userService.findUserByUserName(user.getUsername());

      if (queryUser == null || !encoder.matches(user.getOldPassword(), queryUser.getPassword())) {
        errors.rejectValue("oldPassword", "user.wrongpassword", "<label class='error'>Wrong password.</label>");
      }
    }
  }
}
