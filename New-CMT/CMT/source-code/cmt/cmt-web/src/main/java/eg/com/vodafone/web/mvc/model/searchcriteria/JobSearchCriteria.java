package eg.com.vodafone.web.mvc.model.searchcriteria;

import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;

/**
 * @author marwa.goda
 * @since 4/28/13
 */
public class JobSearchCriteria extends SearchCriteria implements Serializable {
    @NotEmpty
    private String keyword;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

}
