package eg.com.vodafone.model;

/**
 * Create By: Radwa Osama
 * Date: 4/18/13, 7:21 AM
 */
public class XmlConverterElement {

  private String name;

  private int typeCode;

  public XmlConverterElement(String name, int typeCode) {
    this.name = name;
    this.typeCode = typeCode;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getTypeCode() {
    return typeCode;
  }

  public void setTypeCode(int typeCode) {
    this.typeCode = typeCode;
  }
}
