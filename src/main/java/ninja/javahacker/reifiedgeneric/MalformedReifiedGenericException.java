package ninja.javahacker.reifiedgeneric;

/**
 * Thrown when an attempt to create an ill-formed or incomplete {@link ReifiedGeneric} occurs.
 * @author Victor Williams Stafusa da Silva
 */
public class MalformedReifiedGenericException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    public static final String SHOULD_BE_INSTANTIABLE = "The generic type should be instantiable.";
    public static final String ILL_DEFINED = "The generic type is ill-defined.";

    private MalformedReifiedGenericException(String message) {
        super(message, null);
    }

    private MalformedReifiedGenericException(String message, Throwable cause) {
        super(message, cause);
    }

    public static MalformedReifiedGenericException shouldBeInstantiable() {
        return new MalformedReifiedGenericException(SHOULD_BE_INSTANTIABLE);
    }

    public static MalformedReifiedGenericException illDefined() {
        return new MalformedReifiedGenericException(ILL_DEFINED);
    }

    public static MalformedReifiedGenericException illDefined(Throwable cause) {
        return new MalformedReifiedGenericException(ILL_DEFINED, cause);
    }
}
