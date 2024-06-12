package eg.com.vodafone.model;

/**
 * Create By: Radwa Osama
 * Date: 4/17/13, 9:12 PM
 */
public class XmlVendor {

  private long id;

  private String name;

  public XmlVendor(long id, String name) {
    this.id = id;
    this.name = name;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
