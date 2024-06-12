package eg.com.vodafone.web.mvc.validator;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created with IntelliJ IDEA.
 * User: Samaa.ElKomy
 * Date: 3/26/13
 * Time: 1:53 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class RegexValidator {

    public void validate(String value, Errors errors, String fieldName, String regexPattern) {
        if (!StringUtils.hasText(value)) {
            errors.rejectValue(fieldName, "field.required");
        } else {
            Pattern pattern = Pattern.compile(regexPattern);
            Matcher matcher = pattern.matcher(value);
            if (!matcher.matches()) {
                errors.rejectValue(fieldName, "invalid.format");
            }
        }
    }

    public boolean validate(String value, Errors errors, String fieldName, String regexPattern, String messageCode, String defaultMessage) {
        Pattern pattern = Pattern.compile(regexPattern);
        Matcher matcher = pattern.matcher(value);
        if (!matcher.matches()) {
            if (StringUtils.hasLength(fieldName)) {
                errors.rejectValue(fieldName, messageCode, defaultMessage);
            } else {
                errors.rejectValue(fieldName, messageCode, defaultMessage);
            }
            return false;
        }
        return true;
    }

    public void checkValidPattern(String regexPattern, Errors errors, String fieldName) {
        try {
            Pattern.compile(regexPattern);
        } catch (PatternSyntaxException e) {
            errors.rejectValue(fieldName, "invalid.pattern");
        }
    }
}
