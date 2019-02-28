package ninja.javahacker.reifiedgeneric;

/**
 * Thrown when an attempt to create an ill-formed or incomplete {@link ReifiedGeneric} occurs.
 * @author Victor Williams Stafusa da Silva
 */
public class MalformedReifiedGenericException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    public static final String TYPE_VARIABLE = "Type variables aren't realizable.";
    public static final String WILDCARD = "Wildcard types aren't realizable.";
    public static final String GENERIC_ARRAY = "Generic array types aren't realizable.";
    public static final String UNRECOGNIZED = "The generic type couldn't be recognized.";
    public static final String RAW = "Token type generic information uses raw types.";
    public static final String ILL_DEFINED = "The generic type is ill-defined.";

    private MalformedReifiedGenericException(String message) {
        super(message, null);
    }

    private MalformedReifiedGenericException(String message, Throwable cause) {
        super(message, cause);
    }

    public static MalformedReifiedGenericException typeVariable() {
        return new MalformedReifiedGenericException(TYPE_VARIABLE);
    }

    public static MalformedReifiedGenericException wildcard() {
        return new MalformedReifiedGenericException(WILDCARD);
    }

    public static MalformedReifiedGenericException genericArray() {
        return new MalformedReifiedGenericException(GENERIC_ARRAY);
    }

    public static MalformedReifiedGenericException unrecognized() {
        return new MalformedReifiedGenericException(UNRECOGNIZED);
    }

    public static MalformedReifiedGenericException raw() {
        return new MalformedReifiedGenericException(RAW);
    }

    public static MalformedReifiedGenericException illDefined(Throwable cause) {
        return new MalformedReifiedGenericException(ILL_DEFINED, cause);
    }
}
