package ninja.javahacker.reifiedgeneric;

import edu.umd.cs.findbugs.annotations.Nullable;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.MalformedParameterizedTypeException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.function.Supplier;
import lombok.NonNull;
import lombok.experimental.PackagePrivate;

/**
 * Represents a type-safe runtime-existing non-erased reified wrapper of a generic type.
 *
 * <p>Instances should be created with this:</p>
 *
 * <pre>
 * ReifiedGeneric&lt;String&gt; t1 = new Token&lt;&gt;() {}.reify();
 * ReifiedGeneric&lt;Map&lt;String, Thread&gt;&gt; t2 = new Token&lt;&gt;() {}.reify();
 * ReifiedGeneric&lt;Float&gt; t3 = ReifiedGeneric.of(Float.class) {};
 * Type x = ...;
 * ReifiedGeneric&lt;?&gt; tx = ReifiedGeneric.of(t);
 * </pre>
 *
 * @param <X> The compile-time generic to be reified.
 * @author Victor Williams Stafusa da Silva
 */
public abstract class ReifiedGeneric<X> {

    /**
     * Sole constructor usable only by the subclasses declared as private nested classes.
     */
    private ReifiedGeneric() {
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
    public static <X> ReifiedGeneric<X> of(@NonNull Class<X> type) {
        return new ClassReifiedGeneric<>(type);
    }

    /**
     * Wraps {@link Type} instances into {@code ReifiedGeneric} instances.
     *
     * <p>The main purpose of this method is to be able to bridge
     * {@code ReifiedGeneric}-based APIs with {@link Type}-based ones.</p>
     *
     * @param type The {@link Type} instance to be wrapped. Must be a {@link ParameterizedType} or a {@link Class}.
     * @return The wrapping {@code ReifiedGeneric} instance.
     * @throws IllegalArgumentException If {@code type} is {@code null}.
     * @throws MalformedReifiedGenericException If {@code type} is not a {@link ParameterizedType} nor a {@link Class}.
     */
    @NonNull
    public static ReifiedGeneric<?> of(@NonNull Type type) {
        return validate(
                type,
                () -> new ClassReifiedGeneric<>((Class<?>) type),
                () -> new ParameterizedReifiedGeneric<>((ParameterizedType) type)
        );
    }

    @NonNull
    @SuppressFBWarnings("ITC_INHERITANCE_TYPE_CHECKING")
    private static <E> E validate(@NonNull Type type, @NonNull Supplier<E> forClass, @NonNull Supplier<E> forParameterized) {
        if (type instanceof Class<?>) return forClass.get();
        if (type instanceof ParameterizedType) return forParameterized.get();
        if (type instanceof WildcardType) throw MalformedReifiedGenericException.wildcard();
        if (type instanceof GenericArrayType) throw MalformedReifiedGenericException.genericArray();
        if (type instanceof TypeVariable<?>) throw MalformedReifiedGenericException.typeVariable();
        throw MalformedReifiedGenericException.unrecognized();
    }

    @NonNull
    @PackagePrivate
    static ReifiedGeneric<?> ofToken(@NonNull Type superType) {
        ParameterizedType pt = ReifiedGeneric.validate(
                superType,
                () -> { throw MalformedReifiedGenericException.raw(); },
                () -> (ParameterizedType) superType
        );

        Type ppt;
        try {
            ppt = MyParameterizedType.wrap(pt.getActualTypeArguments()[0]);
        } catch (MalformedParameterizedTypeException | TypeNotPresentException | IndexOutOfBoundsException e) {
            throw MalformedReifiedGenericException.illDefined(e);
        }

        return ReifiedGeneric.of(ppt);
    }

    /**
     * Gives the type that is represented by this instance. It is either a {@code Class} or a {@code ParameterizedType}.
     * @return The type that is represented by this instance.
     */
    @NonNull
    public abstract Type getType();

    /**
     * Gives the type that is represented by this instance aproximated as a {@code Class}.
     * @return The raw-type that is represented by this instance.
     */
    @NonNull
    public abstract Class<X> asClass();

    /**
     * Tells if {@code this} represents the same type as {@code that}.
     * @param that Some other {@code ReifiedGeneric}.
     * @return {@code true} if {@code this} represents the same type as {@code that},
     *     {@code false} otherwise (including if {@code that} is {@code null}).
     */
    public boolean isSameOf(@Nullable ReifiedGeneric<?> that) {
        return that != null && getType().equals(that.getType());
    }

    /**
     * Tells if the class in the type held by {@code this} object is assignable from {@code someClass}.
     * I.E, tells if it is has the same or is a supertype of {@code someClass}.
     * @param someClass The class that could possibly be assigned to the class held by {@code this} object.
     * @return {@code true} if {@code this} represents the same type or a supertype of {@code someClass}, {@code false} otherwise.
     * @throws IllegalArgumentException If {@code someClass} is {@code null}.
     * @see Class#isAssignableFrom(Class)
     */
    public boolean isAssignableFrom(@NonNull Class<?> someClass) {
        return asClass().isAssignableFrom(someClass);
    }

    /**
     * Tells if the class in the type held by {@code this} object is assignable to {@code someClass}.
     * I.E, tells if it is has the same or is a subtype of {@code someClass}.
     * @param someClass The class that could possibly be assigned from the class held by {@code this} object.
     * @return {@code true} if {@code this} represents the same type or a subtype of {@code someClass}, {@code false} otherwise.
     * @throws IllegalArgumentException If {@code someClass} is {@code null}.
     * @see Class#isAssignableFrom(Class)
     */
    public boolean isAssignableTo(@NonNull Class<?> someClass) {
        return someClass.isAssignableFrom(asClass());
    }

    /**
     * If this {@code ReifiedGeneric} instance is compatible with the given {@code base}, returns this instance as
     * something that extends the given {@code base}.
     * @param <E> The generic type of the given {@code base} that this instance is expected to be compatible.
     * @param base A supertype of the type contained in this object.
     * @return {@code this}, but with a different generic type.
     * @throws IllegalArgumentException If {@code base} is {@code null}.
     * @throws ClassCastException If {@code this} does not represents a subtype of the given {@code base}.
     */
    @NonNull
    @SuppressWarnings("unchecked")
    public <E> ReifiedGeneric<? extends E> cast(@NonNull Class<E> base) {
        if (!isAssignableTo(base)) throw new ClassCastException();
        return (ReifiedGeneric<? extends E>) this;
    }

    /**
     * Tells if {@code this} represents the same type as {@code other}.
     * @param other Other object that will be compared as possibly being equals to {@code this}.
     * @return {@code true} if {@code this} represents the same type as {@code other}, {@code false} otherwise.
     */
    @Override
    public boolean equals(Object other) {
        return other instanceof ReifiedGeneric<?> && isSameOf((ReifiedGeneric<?>) other);
    }

    /**
     * Gives a hash code for this {@code ReifiedGeneric} instance.
     * @implNote This implementation only returns whatever is the hash code given by the {@link Type} that is
     *     represented by this instance.
     * @return A hash code for this instance.
     */
    @Override
    public int hashCode() {
        return getType().hashCode();
    }

    /**
     * Gives a {@link String} representation of this instance. That representation consists in {@code "ReifiedGeneric"}
     * followed by the represented type name between angle brackets.
     * @return A {@link String} representation of this instance.
     */
    @NonNull
    @Override
    public String toString() {
        return "ReifiedGeneric<" + getType().getTypeName() + ">";
    }

    private static final class ClassReifiedGeneric<X> extends ReifiedGeneric<X> {

        private final Class<X> type;

        private ClassReifiedGeneric(Class<X> type) {
            this.type = type;
        }

        @Override
        public Class<X> getType() {
            return type;
        }

        @Override
        public Class<X> asClass() {
            return type;
        }
    }

    private static final class ParameterizedReifiedGeneric<X> extends ReifiedGeneric<X> {

        private final MyParameterizedType type;

        private ParameterizedReifiedGeneric(ParameterizedType type) {
            this.type = MyParameterizedType.wrap(type);
        }

        @Override
        public Type getType() {
            return type;
        }

        @Override
        @SuppressWarnings("unchecked")
        public Class<X> asClass() {
            return (Class<X>) type.getRawType();
        }
    }
}
