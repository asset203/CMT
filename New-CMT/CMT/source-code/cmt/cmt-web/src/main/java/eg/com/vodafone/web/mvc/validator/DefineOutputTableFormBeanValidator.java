package eg.com.vodafone.web.mvc.validator;

import eg.com.vodafone.model.DataColumn;
import eg.com.vodafone.model.enums.DataColumnType;
import eg.com.vodafone.web.exception.GenericException;
import eg.com.vodafone.web.mvc.controller.DataCollectionUtil;
import eg.com.vodafone.web.mvc.formbean.dataCollection.*;
import eg.com.vodafone.web.mvc.util.CMTConstants;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Create By: Radwa Osama Date: 4/12/13, 2:06 PM
 */
@Component
public class DefineOutputTableFormBeanValidator implements Validator {
    
    @Autowired
    private RegexValidator regexValidator;
    
    @Override
    public boolean supports(Class<?> aClass) {
        return DefineOutputTableFormBean.class.isAssignableFrom(aClass);
    }
    
    @Override
    public void validate(Object target, Errors errors) {
        
        if (!(target instanceof DefineOutputTableFormBean)) {
            throw new GenericException("Invalid bean type");
        }
    }
    
    public void validateAddNewColumn(DefineOutputTableFormBean formBean, Errors errors) {
        
        SourceColumn column = formBean.getSourceColumn();
        
        if (StringUtils.isEmpty(column.getName())) {
            errors.rejectValue("defineOutputTableFormBean.sourceColumn", "DataCollectionTypeFormBean.field.required",
                    "Column name can't be empty");
            column.setHasError(true);
            return; //no need to proceed validation
        }
        
        if (column.getName().contains(" ")) {
            errors.rejectValue("defineOutputTableFormBean.sourceColumn", "DataCollectionTypeFormBean.field.required",
                    "Column name shouldn't contain spaces");
            column.setHasError(true);
            return; //no need to proceed validation
        }
        
        if (!checkValidDatabaseColumnName(column.getName(), "defineOutputTableFormBean.sourceColumn", errors)) {
            column.setHasError(true);
            return;
        }
        
        if (formBean.getExistingTableColumns() != null) {
            for (DataColumn dataColumn : formBean.getExistingTableColumns()) {
                
                if (dataColumn.getName().equalsIgnoreCase(column.getName())) {
                    errors.rejectValue("defineOutputTableFormBean.sourceColumn",
                            "DataCollectionTypeFormBean.field.required",
                            "Duplicate name: column name is already used by other column");
                    column.setHasError(true);
                    return;  //no need to proceed validation
                }
                
            }
            if (formBean.getNewOutputTableColumns() != null) {
                for (SourceColumn col : formBean.getNewOutputTableColumns()) {
                    if (column.getName().equalsIgnoreCase(col.getName())) {
                        errors.rejectValue("defineOutputTableFormBean.sourceColumn",
                                "DataCollectionTypeFormBean.field.required",
                                "Duplicate name: column name is already used by other column");
                        column.setHasError(true);
                        return;//no need to proceed validation
                    }
                }
            }
        }
        switch (column.getType()) {
            case STRING:
                if (!StringUtils.isEmpty(column.getDefaultValue()) && column.getDefaultValue().length() > 4000) {
                    errors.rejectValue("defineOutputTableFormBean.sourceColumn",
                            "DataCollectionTypeFormBean.field.required", "Default value must be less than 4000 character");
                    column.setHasError(true);
                }
                break;
            
            case NUMBER:
            case FLOAT:
                if (!StringUtils.isEmpty(column.getDefaultValue()) && !NumberUtils.isNumber(column.getDefaultValue().trim())) {
                    errors.rejectValue("defineOutputTableFormBean.sourceColumn",
                            "DataCollectionTypeFormBean.field.required", "Default value is not a valid numeric value");
                    column.setHasError(true);
                }
                break;
        }
        
    }
    
