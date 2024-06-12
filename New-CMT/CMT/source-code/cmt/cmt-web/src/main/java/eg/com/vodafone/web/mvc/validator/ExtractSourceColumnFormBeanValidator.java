package eg.com.vodafone.web.mvc.validator;

import eg.com.vodafone.web.exception.GenericException;
import eg.com.vodafone.web.mvc.controller.DataCollectionUtil;
import eg.com.vodafone.web.mvc.formbean.dataCollection.DateColumnPrecession;
import eg.com.vodafone.web.mvc.formbean.dataCollection.ExtractSourceColumnFormBean;
import eg.com.vodafone.web.mvc.formbean.dataCollection.SourceColumn;
import eg.com.vodafone.web.mvc.formbean.dataCollection.Type;
import eg.com.vodafone.web.mvc.util.CMTConstants;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Radwa Osama
 * @since 4/9/13
 */
@Component
public class ExtractSourceColumnFormBeanValidator implements Validator {

  @Autowired
  RegexValidator regexValidator;
  @Override
  public boolean supports(Class<?> aClass) {
    return ExtractSourceColumnFormBean.class.isAssignableFrom(aClass);
  }

  @Override
  public void validate(Object target, Errors errors) {

    if (!(target instanceof ExtractSourceColumnFormBean)) {
      throw new GenericException("Invalid bean type");
    }

    ExtractSourceColumnFormBean formBean = (ExtractSourceColumnFormBean) target;

    if (formBean.getColumns() == null || formBean.getColumns().isEmpty()) {
      errors.rejectValue("extractSourceColumnFormBean.errors",
        "DefineSQLColumnsFormBean.field.required", "There are no extracted columns.");

      return;
    }

    int index = 1;
    // for each column validate:
    // 1- Name is not empty
    // 2- Default value is compatible with type
    for (SourceColumn column : formBean.getColumns()) {

      if (StringUtils.isEmpty(column.getName())) {
        errors.rejectValue("extractSourceColumnFormBean.errors",
          "DefineSQLColumnsFormBean.field.required", "In Row " + index + ": column name can't be empty");
        column.setHasError(true);
      }

      if ("ID".equalsIgnoreCase(column.getName())|| "NODE_NAME".equalsIgnoreCase(column.getName())) {
        errors.rejectValue("extractSourceColumnFormBean.errors",
          "DefineSQLColumnsFormBean.field.required", "In Row " + index + ": column name can't be \"ID\" OR \"NODE_NAME\"");
        column.setHasError(true);
      }

      if (StringUtils.isNotEmpty(column.getName()) && column.getName().contains(" ")) {
        errors.rejectValue("extractSourceColumnFormBean.errors",
          "DefineSQLColumnsFormBean.field.required", "In Row " + index + ": column name shouldn't contain spaces");
        column.setHasError(true);
      }

      DataCollectionUtil dataCollectionUtil = new DataCollectionUtil();
      if (StringUtils.isNotEmpty(column.getName()) && dataCollectionUtil.isReservedWord(column.getName())) {
        errors.rejectValue("extractSourceColumnFormBean.errors",
          "DefineSQLColumnsFormBean.field.required", "In Row " + index + ": invalid column name");
        column.setHasError(true);
      }
      if(!column.isHasError()){
              int errorsCount = errors.getErrorCount();
              regexValidator.validate(column.getName(),errors,
                      "extractSourceColumnFormBean.errors", CMTConstants.VALID_DB_IDENTEFIER_PATTERN,
                      "DefineSQLColumnsFormBean.field.required","In Row " + index + ": invalid column name");
            if(errors.getErrorCount() > errorsCount){
                column.setHasError(true);
            }
      }
      switch (column.getType()) {
        case STRING:
            int  size=0;
          if (StringUtils.isEmpty(column.getCustomType()) || !StringUtils.isNumeric(column.getCustomType())) {
            errors.rejectValue("extractSourceColumnFormBean.errors",
              "DefineSQLColumnsFormBean.field.required", "In Row " + index + ": column size must be positive integer value");
            column.setHasError(true);
          }
          else
          {

               size = Integer.parseInt(column.getCustomType())  ;
               if( size <= 0 || size>4000) {
                  errors.rejectValue("extractSourceColumnFormBean.errors",
                          "DefineSQLColumnsFormBean.field.required", "In Row " + index + ": column size must be integer between 1 and 4000");
                  column.setHasError(true);
               }
          }

          if (!column.isHasError() && !StringUtils.isEmpty(column.getDefaultValue()) && !StringUtils.isWhitespace(column.getDefaultValue()))
            {

                if(column.getDefaultValue().trim().length() > size){
                    errors.rejectValue("extractSourceColumnFormBean.errors",
                            "DefineSQLColumnsFormBean.field.required", "In Row " + index + ": default value length must be less than column size");
                    column.setHasError(true);
            }

          }
          break;

        case DATE:
          boolean  isValidDateFormat = false;
          if (StringUtils.isEmpty(column.getCustomType())) {
            errors.rejectValue("extractSourceColumnFormBean.errors",
              "DefineSQLColumnsFormBean.field.required", "In Row " + index + ": please enter date format");
            column.setHasError(true);
          }

          else
          {
              isValidDateFormat = isValidDatePattern(column.getCustomType());
              if (!isValidDateFormat) {
                  errors.rejectValue("extractSourceColumnFormBean.errors",
                          "DefineSQLColumnsFormBean.field.notValidFormat", "In Row " + index + ": please enter valid date format");
                  column.setHasError(true);
              }
          }

          if (isValidDateFormat && !StringUtils.isEmpty(column.getDefaultValue())
            && !StringUtils.isWhitespace(column.getDefaultValue())) {
                  boolean isValidDate = isValidDate(column.getCustomType(), column.getDefaultValue().trim());
                  if (!isValidDate) {
                      errors.rejectValue("extractSourceColumnFormBean.errors",
                              "DefineSQLColumnsFormBean.field.notValidFormat", "In Row " + index + ": default value format doesn't match the entered format");
                      column.setHasError(true);
              }
          }
          break;
        case NUMBER:
        case FLOAT:
          if (!StringUtils.isEmpty(column.getDefaultValue()) && !NumberUtils.isNumber(column.getDefaultValue().trim())) {
            errors.rejectValue("extractSourceColumnFormBean.errors",
              "DefineSQLColumnsFormBean.field.required", "In Row " + index + ": default value is not a valid numeric value");
            column.setHasError(true);
          }
          break;

      }

      index++;
    }

  }

