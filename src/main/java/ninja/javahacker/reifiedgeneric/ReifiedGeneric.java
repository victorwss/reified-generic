package ninja.javahacker.reifiedgeneric;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
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

    public static <X> ReifiedGeneric<X> forClass(@NonNull Class<X> type)
            throws MalformedReifiedGenericException, NullPointerException
    {
        return new ReifiedGeneric<>(type);
    }

    /**
     * This method wraps {@code Type} instances to {@code ReifiedGeneric} instances.
     *
     * <p>The main purpose of this method is to be able to integrat
     * {@code ReifiedGeneric}-based APIs with {@code Type}-based ones.</p>
     *
     * @param type The {@code Type} instance to be wrapped. Must be a {@link ParameterizedType} or a {@link Class}.
     * @return The wrapping {@code ReifiedGeneric} instance.
     * @throws NullPointerException If {@code type} is {@code null}.
     * @throws MalformedReifiedGenericException If {@code type} is not a {@link ParameterizedType} nor a {@link Class}.
     */
    public static ReifiedGeneric<?> forType(@NonNull Type type) throws MalformedReifiedGenericException, NullPointerException {
        return new ReifiedGeneric<>(type);
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

    public boolean isAssignableFrom(@NonNull Class<?> someClass) {
        return raw().isAssignableFrom(someClass);
    }

    @Override
    public String toString() {
        String name = (generic instanceof Class) ? ((Class<?>) generic).getName() : generic.toString();
        return "ReifiedGeneric<" + name + ">";
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof ReifiedGeneric && isSameOf((ReifiedGeneric) other);
    }

    @Override
    public int hashCode() {
        return generic.hashCode();
    }
}
