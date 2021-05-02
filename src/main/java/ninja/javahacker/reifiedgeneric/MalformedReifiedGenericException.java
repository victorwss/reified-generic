package ninja.javahacker.reifiedgeneric;

/**
 * Thrown when an attempt to create an ill-formed or incomplete {@link ReifiedGeneric} happens.
 * @author Victor Williams Stafusa da Silva
 */
public class MalformedReifiedGenericException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Error message used when the used type variable isn't realizable.
     * @see #typeVariable()
     */
    public static final String TYPE_VARIABLE_ERROR_MESSAGE = "Type variables aren't realizable.";

    /**
     * Error message used when the generic type is a wildcard instead of a real runtime type.
     * @see #wildcard()
     */
    public static final String WILDCARD_ERROR_MESSAGE = "Wildcard types aren't realizable.";

    /**
     * Error message used when the the generic type refers to an array type whose base type is a generic type.
     * @see #genericArray()
     */
    public static final String GENERIC_ARRAY_ERROR_MESSAGE = "Generic array types aren't realizable.";

    /**
     * Error message used when the generic type can't be recognized.
     * @see #unrecognized()
     */
    public static final String UNRECOGNIZED_ERROR_MESSAGE = "The generic type couldn't be recognized.";

    /**
     * Error message used when a raw type was used instead of a generic type.
     * @see #raw()
     */
    public static final String RAW_ERROR_MESSAGE = "Token type generic information uses raw types.";

    /**
     * Error message used when the generic type is ill-defined due to another error.
     * @see #illDefined(Throwable)
     */
    public static final String ILL_DEFINED_ERROR_MESSAGE = "The generic type is ill-defined.";

    /**
     * Constructor that receives a message and no cause.
     * @param message The exception's message.
     */
    private MalformedReifiedGenericException(String message) {
        super(message, null);
    }

    /**
     * Constructor that receives a message and a cause.
     * @param message The exception's message.
     * @param cause The cause of this exception.
     */
    private MalformedReifiedGenericException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs an instance where the used type variable isn't realizable.
     * @return The created instance.
     */
    public static MalformedReifiedGenericException typeVariable() {
        return new MalformedReifiedGenericException(TYPE_VARIABLE_ERROR_MESSAGE);
    }

    /**
     * Constructs an instance where the generic type is a wildcard instead of a real runtime type.
     * @return The created instance.
     */
    public static MalformedReifiedGenericException wildcard() {
        return new MalformedReifiedGenericException(WILDCARD_ERROR_MESSAGE);
    }

    /**
     * Constructs an instance where a raw type was used instead of a generic type.
     * @return The created instance.
     */
    public static MalformedReifiedGenericException genericArray() {
        return new MalformedReifiedGenericException(GENERIC_ARRAY_ERROR_MESSAGE);
    }

    /**
     * Constructs an instance where the generic type can't be recognized.
     * @return The created instance.
     */
    public static MalformedReifiedGenericException unrecognized() {
        return new MalformedReifiedGenericException(UNRECOGNIZED_ERROR_MESSAGE);
    }

    /**
     * Constructs an instance where a raw type was used instead of a generic type.
     * @return The created instance.
     */
    public static MalformedReifiedGenericException raw() {
        return new MalformedReifiedGenericException(RAW_ERROR_MESSAGE);
    }

    /**
     * Constructs an instance where the generic type is ill-defined due to another error.
     * @param cause The causing error.
     * @return The created instance.
     */
    public static MalformedReifiedGenericException illDefined(Throwable cause) {
        return new MalformedReifiedGenericException(ILL_DEFINED_ERROR_MESSAGE, cause);
    }
}
