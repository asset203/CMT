package eg.com.vodafone.model.constants;

/**
 * Created with IntelliJ IDEA.
 * User: Alia.Adel
 * Date: 4/18/13
 * Time: 9:14 AM
 */
public class CMTConstants {
  public static final String USERNAME_PATTERN = "[[[0-9][A-Za-z]|(_)|(.)]+]{4,15}";
  public static final String MOBILE_PATTERN = "^01[0-2]{1}[0-9]{8}";
  public static final String PASSWORD_PATTERN = "^.*(?=.{6,20})(?=.*\\d)(?=.*[a-zA-Z]).*$";

  //KPI nodeProperties pattern
  public static final String PROPERTY_NAME_PATTERN = "[[[0-9][A-Za-z]|(_)|(.)]+]{1,50}";
  public static final String TRAFFIC_TABLE_NAME_PATTERN ="[[[0-9][A-Za-z]|(_)|(.)]+]{1,30}";

    //Jobs regex
    //allows alphanumeric characters in addition to _ -
    public static final String JOB_NAME_REGEX = "^[a-zA-Z0-9_-]+$";
    //allows alphanumeric characters in addition to _ - & space
    public static final String JOB_DESCRIPTION_REGEX = "^[\\sa-zA-Z0-9_-]+$";
}
