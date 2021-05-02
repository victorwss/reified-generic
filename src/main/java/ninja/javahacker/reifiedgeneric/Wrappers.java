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

    /**
     * {@link ParameterizedType} is an interface, but directly constructing its instances is difficult, so this method
     * provides a direct and simple way for that.
     *
     * <p>Unfortunately, there is no out-of-box easy way to get an instance of the {@link ParameterizedType} interface
     * in the JDK that also obeys implicit contracts further than the definition given by the interface methods (like
     * {@code equals(Object)}, {@code hashCode()} and {@code toString()} implementations.</p>
     *
     * @param rawType The value to be returned by the {@link ParameterizedType#getRawType()} method.
     * @param actualTypeArguments The value to be returned by the {@link ParameterizedType#getActualTypeArguments()} method.
     * @param ownerType The value to be returned by the {@link ParameterizedType#getOwnerType()} method.
     *
     * @return An instance of {@link ParameterizedType} with the given arguments.
     */
    public ParameterizedType make(Class<?> rawType, Type[] actualTypeArguments, Type ownerType) {
        return new MyParameterizedType(rawType, actualTypeArguments, ownerType);
    }

    /**
     * Unwrap a {@link ReifiedGeneric} of an {@link Iterable} to an {@link ReifiedGeneric} of its base type.
     * @param target The {@link ReifiedGeneric} of an {@link Iterable}.
     * @param <E> The generic type of the {@link Iterable}.
     * @return The {@link ReifiedGeneric} of the base type.
     * @throws IllegalArgumentException If the parameters is {@code null} or is not of an {@link Iterable} type.
     * @see #iterable(ReifiedGeneric)
     * @see #list(ReifiedGeneric)
     * @see #set(ReifiedGeneric)
     * @see #sortedSet(ReifiedGeneric)
     * @see #navigableSet(ReifiedGeneric)
     */
    public <E> ReifiedGeneric<E> unwrapIterable(@NonNull ReifiedGeneric<? extends Iterable<E>> target) {
        Class<?> raw = target.asClass();
        if (!Iterable.class.isAssignableFrom(raw)) throw new IllegalArgumentException(raw.getName() + " is not an Iterable.");
        ParameterizedType p = (ParameterizedType) target.getType();
        return (ReifiedGeneric<E>) ReifiedGeneric.of(p.getActualTypeArguments()[TYPE_INDEX]);
    }

    /**
     * Unwrap a {@link ReifiedGeneric} of an {@link Iterator} to an {@link ReifiedGeneric} of its base type.
     * @param target The {@link ReifiedGeneric} of an {@link Iterator}.
     * @param <E> The generic type of the {@link Iterator}.
     * @return The {@link ReifiedGeneric} of the base type.
     * @throws IllegalArgumentException If the parameters is {@code null} or is not of an {@link Iterator} type.
     * @see #iterator(ReifiedGeneric)
     */
    public <E> ReifiedGeneric<E> unwrapIterator(@NonNull ReifiedGeneric<? extends Iterator<E>> target) {
        Class<?> raw = target.asClass();
        if (!Iterator.class.isAssignableFrom(raw)) throw new IllegalArgumentException(raw.getName() + " is not an Iterator.");
        ParameterizedType p = (ParameterizedType) target.getType();
        return (ReifiedGeneric<E>) ReifiedGeneric.of(p.getActualTypeArguments()[TYPE_INDEX]);
    }

    /**
     * Unwrap a {@link ReifiedGeneric} of a {@link Stream} to an {@link ReifiedGeneric} of its base type.
     * @param target The {@link ReifiedGeneric} of a {@link Stream}.
     * @param <E> The generic type of the {@link Stream}.
     * @return The {@link ReifiedGeneric} of the base type.
     * @throws IllegalArgumentException If the parameters is {@code null} or is not of a {@link Stream} type.
     * @see #stream(ReifiedGeneric)
     */
    public <E> ReifiedGeneric<E> unwrapStream(@NonNull ReifiedGeneric<? extends Stream<E>> target) {
        Class<?> raw = target.asClass();
        if (!Stream.class.isAssignableFrom(raw)) throw new IllegalArgumentException(raw.getName() + " is not an Stream.");
        ParameterizedType p = (ParameterizedType) target.getType();
        return (ReifiedGeneric<E>) ReifiedGeneric.of(p.getActualTypeArguments()[TYPE_INDEX]);
    }

    /**
     * Unwrap a {@link ReifiedGeneric} of a {@link Map} to an {@link ReifiedGeneric} of its key type.
     * @param target The {@link ReifiedGeneric} of a {@link Map}.
     * @param <E> The generic type of the {@link Map}'s key.
     * @return The {@link ReifiedGeneric} of the key type.
     * @throws IllegalArgumentException If the parameters is {@code null} or is not of a {@link Map} type.
     * @see #map(ReifiedGeneric, ReifiedGeneric)
     * @see #sortedMap(ReifiedGeneric, ReifiedGeneric)
     * @see #navigableMap(ReifiedGeneric, ReifiedGeneric)
     */
    public <E> ReifiedGeneric<E> unwrapMapKey(@NonNull ReifiedGeneric<? extends Map<E, ?>> target) {
        Class<?> raw = target.asClass();
        if (!Map.class.isAssignableFrom(raw)) throw new IllegalArgumentException(raw.getName() + " is not a Map.");
        ParameterizedType p = (ParameterizedType) target.getType();
        return (ReifiedGeneric<E>) ReifiedGeneric.of(p.getActualTypeArguments()[KEY_INDEX]);
    }

    /**
     * Unwrap a {@link ReifiedGeneric} of a {@link Map} to an {@link ReifiedGeneric} of its value type.
     * @param target The {@link ReifiedGeneric} of a {@link Map}.
     * @param <E> The generic type of the {@link Map}'s value.
     * @return The {@link ReifiedGeneric} of the value type.
     * @throws IllegalArgumentException If the parameters is {@code null} or is not of a {@link Map} type.
     * @see #map(ReifiedGeneric, ReifiedGeneric)
     * @see #sortedMap(ReifiedGeneric, ReifiedGeneric)
     * @see #navigableMap(ReifiedGeneric, ReifiedGeneric)
     */
    public <E> ReifiedGeneric<E> unwrapMapValue(@NonNull ReifiedGeneric<? extends Map<?, E>> target) {
        Class<?> raw = target.asClass();
        if (!Map.class.isAssignableFrom(raw)) throw new IllegalArgumentException(raw.getName() + " is not a Map.");
        ParameterizedType p = (ParameterizedType) target.getType();
        return (ReifiedGeneric<E>) ReifiedGeneric.of(p.getActualTypeArguments()[VALUE_INDEX]);
    }

    /**
     * Unwrap a {@link ReifiedGeneric} of a {@link Map.Entry} to an {@link ReifiedGeneric} of its key type.
     * @param target The {@link ReifiedGeneric} of a {@link Map.Entry}.
     * @param <E> The generic type of the {@link Map.Entry}'s key.
     * @return The {@link ReifiedGeneric} of the entry's key type.
     * @throws IllegalArgumentException If the parameters is {@code null} or is not of a {@link Map.Entry} type.
     * @see #entry(ReifiedGeneric, ReifiedGeneric)
     */
    public <E> ReifiedGeneric<E> unwrapMapEntryKey(@NonNull ReifiedGeneric<? extends Map.Entry<E, ?>> target) {
        Class<?> raw = target.asClass();
        if (!Map.Entry.class.isAssignableFrom(raw)) throw new IllegalArgumentException(raw.getName() + " is not a Map.Entry.");
        ParameterizedType p = (ParameterizedType) target.getType();
        return (ReifiedGeneric<E>) ReifiedGeneric.of(p.getActualTypeArguments()[KEY_INDEX]);
    }

    /**
     * Unwrap a {@link ReifiedGeneric} of a {@link Map.Entry} to an {@link ReifiedGeneric} of its value type.
     * @param target The {@link ReifiedGeneric} of a {@link Map.Entry}.
     * @param <E> The generic type of the {@link Map.Entry}'s value.
     * @return The {@link ReifiedGeneric} of the entry's value type.
     * @throws IllegalArgumentException If the parameters is {@code null} or is not of a {@link Map.Entry} type.
     * @see #entry(ReifiedGeneric, ReifiedGeneric)
     */
    public <E> ReifiedGeneric<E> unwrapMapEntryValue(@NonNull ReifiedGeneric<? extends Map.Entry<?, E>> target) {
        Class<?> raw = target.asClass();
        if (!Map.Entry.class.isAssignableFrom(raw)) throw new IllegalArgumentException(raw.getName() + " is not a Map.Entry.");
        ParameterizedType p = (ParameterizedType) target.getType();
        return (ReifiedGeneric<E>) ReifiedGeneric.of(p.getActualTypeArguments()[VALUE_INDEX]);
    }

    /**
     * Wrap a {@link ReifiedGeneric} of a type into a {@link ReifiedGeneric} of an {@link Iterable} of that type.
     * @param base What should be wrapped.
     * @param <E> The generic type of the given {@link ReifiedGeneric}.
     * @return The {@link ReifiedGeneric} of an {@link Iterable} of the given base type.
     * @throws IllegalArgumentException If the parameters is {@code null}.
     * @see #unwrapIterable(ReifiedGeneric)
     */
    public <E> ReifiedGeneric<Iterable<E>> iterable(@NonNull ReifiedGeneric<E> base) {
        return (ReifiedGeneric<Iterable<E>>) ReifiedGeneric.of(make(Iterable.class, new Type[] {base.getType()}, null));
    }

    /**
     * Wrap a {@link ReifiedGeneric} of a type into a {@link ReifiedGeneric} of an {@link Iterator} of that type.
     * @param base What should be wrapped.
     * @param <E> The generic type of the given {@link ReifiedGeneric}.
     * @return The {@link ReifiedGeneric} of an {@link Iterator} of the given base type.
     * @throws IllegalArgumentException If the parameters is {@code null}.
     * @see #unwrapIterator(ReifiedGeneric)
     */
    public <E> ReifiedGeneric<Iterator<E>> iterator(@NonNull ReifiedGeneric<E> base) {
        return (ReifiedGeneric<Iterator<E>>) ReifiedGeneric.of(make(Iterator.class, new Type[] {base.getType()}, null));
    }

    /**
     * Wrap a {@link ReifiedGeneric} of a type into a {@link ReifiedGeneric} of a {@link Stream} of that type.
     * @param base What should be wrapped.
     * @param <E> The generic type of the given {@link ReifiedGeneric}.
     * @return The {@link ReifiedGeneric} of a {@link Stream} of the given base type.
     * @throws IllegalArgumentException If the parameters is {@code null}.
     * @see #unwrapStream(ReifiedGeneric)
     */
    public <E> ReifiedGeneric<Stream<E>> stream(@NonNull ReifiedGeneric<E> base) {
        return (ReifiedGeneric<Stream<E>>) ReifiedGeneric.of(make(Stream.class, new Type[] {base.getType()}, null));
    }

    /**
     * Wrap a {@link ReifiedGeneric} of a type into a {@link ReifiedGeneric} of a {@link Collection} of that type.
     * @param base What should be wrapped.
     * @param <E> The generic type of the given {@link ReifiedGeneric}.
     * @return The {@link ReifiedGeneric} of a {@link Collection} of the given base type.
     * @throws IllegalArgumentException If the parameters is {@code null}.
     * @see #unwrapIterable(ReifiedGeneric)
     */
    public <E> ReifiedGeneric<Collection<E>> collection(@NonNull ReifiedGeneric<E> base) {
        return (ReifiedGeneric<Collection<E>>) ReifiedGeneric.of(make(Collection.class, new Type[] {base.getType()}, null));
    }

    /**
     * Wrap a {@link ReifiedGeneric} of a type into a {@link ReifiedGeneric} of a {@link List} of that type.
     * @param base What should be wrapped.
     * @param <E> The generic type of the given {@link ReifiedGeneric}.
     * @return The {@link ReifiedGeneric} of a {@link List} of the given base type.
     * @throws IllegalArgumentException If the parameter is {@code null}.
     * @see #unwrapIterable(ReifiedGeneric)
     */
    public <E> ReifiedGeneric<List<E>> list(@NonNull ReifiedGeneric<E> base) {
        return (ReifiedGeneric<List<E>>) ReifiedGeneric.of(make(List.class, new Type[] {base.getType()}, null));
    }

    /**
     * Wrap a {@link ReifiedGeneric} of a type into a {@link ReifiedGeneric} of a {@link Set} of that type.
     * @param base What should be wrapped.
     * @param <E> The generic type of the given {@link ReifiedGeneric}.
     * @return The {@link ReifiedGeneric} of a {@link Set} of the given base type.
     * @throws IllegalArgumentException If the parameter is {@code null}.
     * @see #unwrapIterable(ReifiedGeneric)
     */
    public <E> ReifiedGeneric<Set<E>> set(@NonNull ReifiedGeneric<E> base) {
        return (ReifiedGeneric<Set<E>>) ReifiedGeneric.of(make(Set.class, new Type[] {base.getType()}, null));
    }

    /**
     * Wrap a {@link ReifiedGeneric} of a type into a {@link ReifiedGeneric} of a {@link SortedSet} of that type.
     * @param base What should be wrapped.
     * @param <E> The generic type of the given {@link ReifiedGeneric}.
     * @return The {@link ReifiedGeneric} of a {@link SortedSet} of the given base type.
     * @throws IllegalArgumentException If the parameter is {@code null}.
     * @see #unwrapIterable(ReifiedGeneric)
     */
    public <E> ReifiedGeneric<SortedSet<E>> sortedSet(@NonNull ReifiedGeneric<E> base) {
        return (ReifiedGeneric<SortedSet<E>>) ReifiedGeneric.of(make(SortedSet.class, new Type[] {base.getType()}, null));
    }

    /**
     * Wrap a {@link ReifiedGeneric} of a type into a {@link ReifiedGeneric} of a {@link NavigableSet} of that type.
     * @param base What should be wrapped.
     * @param <E> The generic type of the given {@link ReifiedGeneric}.
     * @return The {@link ReifiedGeneric} of a {@link NavigableSet} of the given base type.
     * @throws IllegalArgumentException If the parameter is {@code null}.
     * @see #unwrapIterable(ReifiedGeneric)
     */
    public <E> ReifiedGeneric<NavigableSet<E>> navigableSet(@NonNull ReifiedGeneric<E> base) {
        return (ReifiedGeneric<NavigableSet<E>>) ReifiedGeneric.of(make(NavigableSet.class, new Type[] {base.getType()}, null));
    }

    /**
     * Wrap two {@link ReifiedGeneric} of some types into a {@link ReifiedGeneric} of a {@link Map} with the first type as
     * the key type and the second type as the value type.
     * @param base1 The type of the key that should be wrapped.
     * @param base2 The type of the value that should be wrapped.
     * @param <E> The generic type of the first given {@link ReifiedGeneric} (i.e., the {@code base1}).
     * @param <F> The generic type of the second given {@link ReifiedGeneric} (i.e., the {@code base2}).
     * @return The {@link ReifiedGeneric} of a {@link Map} of the two given base types.
     * @throws IllegalArgumentException If any of the given parameters is {@code null}.
     * @see #unwrapMapKey(ReifiedGeneric)
     * @see #unwrapMapValue(ReifiedGeneric)
     */
    public <E, F> ReifiedGeneric<Map<E, F>> map(@NonNull ReifiedGeneric<E> base1, @NonNull ReifiedGeneric<F> base2) {
        return (ReifiedGeneric<Map<E, F>>) ReifiedGeneric.of(
                make(Map.class, new Type[] {base1.getType(), base2.getType()}, null));
    }

    /**
     * Wrap two {@link ReifiedGeneric} of some types into a {@link ReifiedGeneric} of a {@link SortedMap} with the first type as
     * the key type and the second type as the value type.
     * @param base1 The type of the key that should be wrapped.
     * @param base2 The type of the value that should be wrapped.
     * @param <E> The generic type of the first given {@link ReifiedGeneric} (i.e., the {@code base1}).
     * @param <F> The generic type of the second given {@link ReifiedGeneric} (i.e., the {@code base2}).
     * @return The {@link ReifiedGeneric} of a {@link SortedMap} of the two given base types.
     * @throws IllegalArgumentException If any of the given parameters is {@code null}.
     * @see #unwrapMapKey(ReifiedGeneric)
     * @see #unwrapMapValue(ReifiedGeneric)
     */
    public <E, F> ReifiedGeneric<SortedMap<E, F>> sortedMap(@NonNull ReifiedGeneric<E> base1, @NonNull ReifiedGeneric<F> base2) {
        return (ReifiedGeneric<SortedMap<E, F>>) ReifiedGeneric.of(
                make(SortedMap.class, new Type[] {base1.getType(), base2.getType()}, null));
    }

    /**
     * Wrap two {@link ReifiedGeneric} of some types into a {@link ReifiedGeneric} of a {@link NavigableMap} with the first type as
     * the key type and the second type as the value type.
     * @param base1 The type of the key that should be wrapped.
     * @param base2 The type of the value that should be wrapped.
     * @param <E> The generic type of the first given {@link ReifiedGeneric} (i.e., the {@code base1}).
     * @param <F> The generic type of the second given {@link ReifiedGeneric} (i.e., the {@code base2}).
     * @return The {@link ReifiedGeneric} of a {@link NavigableMap} of the two given base types.
     * @throws IllegalArgumentException If any of the given parameters is {@code null}.
     * @see #unwrapMapKey(ReifiedGeneric)
     * @see #unwrapMapValue(ReifiedGeneric)
     */
    public <E, F> ReifiedGeneric<NavigableMap<E, F>> navigableMap(@NonNull ReifiedGeneric<E> base1, @NonNull ReifiedGeneric<F> base2) {
        return (ReifiedGeneric<NavigableMap<E, F>>) ReifiedGeneric.of(
                make(NavigableMap.class, new Type[] {base1.getType(), base2.getType()}, null));
    }

    /**
     * Wrap two {@link ReifiedGeneric} of some types into a {@link ReifiedGeneric} of a {@link Map.Entry} with the first type as
     * the key type and the second type as the value type.
     * @param base1 The type of the key that should be wrapped.
     * @param base2 The type of the value that should be wrapped.
     * @param <E> The generic type of the first given {@link ReifiedGeneric} (i.e., the {@code base1}).
     * @param <F> The generic type of the second given {@link ReifiedGeneric} (i.e., the {@code base2}).
     * @return The {@link ReifiedGeneric} of a {@link Map.Entry} of the two given base types.
     * @throws IllegalArgumentException If any of the given parameters is {@code null}.
     * @see #unwrapMapEntryKey(ReifiedGeneric)
     * @see #unwrapMapEntryValue(ReifiedGeneric)
     */
    public <E, F> ReifiedGeneric<Map.Entry<E, F>> entry(@NonNull ReifiedGeneric<E> base1, @NonNull ReifiedGeneric<F> base2) {
        return (ReifiedGeneric<Map.Entry<E, F>>) ReifiedGeneric.of(
                make(Map.Entry.class, new Type[] {base1.getType(), base2.getType()}, null));
    }
}
