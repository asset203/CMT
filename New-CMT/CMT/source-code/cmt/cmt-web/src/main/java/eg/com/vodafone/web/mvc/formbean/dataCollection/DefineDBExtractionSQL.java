package eg.com.vodafone.web.mvc.formbean.dataCollection;

import java.util.Map;

/**
 * @author Radwa Osama
 * @since 4/15/13
 */
public class DefineDBExtractionSQL {

  private Map<String, DBType> dbTypes;

  private DBType selectedDBType;

  private String columns;

  private String query;

  public Map<String, DBType> getDbTypes() {
    return dbTypes;
  }

  public void setDbTypes(Map<String, DBType> dbTypes) {
    this.dbTypes = dbTypes;
  }

  public DBType getSelectedDBType() {
    return selectedDBType;
  }

  public void setSelectedDBType(DBType selectedDBType) {
    this.selectedDBType = selectedDBType;
  }

  public String getColumns() {
    return columns;
  }

  public void setColumns(String columns) {
    this.columns = columns;
  }

  public String getQuery() {
    return query;
  }

  public void setQuery(String query) {
    this.query = query;
  }
}
