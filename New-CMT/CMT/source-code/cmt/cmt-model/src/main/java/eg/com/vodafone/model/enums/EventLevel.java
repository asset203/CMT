package eg.com.vodafone.model.enums;

/**
 * Created with IntelliJ IDEA.
 * User: Samaa.ElKomy
 * Date: 3/18/13
 * Time: 4:10 PM
 * To change this template use File | Settings | File Templates.
 */
public enum EventLevel {
  LOW("Low"), MEDIUM("Medium"), HIGH("High");

  private String level;

  private EventLevel(String level) {
    this.level = level;
  }

  public String getLevel() {
    return level;
  }

  public void setLevel(String level) {
    this.level = level;
  }
}
