package eg.com.vodafone.web.mvc.formbean.dataCollection;

import eg.com.vodafone.web.mvc.model.searchcriteria.SearchCriteria;

import java.io.Serializable;

/**
 * @author Radwa Osama
 * @since 4/4/13
 */
public class DataCollectionSearchCriteria extends SearchCriteria implements Serializable {

  private String keyword;

  public String getKeyword() {
    return keyword;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }
}
