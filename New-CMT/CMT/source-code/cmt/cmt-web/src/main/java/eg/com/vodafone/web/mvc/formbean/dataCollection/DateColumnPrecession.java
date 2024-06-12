package eg.com.vodafone.web.mvc.formbean.dataCollection;

/**
 * Created with IntelliJ IDEA.
 * Author : basma.alkerm
 * Date   : 7/7/13
 * Time   : 11:53 AM
 */
public enum DateColumnPrecession {

    NONE("please select",""),
    DAYS("Days","yyyy-MM-dd"),
    HOURS("Hours","yyyy-MM-dd HH");

    private String description;
    private String format;

    private DateColumnPrecession(String description,String format){
        this.description = description;
        this.format = format;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
}
