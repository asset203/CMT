package eg.com.vodafone.web.mvc.formbean.dataCollection;

import java.util.List;
import java.util.Map;

/**
 * @author Radwa Osama
 * @since 4/11/13
 */
public class DefineDataCollectionMappingFormBean {

    private List<SourceColumn> outputColumns;

    private SourceColumn nodeNameColumn;

    private String errorMessage;

    private boolean mappedNodeName;

    //Added by Awad 27-9-2018
    //configuration for CMT DashBoard
    private List<DataCollectionColumnKpiType> kpiTypesColumns;
    
    private Map<String, String> dataCollectionKpiTypes ;
    
    private String kpiType;

    //End 
    public List<SourceColumn> getOutputColumns() {
        return outputColumns;
    }

    public void setOutputColumns(List<SourceColumn> outputColumns) {
        this.outputColumns = outputColumns;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public SourceColumn getNodeNameColumn() {
        return nodeNameColumn;
    }

    public void setNodeNameColumn(SourceColumn nodeNameColumn) {
        this.nodeNameColumn = nodeNameColumn;
    }

    public boolean isMappedNodeName() {
        return mappedNodeName;
    }

    public void setMappedNodeName(boolean mappedNodeName) {
        this.mappedNodeName = mappedNodeName;
    }

    public List<DataCollectionColumnKpiType> getKpiTypesColumns() {
        return kpiTypesColumns;
    }

    public void setKpiTypesColumns(List<DataCollectionColumnKpiType> kpiTypesColumns) {
        this.kpiTypesColumns = kpiTypesColumns;
    }

    public Map<String, String> getDataCollectionKpiTypes() {
        return dataCollectionKpiTypes;
    }

    public void setDataCollectionKpiTypes(Map<String, String> dataCollectionKpiTypes) {
        this.dataCollectionKpiTypes = dataCollectionKpiTypes;
    }

    public String getKpiType() {
        return kpiType;
    }

    public void setKpiType(String kpiType) {
        this.kpiType = kpiType;
    }
}
