package eg.com.vodafone.web.mvc.formbean.dataCollection;

/**
 * @author Radwa Osama
 * @since 4/7/13
 */
public enum Header {

  DONT_USE("Do not use headers", "The tool extracts all the data from the file based on the fact that it has no header line."),
  USE_NOW_ONLY("Use headers now only", "The tool extracts all the data from the file based on the fact that it has no header line."+
          " Yet the currently uploaded sample file contains a header line that should be omitted."),
  ALWAYS_USE("Use headers always", "The tool extracts data from the file omitting the first line all the time based on the fact it is a header.");

  private String description;
  private String hint;

  Header(String description, String hint) {
     this.description = description;
     this.hint = hint;
   }

   public String getDescription() {
     return description;
   }

  public String getHint() {
    return hint;
  }
}
