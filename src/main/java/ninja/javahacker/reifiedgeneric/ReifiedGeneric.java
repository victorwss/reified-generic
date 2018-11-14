package ninja.javahacker.reifiedgeneric;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import lombok.NonNull;

/**
 * Represents a type-safe runtime-existing non-erased reified wrapper of a generic type.
 *
 * <p>Instances should be created with this:</p>
 * <pre>
 * ReifiedGeneric&lt;String&gt; t1 = new Token&lt;&gt;() {}.reify();
 * ReifiedGeneric&lt;Map&lt;String, Thread&gt;&gt; t2 = new Token&lt;&gt;() {}.reify();
 * ReifiedGeneric&lt;Float&gt; t3 = ReifiedGeneric.of(Float.class) {};
 * Type x = ...;
 * ReifiedGeneric&lt;?&gt; tx = ReifiedGeneric.of(t);
 * </pre>
 * @param <X> The compile-time generic to be reified.
 * @author Victor Williams Stafusa da Silva
 */
public interface ReifiedGeneric<X> extends Type {

    /**
     * Wraps {@link Class} instances into {@code ReifiedGeneric} instances.
     *
     * <p>The main purpose of this method is to be able to integrate
     * {@code ReifiedGeneric}-based APIs with {@link Class}-based ones.</p>
     *
     * @param type The {@link Class} instance to be wrapped.
     * @param <X> The compile-time generic to be reified.
     * @return The wrapping {@code ReifiedGeneric} instance.
     * @throws IllegalArgumentException If {@code type} is {@code null}.
     */
    public static <X> ReifiedGeneric<X> of(@NonNull Class<X> type) {
        return ClassReifiedGeneric.forClass(type);
    }

    /**
     * Wraps {@link Type} instances into {@code ReifiedGeneric} instances.
     *
     * <p>The main purpose of this method is to be able to integrate
     * {@code ReifiedGeneric}-based APIs with {@link Type}-based ones.</p>
     *
     * @param type The {@link Type} instance to be wrapped. Must be a {@link ParameterizedType} or a {@link Class}.
     * @return The wrapping {@code ReifiedGeneric} instance.
     * @throws IllegalArgumentException If {@code type} is {@code null}.
     * @throws MalformedReifiedGenericException If {@code type} is not a {@link ParameterizedType} nor a {@link Class}.
     */
    @SuppressFBWarnings("ITC_INHERITANCE_TYPE_CHECKING")
    public static ReifiedGeneric<?> of(@NonNull Type type) {
        if (type instanceof Class<?>) return ClassReifiedGeneric.forClass((Class<?>) type);
        if (type instanceof ParameterizedType) return ParameterizedReifiedGeneric.forType((ParameterizedType) type);
        throw MalformedReifiedGenericException.shouldBeInstantiable();
    }

    public Type getType();

    public default boolean isSameOf(@NonNull ReifiedGeneric<?> other) {
        return this.equals(other);
    }

    public Class<X> raw();

    public default boolean isAssignableFrom(@NonNull Class<?> someClass) {
        return raw().isAssignableFrom(someClass);
    }

    public default boolean isCompatibleWith(@NonNull Class<?> base) {
        return base.isAssignableFrom(raw());
    }

    @SuppressWarnings("unchecked")
    public default <E> ReifiedGeneric<? extends E> cast(Class<E> base) {
        if (!isCompatibleWith(base)) throw new ClassCastException();
        return (ReifiedGeneric<? extends E>) this;
    }
}
