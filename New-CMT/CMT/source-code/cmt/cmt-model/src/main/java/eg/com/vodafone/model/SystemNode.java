package eg.com.vodafone.model;

/**
 * @author marwa.goda
 * @since 4/30/13
 */
public class SystemNode {

  long systemNodeId;
    String systemName;
    String nodeName;
    String inUse;
    String description;

  public long getSystemNodeId() {
    return systemNodeId;
  }

  public void setSystemNodeId(long systemNodeId) {
    this.systemNodeId = systemNodeId;
  }

  public String getSystemName() {
    return systemName;
  }

  public void setSystemName(String systemName) {
    this.systemName = systemName;
  }

  public String getNodeName() {
    return nodeName;
  }

  public void setNodeName(String nodeName) {
    this.nodeName = nodeName;
  }

  public String getInUse() {
    return inUse;
  }

  public void setInUse(String inUse) {
    this.inUse = inUse;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }


}
