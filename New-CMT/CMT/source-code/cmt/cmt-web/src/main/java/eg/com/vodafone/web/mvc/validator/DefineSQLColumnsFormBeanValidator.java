package eg.com.vodafone.web.mvc.validator;

import eg.com.vodafone.web.exception.GenericException;
import eg.com.vodafone.web.mvc.controller.DataCollectionUtil;
import eg.com.vodafone.web.mvc.formbean.dataCollection.DefineSQLColumnsFormBean;
import eg.com.vodafone.web.mvc.formbean.dataCollection.SourceColumn;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

/**
 * @author Radwa Osama
 * @since 4/10/13
 */
@Component
public class DefineSQLColumnsFormBeanValidator implements Validator {

  @Override
  public boolean supports(Class<?> aClass) {
    return DefineSQLColumnsFormBean.class.isAssignableFrom(aClass);
  }

  @Override
  public void validate(Object target, Errors errors) {

    if (!(target instanceof DefineSQLColumnsFormBean)) {
      throw new GenericException("Invalid bean type");
    }

    DefineSQLColumnsFormBean formBean = (DefineSQLColumnsFormBean) target;

    if (StringUtils.isNotEmpty(formBean.getGroupByClause()) &&
      !StringUtils.isWhitespace(formBean.getGroupByClause()) &&
      formBean.getGroupByClause().length() > 3000) {
      errors.rejectValue("defineSQLColumnsFormBean.groupByClause", "DefineSQLColumnsFormBean.field.required", "Group By Expression must be less than 3000 character");
    }

    if (StringUtils.isNotEmpty(formBean.getHavingClause()) &&
      !StringUtils.isWhitespace(formBean.getHavingClause()) &&
      formBean.getHavingClause().length() > 3000) {
      errors.rejectValue("defineSQLColumnsFormBean.havingClause", "DefineSQLColumnsFormBean.field.required", "Having Expression must be less than 3000 character");
    }

    if (StringUtils.isNotEmpty(formBean.getWhereClause()) &&
      !StringUtils.isWhitespace(formBean.getWhereClause()) &&
      formBean.getWhereClause().length() > 3000) {
      errors.rejectValue("defineSQLColumnsFormBean.whereClause", "DefineSQLColumnsFormBean.field.required", "Where Expression must be less than 3000 character");
    }

    SourceColumn column = formBean.getSourceColumn();

    if (column != null) {
      if (StringUtils.isEmpty(column.getName())) {
        errors.rejectValue("defineSQLColumnsFormBean.sourceColumn", "DefineSQLColumnsFormBean.field.required", "Column name can't be empty");
        column.setHasError(true);
      }

      if (StringUtils.isEmpty(column.getSqlExpression())) {
        errors.rejectValue("defineSQLColumnsFormBean.sourceColumn", "DefineSQLColumnsFormBean.field.required", "Sql Expression can't be empty");
        column.setHasError(true);
      }

      if ("ID".equalsIgnoreCase(column.getName()) || "NODE_NAME".equalsIgnoreCase(column.getName())) {
        errors.rejectValue("defineSQLColumnsFormBean.sourceColumn", "DefineSQLColumnsFormBean.field.required", "Column name can't be \"ID\" or \"NODE_NAME\"");
        column.setHasError(true);
      }

      if (StringUtils.isNotEmpty(column.getName()) && column.getName().contains(" ")) {
        errors.rejectValue("defineSQLColumnsFormBean.sourceColumn", "DefineSQLColumnsFormBean.field.required", "Column name shouldn't contain spaces");
        column.setHasError(true);
      }

      DataCollectionUtil dataCollectionUtil = new DataCollectionUtil();
      if (StringUtils.isNotEmpty(column.getName()) && dataCollectionUtil.isReservedWord(column.getName())) {
        errors.rejectValue("defineSQLColumnsFormBean.sourceColumn", "DefineSQLColumnsFormBean.field.required", "Invalid column name");
        column.setHasError(true);
      }

      switch (column.getType()) {
        case STRING:
          if (!StringUtils.isEmpty(column.getDefaultValue()) && column.getDefaultValue().length() > 4000) {
            errors.rejectValue("defineSQLColumnsFormBean.sourceColumn",
              "DefineSQLColumnsFormBean.field.required", "Default value must be less than 4000 character");
            column.setHasError(true);
          }
          break;

        case NUMBER:
        case FLOAT:
          if (!StringUtils.isEmpty(column.getDefaultValue()) && !NumberUtils.isNumber(column.getDefaultValue().trim())) {
            errors.rejectValue("defineSQLColumnsFormBean.sourceColumn",
              "DefineSQLColumnsFormBean.field.required", "Default value is not a valid numeric value");
            column.setHasError(true);
          }
          break;
      }
    }
  }

  public void validateUniqueColumnName(List<SourceColumn> columnsList, SourceColumn newColumn, Errors errors) {
    for (SourceColumn column : columnsList) {
      if (column.getName().equalsIgnoreCase(newColumn.getName())) {
        errors.rejectValue("defineSQLColumnsFormBean.sourceColumn",
          "DefineSQLColumnsFormBean.field.required",
          "Multiple columns are using \"" +
            newColumn.getName() + "\" as column name. column name should be unique.");
        newColumn.setHasError(true);
        break;
      }
    }
  }
}
