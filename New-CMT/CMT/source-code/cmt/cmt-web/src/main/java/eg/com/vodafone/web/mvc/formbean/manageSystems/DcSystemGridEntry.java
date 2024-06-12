package eg.com.vodafone.web.mvc.formbean.manageSystems;

import eg.com.vodafone.web.mvc.model.searchresult.SearchResult;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * Author : basma.alkerm
 * Date   : 4/15/13
 * Time   : 9:20 AM
 */
public class DcSystemGridEntry extends SearchResult implements Serializable {
    private String name;

    public DcSystemGridEntry() {
    }

    public DcSystemGridEntry(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
