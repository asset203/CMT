package eg.com.vodafone.web.mvc.validator;

import eg.com.vodafone.web.mvc.formbean.manageSystems.DcNodeFormBean;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created with IntelliJ IDEA.
 * Author : basma.alkerm
 * Date   : 4/23/13
 * Time   : 1:30 PM
 */
@Component
public class DcNodeValidator  implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return DcNodeFormBean.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object model, Errors errors) {
       DcNodeFormBean nodeFormBean = (DcNodeFormBean)model;
       if(nodeFormBean.getNode().getInputsList() == null
               || nodeFormBean.getNode().getInputsList().isEmpty()){
           errors.rejectValue("node.inputsList","dcNodeFormBean.node.inputsList.Empty");
       }
    }
}