    public void validateUseExistingTable(DataCollectionWizardFormBean dataCollectionWizardFormBean, Errors errors) {
        
        DefineOutputTableFormBean defineOutputTableFormBean
                = dataCollectionWizardFormBean.getDefineOutputTableFormBean();

        //1- validate that a table is selected
        final String DEFAULT_SELECT = "Select ... ";
        if (DEFAULT_SELECT.equals(defineOutputTableFormBean.getExistingTable())) {
            
            errors.rejectValue("defineOutputTableFormBean.sourceColumn",
                    "DataCollectionTypeFormBean.field.required", "Please, Select output table");
            return;
        }

        //2- validate the table selected has column
        if ((defineOutputTableFormBean.getExistingTableColumns() == null
                || defineOutputTableFormBean.getExistingTableColumns().isEmpty())
                && (defineOutputTableFormBean.getNewOutputTableColumns() == null
                || defineOutputTableFormBean.getNewOutputTableColumns().isEmpty())) {
            
            errors.rejectValue("defineOutputTableFormBean.sourceColumn",
                    "DataCollectionTypeFormBean.field.required", "Please, Select another output table or add new columns");
            return;
        }

        //3- validate "Auto Filled Date Column Name" value
        validateAutoFilledDateColumnUsingExistingTable(defineOutputTableFormBean, errors);

        //4- validate " Truncate Based on Date Column Name" value
        validateDateColumnForExistingTable(defineOutputTableFormBean, errors);
        
         //5- validate " PartitionColumn Name" value
        validatePartitionColumnForExistingTable(defineOutputTableFormBean, errors);

        //6- validate the selected table have enough columns to match ALL DC columns
        Map<String, Integer> sourceColumnCounts = getCountsForSourceColumns(dataCollectionWizardFormBean);
        Map<String, Integer> outputColumnCounts = getCountsForOutputColumns(dataCollectionWizardFormBean);
        
        for (String type : sourceColumnCounts.keySet()) {
            if (!outputColumnCounts.containsKey(type)
                    || outputColumnCounts.get(type) < sourceColumnCounts.get(type)) {
                
                errors.rejectValue("defineOutputTableFormBean.sourceColumn",
                        "DataCollectionTypeFormBean.field.required",
                        "Some of the source columns are not having columns of the same type");
                return;
            }
        }
        
    }
    
    public void validateCreateNewTable(DataCollectionWizardFormBean formBean, Errors errors) {
        DataCollectionUtil dataCollectionUtil = new DataCollectionUtil();
        List<SourceColumn> queryColumns
                = dataCollectionUtil.getSelectedSourceColumn(
                        formBean.getExtractSourceColumnFormBean().getColumns());
        
        List<SourceColumn> expressionColumns
                = formBean.getDefineSQLColumnsFormBean().getSqlExpressionColumns();
        if (expressionColumns != null) {
            queryColumns.addAll(expressionColumns);
        }
        
        validateAutoFilledDateColumnUsingNewTable(formBean.getDefineOutputTableFormBean(), queryColumns, errors);
        validateDateColumnForNewTable(formBean.getDefineOutputTableFormBean(), queryColumns, errors);
        validatePartitionColumnForNewTable(formBean.getDefineOutputTableFormBean(), queryColumns, errors);
    }

    private Map<String, Integer> getCountsForSourceColumns(DataCollectionWizardFormBean dataCollectionWizardFormBean) {
        
        DataCollectionUtil dataCollectionUtil = new DataCollectionUtil();
        
        List<SourceColumn> sourceColumnList
                = dataCollectionUtil.getSelectedSourceColumn(
                        dataCollectionWizardFormBean.getExtractSourceColumnFormBean().getColumns());
        
        List<SourceColumn> expressionColumns
                = dataCollectionWizardFormBean.getDefineSQLColumnsFormBean().getSqlExpressionColumns();
        
        if (expressionColumns != null) {
            sourceColumnList.addAll(expressionColumns);
        }
        
        return countType(sourceColumnList);
    }
    
    private Map<String, Integer> getCountsForOutputColumns(DataCollectionWizardFormBean dataCollectionWizardFormBean) {
        
        List<SourceColumn> outputTableColumnsToCount = getCountableOutputColumns(dataCollectionWizardFormBean);
        List<SourceColumn> newOutputTableColumns
                = dataCollectionWizardFormBean.getDefineOutputTableFormBean().getNewOutputTableColumns();
        
        if (newOutputTableColumns != null) {
            outputTableColumnsToCount.addAll(newOutputTableColumns);
        }
        
        return countType(outputTableColumnsToCount);
    }
    
    private Map<String, Integer> countType(List<SourceColumn> sourceColumnList) {
        
        Map<String, Integer> sourceColumnType = new HashMap<String, Integer>();
        
        for (SourceColumn sourceColumn : sourceColumnList) {
            if (sourceColumnType.containsKey(sourceColumn.getType().name())) {
                sourceColumnType.put(sourceColumn.getType().name(), sourceColumnType.get(sourceColumn.getType().name()) + 1);
            } else {
                sourceColumnType.put(sourceColumn.getType().name(), 1);
            }
        }
        
        return sourceColumnType;
    }

