package ninja.javahacker.reifiedgeneric;

import java.lang.reflect.ParameterizedType;
import lombok.NonNull;

/**
 * Represents a factory for a {@link ReifiedGeneric}.
 *
 * <p>Should be used as this:</p>
 *
 * <pre>
 * ReifiedGeneric&lt;String&gt; t1 = new Token&lt;String&gt;() {}.getReified();
 * ReifiedGeneric&lt;Map&lt;String, Thread&gt;&gt; t2 = new Token&lt;Map&lt;String, Thread&gt;&gt;() {}.getReified();
 * ReifiedGeneric&lt;Float&gt; t3 = new Token&lt;Float&gt;() {}.getReified();
 * </pre>
 *
 * <p>Don't miss the <code>{}</code> about the instantiation of the {@code Token}. This creates an anonymous subclass,
 * which is a trick needed to allow the JVM be able to read the non-erased generic type through reflection, allowing
 * that type to be reifiable.</p>
 *
 * <p>Also, {@code Token} instances by themselves are almost worthless, and the {@link #getReified()} method should be
 * called rightly after the constructor call, discarding/forgetting the {@code Token} instance immediately. This is why
 * there isn't even any implementation of {@link #equals(Object)}, {@link #hashCode()} ou {@link #toString()} other than
 * the default one inherited from the {@link Object} class. So, never ever bother to save {@code Token} instances to
 * variables or to cache them. The sole purpose of the existence of this class is to use a trick to workaround the
 * type-erasure by hiding the generic signature into the inheritance tree. Once the trick is sucessfully, instances of
 * this class should be discarded. So, the only useful thing that could be performed with instances of this class is
 * calling the {@link #getReified()} method in order to obtain the resulting {@link ReifiedGeneric} instance.</p>
 *
 * @param <X> The compile-time generic to be reified.
 * @see ReifiedGeneric
 * @author Victor Williams Stafusa da Silva
 */
public abstract class Token<X> {

    /**
     * The {@link ReifiedGeneric} instance produced by this {@code Token}.
     */
    @NonNull
    private final ReifiedGeneric<X> reified;

    /**
     * Used as a superconstructor for anonymous subclasses specifying the generic type.
     *
     * @throws MalformedReifiedGenericException If the anonymous subclass do not specifies a {@link ParameterizedType} nor a
     *     {@link Class} in it's generic declaration (likely to be a raw type, a generic array type or a type variable).
     */
    @SuppressWarnings("unchecked")
    protected Token() {
        this.reified = (ReifiedGeneric<X>) ReifiedGeneric.ofToken(this.getClass().getGenericSuperclass());
    }

    /**
     * Gives the {@link ReifiedGeneric} instance produced by this {@code Token}.
     *
     * <p>Multiple calls of this method on the same instance of {@code Token} always produces the same
     * object as a result.</p>
     *
     * @return The {@link ReifiedGeneric} instance produced by this {@code Token}.
     */
    @NonNull
    public ReifiedGeneric<X> getReified() {
        return reified;
    }
}
