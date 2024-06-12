package eg.com.vodafone.web.mvc.util;

/**
 * @author marwa.goda
 * @since 4/30/13
 */
public class ControllersUtil {

  private static final int ONE_ROW_AFFECTED = 1;

  public static boolean isSuccessfulTransaction(int affectedRows) {
      boolean success = false;
      success = (affectedRows == ONE_ROW_AFFECTED) ? true : false;
      return success;
    }
}
