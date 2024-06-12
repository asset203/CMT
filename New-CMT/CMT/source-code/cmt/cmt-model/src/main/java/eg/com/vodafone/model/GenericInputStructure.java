package eg.com.vodafone.model;

/**
 * Created with IntelliJ IDEA.
 * Author : basma.alkerm
 * Date   : 4/10/13
 * Time   : 1:19 PM
 */
public class GenericInputStructure extends VInputStructure {

    public static final int DELIMITER_EXTRACTOR = 0;
    public static final int REGULAR_EXP_EXTRACTOR = 1;
    public static final int EXCEL_EXTRACTOR = 2;
    public static final int DATABASE_EXTRACTOR = 3;
    public static final int JAVA_CODE_EXTRACTOR = 4;

    private int ignoredLinesCount;
    private boolean useHeaders;
    private String controlFileName;
    private int extractionMethod;
    private Object extractionParameter;
    private ExtractionField[] extractionFields;
    private GenericMapping outputMapping;
    private boolean extractDate;
    private String dateFormat;

    public void setControlFileName(String controlFileName) {
        this.controlFileName = controlFileName;
    }

    public String getControlFileName() {
        return controlFileName;
    }

    public void setExtractionMethod(int extractionMethod) {
        this.extractionMethod = extractionMethod;
    }

    public int getExtractionMethod() {
        return extractionMethod;
    }

    public void setExtractionParameter(Object extractionParameter) {
        this.extractionParameter = extractionParameter;
    }

    public Object getExtractionParameter() {
        return extractionParameter;
    }

    public void setExtractionFields(ExtractionField[] extractionFields) {
        this.extractionFields = extractionFields;
    }

    public ExtractionField[] getExtractionFields() {
        return extractionFields;
    }

    public void setExtractionSQL(String extractionSQL) {
        super.setExtractionSql( extractionSQL);
    }

    public String getExtractionSQL() {
        return super.getExtractionSql();
    }

    public void setOutputMapping(GenericMapping outputMapping) {
        this.outputMapping = outputMapping;
    }

    public GenericMapping getOutputMapping() {
        return outputMapping;
    }

    public boolean isUseHeaders() {
        return useHeaders;
    }

    public void setUseHeaders(boolean useHeaders) {
        this.useHeaders = useHeaders;
    }

    public int getIgnoredLinesCount() {
        return ignoredLinesCount;
    }

    public void setIgnoredLinesCount(int ignoredLinesCount) {
        this.ignoredLinesCount = ignoredLinesCount;
    }

    public boolean isExtractDate() {
        return extractDate;
    }

    public void setExtractDate(boolean extractDate) {
        this.extractDate = extractDate;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }
}
