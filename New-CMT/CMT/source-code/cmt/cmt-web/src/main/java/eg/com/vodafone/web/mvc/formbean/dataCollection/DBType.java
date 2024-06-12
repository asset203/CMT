package eg.com.vodafone.web.mvc.formbean.dataCollection;

/**
 * @author Radwa Osama
 * @since 4/15/13
 */
public enum DBType {

  ORACLE("Oracle DB"),
  MS_SQL_SERVER("MS SQL SERVER"),
  MY_SQL("MY SQL"),
  SYBASE("SYBASE"),
  ORACLE_RAC("ORACLE RAC");

  DBType(String description) {
    this.description = description;
  }

  public static DBType getDBType(long code) {
    if (code == 1) {
      return ORACLE;
    }

    if (code == 2) {
      return MS_SQL_SERVER;
    }

    if (code == 3) {
      return MY_SQL;
    }

    if (code == 4) {
      return SYBASE;
    }

    if (code == 5) {
      return ORACLE_RAC;
    }

    return null;
  }

  public String getDescription() {
    return description;
  }

  private String description;
}
