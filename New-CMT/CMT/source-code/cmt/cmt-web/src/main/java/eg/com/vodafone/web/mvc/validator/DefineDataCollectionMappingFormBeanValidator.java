package eg.com.vodafone.web.mvc.validator;

import eg.com.vodafone.model.DataColumn;
import eg.com.vodafone.model.enums.DataColumnType;
import eg.com.vodafone.web.exception.GenericException;
import eg.com.vodafone.web.mvc.formbean.dataCollection.DefineDataCollectionMappingFormBean;
import eg.com.vodafone.web.mvc.formbean.dataCollection.SourceColumn;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.*;

/**
 * Create By: Radwa Osama
 * Date: 4/12/13, 5:47 PM
 */
@Component
public class DefineDataCollectionMappingFormBeanValidator implements Validator {

  @Override
  public boolean supports(Class<?> aClass) {
    return DefineDataCollectionMappingFormBean.class.isAssignableFrom(aClass);
  }

  @Override
  public void validate(Object target, Errors errors) {

    if (!(target instanceof DefineDataCollectionMappingFormBean)) {
      throw new GenericException("Invalid bean type");
    }
  }

  public void validate(List<DataColumn> outputColumns, Errors errors) {

    Set<String> usedOutputColumns = new HashSet<String>();

    for (DataColumn dataColumn : outputColumns) {

      // Name attribute is supposed to hold output column name
      if (StringUtils.isNotEmpty(dataColumn.getName()) && usedOutputColumns.contains(dataColumn.getName())) {
        errors.rejectValue("defineDataCollectionMappingFormBean.outputColumns",
          "DataCollectionTypeFormBean.field.required", "Multiple source columns are using \"" +
          dataColumn.getName() + "\" as output column. It can be used only once.");
        break;
      }

      usedOutputColumns.add(dataColumn.getName());
    }

    }

    public void validate(List<DataColumn> outputColumns,List<SourceColumn> sourceColumns ,Errors errors) {

        Map<String,SourceColumn> columnsByName = new HashMap<String, SourceColumn>();
        for(SourceColumn sc:sourceColumns){
            columnsByName.put(sc.getName(),sc);
        }

        for (DataColumn dataColumn : outputColumns) {
            if(dataColumn.getTypeCode() == DataColumnType.STRING.getTypeCode()){
                SourceColumn sourceColumn = columnsByName.get(dataColumn.getSrcColumn());
                int sourceColumnSize = Integer.parseInt(sourceColumn.getCustomType());
                if(dataColumn.getStrSize() <sourceColumnSize)
                {
                    errors.rejectValue("defineDataCollectionMappingFormBean.outputColumns",
                            "DataCollectionTypeFormBean.field.required", "Multiple source columns are using \"" +
                            sourceColumn.getName() + "\" is mapped to column with less size.");
                }
            }
        }
    }

}
