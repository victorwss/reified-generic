package ninja.javahacker.reifiedgeneric;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import lombok.NonNull;
import lombok.experimental.PackagePrivate;

/**
 * A {@link ReifiedGeneric} containing a {@link ParameterizedType}.
 *
 * @param <X> The compile-time generic to be reified.
 * @author Victor Williams Stafusa da Silva
 */
@PackagePrivate
final class ParameterizedReifiedGeneric<X> implements ReifiedGeneric<X> {

    private final MyParameterizedType type;

    /**
     * Used as constructor directly specifying the generic type (likely from reflection).
     *
     * @param type The {@link ParameterizedType} instance to be wrapped.
     * @throws MalformedReifiedGenericException If {@code type} is not a {@link ParameterizedType} nor a {@link Class}.
     */
    private ParameterizedReifiedGeneric(ParameterizedType type) {
        this.type = MyParameterizedType.wrap(type);
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
    public static ParameterizedReifiedGeneric<?> forType(@NonNull ParameterizedType type) {
        return new ParameterizedReifiedGeneric<>(type);
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<X> raw() {
        return (Class<X>) type.getRawType();
    }

    @Override
    public String toString() {
        return "ReifiedGeneric<" + type.getTypeName() + ">";
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof ParameterizedReifiedGeneric
                && type.equals(((ParameterizedReifiedGeneric<?>) other).type);
    }

    @Override
    public int hashCode() {
        return type.hashCode();
    }
}
