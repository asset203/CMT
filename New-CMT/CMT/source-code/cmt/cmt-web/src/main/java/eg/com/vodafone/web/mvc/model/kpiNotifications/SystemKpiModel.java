package eg.com.vodafone.web.mvc.model.kpiNotifications;

import eg.com.vodafone.model.NodeProperties;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author marwa.goda
 * @since 4/29/13
 */
public class SystemKpiModel implements Serializable {
  private static final long serialVersionUID = 1;


  String systemName;

  @NotNull
  long nodeID;

  @NotNull
  Boolean inUse;

  NodeProperties nodeProperties;

  //@NotEmpty(message = "<label class='error'> at least one Node property must be added </label>")//"NotEmpty.formBean.nodePropertiesList"
    //basma: validation added in KPIValidator to be able to change the error default code
  @Valid
  List<NodeProperties> nodePropertiesList;


  public String getSystemName() {
    return systemName;
  }

  public void setSystemName(String systemName) {
    this.systemName = systemName;
  }

  public long getNodeID() {
    return nodeID;
  }

  public void setNodeID(long nodeID) {
    this.nodeID = nodeID;
  }


  public Boolean getInUse() {
    return inUse;
  }

  public void setInUse(Boolean inUse) {
    this.inUse = inUse;
  }

  public List<NodeProperties> getNodePropertiesList() {
    return nodePropertiesList;
  }

  public void setNodePropertiesList(List<NodeProperties> nodePropertiesList) {
    this.nodePropertiesList = nodePropertiesList;
  }


  public NodeProperties getNodeProperties() {
    return nodeProperties;
  }

  public void setNodeProperties(NodeProperties nodeProperties) {
    this.nodeProperties = nodeProperties;
  }
}
