package eg.com.vodafone.web.mvc.formbean.dataCollection;

/**
 * @author Radwa Osama
 * @since 4/16/13
 */
public enum XMLComplexity {

  SIMPLE("Simple"),
  VENDOR_SPECIFIC("Vendor Specific");

  private String description;

  XMLComplexity(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }
}
