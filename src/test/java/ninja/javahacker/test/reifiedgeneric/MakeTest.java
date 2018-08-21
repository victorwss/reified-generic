package ninja.javahacker.test.reifiedgeneric;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import ninja.javahacker.reifiedgeneric.Wrappers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Victor Williams Stafusa da Silva
 */
public class MakeTest {

    private static class NestedFoo<A> {
        private class NestedFooB {}
    }

    private static NestedFoo<String>.NestedFooB foo4() {
        return null;
    }

    private static NestedFoo<String> foo5() {
        return null;
    }

    @Test
    public void hard() throws Exception {
        ParameterizedType t1 = (ParameterizedType) MakeTest.class.getDeclaredMethod("foo4").getGenericReturnType();
        ParameterizedType t2 = (ParameterizedType) MakeTest.class.getDeclaredMethod("foo5").getGenericReturnType();
        ParameterizedType t3 = Wrappers.make(NestedFoo.NestedFooB.class, new Type[] {}, t2);
        Assertions.assertAll("hard",
                () -> Assertions.assertEquals(t1, t3),
                () -> Assertions.assertEquals(t3, t1),
                () -> Assertions.assertEquals(t1.toString(), t3.toString()),
                () -> Assertions.assertEquals(t1.hashCode(), t3.hashCode())
        );
    }
}
