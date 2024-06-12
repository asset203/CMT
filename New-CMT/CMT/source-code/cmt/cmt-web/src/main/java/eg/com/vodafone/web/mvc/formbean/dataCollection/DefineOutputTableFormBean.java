package eg.com.vodafone.web.mvc.formbean.dataCollection;

import eg.com.vodafone.model.DataColumn;

import java.util.List;
import java.util.Map;

/**
 * @author Radwa Osama
 * @since 4/10/13
 */
public class DefineOutputTableFormBean {

  private Map<String, NODE_NAME> nodeNameTypes;

  private NODE_NAME nodeName;

  private OUTPUT_TABLE_OPTION outputTableOption;

  private String errorMessage;

  private boolean truncateBeforeInsertion;

  private List<String> existingTablesNames;

  private String existingTable;

  private List<DataColumn> existingTableColumns;

  private Map<String, String> databaseDataTypes;

  private Map<String, Type> dataTypes;

  private List<SourceColumn> newOutputTableColumns;

  private SourceColumn sourceColumn;

  private boolean tableSelected;

  private String dateColumn;

  private boolean addAutoFilledDateColumn;

  private String autoFilledDateColumnName;
  
  private boolean isPartitioned;
  
  private String partitionColumnName;

    public boolean isIsPartitioned() {
        return isPartitioned;
    }

    public void setIsPartitioned(boolean isPartitioned) {
        this.isPartitioned = isPartitioned;
    }

    public String getPartitionColumnName() {
        return partitionColumnName;
    }

    public void setPartitionColumnName(String partitionColumnName) {
        this.partitionColumnName = partitionColumnName;
    }
  

  public OUTPUT_TABLE_OPTION getOutputTableOption() {
    return outputTableOption;
  }

  public void setOutputTableOption(OUTPUT_TABLE_OPTION outputTableOption) {
    this.outputTableOption = outputTableOption;
  }

  public Map<String, NODE_NAME> getNodeNameTypes() {
    return nodeNameTypes;
  }

  public void setNodeNameTypes(Map<String, NODE_NAME> nodeNameTypes) {
    this.nodeNameTypes = nodeNameTypes;
  }

  public NODE_NAME getNodeName() {
    return nodeName;
  }

  public void setNodeName(NODE_NAME nodeName) {
    this.nodeName = nodeName;
  }

  public boolean isTruncateBeforeInsertion() {
    return truncateBeforeInsertion;
  }

  public void setTruncateBeforeInsertion(boolean truncateBeforeInsertion) {
    this.truncateBeforeInsertion = truncateBeforeInsertion;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  public List<String> getExistingTablesNames() {
    return existingTablesNames;
  }

  public void setExistingTablesNames(List<String> existingTablesNames) {
    this.existingTablesNames = existingTablesNames;
  }

  public String getExistingTable() {
    return existingTable;
  }

  public void setExistingTable(String existingTable) {
    this.existingTable = existingTable;
  }

  public List<DataColumn> getExistingTableColumns() {
    return existingTableColumns;
  }

  public void setExistingTableColumns(List<DataColumn> existingTableColumns) {
    this.existingTableColumns = existingTableColumns;
  }

  public Map<String, String> getDatabaseDataTypes() {
    return databaseDataTypes;
  }

  public void setDatabaseDataTypes(Map<String, String> databaseDataTypes) {
    this.databaseDataTypes = databaseDataTypes;
  }

  public List<SourceColumn> getNewOutputTableColumns() {
    return newOutputTableColumns;
  }

  public void setNewOutputTableColumns(List<SourceColumn> newOutputTableColumns) {
    this.newOutputTableColumns = newOutputTableColumns;
  }

  public SourceColumn getSourceColumn() {
    return sourceColumn;
  }

  public void setSourceColumn(SourceColumn sourceColumn) {
    this.sourceColumn = sourceColumn;
  }

  public Map<String, Type> getDataTypes() {
    return dataTypes;
  }

  public void setDataTypes(Map<String, Type> dataTypes) {
    this.dataTypes = dataTypes;
  }



  public boolean isTableSelected() {
    return tableSelected;
  }

    public String getDateColumn() {
        return dateColumn;
    }

    public void setDateColumn(String dateColumn) {
        this.dateColumn = dateColumn;
    }

    public void setTableSelected(boolean tableSelected) {
    this.tableSelected = tableSelected;
  }

    public boolean isAddAutoFilledDateColumn() {
        return addAutoFilledDateColumn;
    }

    public void setAddAutoFilledDateColumn(boolean addAutoFilledDateColumn) {
        this.addAutoFilledDateColumn = addAutoFilledDateColumn;
    }

    public String getAutoFilledDateColumnName() {
        return autoFilledDateColumnName;
    }

    public void setAutoFilledDateColumnName(String autoFilledDateColumnName) {
        this.autoFilledDateColumnName = autoFilledDateColumnName;
    }
}
