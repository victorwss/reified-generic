package ninja.javahacker.test.reifiedgeneric;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import ninja.javahacker.reifiedgeneric.Wrappers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Victor Williams Stafusa da Silva
 */
public class ParameterizedTypeTest {

    private static final String NAME1 = ParameterizedTypeTest.class.getName() + "$FooA<java.lang.String>$FooB";

    private static final String NAME2 = ParameterizedTypeTest.class.getName() + "$FooA<java.lang.String>$FooC<java.lang.Integer>";

    private static final String NAME3 = NAME2 + "$FooD<java.lang.Long>";

    private static class FooA<A> {

        private class FooB {}

        private class FooC<E> {
            private class FooD<F> {}
        }
    }

    private static FooA<String> foo1() {
        throw new UnsupportedOperationException();
    }

    private static FooA<String>.FooB foo2() {
        throw new UnsupportedOperationException();
    }

    private static FooA<String>.FooC<Integer> foo3() {
        throw new UnsupportedOperationException();
    }

    private static FooA<String>.FooC<Integer>.FooD<Long> foo4() {
        throw new UnsupportedOperationException();
    }

    private static List<String> foo5() {
        throw new UnsupportedOperationException();
    }

    private static <T> List<T> foo6() {
        throw new UnsupportedOperationException();
    }

    private static int unhash(ParameterizedType x) {
        Type owner = x.getOwnerType();
        return x.getRawType().hashCode()
                ^ (owner == null ? 0 : owner.hashCode())
                ^ Arrays.hashCode(x.getActualTypeArguments())
                ^ x.hashCode();
    }

    private static void verify(ParameterizedType a, ParameterizedType b, String name) {
        Type ownerA = a.getOwnerType();
        Type ownerB = b.getOwnerType();
        Assertions.assertAll(
                () -> Assertions.assertEquals(a, b),
                () -> Assertions.assertEquals(b, a),
                () -> Assertions.assertEquals(a.toString(), b.toString()),
                () -> Assertions.assertEquals(name, b.toString()),
                () -> Assertions.assertEquals("W" + a.hashCode(), "W" + b.hashCode()),
                () -> Assertions.assertEquals("x" + a.getRawType().hashCode(), "x" + b.getRawType().hashCode()),
                () -> Assertions.assertEquals(
                        "y" + (ownerA == null ? 0 : ownerA.hashCode()),
                        "y" + (ownerB == null ? 0 : ownerB.hashCode())),
                () -> Assertions.assertEquals(
                        "z" + Arrays.hashCode(a.getActualTypeArguments()),
                        "z" + Arrays.hashCode(b.getActualTypeArguments())),
                () -> Assertions.assertEquals("U0", "U" + unhash(a)),
                () -> Assertions.assertEquals("V0", "V" + unhash(b))
        );
    }

    private static ParameterizedType reflect(String methodName) throws Exception {
        return (ParameterizedType) ParameterizedTypeTest.class.getDeclaredMethod(methodName).getGenericReturnType();
    }

    @Test
    public void easyParameterizedTest() throws Exception {
        ParameterizedType t = Wrappers.make(List.class, new Type[] {String.class}, null);
        verify(reflect("foo5"), t, "java.util.List<java.lang.String>");
    }

    @Test
    public void innerClassTest() throws Exception {
        ParameterizedType t = Wrappers.make(FooA.FooB.class, new Type[] {}, reflect("foo1"));
        verify(reflect("foo2"), t, NAME1);
    }

    @Test
    public void parameterizedInnerClassTest() throws Exception {
        ParameterizedType t = Wrappers.make(FooA.FooC.class, new Type[] {Integer.class}, reflect("foo1"));
        verify(reflect("foo3"), t, NAME2);
    }

    @Test
    public void deepParameterizedInnerClassTest() throws Exception {
        ParameterizedType t = Wrappers.make(FooA.FooC.FooD.class, new Type[] {Long.class}, reflect("foo3"));
        verify(reflect("foo4"), t, NAME3);
    }

    @Test
    public void oopsParameterizedTest() throws Exception {
        ParameterizedType p = reflect("foo6");
        ParameterizedType t = Wrappers.make(List.class, p.getActualTypeArguments(), null);
        verify(p, t, "java.util.List<T>");
    }
}
