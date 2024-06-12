package eg.com.vodafone.web.mvc.model;

/**
 * @author marwa.goda
 * @since 4/23/13
 */
public enum JobAction {
  SAVE_JOB("saveNewJob"),
  UPDATE_JOB("updateJob");
  String actionName;

  JobAction(String actionName) {
    this.actionName = actionName;
  }

  @Override
  public String toString() {
    return this.actionName;
  }
}
