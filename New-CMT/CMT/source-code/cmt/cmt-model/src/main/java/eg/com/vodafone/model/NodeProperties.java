package eg.com.vodafone.model;

import eg.com.vodafone.model.constants.CMTConstants;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @author marwa.goda
 * @since 4/29/13
 */
public class NodeProperties {

  @NotNull(message = "field.required")
  long nodeId;

  @NotEmpty(message ="<label class='error'>property name is required.</label>" )
  @Size(min = 1, max = 20, message = "<label class='error'>Length of property name must be between 1 and 20.</label>")
  @Pattern(regexp = CMTConstants.PROPERTY_NAME_PATTERN, message = "<label class='error'>Invalid property name format.</label>")
  String propertyName;

  @NotEmpty(message = "<label class='error'>grain is required.</label>")
  @Size(min = 1, max = 1, message = "<label class='error'>grain Length should be one character.</label>")
  String grain;

  @NotNull(message = "<label class='error'>property value is required.</label>")
  @NumberFormat(style = NumberFormat.Style.NUMBER)
  @Range(min = 0, message = "<label class='error'>property value should be positive.</label>")
  long value;

  @NotEmpty(message = "<label class='error'>trafficTableName is required.</label>")
  @Size(min = 1, max = 30, message = "<label class='error'>traffic table name Length must be between 30 and 1.</label>" )
  @Pattern(regexp = CMTConstants.TRAFFIC_TABLE_NAME_PATTERN, message = "<label class='error'>Invalid traffic table name format.</label>")
  String trafficTableName;

  @NotNull(message = "<label class='error'>notificationThreshold is required.</label>")
  @NumberFormat(style = NumberFormat.Style.NUMBER)
  @Range(min = 0, max = 100, message = "<label class='error'>notification threshold should be between 0 and 100.</label>")
  double notificationThreshold;

  public long getNodeId() {
    return nodeId;
  }

  public void setNodeId(long nodeId) {
    this.nodeId = nodeId;
  }

  public String getPropertyName() {
    return propertyName;
  }

  public void setPropertyName(String propertyName) {
    this.propertyName = propertyName;
  }

  public long getValue() {
    return value;
  }

  public void setValue(long value) {
    this.value = value;
  }

  public String getGrain() {
    return grain;
  }

  public void setGrain(String grain) {
    this.grain = grain;
  }

  public String getTrafficTableName() {
    return trafficTableName;
  }

  public void setTrafficTableName(String trafficTableName) {
    this.trafficTableName = trafficTableName;
  }

  public double getNotificationThreshold() {
    return notificationThreshold;
  }

  public void setNotificationThreshold(double notificationThreshold) {
    this.notificationThreshold = notificationThreshold;
  }


}
