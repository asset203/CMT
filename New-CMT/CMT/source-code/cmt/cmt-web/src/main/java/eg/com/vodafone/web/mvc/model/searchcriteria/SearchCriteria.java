package eg.com.vodafone.web.mvc.model.searchcriteria;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author rania.helal
 * @since Jun 29, 2011
 */
public class SearchCriteria implements Serializable  {
    private List<SortField> sortFields;

    public SearchCriteria(){
        sortFields = new ArrayList<SortField>();
    }

    public List<SortField> getSortFields() {
        return sortFields;
    }

    public void setSortFields(List<SortField> sortFields) {
        this.sortFields = sortFields;
    }

}
