package eg.com.vodafone.web.mvc.model;

/**
 * @author marwa.goda
 * @since 4/28/13
 */
public enum JobLevel {
  NODE_LEVEL("Node Level Job"),
  SYSTEM_LEVEL("System Level Job");
  String levelName;

  JobLevel(String levelName) {
    this.levelName = levelName;
  }

  @Override
  public String toString() {
    return this.levelName;
  }
}