    //*** added by basma
    private List<SourceColumn> getCountableOutputColumns(DataCollectionWizardFormBean dataCollectionWizardFormBean) {
        DataCollectionUtil dataCollectionUtil = new DataCollectionUtil();
        List<SourceColumn> outputTableColumnsToCount
                = dataCollectionUtil.createSourceColumn(
                        dataCollectionWizardFormBean.getDefineOutputTableFormBean().getExistingTableColumns());
        //excluding configurable "NODE_NAME" column from counting
        if (dataCollectionWizardFormBean.getDefineOutputTableFormBean().getNodeName() == NODE_NAME.CONFIGURABLE) {
            for (SourceColumn col : outputTableColumnsToCount) {
                if (CMTConstants.NODE_NAME_COLUMN.equalsIgnoreCase(col.getOutputColumnName())) {
                    outputTableColumnsToCount.remove(col);
                    break;
                }
            }
        }
        //excluding autoFilled date column from counting
        if (dataCollectionWizardFormBean.getDefineOutputTableFormBean().isAddAutoFilledDateColumn()
                && StringUtils.isNotBlank(dataCollectionWizardFormBean.getDefineOutputTableFormBean().getAutoFilledDateColumnName())) {
            for (SourceColumn col : outputTableColumnsToCount) {
                if (dataCollectionWizardFormBean.getDefineOutputTableFormBean().getAutoFilledDateColumnName()
                        .equalsIgnoreCase(col.getOutputColumnName())) {
                    outputTableColumnsToCount.remove(col);
                    break;
                }
            }
        }
        return outputTableColumnsToCount;
    }
    //*** end added by basma

    public void validateDateColumn(DataCollectionWizardFormBean formBean, Errors errors) {
        String dateColumnName = formBean.getDefineOutputTableFormBean().getDateColumn();
        if (!StringUtils.isEmpty(dateColumnName)) {
            boolean validColumnName = false;
            dateColumnName = dateColumnName.trim();
            
            DataCollectionUtil dataCollectionUtil = new DataCollectionUtil();
            List<SourceColumn> queryColumns
                    = dataCollectionUtil.getSelectedSourceColumn(
                            formBean.getExtractSourceColumnFormBean().getColumns());
            
            List<SourceColumn> expressionColumns
                    = formBean.getDefineSQLColumnsFormBean().getSqlExpressionColumns();
            if (expressionColumns != null) {
                queryColumns.addAll(expressionColumns);
            }
            if (queryColumns != null) {
                for (SourceColumn col : queryColumns) {
                    if (dateColumnName.equalsIgnoreCase(col.getName())) {
                        validColumnName = true;
                        if (!(col.getType() == Type.DATE)) {
                            errors.rejectValue("defineOutputTableFormBean.dateColumn",
                                    "DataCollectionTypeFormBean.field.required", "Date Column  must be of type Date");
                        }
                        break;
                    }
                }
            }
            if (!validColumnName) {
                errors.rejectValue("defineOutputTableFormBean.dateColumn",
                        "DataCollectionTypeFormBean.field.required", "Date Column must be one of output table columns");
            }
            
        }
    }

    private void validateDateColumnForExistingTable(DefineOutputTableFormBean defineOutputTableFormBean, Errors errors) {
        
        String dateColumn = defineOutputTableFormBean.getDateColumn();
        
        if (!StringUtils.isEmpty(dateColumn) && defineOutputTableFormBean.isTruncateBeforeInsertion()) {
            dateColumn = dateColumn.trim();
            if (defineOutputTableFormBean.getExistingTableColumns() != null) {
                for (DataColumn col : defineOutputTableFormBean.getExistingTableColumns()) {
                    if (col.getName().equalsIgnoreCase(dateColumn)) {
                        if (col.getTypeCode() != DataColumnType.DATE.getTypeCode()) {
                            errors.rejectValue("defineOutputTableFormBean.dateColumn",
                                    "DataCollectionTypeFormBean.field.required", "Date Column must be of type Date");
                        }
                        return;
                    }
                }
            }
            if (defineOutputTableFormBean.getNewOutputTableColumns() != null) {
                for (SourceColumn col : defineOutputTableFormBean.getNewOutputTableColumns()) {
                    if (col.getName().equalsIgnoreCase(dateColumn)) {
                        if (!(col.getType() == Type.DATE)) {
                            errors.rejectValue("defineOutputTableFormBean.dateColumn",
                                    "DataCollectionTypeFormBean.field.required", "Date Column  must be of type Date");
                        }
                        return;
                    }
                }
            }
            
            if (defineOutputTableFormBean.isAddAutoFilledDateColumn()
                    && errors.getFieldErrorCount("defineOutputTableFormBean.autoFilledDateColumnName") == 0
                    && dateColumn.equalsIgnoreCase(defineOutputTableFormBean.getAutoFilledDateColumnName())) {
                return;
            } else {
                errors.rejectValue("defineOutputTableFormBean.dateColumn",
                        "DataCollectionTypeFormBean.field.required", "Date Column must be one of output table columns");
            }
            
        }
    }
    
    
    
