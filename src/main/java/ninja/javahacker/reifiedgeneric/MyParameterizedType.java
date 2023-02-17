package ninja.javahacker.reifiedgeneric;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.lang.reflect.MalformedParameterizedTypeException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.stream.Stream;
import lombok.NonNull;
import lombok.experimental.PackagePrivate;

/**
 * The {@link ParameterizedType} implementation is not public, but this tool need to instantiate some of them.
 * So, this class was mostly copied from Java 9's internal {@code ParameterizedTypeImpl}.
 * <p>Note that Java 8 or before has a buggy implementation of {@code ParameterizedTypeImpl} {@link #toString()} method.</p>
 * @author Victor Williams Stafusa da Silva
 */
@PackagePrivate
final class MyParameterizedType implements ParameterizedType {
    private final Type[] actualTypeArguments;
    private final Class<?> rawType;
    private final Type ownerType;

    @PackagePrivate
    MyParameterizedType(@NonNull Class<?> rawType, @NonNull Type[] actualTypeArguments, Type ownerType) {
        this.actualTypeArguments = Stream.of(actualTypeArguments).map(MyParameterizedType::wrap).toArray(Type[]::new);
        this.rawType = rawType;
        this.ownerType = ownerType != null ? ownerType : rawType.getDeclaringClass();
        validateConstructorArguments();
    }

    @PackagePrivate
    static Type wrap(@NonNull Type other) {
        return other instanceof ParameterizedType ? wrap((ParameterizedType) other) : other;
    }

    @PackagePrivate
    static MyParameterizedType wrap(@NonNull ParameterizedType other) {
        return other instanceof MyParameterizedType
                ? (MyParameterizedType) other
                : new MyParameterizedType((Class<?>) other.getRawType(), other.getActualTypeArguments(), other.getOwnerType());
    }

    private void validateConstructorArguments() {
        TypeVariable<?>[] formals = rawType.getTypeParameters();
        if (formals.length != actualTypeArguments.length) throw new MalformedParameterizedTypeException();
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public Type[] getActualTypeArguments() {
        return actualTypeArguments.clone();
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public Class<?> getRawType() {
        return rawType;
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public Type getOwnerType() {
        return ownerType;
    }

    /**
     * {@inheritDoc}
     * @param o {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    @SuppressFBWarnings(
            value = "NSE_NON_SYMMETRIC_EQUALS",
            justification = "Should be equals to other ParameterizedType implementations."
    )
    public boolean equals(Object o) {
        if (!(o instanceof ParameterizedType)) return false;
        if (this == o) return true;

        ParameterizedType that = (ParameterizedType) o;

        return Objects.equals(ownerType, that.getOwnerType())
                && Objects.equals(rawType, that.getRawType())
                && Arrays.equals(actualTypeArguments, that.getActualTypeArguments());
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Arrays.hashCode(actualTypeArguments)
                ^ (ownerType == null ? 0 : ownerType.hashCode())
                ^ rawType.hashCode();
    }

    /**
     * {@inheritDoc}
     * @implSpec The implementation was mostly copied from Java 9-19's internal {@code ParameterizedTypeImpl#toString()} method.
     * @return {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(256);

        if (ownerType != null) {
            if (ownerType instanceof Class) {
                sb.append(((Class<?>) ownerType).getName());
            } else {
                sb.append(ownerType);
            }

            sb.append('$');

            if (ownerType instanceof ParameterizedType) {
                // Find simple name of nested type by removing the shared prefix with owner.
                String parentName = ((ParameterizedType) ownerType).getRawType().getTypeName();
                sb.append(rawType.getName().substring(parentName.length() + 1)); // + 1 due the "$" after the parent's name.
            } else {
                sb.append(rawType.getName());
            }
        } else {
            sb.append(rawType.getName());
        }

        if (actualTypeArguments != null && actualTypeArguments.length > 0) {
            StringJoiner sj = new StringJoiner(", ", "<", ">");
            sj.setEmptyValue("");
            for (Type t: actualTypeArguments) {
                sj.add(t.getTypeName());
            }
            sb.append(sj.toString());
        }

        return sb.toString();
    }
}
