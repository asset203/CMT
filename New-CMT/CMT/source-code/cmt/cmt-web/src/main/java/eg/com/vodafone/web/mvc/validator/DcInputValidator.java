package eg.com.vodafone.web.mvc.validator;

import eg.com.vodafone.model.enums.DataBaseType;
import eg.com.vodafone.model.enums.InputAccessMethod;
import eg.com.vodafone.service.DataCollectionServiceInterface;
import eg.com.vodafone.web.mvc.formbean.manageSystems.DcInputFormBean;
import eg.com.vodafone.web.mvc.formbean.manageSystems.FileType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static eg.com.vodafone.web.mvc.util.CMTConstants.*;

/**
 * Created with IntelliJ IDEA.
 * Author : basma.alkerm
 * Date   : 4/23/13
 * Time   : 10:29 AM
 */
@Component
public class DcInputValidator  implements Validator {


    @Autowired
    private RegexValidator regexValidator;

    @Autowired
    DataCollectionServiceInterface dataCollectionService;

    @Override
    public boolean supports(Class<?> aClass) {
        return DcInputFormBean.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object model, Errors errors) {
       DcInputFormBean inputFormBean = (DcInputFormBean)model;
        //1- validate common required fields
        if(StringUtils.hasText(inputFormBean.getInputName())){
            regexValidator.validate(inputFormBean.getInputName(),errors,"inputName",VALID_INPUT_NAME_PATTERN);
        }
        if(StringUtils.hasText(inputFormBean.getPort())){
            regexValidator.validate(inputFormBean.getPort(),errors,"port",PORT_PATTERN);
        }
        //2- validate not empty data collection list
        if(inputFormBean.getDataCollections() == null || inputFormBean.getDataCollections().isEmpty()){
                      errors.rejectValue("dataCollections","dcInputFormBean.dataCollections.notEmpty");
        }
        else{
            //3- validate correct data collection list size
            if(inputFormBean.getInputAccessMethod() == InputAccessMethod.DB_ACCESS ||
                    ( inputFormBean.getInputAccessMethod() != InputAccessMethod.DB_ACCESS && inputFormBean.getFileType() == FileType.Excel) )
            {
                if(inputFormBean.getDataCollections().size() >1){
                    errors.rejectValue("dataCollections","dcInputFormBean.dataCollections.size");
                }
            }
        }

        //******//

        //4- validate required fields for  DB access method inputs
        if( inputFormBean.getInputAccessMethod() == InputAccessMethod.DB_ACCESS) {

            if(inputFormBean.getDataBaseType() == DataBaseType.ORACLE_RAC){
                if(!StringUtils.hasText(inputFormBean.getTns())){
                    errors.rejectValue("tns", FIELD_REQUIRED);
                }
            }
            else
            {
                if(!StringUtils.hasText(inputFormBean.getDataBaseName())){
                    errors.rejectValue("dataBaseName", FIELD_REQUIRED);
                }
                if(!StringUtils.hasText(inputFormBean.getServer())){
                    errors.rejectValue("server", FIELD_REQUIRED);
                }
            }
        }
        //5-validate required fields for File access inputs
        else{

            if(!StringUtils.hasText(inputFormBean.getFileNamePattern())){
                errors.rejectValue("fileNamePattern", FIELD_REQUIRED);
            }else{
                regexValidator.checkValidPattern(inputFormBean.getFileNamePattern(),errors,"fileNamePattern");
            }
            if(inputFormBean.getPathsList() == null || inputFormBean.getPathsList().isEmpty()){
                errors.rejectValue("pathsList", "dcInputFormBean.pathsList.noEmpty");
            }
            if(!StringUtils.hasText(inputFormBean.getServer())){
                errors.rejectValue("server", FIELD_REQUIRED);
            }
        }

    }
}
