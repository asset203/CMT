package eg.com.vodafone.web.mvc.validator;

import eg.com.vodafone.service.DataCollectionSystemServiceInterface;
import eg.com.vodafone.web.mvc.formbean.DCLogNotificationForm;
import eg.com.vodafone.web.mvc.formbean.DCLogSearchForm;
import eg.com.vodafone.web.mvc.util.CMTConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: Alia
 * Date: 3/16/13
 * Time: 3:29 PM
 */
@Component
public class DCLogSearchValidator implements Validator {

    private static final Logger logger = LoggerFactory.getLogger(DCLogSearchValidator.class);
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT =
            new SimpleDateFormat(CMTConstants.DATE_DB_PATTERN, Locale.US);


    @Autowired
    private DataCollectionSystemServiceInterface dataCollectionService;

    @Override
    public boolean supports(Class<?> aClass) {
        return DCLogSearchForm.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        logger.debug("validating DCLogSearchForm fields");

        DCLogSearchForm dcLogSearchForm = (DCLogSearchForm) o;

        logger.debug("Fields values to validate:\nSystem name:{}\nLog type:{}\nFrom date:{}\nTo date:{}",
                new Object[]{dcLogSearchForm.getSystemName(), dcLogSearchForm.getLogType(),
                        dcLogSearchForm.getFromDate(), dcLogSearchForm.getToDate()});

        if(StringUtils.hasText(dcLogSearchForm.getSystemName())){
            List<String> systemNames = dataCollectionService.listAllSystems();
            if(!systemNames.contains(dcLogSearchForm.getSystemName())){
                errors.rejectValue("systemName",
                        CMTConstants.FIELD_INVALID);
            }
        }

        if (StringUtils.hasText(dcLogSearchForm.getFromDate())) {
            boolean validFromDate = false;
            Date fromDate = null;

            try {
                synchronized (SIMPLE_DATE_FORMAT) {
                    fromDate = SIMPLE_DATE_FORMAT.parse(dcLogSearchForm.getFromDate());
                }
                validFromDate = true;
            } catch (ParseException e) {
                logger.error("ParseException is thrown while validating From Date", e.getMessage());
                errors.rejectValue("fromDate", "field.invalid.date",
                        "<label class='error'>From date is invalid format (dd-MMM-yyyy)</label>");
            }
            if (StringUtils.hasText(dcLogSearchForm.getToDate()) && fromDate != null) {
                try {
                    Date toDate;
                    synchronized (SIMPLE_DATE_FORMAT) {
                        toDate = SIMPLE_DATE_FORMAT.parse(dcLogSearchForm.getToDate());
                    }
                    if (validFromDate && toDate.before(fromDate)) {
                        errors.rejectValue("toDate", "field.invalid.date",
                                "<label class='error'>To date cannot be before From date</label>");
                    } else {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(new Date());
                        calendar.add(Calendar.DAY_OF_MONTH, -1);

                        if (fromDate.after(calendar.getTime())) {
                            errors.rejectValue("fromDate", "field.invalid.future.date",
                                    "<label class='error'>No logs will be available for this future date</label>");
                        }
                        if (toDate.after(calendar.getTime())) {
                            errors.rejectValue("toDate", "field.invalid.future.date",
                                    "<label class='error'>No logs will be available for this future date</label>");
                        }
                    }


                } catch (ParseException e) {
                    logger.error("ParseException is thrown while validating To Date", e.getMessage());
                    errors.rejectValue("toDate", "field.invalid.date",
                            "<label class='error'>To date is invalid format (dd-MMM-yyyy)</label>");
                }

            }
        }


    }
}
