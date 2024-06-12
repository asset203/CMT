package eg.com.vodafone.web.mvc.formbean.dataCollection;

import eg.com.vodafone.model.enums.NodeColumnType;

/**
 * @author Radwa Osama
 * @since 4/10/13
 */
public enum NODE_NAME {

  CONFIGURABLE("Configurable", "The tool automatically adds a column named \"NODE_NAME\" and inserts in it the name of the node it extracts data."),
  MAPPED("Mapped", "The tool adds a column named \"NODE_NAME\" and prompts you in the next step to map a column from the source to it."),
  NON("Don't use", "The tool does not add a column named \"NODE_NAME\".");

  private String description;
  private String hint;

  private NODE_NAME(String description, String hint) {
    this.description = description;
    this.hint = hint;
  }

  public String getDescription() {
    return description;
  }

  public String getHint() {
    return hint;
  }

  public static NODE_NAME getNodeName(int typeCode) {

    if (typeCode == 1)
      return CONFIGURABLE;

    if (typeCode == 2)
      return MAPPED;

    return NON;
  }

  public static NODE_NAME getNodeName(NodeColumnType nodeColumnType) {
    switch (nodeColumnType) {
      case CONFIGURABLE:
        return CONFIGURABLE;
      case MAPPED:
        return MAPPED;
      case NON:
        return NON;
    }

    return NON;
  }
}
