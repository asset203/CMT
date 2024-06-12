package eg.com.vodafone.web.mvc.component.grid;

/**
 * @author Radwa Osama
 * @since 4/4/13
 */
public class UILink {

  private String href;
  private String dynamicHref;
  private String onClick;
  private String linkStyle;
  private String linkLabel;
  private String enable;

  public String getHref() {
    return href;
  }

  public void setHref(String href) {
    this.href = href;
  }

  public String getOnClick() {
    return onClick;
  }

  public void setOnClick(String onClick) {
    this.onClick = onClick;
  }

  public String getLinkStyle() {
    return linkStyle;
  }

  public void setLinkStyle(String linkStyle) {
    this.linkStyle = linkStyle;
  }

  public String getLinkLabel() {
    return linkLabel;
  }

  public void setLinkLabel(String linkLabel) {
    this.linkLabel = linkLabel;
  }

  public String getDynamicHref() {
    return dynamicHref;
  }

  public void setDynamicHref(String dynamicHref) {
    this.dynamicHref = dynamicHref;
  }

  public String getEnable() {
    return enable;
  }

  public void setEnable(String enable) {
    this.enable = enable;
  }
}
