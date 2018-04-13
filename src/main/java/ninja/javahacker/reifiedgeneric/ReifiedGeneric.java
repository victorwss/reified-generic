package ninja.javahacker.reifiedgeneric;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import lombok.NonNull;

/**
 * Represents a type-safe runtime-existing non-erased reified wrapper of a generic type.
 *
 * <p>Instances should be created with this:</p>
 * <pre>
 * ReifiedGeneric&lt;String&gt; t1 = new ReifiedGeneric&lt;&gt;() {};
 * ReifiedGeneric&lt;Map&lt;String, Thread&gt;&gt; t2 = new ReifiedGeneric&lt;&gt;() {};
 * ReifiedGeneric&lt;Float&gt; t3 = ReifiedGeneric.forClass(Float.class) {};
 * Type x = ...;
 * ReifiedGeneric&lt;?&gt; tx = TargetType.forType(t);
 * </pre>
 * @param <X> The compile-time generic to be reified.
 * @author Victor Williams Stafusa da Silva
 */
public class ReifiedGeneric<X> {

    private final Type generic;

    protected ReifiedGeneric() throws MalformedReifiedGenericException {
        try {
            this.generic = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        } catch (ClassCastException | IndexOutOfBoundsException | NullPointerException e) {
            throw MalformedReifiedGenericException.illDefined();
        }
        if (!(this.generic instanceof ParameterizedType) && !(this.generic instanceof Class)) {
            throw MalformedReifiedGenericException.shouldBeInstantiable();
        }
    }

    private ReifiedGeneric(Type type) throws MalformedReifiedGenericException {
        if (!(type instanceof ParameterizedType) && !(type instanceof Class)) {
            throw MalformedReifiedGenericException.shouldBeInstantiable();
        }
        this.generic = type;
    }

    public static <X> ReifiedGeneric<X> forClass(Class<X> klass) throws MalformedReifiedGenericException {
        return new ReifiedGeneric<>(klass);
    }

    /**
     * This method wraps {@code Type} instances to {@code ReifiedGeneric} instances.
     *
     * <p>The main purpose of this method is to be able to integrate {@code ReifiedGeneric}-based APIs with {@code Type}-based ones.</p>
     *
     * @param t The {@code Type} instance to be wrapped. Must be a {@link ParameterizedType} or a {@link Class}.
     * @return The wrapping {@code ReifiedGeneric} instance.
     * @throws NullPointerException If {@code t} is {@code null}.
     * @throws MalformedReifiedGenericException If {@code t} is not a {@link ParameterizedType} nor a {@link Class}.
     */
    public static ReifiedGeneric<?> forType(@NonNull Type t) throws MalformedReifiedGenericException, NullPointerException {
        return new ReifiedGeneric<>(t);
    }

    public Type getGeneric() {
        return generic;
    }

    public boolean isSameOf(@NonNull ReifiedGeneric<?> other) {
        return generic.equals(other.generic);
    }

    @SuppressWarnings("unchecked")
    public Class<X> raw() {
        if (generic instanceof Class) return (Class<X>) generic;
        ParameterizedType pt = (ParameterizedType) generic;
        return (Class<X>) pt.getRawType();
    }

    public boolean isAssignableFrom(Class<?> someClass) {
        return raw().isAssignableFrom(someClass);
    }

    @SuppressWarnings("unchecked")
    public static <E> ReifiedGeneric<E> unwrapIterableGenericType(@NonNull ReifiedGeneric<? extends Iterable<E>> target) {
        if (!target.raw().isAssignableFrom(Iterable.class)) throw new IllegalArgumentException();
        return (ReifiedGeneric<E>) ReifiedGeneric.forType(((ParameterizedType) target.generic).getActualTypeArguments()[0]);
    }

    @SuppressWarnings("unchecked")
    public static <E> ReifiedGeneric<E> unwrapMapKeyType(@NonNull ReifiedGeneric<? extends Map<E, ?>> target) {
        if (!target.raw().isAssignableFrom(Map.class)) throw new IllegalArgumentException();
        return (ReifiedGeneric<E>) ReifiedGeneric.forType(((ParameterizedType) target.generic).getActualTypeArguments()[0]);
    }

    @SuppressWarnings("unchecked")
    public static <E> ReifiedGeneric<E> unwrapMapValueType(@NonNull ReifiedGeneric<? extends Map<?, E>> target) {
        if (!target.raw().isAssignableFrom(Map.class)) throw new IllegalArgumentException();
        return (ReifiedGeneric<E>) ReifiedGeneric.forType(((ParameterizedType) target.generic).getActualTypeArguments()[1]);
    }

    @Override
    public String toString() {
        return "TargetType[" + generic + "]";
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof ReifiedGeneric && isSameOf((ReifiedGeneric) other);
    }

    @Override
    public int hashCode() {
        return generic.hashCode();
    }

    public static class MalformedReifiedGenericException extends RuntimeException {

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
}
