package eg.com.vodafone.web.mvc.formbean.dataCollection;

/**
 * @author Radwa Osama
 * @since 4/7/13
 */
public enum Delimiter {
  DOT("\\.", "Dot \".\""),
  COMMA(",", "Comma \",\""),
  SEMICOLON(";", "Semicolon \";\""),
  PIPE("\\|", "Pipe \"|\""),
  COLON(":", "Colon \":\""),
  SLASH("/", "Slash \"/\""),
  SPACE(" ", "Space \" \""),
  TAB("  ", "Tab \"  \""),
  OTHER(null, "Other Delimiter");

  private String value;
  private String description;

  Delimiter(String value, String description) {
    this.value = value;
    this.description = description;
  }

  Delimiter(String value) {
    this(value, value);
  }

  public String getDescription() {
    return description;
  }

  public String getValue() {
    return value;
  }

  public static boolean contains(String inDelimiter) {
    for (Delimiter delimiter : Delimiter.values()) {
      if (delimiter.value.equals(inDelimiter)) {
        return true;
      }
    }

    return false;
  }

  public static Delimiter getDelimiter(String inDelimiter) {
    for (Delimiter delimiter : Delimiter.values()) {
      if (delimiter.value.equals(inDelimiter)) {
        return delimiter;
      }
    }

    return OTHER;
  }
}
