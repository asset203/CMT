package eg.com.vodafone.web.mvc.formbean.dataCollection;

/**
 * @author Radwa Osama
 * @since 4/8/13
 */
public class SourceColumn {

    private boolean selected;

    private String sampleValue;

    private String name;

    private int index = -1;

    private String defaultValue;

    private Type type;

    private String customType;

    private boolean hasError;

    private String sqlExpression;

    private String outputColumnName;

    //Added by Awad 27-9-2018
    //configuration for CMT DashBoard
    private int kpiValue;
    //End Awad
    
    
    
    public SourceColumn() {
    }

    public SourceColumn(String sampleValue, String name, String defaultValue, Type type, String customType, int index , int kpiValue) {
        this.sampleValue = sampleValue;
        this.name = name;
        this.defaultValue = defaultValue;
        this.type = type;
        this.customType = customType;
        this.index = index;
        this.kpiValue = kpiValue;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getSampleValue() {
        return sampleValue;
    }

    public void setSampleValue(String sampleValue) {
        this.sampleValue = sampleValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getCustomType() {
        return customType;
    }

    public void setCustomType(String customType) {
        this.customType = customType;
    }

    public boolean isHasError() {
        return hasError;
    }

    public void setHasError(boolean hasError) {
        this.hasError = hasError;
    }

    public String getSqlExpression() {
        return sqlExpression;
    }

    public void setSqlExpression(String sqlExpression) {
        this.sqlExpression = sqlExpression;
    }

    public String getOutputColumnName() {
        return outputColumnName;
    }

    public void setOutputColumnName(String outputColumnName) {
        this.outputColumnName = outputColumnName;
    }

    //Added by Awad 27-9-2018
    //configuration for CMT DashBoard
    public int getKpiValue() {
        return kpiValue;
    }

    public void setKpiValue(int kpiValue) {
        this.kpiValue = kpiValue;
    }
    
    //End Awad

    

    
}