       private void validatePartitionColumnForExistingTable(DefineOutputTableFormBean defineOutputTableFormBean, Errors errors) {
        
        String dateColumn = defineOutputTableFormBean.getDateColumn();
        
        if (!StringUtils.isEmpty(dateColumn) && defineOutputTableFormBean.isIsPartitioned()) {
            dateColumn = dateColumn.trim();
            if (defineOutputTableFormBean.getExistingTableColumns() != null) {
                for (DataColumn col : defineOutputTableFormBean.getExistingTableColumns()) {
                    if (col.getName().equalsIgnoreCase(dateColumn)) {
                        if (col.getTypeCode() != DataColumnType.DATE.getTypeCode()) {
                            errors.rejectValue("defineOutputTableFormBean.partitionColumnName",
                                    "DataCollectionTypeFormBean.field.required", "Partition Column must be of type Date");
                        }
                        return;
                    }
                }
            }
            if (defineOutputTableFormBean.getNewOutputTableColumns() != null) {
                for (SourceColumn col : defineOutputTableFormBean.getNewOutputTableColumns()) {
                    if (col.getName().equalsIgnoreCase(dateColumn)) {
                        if (!(col.getType() == Type.DATE)) {
                            errors.rejectValue("defineOutputTableFormBean.partitionColumnName",
                                    "DataCollectionTypeFormBean.field.required", "partition Column  must be of type Date");
                        }
                        return;
                    }
                }
            }
            
   
            
        }
    }


    private boolean checkValidDatabaseColumnName(String fieldValue, String fieldKey, Errors errors) {
        if (!regexValidator.validate(fieldValue.trim(), errors,
                fieldKey,
                CMTConstants.VALID_DB_IDENTEFIER_PATTERN_LNGTH_30, "",
                "[" + fieldValue + "] is not valid column name")) {
            return false;
        }
        if ("ID".equalsIgnoreCase(fieldValue) || "NODE_NAME".equalsIgnoreCase(fieldValue)) {
            errors.rejectValue(fieldKey, "",
                    "Invalid name: column name can't be \"ID\" OR \"NODE_NAME\"");
            return false;
        }
        
        DataCollectionUtil dataCollectionUtil = new DataCollectionUtil();
        if (dataCollectionUtil.isReservedWord(fieldValue)) {
            errors.rejectValue(fieldKey, "",
                    "Invalid name: [ " + fieldValue + " ] is reserved word");
            return false;
        }
        return true;
    }

