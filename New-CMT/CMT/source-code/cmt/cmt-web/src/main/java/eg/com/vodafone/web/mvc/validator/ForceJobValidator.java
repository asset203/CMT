package eg.com.vodafone.web.mvc.validator;

import eg.com.vodafone.model.enums.JobExecutionPeriod;
import eg.com.vodafone.web.exception.GenericException;
import eg.com.vodafone.web.mvc.formbean.ForceJobFormBean;
import eg.com.vodafone.web.mvc.util.CMTConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: Alia.Adel
 * Date: 5/13/13
 * Time: 12:46 PM
 */
@Component
public class ForceJobValidator implements Validator {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(DCLogNotificationValidator.class);
    private static final String FORM_BEAN_FROM_HOUR = "fromHour";
    private static final String FORM_BEAN_TO_HOUR = "toHour";
    private static final String FORM_BEAN_FROM_DATE = "fromDate";
    private static final String FORM_BEAN_TO_DATE = "toDate";
    private final static SimpleDateFormat DAILY_SIMPLE_DATE_FORMAT =
            new SimpleDateFormat(CMTConstants.FORCE_JOB_DATE_PATTERN, Locale.US);

    @Override
    public boolean supports(Class<?> aClass) {
        return ForceJobFormBean.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object bean, Errors errors) {
        if (!(bean instanceof ForceJobFormBean)) {
            throw new GenericException("Invalid form bean type");
        }
        ForceJobFormBean formBean = (ForceJobFormBean) bean;
        int fromHour = 0, toHour = 0;
        if (formBean.getJob().getExecutionPeriod() != null
                && formBean.getJob().getExecutionPeriod().equals(JobExecutionPeriod.HOURLY)) {

            if (!StringUtils.hasText(formBean.getFromHour())) {
                errors.rejectValue(FORM_BEAN_FROM_HOUR,
                        CMTConstants.FIELD_REQUIRED);
            } else {
                try {
                    fromHour = Integer.parseInt(formBean.getFromHour());
                    if (fromHour < 0 || fromHour > 23) {
                        errors.rejectValue(FORM_BEAN_FROM_HOUR,
                                "invalid.fromHour.range",
                                "<label class='error'>From hour range is invalid.</label>");
                    }
                } catch (NumberFormatException ex) {
                    LOGGER.error("Error parsing from Hour");
                    errors.rejectValue(FORM_BEAN_FROM_HOUR,
                            CMTConstants.FIELD_INVALID);
                }
            }
            if (!StringUtils.hasText(formBean.getToHour())) {
                errors.rejectValue(FORM_BEAN_TO_HOUR,
                        CMTConstants.FIELD_REQUIRED);
            } else {
                try {
                    toHour = Integer.parseInt(formBean.getToHour());
                    if (toHour < 0 || toHour > 23) {
                        errors.rejectValue(FORM_BEAN_TO_HOUR,
                                "invalid.toHour.range",
                                "<label class='error'>To hour range is invalid.</label>");
                    }
                } catch (NumberFormatException ex) {
                    LOGGER.error("Error parsing to Hour");
                    errors.rejectValue(FORM_BEAN_TO_HOUR,
                            CMTConstants.FIELD_INVALID);
                }
            }

        }

        /**
         * Validate that From date is before To date
         */
        if (StringUtils.hasText(formBean.getFromDate()) && StringUtils.hasText(formBean.getToDate())) {
            Date fromDate, toDate;
            boolean isHourly = false;
            if (formBean.getJob().getExecutionPeriod() != null
                    && formBean.getJob().getExecutionPeriod().equals(JobExecutionPeriod.HOURLY)) {
                isHourly = true;
            }
            synchronized (DAILY_SIMPLE_DATE_FORMAT) {
                try {
                    fromDate = DAILY_SIMPLE_DATE_FORMAT.parse(formBean.getFromDate());
                    try {
                        toDate = DAILY_SIMPLE_DATE_FORMAT.parse(formBean.getToDate());


                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(new Date());
                        /**
                         * This line was commented by Alia.Adel on 2013.09.04 to allow force running to execute on TODAY date
                         */
                        //calendar.add(Calendar.DAY_OF_MONTH, -1);
                        if (!isHourly) {
                            if (fromDate.after(calendar.getTime())) {
                                errors.rejectValue(FORM_BEAN_FROM_DATE, "job.field.invalid.future.date");
                            }
                            if (toDate.after(calendar.getTime())) {
                                errors.rejectValue(FORM_BEAN_TO_DATE, "job.field.invalid.future.date");
                            }
                        }

                        if (toDate.before(fromDate)) {
                            errors.rejectValue(FORM_BEAN_TO_DATE, "invalid.toDate.start");
                        }
                        Calendar todayDate = Calendar.getInstance();
                        Calendar fromDateCal = Calendar.getInstance();
                        fromDateCal.setTime(fromDate);
                        boolean isTodayDate = false;
                        if (fromDateCal.get(Calendar.DAY_OF_MONTH) == todayDate.get(Calendar.DAY_OF_MONTH)
                                && fromDateCal.get(Calendar.MONTH) == todayDate.get(Calendar.MONTH)
                                && fromDateCal.get(Calendar.YEAR) == todayDate.get(Calendar.YEAR)) {
                            isTodayDate = true;
                        }


                        if (fromDate.compareTo(toDate) == 0
                                && isHourly
                                && toHour > -1 && fromHour > -1
                                && (toHour < fromHour)) {
                            errors.rejectValue(FORM_BEAN_TO_HOUR, "invalid.toHour.start");
                        }
                        if (isTodayDate && toHour >= calendar.get(Calendar.HOUR_OF_DAY)) {
                            errors.rejectValue(FORM_BEAN_TO_HOUR, "job.field.invalid.future.hour");
                        }


                    } catch (ParseException e) {
                        errors.rejectValue(FORM_BEAN_TO_DATE, "invalid.format");
                    }
                } catch (ParseException e) {
                    errors.rejectValue(FORM_BEAN_FROM_DATE, "invalid.format");
                }
            }
        }
    }
}
