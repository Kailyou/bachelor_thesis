package hochschule.de.bachelorthesis.utility;

public class Converter {
    public static String convertBoolean(Boolean b) {
        if(b)
            return "Yes";
        else
            return "No";
    }

    public static String convertInteger(Integer i) {
        if(i == 0)
            return "";
        else
            return String.valueOf(i);
    }

    public static String convertString(String s) {
        if(s == null)
            return "is null";

        if(s.equals(""))
            return "unrated";
        else
            return s;
    }
}
