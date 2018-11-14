package ninja.javahacker.test.reifiedgeneric;

import java.util.List;
import java.util.Map;
import ninja.javahacker.reifiedgeneric.ReifiedGeneric;
import ninja.javahacker.reifiedgeneric.Token;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Victor Williams Stafusa da Silva
 */
public class TokenTest {

    private <E> void testToken(Token<E> t, String x) {
        ReifiedGeneric<E> r = t.reify();
        Assertions.assertAll("tests",
                () -> Assertions.assertSame(r, t.reify()),
                () -> Assertions.assertSame(r, t.reify()),
                () -> Assertions.assertEquals("Token<" + x + ">", t.toString()),
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
}
