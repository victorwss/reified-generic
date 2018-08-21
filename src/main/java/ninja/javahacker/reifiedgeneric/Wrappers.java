package ninja.javahacker.reifiedgeneric;

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
 * @author Victor Williams Stafusa da Silva
 */
@UtilityClass
@SuppressWarnings("unchecked")
public class Wrappers {

    public ParameterizedType make(Class<?> rawType, Type[] actualTypeArguments, Type ownerType) {
        return new MyParameterizedType(rawType, actualTypeArguments, ownerType);
    }

    public <E> ReifiedGeneric<E> unwrapIterable(@NonNull ReifiedGeneric<? extends Iterable<E>> target) {
        Class<?> raw = target.raw();
        if (!Iterable.class.isAssignableFrom(raw)) throw new IllegalArgumentException(raw.getName() + " is not an Iterable.");
        return (ReifiedGeneric<E>) ReifiedGeneric.forType(((ParameterizedType) target.getGeneric()).getActualTypeArguments()[0]);
    }

    public <E> ReifiedGeneric<E> unwrapIterator(@NonNull ReifiedGeneric<? extends Iterator<E>> target) {
        Class<?> raw = target.raw();
        if (!Iterator.class.isAssignableFrom(raw)) throw new IllegalArgumentException(raw.getName() + " is not an Iterator.");
        return (ReifiedGeneric<E>) ReifiedGeneric.forType(((ParameterizedType) target.getGeneric()).getActualTypeArguments()[0]);
    }

    public <E> ReifiedGeneric<E> unwrapStream(@NonNull ReifiedGeneric<? extends Stream<E>> target) {
        Class<?> raw = target.raw();
        if (!Stream.class.isAssignableFrom(raw)) throw new IllegalArgumentException(raw.getName() + " is not an Stream.");
        return (ReifiedGeneric<E>) ReifiedGeneric.forType(((ParameterizedType) target.getGeneric()).getActualTypeArguments()[0]);
    }

    public <E> ReifiedGeneric<E> unwrapMapKey(@NonNull ReifiedGeneric<? extends Map<E, ?>> target) {
        Class<?> raw = target.raw();
        if (!Map.class.isAssignableFrom(raw)) throw new IllegalArgumentException(raw.getName() + " is not a Map.");
        return (ReifiedGeneric<E>) ReifiedGeneric.forType(((ParameterizedType) target.getGeneric()).getActualTypeArguments()[0]);
    }

    public <E> ReifiedGeneric<E> unwrapMapValue(@NonNull ReifiedGeneric<? extends Map<?, E>> target) {
        Class<?> raw = target.raw();
        if (!Map.class.isAssignableFrom(raw)) throw new IllegalArgumentException(raw.getName() + " is not a Map.");
        return (ReifiedGeneric<E>) ReifiedGeneric.forType(((ParameterizedType) target.getGeneric()).getActualTypeArguments()[1]);
    }

    public <E> ReifiedGeneric<E> unwrapMapEntryKey(@NonNull ReifiedGeneric<? extends Map.Entry<E, ?>> target) {
        Class<?> raw = target.raw();
        if (!Map.Entry.class.isAssignableFrom(raw)) throw new IllegalArgumentException(raw.getName() + " is not a Map.Entry.");
        return (ReifiedGeneric<E>) ReifiedGeneric.forType(((ParameterizedType) target.getGeneric()).getActualTypeArguments()[0]);
    }

    public <E> ReifiedGeneric<E> unwrapMapEntryValue(@NonNull ReifiedGeneric<? extends Map.Entry<?, E>> target) {
        Class<?> raw = target.raw();
        if (!Map.Entry.class.isAssignableFrom(raw)) throw new IllegalArgumentException(raw.getName() + " is not a Map.Entry.");
        return (ReifiedGeneric<E>) ReifiedGeneric.forType(((ParameterizedType) target.getGeneric()).getActualTypeArguments()[1]);
    }

