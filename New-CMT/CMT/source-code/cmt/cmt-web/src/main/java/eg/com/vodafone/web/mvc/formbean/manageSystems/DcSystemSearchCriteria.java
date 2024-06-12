package eg.com.vodafone.web.mvc.formbean.manageSystems;

import eg.com.vodafone.web.mvc.model.searchcriteria.SearchCriteria;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * Author : basma.alkerm
 * Date   : 4/15/13
 * Time   : 8:54 AM
 */
public class DcSystemSearchCriteria  extends SearchCriteria implements Serializable {
    @NotEmpty
    private String keyword;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
