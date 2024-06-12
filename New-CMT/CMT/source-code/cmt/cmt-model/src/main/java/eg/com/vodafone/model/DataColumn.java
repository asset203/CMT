package eg.com.vodafone.model;

import eg.com.vodafone.model.enums.DataColumnType;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: basma.alkerm
 * Date: 3/13/13
 * Time: 9:47 AM
 * To change this template use File | Settings | File Templates.
 */
public class DataColumn implements Serializable {

    private String name;
    private int typeCode;
    private int index;
    private String srcColumn;
    private String defaultValue;
    private boolean active;


    private String sqlExpression;
    private String dateFormat;
    private int strSize;
    private String inputStructureId;
    
    private int kpiType;

    public DataColumn(){

    }
    public DataColumn(String name, String type) {
        this.name = name;
        DataColumnType dataColumnType = DataColumnType.getDataColumnType(type);
        if(dataColumnType!=null){
            this.typeCode = DataColumnType.getDataColumnType(type).getTypeCode();
        }
    }

    public int getStrSize() {
        return strSize;
    }

    public void setStrSize(int strSize) {
        this.strSize = strSize;
    }

    public String getInputStructureId() {
        return inputStructureId;
    }

    public void setInputStructureId(String inputStructureId) {
        this.inputStructureId = inputStructureId;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getSqlExpression() {
        return sqlExpression;
    }

    public void setSqlExpression(String sqlExpression) {
        this.sqlExpression = sqlExpression;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public String getSrcColumn() {
        return srcColumn;
    }

    public void setSrcColumn(String srcColumn) {
        this.srcColumn = srcColumn;
    }

    public int getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(int typeCode) {
        this.typeCode = typeCode;
    }

    public int getKpiType() {
        return kpiType;
    }

    public void setKpiType(int kpiType) {
        this.kpiType = kpiType;
    }
    
    public String getType(){
        DataColumnType type = DataColumnType.getDataColumnType(typeCode);
        if(type == null){
            return "";
        }
        return type.getName();
    }
    public void setType(String typeName){
        DataColumnType type = DataColumnType.getDataColumnType(typeName);
        if(type != null){
           this.typeCode = type.getTypeCode();
        }
    }
}

