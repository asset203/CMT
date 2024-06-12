package eg.com.vodafone.web.mvc.formbean.manageSystems;

/**
 * Created with IntelliJ IDEA.
 * Author : basma.alkerm
 * Date   : 4/19/13
 * Time   : 9:09 AM
 */
public enum FileType {

    Text(1,"Text"),
    Excel(2,"Excel"),
    XML(3,"Xml");

    private int code;
    private String label;

    private FileType(int code, String label){
        this.code=code;
        this.label = label;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
