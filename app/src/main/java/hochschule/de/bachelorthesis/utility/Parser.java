package hochschule.de.bachelorthesis.utility;

/**
 * @author Maik Thielen
 * <p>
 * Functions to create numbers out of Strings.
 * <p>
 * This is used to take the input of a user out of a text view, create the data with, then create
 * an object which will be saved in the database then.
 */
public class Parser {

    /**
     * Converts a given String to a float.
     *
     * @param s - The given String.
     * @return - The float value if the string is a valid string. Else -1, this will be used to get a
     * "" string.
     */
    public static float parseFloat(String s) {
        if (s != null && s.length() > 0) {
            return Float.parseFloat(s);
        }
        return -1;
    }

    /**
     * Converts a given String to an Integer.
     *
     * @param s - The given String.
     * @return - The Integer value if the string is a valid string. Else -1, this will be used to get
     * a "" string.
     */
    public static Integer parseInteger(String s) {
        if (s != null && s.length() > 0) {
            return Integer.parseInt(s);
        }
        return -1;
    }
}
