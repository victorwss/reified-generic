package ninja.javahacker.reifiedgeneric;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.stream.Stream;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

/**
 * Utility methods used to wrap, unwrap and compose {@link ReifiedGeneric} instances.
 * @author Victor Williams Stafusa da Silva
 */
@UtilityClass
@SuppressWarnings("unchecked")
@SuppressFBWarnings("CLI_CONSTANT_LIST_INDEX")
public class Wrappers {

    private static final int TYPE_INDEX = 0;
    private static final int KEY_INDEX = 0;
    private static final int VALUE_INDEX = 1;

    public ParameterizedType make(Class<?> rawType, Type[] actualTypeArguments, Type ownerType) {
        return new MyParameterizedType(rawType, actualTypeArguments, ownerType);
    }

    public <E> ReifiedGeneric<E> unwrapIterable(@NonNull ReifiedGeneric<? extends Iterable<E>> target) {
        Class<?> raw = target.raw();
        if (!Iterable.class.isAssignableFrom(raw)) throw new IllegalArgumentException(raw.getName() + " is not an Iterable.");
        ParameterizedType p = (ParameterizedType) target.getType();
        return (ReifiedGeneric<E>) ReifiedGeneric.of(p.getActualTypeArguments()[TYPE_INDEX]);
    }

    public <E> ReifiedGeneric<E> unwrapIterator(@NonNull ReifiedGeneric<? extends Iterator<E>> target) {
        Class<?> raw = target.raw();
        if (!Iterator.class.isAssignableFrom(raw)) throw new IllegalArgumentException(raw.getName() + " is not an Iterator.");
        ParameterizedType p = (ParameterizedType) target.getType();
        return (ReifiedGeneric<E>) ReifiedGeneric.of(p.getActualTypeArguments()[TYPE_INDEX]);
    }

    public <E> ReifiedGeneric<E> unwrapStream(@NonNull ReifiedGeneric<? extends Stream<E>> target) {
        Class<?> raw = target.raw();
        if (!Stream.class.isAssignableFrom(raw)) throw new IllegalArgumentException(raw.getName() + " is not an Stream.");
        ParameterizedType p = (ParameterizedType) target.getType();
        return (ReifiedGeneric<E>) ReifiedGeneric.of(p.getActualTypeArguments()[TYPE_INDEX]);
    }

    public <E> ReifiedGeneric<E> unwrapMapKey(@NonNull ReifiedGeneric<? extends Map<E, ?>> target) {
        Class<?> raw = target.raw();
        if (!Map.class.isAssignableFrom(raw)) throw new IllegalArgumentException(raw.getName() + " is not a Map.");
        ParameterizedType p = (ParameterizedType) target.getType();
        return (ReifiedGeneric<E>) ReifiedGeneric.of(p.getActualTypeArguments()[KEY_INDEX]);
    }

    public <E> ReifiedGeneric<E> unwrapMapValue(@NonNull ReifiedGeneric<? extends Map<?, E>> target) {
        Class<?> raw = target.raw();
        if (!Map.class.isAssignableFrom(raw)) throw new IllegalArgumentException(raw.getName() + " is not a Map.");
        ParameterizedType p = (ParameterizedType) target.getType();
        return (ReifiedGeneric<E>) ReifiedGeneric.of(p.getActualTypeArguments()[VALUE_INDEX]);
    }

    public <E> ReifiedGeneric<E> unwrapMapEntryKey(@NonNull ReifiedGeneric<? extends Map.Entry<E, ?>> target) {
        Class<?> raw = target.raw();
        if (!Map.Entry.class.isAssignableFrom(raw)) throw new IllegalArgumentException(raw.getName() + " is not a Map.Entry.");
        ParameterizedType p = (ParameterizedType) target.getType();
        return (ReifiedGeneric<E>) ReifiedGeneric.of(p.getActualTypeArguments()[KEY_INDEX]);
    }

