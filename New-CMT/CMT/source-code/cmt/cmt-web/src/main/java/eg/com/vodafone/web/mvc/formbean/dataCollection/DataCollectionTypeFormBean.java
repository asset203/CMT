package eg.com.vodafone.web.mvc.formbean.dataCollection;

import java.util.Map;

/**
 * Create By: Radwa Osama
 * Date: 4/5/13, 12:48 PM
 */
public class DataCollectionTypeFormBean {

  private DataCollectionType dataCollectionType;

  private Map<String, String> dataCollectionTypes;

  private String dataCollectionName;


  public DataCollectionType getDataCollectionType() {
    return dataCollectionType;
  }

  public void setDataCollectionType(DataCollectionType dataCollectionType) {
    this.dataCollectionType = dataCollectionType;
  }

  public Map<String, String> getDataCollectionTypes() {
    return dataCollectionTypes;
  }

  public void setDataCollectionTypes(Map<String, String> dataCollectionTypes) {
    this.dataCollectionTypes = dataCollectionTypes;
  }

  public String getDataCollectionName() {
    return dataCollectionName;
  }

  public void setDataCollectionName(String dataCollectionName) {
    this.dataCollectionName = dataCollectionName;
  }
}
