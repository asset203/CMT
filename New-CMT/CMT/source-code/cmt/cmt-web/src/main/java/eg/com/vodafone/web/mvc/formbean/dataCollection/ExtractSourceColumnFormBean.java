package eg.com.vodafone.web.mvc.formbean.dataCollection;

import java.util.List;
import java.util.Map;

/**
 * @author Radwa Osama
 * @since 4/8/13
 */
public class ExtractSourceColumnFormBean {

  private List<SourceColumn> columns;

  private Map<String, Type> dataTypes;

  private boolean useDateColumn;

  private String dateColumnName;

  private String errors;

  private boolean tableCreated;

  private DateColumnPrecession dateColumnPrecession;

  private Map<String,String> dateColumnPrecessionTypes;

  public List<SourceColumn> getColumns() {
    return columns;
  }

  public void setColumns(List<SourceColumn> columns) {
    this.columns = columns;
  }

  public Map<String, Type> getDataTypes() {
    return dataTypes;
  }

  public void setDataTypes(Map<String, Type> dataTypes) {
    this.dataTypes = dataTypes;
  }

  public String getErrors() {
    return errors;
  }

  public void setErrors(String errors) {
    this.errors = errors;
  }

  public boolean isTableCreated() {
    return tableCreated;
  }

  public void setTableCreated(boolean tableCreated) {
    this.tableCreated = tableCreated;
  }

    public boolean getUseDateColumn() {
        return useDateColumn;
    }

    public void setUseDateColumn(boolean useDateColumn) {
        this.useDateColumn = useDateColumn;
    }

    public String getDateColumnName() {
        return dateColumnName;
    }

    public void setDateColumnName(String dateColumnName) {
        this.dateColumnName = dateColumnName;
    }

    public DateColumnPrecession getDateColumnPrecession() {
        return dateColumnPrecession;
    }

    public void setDateColumnPrecession(DateColumnPrecession dateColumnPrecession) {
        this.dateColumnPrecession = dateColumnPrecession;
    }

    public Map<String, String> getDateColumnPrecessionTypes() {
        return dateColumnPrecessionTypes;
    }

    public void setDateColumnPrecessionTypes(Map<String, String> dateColumnPrecessionTypes) {
        this.dateColumnPrecessionTypes = dateColumnPrecessionTypes;
    }
}
