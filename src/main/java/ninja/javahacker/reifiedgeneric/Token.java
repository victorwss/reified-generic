package ninja.javahacker.reifiedgeneric;

import java.lang.reflect.MalformedParameterizedTypeException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import lombok.NonNull;

/**
 * Represents a factory for a {@link ReifiedGeneric}.
 *
 * <p>Should be used as this:</p>
 * <pre>
 * ReifiedGeneric&lt;String&gt; t1 = new Token&lt;String&gt;() {}.reify();
 * ReifiedGeneric&lt;Map&lt;String, Thread&gt;&gt; t2 =  new Token&lt;Map&lt;String, Thread&gt;&gt;() {}.reify();
 * ReifiedGeneric&lt;Float&gt; t3 = new Token&lt;Float&gt;() {}.reify();
 * </pre>
 * @param <X> The compile-time generic to be reified.
 * @see ReifiedGeneric
 * @author Victor Williams Stafusa da Silva
 */
public abstract class Token<X> {

    /**
     * The {@link ReifiedGeneric} instance represented by this {@code Token}.
     */
    @NonNull
    private final ReifiedGeneric<X> reified;

    /**
     * Used as a superconstructor for anonymous subclasses specifying the generic type.
     *
     * @throws MalformedReifiedGenericException If the anonymous subclass do not specifies a {@link ParameterizedType} nor a
     * {@link Class} in it's generic declaration (likely to be a raw type, a generic array type or a type variable).
     */
    @SuppressWarnings("unchecked")
    protected Token() {
        Type superType = this.getClass().getGenericSuperclass();
        ParameterizedType pt = ReifiedGeneric.validate(
                superType,
                () -> { throw MalformedReifiedGenericException.raw(); },
                () -> (ParameterizedType) superType);
        Type ppt;
        try {
            ppt = MyParameterizedType.wrap(pt.getActualTypeArguments()[0]);
        } catch (MalformedParameterizedTypeException | TypeNotPresentException | IndexOutOfBoundsException e) {
            throw MalformedReifiedGenericException.illDefined(e);
        }
        this.reified = (ReifiedGeneric<X>) ReifiedGeneric.of(ppt);
    }

    /**
     * The representation of the {@link Token} with the same generic type given by the {@link ReifiedGeneric}
     * produced by the {@link #reify()} method.
     * @return The representation of the {@link Token} with the same generic type given by the {@link ReifiedGeneric}
     *     produced by the {@link #reify()} method.
     */
    @Override
    public String toString() {
        return "Token<" + reify().getType().getTypeName() + ">";
    }

    /**
     * Produces the {@link ReifiedGeneric} instance represented by this {@code Token}.
     *
     * <p>Multiple calls of this method on the same instance of {@code Token} always produces the same
     * object as a result</p>
     *
     * @return The {@link ReifiedGeneric} instance represented by this {@code Token}.
     */
    public ReifiedGeneric<X> reify() {
        return reified;
    }
}
