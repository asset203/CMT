package eg.com.vodafone.model;

/**
 * Created with IntelliJ IDEA.
 * User: basma.alkerm
 * Date: 3/13/13
 * Time: 10:09 AM
 * To change this template use File | Settings | File Templates.
 */
public class GenericTextInputStructure extends VInputStructure{

    private int ignoredLinesCount;
    private boolean useHeaders;
    private String delimiter;

   public GenericTextInputStructure(){

   }
   public GenericTextInputStructure (VInputStructure inputStructure) {
        super(inputStructure);
    }

    public boolean isUseHeaders() {
        return useHeaders;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
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

}
