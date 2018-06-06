package ninja.javahacker.test.reifiedgeneric;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
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
import ninja.javahacker.reifiedgeneric.ReifiedGeneric;
import ninja.javahacker.reifiedgeneric.Wrappers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

/**
 * @author Victor Williams Stafusa da Silva
 */
public class WrappersTest {

    private static final ReifiedGeneric<String> NIL = null;
    private static final ReifiedGeneric<String> THING = new ReifiedGeneric<String>() {};
    private static final ReifiedGeneric<Integer> OTHER = new ReifiedGeneric<Integer>() {};

    @Test
    @SuppressWarnings("unchecked")
    public void testWrappersUninstantiable() throws Exception {
        Constructor<? extends Wrappers>[] ctors = (Constructor<? extends Wrappers>[]) Wrappers.class.getDeclaredConstructors();
        Assertions.assertEquals(1, ctors.length);
        Assertions.assertEquals(false, Modifier.isPublic(ctors[0].getModifiers()));
        ctors[0].setAccessible(true);
        InvocationTargetException ite = Assertions.assertThrows(InvocationTargetException.class, () -> ctors[0].newInstance());
        Assertions.assertTrue(ite.getCause() instanceof UnsupportedOperationException);
    }

    @Test
    public void testWrapSingleTypes() {
        ReifiedGeneric<String> a = Wrappers.unwrapIterable(new ReifiedGeneric<List<String>>() {});
        ReifiedGeneric<String> b = Wrappers.unwrapIterable(new ReifiedGeneric<Collection<String>>() {});
        ReifiedGeneric<String> c = Wrappers.unwrapIterable(new ReifiedGeneric<NavigableSet<String>>() {});
        ReifiedGeneric<String> d = Wrappers.unwrapIterator(new ReifiedGeneric<Iterator<String>>() {});
        ReifiedGeneric<String> e = Wrappers.unwrapStream(new ReifiedGeneric<Stream<String>>() {});
        for (ReifiedGeneric<String> r : Arrays.asList(a, b, c, d, e)) {
            Assertions.assertEquals(THING, r);
        }
    }

    @Test
    public void testWrapMapTypes() {
        ReifiedGeneric<String> a = Wrappers.unwrapMapKey(new ReifiedGeneric<Map<String, Integer>>() {});
        ReifiedGeneric<String> b = Wrappers.unwrapMapEntryKey(new ReifiedGeneric<Map.Entry<String, Integer>>() {});
        ReifiedGeneric<String> c = Wrappers.unwrapMapValue(new ReifiedGeneric<Map<Integer, String>>() {});
        ReifiedGeneric<String> d = Wrappers.unwrapMapEntryValue(new ReifiedGeneric<Map.Entry<Integer, String>>() {});
        for (ReifiedGeneric<String> r : Arrays.asList(a, b, c, d)) {
            Assertions.assertEquals(THING, r);
        }
    }

    @Test
    public void testUnwrapIterable() {
        Assertions.assertEquals(new ReifiedGeneric<Iterable<String>>() {}, Wrappers.iterable(THING));
    }

    @Test
    public void testUnwrapIterator() {
        Assertions.assertEquals(new ReifiedGeneric<Iterator<String>>() {}, Wrappers.iterator(THING));
    }

    @Test
    public void testUnwrapCollection() {
        Assertions.assertEquals(new ReifiedGeneric<Collection<String>>() {}, Wrappers.collection(THING));
    }

    @Test
    public void testUnwrapList() {
        Assertions.assertEquals(new ReifiedGeneric<List<String>>() {}, Wrappers.list(THING));
    }

    @Test
    public void testUnwrapSet() {
        Assertions.assertEquals(new ReifiedGeneric<Set<String>>() {}, Wrappers.set(THING));
    }

    @Test
    public void testUnwrapSortedSet() {
        Assertions.assertEquals(new ReifiedGeneric<SortedSet<String>>() {}, Wrappers.sortedSet(THING));
    }

    @Test
    public void testUnwrapNavigableSet() {
        Assertions.assertEquals(new ReifiedGeneric<NavigableSet<String>>() {}, Wrappers.navigableSet(THING));
    }

    @Test
    public void testUnwrapMap() {
        Assertions.assertEquals(new ReifiedGeneric<Map<String, Integer>>() {}, Wrappers.map(THING, OTHER));
    }

    @Test
    public void testUnwrapMapEntry() {
        Assertions.assertEquals(new ReifiedGeneric<Map.Entry<String, Integer>>() {}, Wrappers.entry(THING, OTHER));
    }

    @Test
    public void testUnwrapSortedMap() {
        Assertions.assertEquals(new ReifiedGeneric<SortedMap<String, Integer>>() {}, Wrappers.sortedMap(THING, OTHER));
    }

    @Test
    public void testUnwrapNavigableMap() {
        Assertions.assertEquals(new ReifiedGeneric<NavigableMap<String, Integer>>() {}, Wrappers.navigableMap(THING, OTHER));
    }

    private static void npe(String name, Executable e) {
        NullPointerException npe = Assertions.assertThrows(NullPointerException.class, e);
        Assertions.assertEquals(name + " is marked @NonNull but is null", npe.getMessage());
    }

    @Test
    @SuppressWarnings("null")
    public void testUnwrapNull() {
        npe("base", () -> Wrappers.iterable(NIL));
        npe("base", () -> Wrappers.iterator(NIL));
        npe("base", () -> Wrappers.stream(NIL));
        npe("base", () -> Wrappers.collection(NIL));
        npe("base", () -> Wrappers.list(NIL));
        npe("base", () -> Wrappers.set(NIL));
        npe("base", () -> Wrappers.sortedSet(NIL));
        npe("base", () -> Wrappers.navigableSet(NIL));
        npe("base1", () -> Wrappers.map(NIL, THING));
        npe("base1", () -> Wrappers.entry(NIL, THING));
        npe("base1", () -> Wrappers.sortedMap(NIL, THING));
        npe("base1", () -> Wrappers.navigableMap(NIL, THING));
        npe("base2", () -> Wrappers.map(THING, NIL));
        npe("base2", () -> Wrappers.entry(THING, NIL));
        npe("base2", () -> Wrappers.sortedMap(THING, NIL));
        npe("base2", () -> Wrappers.navigableMap(THING, NIL));
    }
}
