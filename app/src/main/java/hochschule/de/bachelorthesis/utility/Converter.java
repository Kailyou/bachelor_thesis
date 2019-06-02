package hochschule.de.bachelorthesis.utility;

public class Converter {

  public static String convertBoolean(Boolean b) {
    if (b) {
      return "Yes";
    } else {
      return "No";
    }
  }

  public static String convertInteger(Integer i) {
    if (i == 0) {
      return "";
    } else {
      return String.valueOf(i);
    }
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

  /**
   * Converts a given float to a String.
   * @param f - The given float.
   * @return - returns empty string if the float is null or -1 (error code), else return a String
   * created out of the float value.
   */
  public static String convertFloat(Float f) {
    if(f == null || f == -1) {
      return "";
    }
    return String.valueOf(f);
  }

  public static String convertString(String s) {
    if (s == null) {
      return "is null";
    }

    if (s.equals("")) {
      return "unrated";
    } else {
      return s;
    }
  }
}
