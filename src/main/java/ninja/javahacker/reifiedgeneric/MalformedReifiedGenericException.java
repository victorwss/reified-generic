package ninja.javahacker.reifiedgeneric;

/**
 * @author Victor Williams Stafusa da Silva
 */
public class MalformedReifiedGenericException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    public static final String SHOULD_BE_INSTANTIABLE = "The generic type should be instantiable.";
    public static final String ILL_DEFINED = "The generic type is ill-defined.";

    public MalformedReifiedGenericException(String message) {
        super(message, null);
    }

    public static MalformedReifiedGenericException shouldBeInstantiable() {
        return new MalformedReifiedGenericException(SHOULD_BE_INSTANTIABLE);
    }

    public static MalformedReifiedGenericException illDefined() {
        return new MalformedReifiedGenericException(ILL_DEFINED);
    }
}
