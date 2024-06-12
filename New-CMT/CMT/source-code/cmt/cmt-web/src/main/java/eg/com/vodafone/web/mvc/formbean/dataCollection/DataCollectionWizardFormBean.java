package eg.com.vodafone.web.mvc.formbean.dataCollection;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Radwa Osama
 * @since 4/4/13
 */
public class DataCollectionWizardFormBean {

  private String uuid;

  private boolean editMode;

  private DataCollectionWizardStep currentStep;

  private List<DataCollectionWizardStep> steps;

  private DataCollectionTypeFormBean dataCollectionTypeFormBean;

  private ExtractSourceDataFormBean extractSourceDataFormBean;

  private ExtractSourceColumnFormBean extractSourceColumnFormBean;

  private DefineSQLColumnsFormBean defineSQLColumnsFormBean;

  private DefineOutputTableFormBean defineOutputTableFormBean;

  private DefineDataCollectionMappingFormBean defineDataCollectionMappingFormBean;

  private DefineDBExtractionSQL defineDBExtractionSQL;

  private ExtractXMLSourceColumns extractXMLSourceColumns;

  public DataCollectionWizardStep getCurrentStep() {
    return currentStep;
  }

  public void setCurrentStep(DataCollectionWizardStep currentStep) {
    this.currentStep = currentStep;
  }

  public List<DataCollectionWizardStep> getSteps() {
    return steps;
  }

  public void setSteps(List<DataCollectionWizardStep> steps) {
    this.steps = steps;
  }

  public DataCollectionTypeFormBean getDataCollectionTypeFormBean() {
    return dataCollectionTypeFormBean;
  }

  public void setDataCollectionTypeFormBean(DataCollectionTypeFormBean dataCollectionTypeFormBean) {
    this.dataCollectionTypeFormBean = dataCollectionTypeFormBean;
  }

  public ExtractSourceDataFormBean getExtractSourceDataFormBean() {
    return extractSourceDataFormBean;
  }

  public void setExtractSourceDataFormBean(ExtractSourceDataFormBean extractSourceDataFormBean) {
    this.extractSourceDataFormBean = extractSourceDataFormBean;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public ExtractSourceColumnFormBean getExtractSourceColumnFormBean() {
    return extractSourceColumnFormBean;
  }

  public void setExtractSourceColumnFormBean(ExtractSourceColumnFormBean extractSourceColumnFormBean) {
    this.extractSourceColumnFormBean = extractSourceColumnFormBean;
  }

  public DefineSQLColumnsFormBean getDefineSQLColumnsFormBean() {
    return defineSQLColumnsFormBean;
  }

  public void setDefineSQLColumnsFormBean(DefineSQLColumnsFormBean defineSQLColumnsFormBean) {
    this.defineSQLColumnsFormBean = defineSQLColumnsFormBean;
  }

  public DefineOutputTableFormBean getDefineOutputTableFormBean() {
    return defineOutputTableFormBean;
  }

  public void setDefineOutputTableFormBean(DefineOutputTableFormBean defineOutputTableFormBean) {
    this.defineOutputTableFormBean = defineOutputTableFormBean;
  }

  public DefineDataCollectionMappingFormBean getDefineDataCollectionMappingFormBean() {
    return defineDataCollectionMappingFormBean;
  }

  public void setDefineDataCollectionMappingFormBean(DefineDataCollectionMappingFormBean defineDataCollectionMappingFormBean) {
    this.defineDataCollectionMappingFormBean = defineDataCollectionMappingFormBean;
  }

  public boolean isEditMode() {
    return editMode;
  }

  public void setEditMode(boolean editMode) {
   this.editMode = editMode;
  }

  public DefineDBExtractionSQL getDefineDBExtractionSQL() {
    return defineDBExtractionSQL;
  }

  public void setDefineDBExtractionSQL(DefineDBExtractionSQL defineDBExtractionSQL) {
    this.defineDBExtractionSQL = defineDBExtractionSQL;
  }

  public ExtractXMLSourceColumns getExtractXMLSourceColumns() {
    return extractXMLSourceColumns;
  }

  public void setExtractXMLSourceColumns(ExtractXMLSourceColumns extractXMLSourceColumns) {
    this.extractXMLSourceColumns = extractXMLSourceColumns;
  }
}
