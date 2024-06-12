package eg.com.vodafone.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA. User: basma.alkerm Date: 3/13/13 Time: 9:47 AM To
 * change this template use File | Settings | File Templates.
 */
public class VInputStructure implements Serializable {

    private String id;
    private String lastCallClassName;
    private boolean directMapping = false;
    private List<DataColumn> columnsList;
    private boolean useUpdateEvent = false;
    private boolean truncateBeforeInsertion;
    private boolean isPartitioned;

 
    public boolean isIsPartitioned() {
        return isPartitioned;
    }

    public void setIsPartitioned(boolean isPartitioned) {
        this.isPartitioned = isPartitioned;
    }

    public String getPartitionColumnName() {
        return   outputTableInfo.partitionColumnName;
    }

    public void setPartitionColumnName(String partitionColumnName) {
       outputTableInfo.partitionColumnName = partitionColumnName;
    }

    //new attributes to be used by "Generic" input structures
    private String extractionSql;
    /*for generic-text and generic-xml*/
    private int nodeColumnType;
    private int type;
    private String dateFormat;
    private boolean extractDate;
    private boolean extractDateMonthly;
    private String extractionDateColumn;
    private OutputTableInfo outputTableInfo;

    public VInputStructure() {
        this.outputTableInfo = new OutputTableInfo();

    }

    public void resetSuper(VInputStructure inputStructure) {
        this.id = inputStructure.id;
        this.type = inputStructure.type;
        this.lastCallClassName = inputStructure.lastCallClassName;
        this.useUpdateEvent = inputStructure.useUpdateEvent;
        this.directMapping = inputStructure.directMapping;
        this.extractDate = inputStructure.isExtractDate();
        this.dateFormat = inputStructure.dateFormat;
        this.nodeColumnType = inputStructure.nodeColumnType;
        this.columnsList = inputStructure.columnsList;
        this.truncateBeforeInsertion = inputStructure.isTruncateBeforeInsertion();
        this.extractionSql = inputStructure.extractionSql;
        this.extractionDateColumn = inputStructure.extractionDateColumn;
        this.outputTableInfo
                = new OutputTableInfo(inputStructure.getOutputTableInfo());

    }

    public VInputStructure(VInputStructure inputStructure) {
        resetSuper(inputStructure);
    }

    public String getAutoFilledDateColumn() {
        return outputTableInfo.autoFilledDateColumnName;
    }

    public void setAutoFilledDateColumn(String autoFilledDateColumn) {
        this.outputTableInfo.autoFilledDateColumnName = autoFilledDateColumn;
    }

    public OutputTableInfo getOutputTableInfo() {
        return outputTableInfo;
    }

    public void setOutputTableInfo(OutputTableInfo outputTableInfo) {
        this.outputTableInfo = outputTableInfo;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public boolean isExtractDate() {
        return extractDate;
    }

    public void setExtractDate(boolean extractDate) {
        this.extractDate = extractDate;
    }

    public boolean isExtractDateMonthly() {
        return extractDateMonthly;
    }

    public void setExtractDateMonthly(boolean extractDateMonthly) {
        this.extractDateMonthly = extractDateMonthly;
    }

    public String getExtractionSql() {
        return extractionSql;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setExtractionSql(String extractionSql) {
        this.extractionSql = extractionSql;
    }

    public int getNodeColumnType() {
        return nodeColumnType;
    }

    public void setNodeColumnType(int nodeColumnType) {
        this.nodeColumnType = nodeColumnType;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getLastCallClassName() {
        return lastCallClassName;
    }

    public void setLastCallClassName(String lastCallClassName) {
        this.lastCallClassName = lastCallClassName;
    }

    public boolean isDirectMapping() {
        return directMapping;
    }

    public void setDirectMapping(boolean directMapping) {
        this.directMapping = directMapping;
    }

    public String getMappedTable() {
        return outputTableInfo.tableName;
    }

    public void setMappedTable(String mappedTable) {
        this.outputTableInfo.tableName = mappedTable;
    }

    public String getNodeColumn() {
        return outputTableInfo.nodeColumnName;
    }

    public void setNodeColumn(String nodeColumn) {
        this.outputTableInfo.nodeColumnName = nodeColumn;
    }

    public String getIdColumn() {
        return outputTableInfo.idColumnName;
    }

    public void setIdColumn(String idColumn) {
        this.outputTableInfo.idColumnName = idColumn;
    }

    public String getSeqName() {
        return outputTableInfo.sequenceName;
    }

    public void setSeqName(String seqName) {
        this.outputTableInfo.sequenceName = seqName;
    }

    public boolean isUseUpdateEvent() {
        return useUpdateEvent;
    }

    public List<DataColumn> getColumnsList() {
        return columnsList;
    }

    public void setColumnsList(List<DataColumn> columnsList) {
        this.columnsList = columnsList;
    }

    public void setUseUpdateEvent(boolean useUpdateEvent) {
        this.useUpdateEvent = useUpdateEvent;
    }

    public String getDateColumn() {
        return outputTableInfo.deleteConditionDateColumn;
    }

    public void setDateColumn(String dateColumn) {
        this.outputTableInfo.deleteConditionDateColumn = dateColumn;
    }

    /*@Override
    public Object clone() throws CloneNotSupportedException {
        try{
            VInputStructure clonedInputStructure = (VInputStructure) super.clone();
            return clonedInputStructure;
        }catch (CloneNotSupportedException e) {
            throw new InternalError(e.toString());
        }
    }*/
    public DataColumn[] getColumns() {
        return this.columnsList.toArray(new DataColumn[]{});
    }

    public boolean isTruncateBeforeInsertion() {
        return truncateBeforeInsertion;
    }

    public String getExtractionDateColumn() {
        return extractionDateColumn;
    }

    public void setExtractionDateColumn(String extractionDateColumn) {
        this.extractionDateColumn = extractionDateColumn;
    }

    public void setTruncateBeforeInsertion(boolean truncateBeforeInsertion) {
        this.truncateBeforeInsertion = truncateBeforeInsertion;
    }

    class OutputTableInfo {

        private String tableName;
        private String nodeColumnName;
        private String idColumnName;
        private String deleteConditionDateColumn;
        private String autoFilledDateColumnName;
        private String sequenceName;
        private String partitionColumnName;


        private OutputTableInfo() {
        }

        private OutputTableInfo(OutputTableInfo outputTableInfo1) {
            this.tableName = outputTableInfo1.tableName;
            this.sequenceName = outputTableInfo1.sequenceName;
            this.nodeColumnName = outputTableInfo1.nodeColumnName;
            this.idColumnName = outputTableInfo1.idColumnName;
            this.deleteConditionDateColumn = outputTableInfo1.deleteConditionDateColumn;
            this.autoFilledDateColumnName = outputTableInfo1.autoFilledDateColumnName;
            this.partitionColumnName = outputTableInfo1.partitionColumnName;
        }
    }

}
