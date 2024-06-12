package eg.com.vodafone.web.mvc.validator;

import eg.com.vodafone.service.impl.SystemEventService;
import eg.com.vodafone.web.mvc.formbean.SystemEventFormBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import static eg.com.vodafone.web.mvc.util.CMTConstants.ACTION_ADD_EVENT;

/**
 * @author samaa.elkomy
 * @since Mar-24, 2013
 */
@Component
public class SystemEventValidator implements Validator {

  final private static SimpleDateFormat SIMPLE_DATE_FORMAT =
    new SimpleDateFormat("dd-MMM-yyyy", Locale.US);

  @Autowired
  SystemEventService systemEventService;

  public boolean supports(Class<?> clazz) {
    return SystemEventFormBean.class.isAssignableFrom(clazz);
  }

  @Override
  public void validate(Object modelBean, Errors errors) {
    SystemEventFormBean systemEventFormBean = (SystemEventFormBean) modelBean;

    if (StringUtils.hasText(systemEventFormBean.getSelectedSystemEventDateStr())) {
      try {
        synchronized (SIMPLE_DATE_FORMAT) {
          SIMPLE_DATE_FORMAT.parse(systemEventFormBean.getSelectedSystemEventDateStr());
        }
      } catch (ParseException ex) {
        errors.rejectValue("selectedSystemEventDateStr", "field.invalid",
          "<label class='error'>Invalid Date</label>");
      }
    }

  }


  /**
   * Validate system name
   *
   * @param modelBean
   * @param errors
   */
  public void validateSystem(Object modelBean, Errors errors) {
    SystemEventFormBean systemEventFormBean = (SystemEventFormBean) modelBean;

    if (!StringUtils.hasText(systemEventFormBean.getSelectedSystem())) {
      errors.rejectValue("selectedSystem", "field.required");
    } else if (!systemEventService.isSystemExist(systemEventFormBean.getSelectedSystem())) {
      errors.rejectValue("selectedSystem", "system.not.exist");
    }
  }

  /**
   * Validate that event date not less than today.
   *
   * @param systemEventFormBean
   * @param errors
   * @param formAction
   * @return
   */
  public void validateDateValue(SystemEventFormBean systemEventFormBean, String formAction, Errors errors) {
    Calendar calendar = Calendar.getInstance();

    Date today = new GregorianCalendar(calendar.get(Calendar.YEAR),
      calendar.get(Calendar.MONTH),
      calendar.get(Calendar.DAY_OF_MONTH)
    ).getTime();

    if (systemEventFormBean.getSelectedSystemEvent() != null) {
      Date systemEventDate = systemEventFormBean.getSelectedSystemEvent().getDateTime();
      if (systemEventDate != null) {
        if (systemEventDate.before(today) && ACTION_ADD_EVENT.equalsIgnoreCase(formAction)) {
          errors.rejectValue("selectedSystemEventDateStr", "date.less.selectedSystemEventDateStr",
            "<label class='error'>selected date is less than current date</label>");
        }

      }

    }
  }


}