    public <E> ReifiedGeneric<E> unwrapMapEntryValue(@NonNull ReifiedGeneric<? extends Map.Entry<?, E>> target) {
        Class<?> raw = target.raw();
        if (!Map.Entry.class.isAssignableFrom(raw)) throw new IllegalArgumentException(raw.getName() + " is not a Map.Entry.");
        ParameterizedType p = (ParameterizedType) target.getType();
        return (ReifiedGeneric<E>) ReifiedGeneric.of(p.getActualTypeArguments()[VALUE_INDEX]);
    }

    public <E> ReifiedGeneric<Iterable<E>> iterable(@NonNull ReifiedGeneric<E> base) {
        return (ReifiedGeneric<Iterable<E>>) ReifiedGeneric.of(make(Iterable.class, new Type[] {base.getType()}, null));
    }

    public <E> ReifiedGeneric<Iterator<E>> iterator(@NonNull ReifiedGeneric<E> base) {
        return (ReifiedGeneric<Iterator<E>>) ReifiedGeneric.of(make(Iterator.class, new Type[] {base.getType()}, null));
    }

    public <E> ReifiedGeneric<Stream<E>> stream(@NonNull ReifiedGeneric<E> base) {
        return (ReifiedGeneric<Stream<E>>) ReifiedGeneric.of(make(Stream.class, new Type[] {base.getType()}, null));
    }

    public <E> ReifiedGeneric<Collection<E>> collection(@NonNull ReifiedGeneric<E> base) {
        return (ReifiedGeneric<Collection<E>>) ReifiedGeneric.of(make(Collection.class, new Type[] {base.getType()}, null));
    }

    public <E> ReifiedGeneric<List<E>> list(@NonNull ReifiedGeneric<E> base) {
        return (ReifiedGeneric<List<E>>) ReifiedGeneric.of(make(List.class, new Type[] {base.getType()}, null));
    }

    public <E> ReifiedGeneric<Set<E>> set(@NonNull ReifiedGeneric<E> base) {
        return (ReifiedGeneric<Set<E>>) ReifiedGeneric.of(make(Set.class, new Type[] {base.getType()}, null));
    }

    public <E> ReifiedGeneric<SortedSet<E>> sortedSet(@NonNull ReifiedGeneric<E> base) {
        return (ReifiedGeneric<SortedSet<E>>) ReifiedGeneric.of(make(SortedSet.class, new Type[] {base.getType()}, null));
    }

    public <E> ReifiedGeneric<NavigableSet<E>> navigableSet(@NonNull ReifiedGeneric<E> base) {
        return (ReifiedGeneric<NavigableSet<E>>) ReifiedGeneric.of(make(NavigableSet.class, new Type[] {base.getType()}, null));
    }

    public <E, F> ReifiedGeneric<Map<E, F>> map(@NonNull ReifiedGeneric<E> base1, @NonNull ReifiedGeneric<F> base2) {
        return (ReifiedGeneric<Map<E, F>>) ReifiedGeneric.of(
                make(Map.class, new Type[] {base1.getType(), base2.getType()}, null));
    }

    public <E, F> ReifiedGeneric<SortedMap<E, F>> sortedMap(@NonNull ReifiedGeneric<E> base1, @NonNull ReifiedGeneric<F> base2) {
        return (ReifiedGeneric<SortedMap<E, F>>) ReifiedGeneric.of(
                make(SortedMap.class, new Type[] {base1.getType(), base2.getType()}, null));
    }

    public <E, F> ReifiedGeneric<NavigableMap<E, F>> navigableMap(@NonNull ReifiedGeneric<E> base1, @NonNull ReifiedGeneric<F> base2) {
        return (ReifiedGeneric<NavigableMap<E, F>>) ReifiedGeneric.of(
                make(NavigableMap.class, new Type[] {base1.getType(), base2.getType()}, null));
    }

    public <E, F> ReifiedGeneric<Map.Entry<E, F>> entry(@NonNull ReifiedGeneric<E> base1, @NonNull ReifiedGeneric<F> base2) {
        return (ReifiedGeneric<Map.Entry<E, F>>) ReifiedGeneric.of(
                make(Map.Entry.class, new Type[] {base1.getType(), base2.getType()}, null));
    }
}
