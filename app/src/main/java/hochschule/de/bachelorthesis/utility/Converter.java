package hochschule.de.bachelorthesis.utility;

import android.util.Log;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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

  public static String convertTimeStampToDate(String timeStamp) {
    if(timeStamp == null) {
      return "";
    }
    return String.copyValueOf(timeStamp.toCharArray(), 0, 10);
  }

  public static String convertTimeStampToTimeStart(String timeStamp) {
    if(timeStamp == null) {
      return "";
    }
    return String.copyValueOf(timeStamp.toCharArray(), 11, 5);
  }

  /**
   * Converts a timestamp with the pattern dd.MM.yyyy_HH:mm to an String, where the time is extracted
   * from and two hours are added. Those two hours are the amount of time a measurement needs.
   *
   * TODO
   * if measurement is not done, here should not be a time, rather a hint that measurement hasn't
   * been finished yet.
   *
   * @param timeStamp - The time stamp.
   * @return - Returns either "" or the correct time as a String
   */
  public static String convertTimeStampToTimeEnd(String timeStamp) {
    if(timeStamp == null) {
      return "";
    }

    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy_HH:mm", Locale.getDefault());
    try {
      // Get Date from the time stamp
      Date date = sdf.parse(timeStamp);

      // Add two hours
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(date);
      calendar.add(Calendar.HOUR, 2);

      // Get new date object
      Date newDate = calendar.getTime();

      // Convert to string again and return
      SimpleDateFormat sdfNew = new SimpleDateFormat("dd.MM.yyyy_HH:mm", Locale.getDefault());
      return convertTimeStampToTimeStart(sdfNew.format(newDate));

    } catch (ParseException e) {
      e.printStackTrace();
    }

    return "";
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
