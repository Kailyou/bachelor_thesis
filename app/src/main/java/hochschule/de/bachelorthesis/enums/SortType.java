package hochschule.de.bachelorthesis.enums;

/**
 * @author Maik Thielen
 * <p>
 * Simple enumeration for the sort type.
 * <p>
 * This is only used for debug reasons.
 * <p>
 * Enumerations are not very performant in Android,
 * so may concisder changing this to a type def later on.
 */
public enum SortType {
    ALPHANUMERIC,
    GI,
    DATE,
    GLUCOSE_MAX
}
