package eg.com.vodafone.web.mvc.formbean.dataCollection;

/**
 * Create By: Radwa Osama
 * Date: 4/5/13, 11:47 AM
 */
public enum DataCollectionType {

  None("Select System"),
  TEXT("Text"),
  XML("Xml"),
  DB("DB");

  private String description;

  DataCollectionType(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }
}
