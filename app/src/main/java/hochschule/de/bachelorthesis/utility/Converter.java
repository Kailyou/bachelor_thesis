package hochschule.de.bachelorthesis.utility;

import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import hochschule.de.bachelorthesis.R;

public class Converter {

    public static String convertBoolean(Context context, Boolean b) {
        if (b == null) {
            return "null";
        }

        if (b) {
            return context.getResources().getString(R.string.yes);
        } else {
            return context.getResources().getString(R.string.no);
        }
    }

    /**
     * Converts a given Integer to a String.
     *
     * @param i - The given Integer.
     * @return - returns empty string if the Integer is null or -1 (error code), else return a String
     * created out of the float value.
     */
    public static String convertInteger(Integer i) {
        if (i == null || i == -1) {
            return "";
        }
        return String.valueOf(i);
    }

    /**
     * Converts a given Integer to a String.
     *
     * @param i - The given Integer.
     * @return - returns empty string if the Integer is null or -1 (error code), else return a String
     * created out of the float value.
     */
    public static String convertAge(Integer i) {
        if (i == null || i == -1) {
            return "";
        }
        return String.valueOf(i) + " years";
    }

    /**
     * Converts a given Integer to a String.
     *
     * @param i - The given Integer.
     * @return - returns empty string if the Integer is null or -1 (error code), else return a String
     * created out of the float value.
     */
    public static String convertHeight(Integer i) {
        if (i == null || i == -1) {
            return "";
        }
        return String.valueOf(i) + " cm";
    }

    /**
     * Converts a given Integer to a String.
     *
     * @param i - The given Integer.
     * @return - returns empty string if the Integer is null or -1 (error code), else return a String
     * created out of the float value.
     */
    public static String convertWeight(Integer i) {
        if (i == null || i == -1) {
            return "";
        }
        return String.valueOf(i) + " kg";
    }


    public static String convertGlucose(Integer i) {
        if (i == null || i == -1 || i == 0) {
            return "N/A";
        }
        return i + " mg/dl";
    }

    /**
     * Converts a given Integer for a measurement to a String.
     *
     * @param i - The given Integer.
     * @return - returns empty string if the Integer is null or -1 (error code), else return a String
     * created out of the float value.
     */
    public static String convertIntegerMeasurement(Integer i) {
        if (i == null || i == 0 || i == -1) {
            return "";
        }
        return String.valueOf(i);
    }

    /**
     * Converts a given float to a String.
     *
     * @param f - The given float.
     * @return - returns empty string if the float is null or -1 (error code), else return a String
     * created out of the float value.
     */
    public static String convertFloat(Float f) {
        if (f == null || f == -1) {
            return "";
        }
        return String.valueOf(f);
    }

    public static String convertTimeStampToDate(String timeStamp) {
        if (timeStamp == null || timeStamp.length() == 0) {
            return "";
        }
        return String.copyValueOf(timeStamp.toCharArray(), 0, 10);
    }

    /**
     * Of a given timeStamp pattern (dd.MM.yyyy_HH:mm FORMAT) there will be taken the HH.mm FORMAT
     * part to get the time.
     */
    public static String convertTimeStampToTimeStart(String timeStamp) {
        if (timeStamp == null || timeStamp.length() == 0) {
            return "";
        }

        return timeStamp.substring(11);
    }

    /**
     * Converts a timestamp with the pattern dd.MM.yyyy_HH:mm to an String, where the time is
     * extracted from and two hours are added. Those two hours are the amount of time a measurement
     * needs.
     * <p>
     * TODO if measurement is not done, here should not be a time, rather a hint that measurement
     * hasn't been finished yet.
     *
     * @param timeStamp - The time stamp.
     * @return - Returns either "" or the correct time as a String
     */
    public static String convertTimeStampToTimeEnd(String timeStamp) {
        if (timeStamp == null || timeStamp.length() == 0) {
            return "";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy_hh:mm aa", Locale.getDefault());

        try {
            // Get Date from the time stamp
            Date date = sdf.parse(timeStamp);

            // Add two hours
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.HOUR, 2);

            // Get new date object
            date = calendar.getTime();

            // Convert to string again and return
            sdf = new SimpleDateFormat("hh:mm aa", Locale.getDefault());

            return sdf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
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