  public void validateUseDateOption(ExtractSourceColumnFormBean formBean, Errors errors){
      if (formBean.getUseDateColumn()) {
          if (StringUtils.isEmpty(formBean.getDateColumnName())) {
              errors.rejectValue("extractSourceColumnFormBean.dateColumnName",
                      "DefineSQLColumnsFormBean.field.required", "Please enter date column name");
          } else {
              boolean columnFound = false;
              boolean isDate = false;
              for (SourceColumn column : formBean.getColumns()) {
                  if (formBean.getDateColumnName().equals(column.getName())) {
                      columnFound = true;
                      if (column.getType() == Type.DATE) {
                          isDate = true;

                      }
                      break;
                  }
              }
              if (!columnFound) {
                  errors.rejectValue("extractSourceColumnFormBean.dateColumnName",
                          "DefineSQLColumnsFormBean.dateColumnName.nameNotFound", "The column name must be one of the extracted columns from file");
              } else if (!isDate) {
                  errors.rejectValue("extractSourceColumnFormBean.dateColumnName",
                          "DefineSQLColumnsFormBean.dateColumnName.nameNotFound", "The column must be of type Date");
              }
          }

          if(formBean.getDateColumnPrecession() == null || formBean.getDateColumnPrecession() == DateColumnPrecession.NONE){
              errors.rejectValue("extractSourceColumnFormBean.dateColumnName",
                      "DefineSQLColumnsFormBean.dateColumnPrecession.nameNotFound", "please select date column precession");
          }
      }
  }

  private boolean isValidDate(String dateFormat, String dateValue) {

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
    simpleDateFormat.applyPattern(dateFormat);
    boolean valid = true;
    try {
      Date date = simpleDateFormat.parse(dateValue);
      if (!simpleDateFormat.format(date).equals(dateValue)) {
        valid = false;
      }
    } catch (ParseException parseException) {
      valid = false;
    }

    return valid;
  }

  private boolean isValidDatePattern(String dateFormat) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat();

        boolean valid = true;
        try {
            simpleDateFormat.applyPattern(dateFormat);
        } catch (IllegalArgumentException e) {
            valid = false;
        }
        return valid;
  }
}
