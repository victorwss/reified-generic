package ninja.javahacker.test.reifiedgeneric;

import java.util.List;
import java.util.Map;
import ninja.javahacker.reifiedgeneric.MalformedReifiedGenericException;
import ninja.javahacker.reifiedgeneric.ReifiedGeneric;
import ninja.javahacker.reifiedgeneric.Token;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Victor Williams Stafusa da Silva
 */
public class TokenTest {

    private <E> void testToken(Token<E> t, String x) {
        ReifiedGeneric<E> r = t.getReified();
        Assertions.assertAll("tests",
                () -> Assertions.assertSame(r, t.getReified()),
                () -> Assertions.assertSame(r, t.getReified()),
                () -> Assertions.assertEquals("ReifiedGeneric<" + x + ">", r.toString())
        );
    }

    @Test
    public void testSimple() {
        testToken(new Token<String>() {}, String.class.getName());
    }

    @Test
    public void testComplex() throws Exception {
        testToken(new Token<Map<Integer, ? extends List<String>>>() {},
                TokenTest.class.getDeclaredMethod("foo").getGenericReturnType().getTypeName());
    }

    private Map<Integer, ? extends List<String>> foo() {
        throw new UnsupportedOperationException();
    }

    @Test
    @SuppressWarnings("rawtypes") // It's intended here.
    public void testBadRawType() throws Exception {
        var ex = Assertions.assertThrows(MalformedReifiedGenericException.class, () -> new Token() {});
        Assertions.assertEquals(MalformedReifiedGenericException.RAW_ERROR_MESSAGE, ex.getMessage());
    }

    @SuppressWarnings("rawtypes") // It's intended here.
    private static class Foo1 extends Token {}

    private static class Foo2<X> extends Token<X> {}

    private static class Foo3<X> extends Token<X[]> {}

    @Test
    public void testBadTokenSubclass() throws Exception {
        var ex = Assertions.assertThrows(MalformedReifiedGenericException.class, () -> new Foo1());
        Assertions.assertEquals(MalformedReifiedGenericException.RAW_ERROR_MESSAGE, ex.getMessage());
    }

    @Test
    public void testVeryBadTokenSubclass() throws Exception {
        var ex = Assertions.assertThrows(MalformedReifiedGenericException.class, () -> new Foo2<String>());
        Assertions.assertEquals(MalformedReifiedGenericException.TYPE_VARIABLE_ERROR_MESSAGE, ex.getMessage());
    }

    @Test
    public void testVeryBadTokenSubclass2() throws Exception {
        var ex = Assertions.assertThrows(MalformedReifiedGenericException.class, () -> new Foo3<String>());
        Assertions.assertEquals(MalformedReifiedGenericException.GENERIC_ARRAY_ERROR_MESSAGE, ex.getMessage());
    }
}
