package eg.com.vodafone.web.mvc.validator;

import eg.com.vodafone.web.exception.GenericException;
import eg.com.vodafone.web.mvc.formbean.dataCollection.DefineDBExtractionSQL;
import eg.com.vodafone.web.mvc.util.CMTConstants;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Radwa Osama
 * @since 4/15/13
 */
@Component
public class DefineDBExtractionSQLValidator implements Validator {

    @Autowired
    private RegexValidator regexValidator;

  @Override
  public boolean supports(Class<?> aClass) {
    return DefineDBExtractionSQL.class.isAssignableFrom(aClass);
  }

  @Override
  public void validate(Object target, Errors errors) {

    if (!(target instanceof DefineDBExtractionSQL)) {
      throw new GenericException("Invalid bean type");
    }

    DefineDBExtractionSQL formBean = (DefineDBExtractionSQL) target;

    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "defineDBExtractionSQL.columns",
      "DataCollectionTypeFormBean.field.required", "Please, Enter columns value");


      ValidationUtils.rejectIfEmptyOrWhitespace(errors, "defineDBExtractionSQL.query",
              "DataCollectionTypeFormBean.field.required", "Please, Enter Query value");

    if (StringUtils.isNotEmpty(formBean.getColumns())) {

      String[] columns = formBean.getColumns().trim().split(",");

      if (columns == null || columns.length == 0) {

        errors.rejectValue("defineDBExtractionSQL.columns",
          "DataCollectionTypeFormBean.field.required", "Please, Enter a valid columns value");
      } else {
          List<String> usedNames=new ArrayList<String>();
        for (String columnName : columns) {
            columnName = columnName.trim();
            if( usedNames.contains(columnName)){
                errors.rejectValue("defineDBExtractionSQL.columns",
                        "DataCollectionTypeFormBean.field.required","[" +columnName + "] is used as name by more than one column");
            }
            else
            {
                usedNames.add(columnName);
                /*boolean isValid = regexValidator.validate(columnName,errors,
                        "defineDBExtractionSQL.columns",
                        CMTConstants.VALID_DB_IDENTEFIER_PATTERN,"DataCollectionTypeFormBean.field.required","["+columnName + "] is invalid column name"); */
                if(regexValidator.validate(columnName,errors,
                    "defineDBExtractionSQL.columns",
                    CMTConstants.VALID_DB_IDENTEFIER_PATTERN,"DataCollectionTypeFormBean.field.required","["+columnName + "] is invalid column name")){
                    if ( "ID".equalsIgnoreCase(columnName)|| "NODE_NAME".equalsIgnoreCase(columnName)) {
                        errors.rejectValue("defineDBExtractionSQL.columns",
                                "DataCollectionTypeFormBean.field.required","["+ columnName + "] is a system reserved word");
                    }
                }
            }
        }
      }
    }

  }
}
