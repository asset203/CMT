package eg.com.vodafone.web.mvc.util;

import com.google.gson.Gson;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.security.crypto.codec.Base64;

import java.nio.charset.Charset;

/**
 * Create By: Radwa Osama
 * Date: 4/5/13, 2:34 PM
 */
public class SerializationUtil {

  public static String UTF_8 = "UTF-8";

  /**
   * @param str need to get encoded
   * @return Encode input String into a String containing characters in the alphabet
   */
  public static String encode64Based(String str) {
    return new String(Base64.encode(str.getBytes()), Charset.forName(UTF_8));
  }

  /**
   * @param str need to get encoded
   * @return Encode input String into a String containing characters in the alphabet
   */
  public static String decode64Based(String str) {
    return new String(Base64.decode(str.getBytes()), Charset.forName(UTF_8));
  }

  /**
   * Serialize input object into Json Formatted String
   *
   * @param aBean Bean  required to  be serialized
   * @return String representation
   */
  public static String serializeBean(Object aBean) {

    Gson gson = new Gson();
    return gson.toJson(aBean);
  }

  /**
   * Convert the passed string from Json format into Object of Passed Class type
   *
   * @param aBean String representation for the bean of type 'classType'
   * @param aClass  class type
   * @return Class representation for the passed string
   */
  public static Object deSerializeBean(String aBean, Class<? extends  Object> aClass) {

    Gson gson = new Gson();
    return gson.fromJson(StringEscapeUtils.unescapeHtml(aBean), aClass);
  }

}
