package eg.com.vodafone.web.mvc.formbean.dataCollection;

/**
 * @author Radwa Osama
 * @since 4/4/13
 */
public enum DataCollectionWizardStep {

  DATA_COLLECTION_TYPE("Data Collection Type"),
  EXTRACT_SOURCE_DATA("Extract Source Data"),
  DEFINE_EXTRACTION_SQL_STATEMENT("Define Extraction SQL Statement"),
  EXTRACT_XML_SOURCE_COLUMN("Extract Source Columns for XML"),
  DEFINE_PLAIN_COLUMNS("Define Plain Columns"),
  DEFINE_SQL_COLUMNS("Define SQL Columns"),
  DEFINE_OUTPUT_TABLE("Define Output Table"),
  DEFINE_DATA_COLLECTION_MAPPING("Define Data Collection Mapping");

  private String description;

  DataCollectionWizardStep(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }
}
