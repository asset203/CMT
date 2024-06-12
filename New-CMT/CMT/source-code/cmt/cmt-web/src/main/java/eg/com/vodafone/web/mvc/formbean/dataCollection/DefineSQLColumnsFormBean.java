package eg.com.vodafone.web.mvc.formbean.dataCollection;

import java.util.List;
import java.util.Map;

/**
 * @author Radwa Osama
 * @since 4/10/13
 */
public class DefineSQLColumnsFormBean {

  private List<SourceColumn> sqlExpressionColumns;

  private Map<String, Type> dataTypes;

  private SourceColumn sourceColumn;

  private String whereClause;

  private String groupByClause;

  private String havingClause;

  private String queryError;

  private String extractionSql;

  public List<SourceColumn> getSqlExpressionColumns() {
    return sqlExpressionColumns;
  }

  public void setSqlExpressionColumns(List<SourceColumn> sqlExpressionColumns) {
    this.sqlExpressionColumns = sqlExpressionColumns;
  }

  public Map<String, Type> getDataTypes() {
    return dataTypes;
  }

  public void setDataTypes(Map<String, Type> dataTypes) {
    this.dataTypes = dataTypes;
  }

  public SourceColumn getSourceColumn() {
    return sourceColumn;
  }

  public void setSourceColumn(SourceColumn sourceColumn) {
    this.sourceColumn = sourceColumn;
  }

  public String getWhereClause() {
    return whereClause;
  }

  public void setWhereClause(String whereClause) {
    this.whereClause = whereClause;
  }

  public String getGroupByClause() {
    return groupByClause;
  }

  public void setGroupByClause(String groupByClause) {
    this.groupByClause = groupByClause;
  }

  public String getHavingClause() {
    return havingClause;
  }

  public void setHavingClause(String havingClause) {
    this.havingClause = havingClause;
  }

  public String getQueryError() {
    return queryError;
  }

  public void setQueryError(String queryError) {
    this.queryError = queryError;
  }

    public String getExtractionSql() {
        return extractionSql;
    }

    public void setExtractionSql(String extractionSql) {
        this.extractionSql = extractionSql;
    }
}
