package ninja.javahacker.reifiedgeneric;

import lombok.NonNull;
import lombok.experimental.PackagePrivate;

/**
 * A {@link ReifiedGeneric} containing a {@link Class}.
 *
 * @param <X> The compile-time generic to be reified.
 * @author Victor Williams Stafusa da Silva
 */
@PackagePrivate
final class ClassReifiedGeneric<X> implements ReifiedGeneric<X> {

    private final Class<X> type;

    private ClassReifiedGeneric(Class<X> type) {
        this.type = type;
    }

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
    public static <X> ClassReifiedGeneric<X> forClass(@NonNull Class<X> type) {
        return new ClassReifiedGeneric<>(type);
    }

    @Override
    public Class<X> getType() {
        return type;
    }

    @Override
    public Class<X> raw() {
        return type;
    }

    @Override
    public String toString() {
        return "ReifiedGeneric<" + type.getName() + ">";
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof ClassReifiedGeneric
                && type.equals(((ClassReifiedGeneric<?>) other).type);
    }

    @Override
    public int hashCode() {
        return type.hashCode();
    }
}
