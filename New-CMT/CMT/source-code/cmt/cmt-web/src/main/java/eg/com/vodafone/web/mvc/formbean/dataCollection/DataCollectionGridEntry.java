package eg.com.vodafone.web.mvc.formbean.dataCollection;

import eg.com.vodafone.web.mvc.model.searchresult.SearchResult;

import java.io.Serializable;

/**
 * @author Radwa Osama
 * @since 4/4/13
 */
public class DataCollectionGridEntry extends SearchResult implements Serializable {

  private String name;
  private boolean enableEdit;

  public DataCollectionGridEntry() {
  }

  public DataCollectionGridEntry(String name, boolean enableEdit) {
    this.name = name;
    this.enableEdit = enableEdit;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isEnableEdit() {
    return enableEdit;
  }

  public void setEnableEdit(boolean enableEdit) {
    this.enableEdit = enableEdit;
  }
}