    public <E> ReifiedGeneric<Iterable<E>> iterable(@NonNull ReifiedGeneric<E> base) {
        return (ReifiedGeneric<Iterable<E>>) ReifiedGeneric.forType(make(Iterable.class, new Type[] {base.getGeneric()}, null));
    }

    public <E> ReifiedGeneric<Iterator<E>> iterator(@NonNull ReifiedGeneric<E> base) {
        return (ReifiedGeneric<Iterator<E>>) ReifiedGeneric.forType(make(Iterator.class, new Type[] {base.getGeneric()}, null));
    }

    public <E> ReifiedGeneric<Stream<E>> stream(@NonNull ReifiedGeneric<E> base) {
        return (ReifiedGeneric<Stream<E>>) ReifiedGeneric.forType(make(Stream.class, new Type[] {base.getGeneric()}, null));
    }

    public <E> ReifiedGeneric<Collection<E>> collection(@NonNull ReifiedGeneric<E> base) {
        return (ReifiedGeneric<Collection<E>>) ReifiedGeneric.forType(make(Collection.class, new Type[] {base.getGeneric()}, null));
    }

    public <E> ReifiedGeneric<List<E>> list(@NonNull ReifiedGeneric<E> base) {
        return (ReifiedGeneric<List<E>>) ReifiedGeneric.forType(make(List.class, new Type[] {base.getGeneric()}, null));
    }

    public <E> ReifiedGeneric<Set<E>> set(@NonNull ReifiedGeneric<E> base) {
        return (ReifiedGeneric<Set<E>>) ReifiedGeneric.forType(make(Set.class, new Type[] {base.getGeneric()}, null));
    }

    public <E> ReifiedGeneric<SortedSet<E>> sortedSet(@NonNull ReifiedGeneric<E> base) {
        return (ReifiedGeneric<SortedSet<E>>) ReifiedGeneric.forType(make(SortedSet.class, new Type[] {base.getGeneric()}, null));
    }

    public <E> ReifiedGeneric<NavigableSet<E>> navigableSet(@NonNull ReifiedGeneric<E> base) {
        return (ReifiedGeneric<NavigableSet<E>>) ReifiedGeneric.forType(make(NavigableSet.class, new Type[] {base.getGeneric()}, null));
    }

    public <E, F> ReifiedGeneric<Map<E, F>> map(@NonNull ReifiedGeneric<E> base1, @NonNull ReifiedGeneric<F> base2) {
        return (ReifiedGeneric<Map<E, F>>) ReifiedGeneric.forType(
                make(Map.class, new Type[] {base1.getGeneric(), base2.getGeneric()}, null));
    }

    public <E, F> ReifiedGeneric<SortedMap<E, F>> sortedMap(@NonNull ReifiedGeneric<E> base1, @NonNull ReifiedGeneric<F> base2) {
        return (ReifiedGeneric<SortedMap<E, F>>) ReifiedGeneric.forType(
                make(SortedMap.class, new Type[] {base1.getGeneric(), base2.getGeneric()}, null));
    }

    public <E, F> ReifiedGeneric<NavigableMap<E, F>> navigableMap(@NonNull ReifiedGeneric<E> base1, @NonNull ReifiedGeneric<F> base2) {
        return (ReifiedGeneric<NavigableMap<E, F>>) ReifiedGeneric.forType(
                make(NavigableMap.class, new Type[] {base1.getGeneric(), base2.getGeneric()}, null));
    }

    public <E, F> ReifiedGeneric<Map.Entry<E, F>> entry(@NonNull ReifiedGeneric<E> base1, @NonNull ReifiedGeneric<F> base2) {
        return (ReifiedGeneric<Map.Entry<E, F>>) ReifiedGeneric.forType(
                make(Map.Entry.class, new Type[] {base1.getGeneric(), base2.getGeneric()}, null));
    }
}
