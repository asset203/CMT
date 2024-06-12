package eg.com.vodafone.web.mvc.validator;

import eg.com.vodafone.model.NodeProperties;
import eg.com.vodafone.model.enums.KpiNodeGrain;
import eg.com.vodafone.service.DataCollectionSystemServiceInterface;
import eg.com.vodafone.service.impl.ManageKpiService;
import eg.com.vodafone.web.exception.GenericException;
import eg.com.vodafone.web.mvc.model.kpiNotifications.SystemKpiModel;
import eg.com.vodafone.web.mvc.util.CMTConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;

import javax.validation.ConstraintViolation;
import javax.validation.metadata.ConstraintDescriptor;
import java.util.*;

/**
 * @author marwa.goda
 * @since 4/30/13
 */
@Component
public class KPIValidator implements Validator {

  @Autowired
  private DataCollectionSystemServiceInterface dataCollectionService;
  @Autowired
  private ManageKpiService manageKpiNotificationsService;

  @Override
  public boolean supports(Class<?> aClass) {
    return SystemKpiModel.class.isAssignableFrom(aClass);
  }

  /**
   * Validates KPI notification list search form
   *
   * @param bean
   * @param errors
   */
  public void validateSearchForm(Object bean, Errors errors) {
    if (!(bean instanceof SystemKpiModel)) {
      throw new GenericException("Invalid form bean type");
    }
    SystemKpiModel formBean = (SystemKpiModel) bean;
    if (!StringUtils.hasText(formBean.getSystemName())) {
      errors.rejectValue("systemName",
        CMTConstants.FIELD_REQUIRED, "");
    } else {
      List<String> systemNames = dataCollectionService.listAllSystems();
      if (!systemNames.contains(formBean.getSystemName())) {
        errors.rejectValue("systemName",
          CMTConstants.FIELD_INVALID, "");
      }
    }

    if (formBean.getNodeID() <= 0) {
      errors.rejectValue("nodeID",
              CMTConstants.FIELD_REQUIRED, "");
    }
  }

  @Override
  public void validate(Object model, Errors errors) {
    if (!(model instanceof SystemKpiModel)) {
      throw new GenericException("Invalid model type");
    }
    SystemKpiModel systemKpiModel = (SystemKpiModel) model;
    if (systemKpiModel.getNodePropertiesList() == null
      || systemKpiModel.getNodePropertiesList().isEmpty()) {
      errors.rejectValue("nodePropertiesList", "NotEmpty.formBean.nodePropertiesList"
        , "<label class='error'> at least one Node property must be added </label>");
    } else {
      //validateTableExist(systemKpiModel, errors);
      validateUniqueTableName(systemKpiModel, errors);
      validateGrainValue(systemKpiModel, errors);
        // fix issue 402405
      validateUniquePropertyName(systemKpiModel,errors);
    }
  }

  private Errors validateGrainValue(SystemKpiModel systemKpiModel, Errors errors) {
    if (systemKpiModel.getNodePropertiesList() != null) {
      int index = 0;
      for (NodeProperties nodeProperties : systemKpiModel.getNodePropertiesList()) {

        if (!KpiNodeGrain.Daily.toString().equalsIgnoreCase(nodeProperties.getGrain())
          && !KpiNodeGrain.Hourly.toString().equalsIgnoreCase(nodeProperties.getGrain())) {
          errors.rejectValue("nodePropertiesList[" + index + "].grain", "nodePropertiesList.invalidValue.grain", "<label class='error'>Grain value should be H or D!</label>");

        }

        index++;
      }
    }
    return errors;
  }


  private Errors validateUniqueTableName(SystemKpiModel systemKpiModel, Errors errors) {
    Map<String, String> nodePropertiesMap = new HashMap<String, String>();
    if (systemKpiModel.getNodePropertiesList() != null) {
      int index = 0;
      for (NodeProperties nodeProperties : systemKpiModel.getNodePropertiesList()) {

        if (nodePropertiesMap.containsKey(nodeProperties.getTrafficTableName())) {
          errors.rejectValue("nodePropertiesList[" + index + "].trafficTableName", "nodeProperties.duplicate.trafficTableName",
            "<label class='error'>Traffic table name must be unique per node!</label>");


        } else {
          nodePropertiesMap.put(nodeProperties.getTrafficTableName(), nodeProperties.getTrafficTableName());

        }

        index++;
      }
    }
    return errors;
  }

  private Errors validateTableExist(SystemKpiModel systemKpiModel, Errors errors) {
    if (systemKpiModel.getNodePropertiesList() != null) {
      int index = 0;
      for (NodeProperties nodeProperties : systemKpiModel.getNodePropertiesList()) {
        if (!manageKpiNotificationsService.isTrafficTableExist(nodeProperties.getTrafficTableName())) {
          errors.rejectValue("nodePropertiesList[" + index + "].trafficTableName",
            "nodeProperties.notExist.trafficTableName",
            "<label class='error'>Traffic table " + nodeProperties.getTrafficTableName() + " dose not exist</label>");
        }
        index++;
      }
    }
    return errors;
  }

  public static <T> void convert(Errors errors, Collection<ConstraintViolation<T>> violations) {

    for (ConstraintViolation<?> violation : violations) {
      String field = violation.getPropertyPath().toString();
      FieldError fieldError = errors.getFieldError(field);
      if (fieldError == null || !fieldError.isBindingFailure()) {
        errors.rejectValue(field, violation.getConstraintDescriptor().getAnnotation().annotationType()
          .getSimpleName(), getArgumentsForConstraint(errors.getObjectName(), field, violation
          .getConstraintDescriptor()), violation.getMessage());
      }
    }

  }

  private static Object[] getArgumentsForConstraint(String objectName, String field,
                                                    ConstraintDescriptor<?> descriptor) {
    List<Object> arguments = new LinkedList<Object>();
    String[] codes = new String[]{objectName + Errors.NESTED_PATH_SEPARATOR + field, field};
    arguments.add(new DefaultMessageSourceResolvable(codes, field));
    arguments.addAll(descriptor.getAttributes().values());
    return arguments.toArray(new Object[arguments.size()]);
  }


    private Errors validateUniquePropertyName(SystemKpiModel systemKpiModel, Errors errors) {
        if (systemKpiModel.getNodePropertiesList() != null) {
            int index = 0;
            List<String> propNames = new ArrayList<String>();
            for (NodeProperties nodeProperties : systemKpiModel.getNodePropertiesList()) {
                String propName = nodeProperties.getPropertyName();
                if (propNames.contains(propName.toUpperCase())) {
                    errors.rejectValue("nodePropertiesList[" + index + "].propertyName",
                            "nodeProperties.duplicate.propertyName",
                            "<label class='error'>Property name must be unique per node!</label>");
                }
                else {
                    propNames.add(propName.toUpperCase());
                }
                index++;
            }
        }
        return errors;
    }
}
