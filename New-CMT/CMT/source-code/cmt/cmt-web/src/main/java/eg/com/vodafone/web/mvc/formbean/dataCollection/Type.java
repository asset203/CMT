package eg.com.vodafone.web.mvc.formbean.dataCollection;

/**
 * @author Radwa Osama
 * @since 4/8/13
 */
public enum Type {

  STRING("String", "Enter expected max number of characters", true),
  NUMBER("Number", false),
  FLOAT("Float", false),
  DATE("Date", "Enter date format ex\"dd\\mm\\yyy\"",true);

  private String description;
  private String hint;
  private boolean customisable;

  private Type(String description, boolean customisable) {
    this.description = description;
    this.customisable = customisable;
  }

  private Type(String description, String hint, boolean customisable) {
    this.description = description;
    this.hint = hint;
    this.customisable = customisable;
  }

  public String getDescription() {
    return description;
  }

  public boolean isCustomisable() {
    return customisable;
  }

  public String getHint() {
    return hint;
  }
}
