package hochschule.de.bachelorthesis.utility;

public class Converter {

  public static String convertBoolean(Boolean b) {
    if (b) {
      return "Yes";
    } else {
      return "No";
    }
  }

  /**
   * Converts a given Integer to a String.
   * @param i - The given Integer.
   * @return - returns empty string if the Integer is null or -1 (error code), else return a String
   * created out of the float value.
   */
  public static String convertInteger(Integer i) {
    if(i == null || i == -1) {
      return "";
    }
    return String.valueOf(i);
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