    private boolean validateAutoFilledDateColumnUsingNewTable(DefineOutputTableFormBean defineOutputTableFormBean,
            List<SourceColumn> queryColumns, Errors errors) {
        if (!defineOutputTableFormBean.isAddAutoFilledDateColumn()) {
            return true;
        }
        
        if (checkValidDatabaseColumnName(defineOutputTableFormBean.getAutoFilledDateColumnName(), "defineOutputTableFormBean.dateColumn",
                errors)) {
            if (queryColumns != null) {
                for (SourceColumn col : queryColumns) {
                    if (defineOutputTableFormBean.getAutoFilledDateColumnName().trim().equalsIgnoreCase(col.getName())) {
                        errors.rejectValue("defineOutputTableFormBean.dateColumn",
                                "DataCollectionTypeFormBean.field.required",
                                "Auto-filled column name is already used by other column");
                        return false;
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    private void validateDateColumnForNewTable(DefineOutputTableFormBean defineOutputTableFormBean,
            List<SourceColumn> queryColumns, Errors errors) {
        
        if (!defineOutputTableFormBean.isTruncateBeforeInsertion()
                || StringUtils.isEmpty(defineOutputTableFormBean.getDateColumn())) {
            return;
        }
        String dateColumnName = defineOutputTableFormBean.getDateColumn().trim();
        
        if (queryColumns != null) {
            for (SourceColumn col : queryColumns) {
                if (dateColumnName.equalsIgnoreCase(col.getName())) {
                    if (col.getType() != Type.DATE) {
                        errors.rejectValue("defineOutputTableFormBean.dateColumn",
                                "DataCollectionTypeFormBean.field.required", "Date Column  must be of type Date");
                    }
                    return;
                }
            }
        }
        
        if (defineOutputTableFormBean.isAddAutoFilledDateColumn()
                && errors.getFieldErrorCount("defineOutputTableFormBean.autoFilledDateColumnName") == 0
                && dateColumnName.equalsIgnoreCase(defineOutputTableFormBean.getAutoFilledDateColumnName())) {
            return;
        }
        errors.rejectValue("defineOutputTableFormBean.dateColumn",
                "DataCollectionTypeFormBean.field.required", "Date Column must be one of output table columns");
    }
    
    private void validatePartitionColumnForNewTable(DefineOutputTableFormBean defineOutputTableFormBean,
            List<SourceColumn> queryColumns, Errors errors) {
        
        if (!defineOutputTableFormBean.isIsPartitioned()
                || StringUtils.isEmpty(defineOutputTableFormBean.getPartitionColumnName())) {
            return;
        }
        String partitionColumnName = defineOutputTableFormBean.getPartitionColumnName().trim();
        
        if (queryColumns != null) {
            for (SourceColumn col : queryColumns) {
                if (partitionColumnName.equalsIgnoreCase(col.getName())) {
                    if (col.getType() != Type.DATE) {
                        errors.rejectValue("defineOutputTableFormBean.partitionColumn",
                                "DataCollectionTypeFormBean.field.required", "Partition Column  must be of type Date");
                    }
                    return;
                }
            }
        }
        
        if (defineOutputTableFormBean.isIsPartitioned()
                && errors.getFieldErrorCount("defineOutputTableFormBean.partitionColumnName") == 0
                && partitionColumnName.equalsIgnoreCase(defineOutputTableFormBean.getPartitionColumnName())) {
            return;
        }
        errors.rejectValue("defineOutputTableFormBean.partitionColumnName",
                "DataCollectionTypeFormBean.field.required", "Partition Column must be one of output table columns");
    }
    
    private boolean validateAutoFilledDateColumnUsingExistingTable(DefineOutputTableFormBean defineOutputTableFormBean, Errors errors) {
        
        if (!defineOutputTableFormBean.isAddAutoFilledDateColumn()) {
            return true;
        }
        if (StringUtils.isEmpty(defineOutputTableFormBean.getAutoFilledDateColumnName())) {
            errors.rejectValue("defineOutputTableFormBean.autoFilledDateColumnName",
                    "", "Please type auto-filled date column name.");
            return false;
        }
        if (defineOutputTableFormBean.getAutoFilledDateColumnName().contains(" ")) {
            errors.rejectValue("defineOutputTableFormBean.autoFilledDateColumnName", "DataCollectionTypeFormBean.field.required",
                    "auto-filled date column name shouldn't contain spaces");
            return false; //no need to proceed validation
        }
        String name = defineOutputTableFormBean.getAutoFilledDateColumnName();
        if (!checkValidDatabaseColumnName(name, "defineOutputTableFormBean.autoFilledDateColumnName", errors)) {
            return false;
        }
        
        if (defineOutputTableFormBean.getExistingTableColumns() != null) {
            for (DataColumn col : defineOutputTableFormBean.getExistingTableColumns()) {
                if (col.getName().equalsIgnoreCase(name)) {
                    if (col.getTypeCode() == DataColumnType.DATE.getTypeCode()) {
                        return true;
                    }
                    errors.rejectValue("defineOutputTableFormBean.autoFilledDateColumnName",
                            "DataCollectionTypeFormBean.field.required", "Auto-filled date Column must be of type Date");
                    return false;
                }
            }
        }
        if (defineOutputTableFormBean.getNewOutputTableColumns() != null) {
            for (SourceColumn col : defineOutputTableFormBean.getNewOutputTableColumns()) {
                if (col.getName().equalsIgnoreCase(name)) {
                    if (col.getType() == Type.DATE) {
                        return true;
                    } else {
                        errors.rejectValue("defineOutputTableFormBean.autoFilledDateColumnName",
                                "DataCollectionTypeFormBean.field.required", "Auto-filled date Column  must be of type Date");
                        return false;
                    }
                }
            }
        }
        return true;
        
    }
}
