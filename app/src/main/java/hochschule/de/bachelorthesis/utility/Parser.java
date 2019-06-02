package hochschule.de.bachelorthesis.utility;

public class Parser {

  /**
   * Converts a given String to a float.
   * @param s - The given String.
   * @return - The float value if the string is a valid string. Else -1, this will be used
   * to get a "" string.
   */
  public static float parseInteger(String s) {
    if (s != null && s.length() > 0) {
      return Integer.parseInt(s);
    }
    return -1;
  }

  /**
   * Converts a given String to a float.
   * @param s - The given String.
   * @return - The float value if the string is a valid string. Else -1, this will be used
   * to get a "" string.
   */
  public static float parseFloat(String s) {
    if (s != null && s.length() > 0) {
      return Float.parseFloat(s);
    }
    return -1;
  }

}
