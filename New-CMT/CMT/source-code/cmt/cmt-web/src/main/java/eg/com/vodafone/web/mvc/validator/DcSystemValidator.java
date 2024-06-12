package eg.com.vodafone.web.mvc.validator;

import eg.com.vodafone.web.mvc.formbean.manageSystems.DcSystemFormBean;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created with IntelliJ IDEA.
 * Author : basma.alkerm
 * Date   : 4/23/13
 * Time   : 1:56 PM
 */
@Component
public class DcSystemValidator  implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return DcSystemFormBean.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object model, Errors errors) {
        DcSystemFormBean dcSystemFormBean = (DcSystemFormBean)model;
        if( (dcSystemFormBean.getSystem().getInputsList()== null
               || dcSystemFormBean.getSystem().getInputsList().isEmpty())
                &&(dcSystemFormBean.getSystem().getNodesList() == null || dcSystemFormBean.getSystem().getNodesList().isEmpty()) ){
            errors.rejectValue("system.nodesList","dcSystemFormBean.system.Empty");
        }

    }
}
