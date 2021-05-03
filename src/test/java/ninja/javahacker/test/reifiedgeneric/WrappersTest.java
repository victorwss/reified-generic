package ninja.javahacker.test.reifiedgeneric;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
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
import ninja.javahacker.reifiedgeneric.Token;
import ninja.javahacker.reifiedgeneric.Wrappers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

/**
 * @author Victor Williams Stafusa da Silva
 */
public class WrappersTest {

    private static final ReifiedGeneric<String> NIL = null;
    private static final ReifiedGeneric<String> THING = new Token<String>() {}.getReified();
    private static final ReifiedGeneric<Integer> OTHER = new Token<Integer>() {}.getReified();

    @Test
    @SuppressWarnings("unchecked")
    public void testWrappersUninstantiable() throws Exception {
        Constructor<? extends Wrappers>[] ctors = (Constructor<? extends Wrappers>[]) Wrappers.class.getDeclaredConstructors();
        Assertions.assertEquals(1, ctors.length);
        Assertions.assertFalse(Modifier.isPublic(ctors[0].getModifiers()));
        ctors[0].setAccessible(true);
        InvocationTargetException ite = Assertions.assertThrows(InvocationTargetException.class, () -> ctors[0].newInstance());
        Assertions.assertTrue(ite.getCause() instanceof UnsupportedOperationException);
    }

    @Test
    public void testWrapSingleTypes() {
        var a = Wrappers.unwrapIterable(new Token<List<String>>() {}.getReified());
        var b = Wrappers.unwrapIterable(new Token<Collection<String>>() {}.getReified());
        var c = Wrappers.unwrapIterable(new Token<NavigableSet<String>>() {}.getReified());
        var d = Wrappers.unwrapIterator(new Token<Iterator<String>>() {}.getReified());
        var e = Wrappers.unwrapStream(new Token<Stream<String>>() {}.getReified());
        Assertions.assertAll(Stream.of(a, b, c, d, e).map(x -> () -> Assertions.assertEquals(THING, x)));
    }

    @Test
    public void testWrapMapTypes() {
        var a = Wrappers.unwrapMapKey(new Token<Map<String, Integer>>() {}.getReified());
        var b = Wrappers.unwrapMapEntryKey(new Token<Map.Entry<String, Integer>>() {}.getReified());
        var c = Wrappers.unwrapMapValue(new Token<Map<Integer, String>>() {}.getReified());
        var d = Wrappers.unwrapMapEntryValue(new Token<Map.Entry<Integer, String>>() {}.getReified());
        Assertions.assertAll(Stream.of(a, b, c, d).map(x -> () -> Assertions.assertEquals(THING, x)));
    }

    @Test
    public void testUnwrapIterable() {
        Assertions.assertEquals(new Token<Iterable<String>>() {}.getReified(), Wrappers.iterable(THING));
    }

    @Test
    public void testUnwrapIterator() {
        Assertions.assertEquals(new Token<Iterator<String>>() {}.getReified(), Wrappers.iterator(THING));
    }

    @Test
    public void testUnwrapCollection() {
        Assertions.assertEquals(new Token<Collection<String>>() {}.getReified(), Wrappers.collection(THING));
    }

    @Test
    public void testUnwrapList() {
        Assertions.assertEquals(new Token<List<String>>() {}.getReified(), Wrappers.list(THING));
    }

    @Test
    public void testUnwrapSet() {
        Assertions.assertEquals(new Token<Set<String>>() {}.getReified(), Wrappers.set(THING));
    }

    @Test
    public void testUnwrapSortedSet() {
        Assertions.assertEquals(new Token<SortedSet<String>>() {}.getReified(), Wrappers.sortedSet(THING));
    }

    @Test
    public void testUnwrapNavigableSet() {
        Assertions.assertEquals(new Token<NavigableSet<String>>() {}.getReified(), Wrappers.navigableSet(THING));
    }

    @Test
    public void testUnwrapMap() {
        Assertions.assertEquals(new Token<Map<String, Integer>>() {}.getReified(), Wrappers.map(THING, OTHER));
    }

    @Test
    public void testUnwrapMapEntry() {
        Assertions.assertEquals(new Token<Map.Entry<String, Integer>>() {}.getReified(), Wrappers.entry(THING, OTHER));
    }

    @Test
    public void testUnwrapSortedMap() {
        Assertions.assertEquals(new Token<SortedMap<String, Integer>>() {}.getReified(), Wrappers.sortedMap(THING, OTHER));
    }

    @Test
    public void testUnwrapNavigableMap() {
        Assertions.assertEquals(new Token<NavigableMap<String, Integer>>() {}.getReified(), Wrappers.navigableMap(THING, OTHER));
    }

    private static void nonNullWasNull(String name, Executable e) {
        IllegalArgumentException npe = Assertions.assertThrows(IllegalArgumentException.class, e);
        Assertions.assertEquals(name + " is marked non-null but is null", npe.getMessage());
    }

    @Test
    @SuppressWarnings("null")
    public void testUnwrapNull() {
        Assertions.assertAll("nulls",
                () -> nonNullWasNull("base", () -> Wrappers.iterable(NIL)),
                () -> nonNullWasNull("base", () -> Wrappers.iterator(NIL)),
                () -> nonNullWasNull("base", () -> Wrappers.stream(NIL)),
                () -> nonNullWasNull("base", () -> Wrappers.collection(NIL)),
                () -> nonNullWasNull("base", () -> Wrappers.list(NIL)),
                () -> nonNullWasNull("base", () -> Wrappers.set(NIL)),
                () -> nonNullWasNull("base", () -> Wrappers.sortedSet(NIL)),
                () -> nonNullWasNull("base", () -> Wrappers.navigableSet(NIL)),
                () -> nonNullWasNull("base1", () -> Wrappers.map(NIL, THING)),
                () -> nonNullWasNull("base1", () -> Wrappers.entry(NIL, THING)),
                () -> nonNullWasNull("base1", () -> Wrappers.sortedMap(NIL, THING)),
                () -> nonNullWasNull("base1", () -> Wrappers.navigableMap(NIL, THING)),
                () -> nonNullWasNull("base2", () -> Wrappers.map(THING, NIL)),
                () -> nonNullWasNull("base2", () -> Wrappers.entry(THING, NIL)),
                () -> nonNullWasNull("base2", () -> Wrappers.sortedMap(THING, NIL)),
                () -> nonNullWasNull("base2", () -> Wrappers.navigableMap(THING, NIL))
        );
    }
}
