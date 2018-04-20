package ninja.javahacker.reifiedgeneric;

import java.lang.reflect.MalformedParameterizedTypeException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;
import lombok.experimental.PackagePrivate;

/**
 * Copied from http://grepcode.com/file/repository.grepcode.com/java/root/jdk/openjdk/8u40-b25/sun/reflect/generics/reflectiveObjects/ParameterizedTypeImpl.java/
 */
@PackagePrivate
class MyParameterizedType implements ParameterizedType {
    private final Type[] actualTypeArguments;
    private final Class<?> rawType;
    private final Type ownerType;

    @PackagePrivate
    MyParameterizedType(Class<?> rawType, Type[] actualTypeArguments, Type ownerType) {
        this.actualTypeArguments = Stream.of(actualTypeArguments).map(MyParameterizedType::wrap).toArray(Type[]::new);
        this.rawType = rawType;
        this.ownerType = (ownerType != null) ? ownerType : rawType.getDeclaringClass();
        validateConstructorArguments();
    }

    private static Type wrap(Type other) {
        if (!(other instanceof ParameterizedType) || (other instanceof MyParameterizedType)) return other;
        ParameterizedType pt = (ParameterizedType) other;
        return new MyParameterizedType((Class<?>) pt.getRawType(), pt.getActualTypeArguments(), pt.getOwnerType());
    }

    private void validateConstructorArguments() {
        TypeVariable<?>[] formals = rawType.getTypeParameters();
        if (formals.length != actualTypeArguments.length) {
            throw new MalformedParameterizedTypeException();
        }
    }

    @Override
    public Type[] getActualTypeArguments() {
        return actualTypeArguments.clone();
    }

    @Override
    public Class<?> getRawType() {
        return rawType;
    }

    @Override
    public Type getOwnerType() {
        return ownerType;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ParameterizedType)) return false;
        if (this == o) return true;

        ParameterizedType that = (ParameterizedType) o;

        return Objects.equals(ownerType, that.getOwnerType())
                && Objects.equals(rawType, that.getRawType())
                && Arrays.equals(actualTypeArguments, that.getActualTypeArguments());
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(actualTypeArguments) ^ Objects.hash(ownerType) ^ Objects.hash(rawType);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(100);

        if (ownerType != null) {
            if (ownerType instanceof Class) {
                sb.append(((Class) ownerType).getName());
            } else {
                sb.append(ownerType.toString());
            }

            sb.append(".");

            if (ownerType instanceof ParameterizedType) {
                // Find simple name of nested type by removing the
                // shared prefix with owner.
                sb.append(rawType.getName().replace(((ParameterizedType) ownerType).getRawType().getTypeName() + "$", ""));
            } else {
                sb.append(rawType.getName());
            }
        } else {
            sb.append(rawType.getName());
        }

        if (actualTypeArguments != null && actualTypeArguments.length > 0) {
            sb.append("<");
            boolean first = true;
            for (Type t: actualTypeArguments) {
                if (!first) sb.append(", ");
                sb.append(t.getTypeName());
                first = false;
            }
            sb.append(">");
        }

        return sb.toString();
    }
}
