package eg.com.vodafone.web.mvc.validator;

import eg.com.vodafone.service.BusinessException;
import eg.com.vodafone.service.DataCollectionServiceInterface;
import eg.com.vodafone.web.exception.GenericException;
import eg.com.vodafone.web.mvc.controller.DataCollectionUtil;
import eg.com.vodafone.web.mvc.formbean.dataCollection.DataCollectionType;
import eg.com.vodafone.web.mvc.formbean.dataCollection.DataCollectionTypeFormBean;
import eg.com.vodafone.web.mvc.util.CMTConstants;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import static eg.com.vodafone.service.BusinessException.DUPLICATE_INPUT_STRUCTURE_NAME;
import static eg.com.vodafone.service.BusinessException.NAME_USED_BY_OTHER_DB_OBJECT;

/**
 * Create By: Radwa Osama
 * Date: 4/5/13, 12:55 PM
 */
@Component
public class DataCollectionTypeFormBeanValidator implements Validator {

  @Autowired
  DataCollectionServiceInterface dataCollectionService;

  @Autowired
  private RegexValidator regexValidator;

  @Override
  public boolean supports(Class<?> aClass) {
    return DataCollectionTypeFormBean.class.isAssignableFrom(aClass);
  }

  @Override
  public void validate(Object target, Errors errors) {

    if (!(target instanceof DataCollectionTypeFormBean)) {
      throw new GenericException("Invalid bean type");
    }

    DataCollectionTypeFormBean formBean = (DataCollectionTypeFormBean) target;

    //  Validate Data Collection Type is selected
    if (DataCollectionType.None.equals(formBean.getDataCollectionType())) {
      errors.rejectValue("dataCollectionTypeFormBean.dataCollectionType",
        "DataCollectionTypeFormBean.field.required",
              "Please, Select data collection type.");
    }

    // Validate Data Collection Name is not empty
    ValidationUtils.
      rejectIfEmptyOrWhitespace(errors, "dataCollectionTypeFormBean.dataCollectionName",
        "DataCollectionTypeFormBean.field.required",
              "Please, Enter data collection name.");

    // If Data Collection Name is not empty, validate:
    // 1- length less than or equal 25
    // 2- Contain no spaces
    //3- ix valid db identifier
    // 4- it's uniqueness
    if (StringUtils.isNotEmpty(formBean.getDataCollectionName()) &&
      !StringUtils.isWhitespace(formBean.getDataCollectionName())) {

      DataCollectionUtil dataCollectionUtil = new DataCollectionUtil();
      if (dataCollectionUtil.isReservedWord(formBean.getDataCollectionName())) {

        errors.rejectValue("dataCollectionTypeFormBean.dataCollectionName",
          "DataCollectionTypeFormBean.field.required",
          "Invalid data collection name.");

      } else {

        int expectedLength = 25;

        if (formBean.getDataCollectionName().length() > expectedLength) {
          errors.rejectValue("dataCollectionTypeFormBean.dataCollectionName",
            "DataCollectionTypeFormBean.field.invalid si",
            "Data collection name shouldn't exceed "
                    + expectedLength + " characters.");
        }

        if (formBean.getDataCollectionName().contains(" ")) {
          errors.rejectValue("dataCollectionTypeFormBean.dataCollectionName",
            "DataCollectionTypeFormBean.field.required",
                  "Data collection name should contain no spaces.");
        }
         regexValidator.validate(formBean.getDataCollectionName(),errors,
                  "dataCollectionTypeFormBean.dataCollectionName", CMTConstants.VALID_DB_IDENTEFIER_PATTERN,
                  "DataCollectionTypeFormBean.field.required","Data collection name should be a valid Database Identifier.");

        try {
          dataCollectionService.validateUniqueDataCollectionName(formBean.getDataCollectionName());
        } catch (BusinessException exception) {

            if(exception.getCode() == DUPLICATE_INPUT_STRUCTURE_NAME) {
                  errors.rejectValue("dataCollectionTypeFormBean.dataCollectionType",
                    "DataCollectionTypeFormBean.field.required", "This name \"" +
                    formBean.getDataCollectionName() +
                    "\" is already used by other data collection process. Please Enter another name.");
            }else  if(exception.getCode() == NAME_USED_BY_OTHER_DB_OBJECT) {
                errors.rejectValue("dataCollectionTypeFormBean.dataCollectionType",
                        "DataCollectionTypeFormBean.field.required", "This name \"" +
                        formBean.getDataCollectionName() +
                        "\" is already used by some Object in the Database. Please Enter another name.");
            }
            else{
                errors.rejectValue("dataCollectionTypeFormBean.dataCollectionType",
                        "DataCollectionTypeFormBean.field.required", "Invalid Name. Please Enter another name.");
            }

        }
      }
    }

  }
}
