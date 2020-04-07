package ninja.javahacker.test.reifiedgeneric;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import ninja.javahacker.reifiedgeneric.MalformedReifiedGenericException;
import ninja.javahacker.reifiedgeneric.ReifiedGeneric;
import ninja.javahacker.reifiedgeneric.Token;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

/**
 * @author Victor Williams Stafusa da Silva
 */
public class ReifiedGenericTest {

    private void testString(ReifiedGeneric<?> s) {
        Assertions.assertAll("tests",
                () -> Assertions.assertEquals(String.class, s.getType()),
                () -> Assertions.assertEquals(String.class, s.raw()),
                () -> Assertions.assertEquals("ReifiedGeneric<" + String.class.getName() + ">", s.toString()),
                () -> Assertions.assertTrue(s.isAssignableFrom(String.class)),
                () -> Assertions.assertTrue(s.isCompatibleWith(String.class))
        );
    }

    @Test
    public void testClassGeneric1() {
        ReifiedGeneric<String> s1 = new Token<String>() {}.reify();
        testString(s1);
    }

    @Test
    public void testClassGeneric2() {
        ReifiedGeneric<String> s2 = ReifiedGeneric.of(String.class);
        testString(s2);
    }

    @Test
    public void testClassGeneric3() {
        ReifiedGeneric<?> s2 = ReifiedGeneric.of(String.class);
        testString(s2);
    }

    @Test
    public void testParameterizedGeneric() {
        ReifiedGeneric<List<String>> s1 = new Token<List<String>>() {}.reify();
        ReifiedGeneric<?> s2 = ReifiedGeneric.of(s1.getType());
        Assertions.assertAll("tests",
                () -> Assertions.assertEquals("java.util.List<java.lang.String>", s1.getType().getTypeName()),
                () -> Assertions.assertEquals("ReifiedGeneric<" + s1.getType().getTypeName() + ">", s1.toString()),
                () -> Assertions.assertEquals(List.class, s1.raw()),
                () -> Assertions.assertEquals("java.util.List<java.lang.String>", s2.getType().getTypeName()),
                () -> Assertions.assertEquals("ReifiedGeneric<" + s2.getType().getTypeName() + ">", s2.toString()),
                () -> Assertions.assertEquals(List.class, s2.raw()),
                () -> Assertions.assertTrue(s1.isAssignableFrom(List.class)),
                () -> Assertions.assertTrue(s1.isCompatibleWith(List.class)),
                () -> Assertions.assertTrue(s2.isAssignableFrom(List.class)),
                () -> Assertions.assertTrue(s2.isCompatibleWith(List.class)),
                () -> Assertions.assertEquals(s1, s2),
                () -> Assertions.assertEquals(s2, s1),
                () -> Assertions.assertEquals(s1.hashCode(), s2.hashCode())
        );
    }

    @Test
    public void testComplexParameterizedGeneric() {
        ReifiedGeneric<List<? extends String>> s1 = new Token<List<? extends String>>() {}.reify();
        ReifiedGeneric<?> s2 = ReifiedGeneric.of(s1.getType());
        Assertions.assertAll("tests",
                () -> Assertions.assertEquals("java.util.List<? extends java.lang.String>", s1.getType().getTypeName()),
                () -> Assertions.assertEquals("ReifiedGeneric<" + s1.getType().getTypeName() + ">", s1.toString()),
                () -> Assertions.assertEquals(List.class, s1.raw()),
                () -> Assertions.assertEquals("java.util.List<? extends java.lang.String>", s2.getType().getTypeName()),
                () -> Assertions.assertEquals("ReifiedGeneric<" + s2.getType().getTypeName() + ">", s2.toString()),
                () -> Assertions.assertEquals(List.class, s2.raw()),
                () -> Assertions.assertTrue(s1.isAssignableFrom(List.class)),
                () -> Assertions.assertTrue(s1.isCompatibleWith(List.class)),
                () -> Assertions.assertTrue(s2.isAssignableFrom(List.class)),
                () -> Assertions.assertTrue(s2.isCompatibleWith(List.class)),
                () -> Assertions.assertEquals(s1, s2),
                () -> Assertions.assertEquals(s2, s1),
                () -> Assertions.assertEquals(s1.hashCode(), s2.hashCode())
        );
    }

    private <X> X foo1() {
        throw new UnsupportedOperationException();
    }

    private <X> X[] foo2() {
        throw new UnsupportedOperationException();
    }

    private List<?> foo3() {
        throw new UnsupportedOperationException();
    }

    private <T> List<T> foo4() {
        throw new UnsupportedOperationException();
    }

    private void shouldGetException(String message, Executable e) {
        MalformedReifiedGenericException exception =
                Assertions.assertThrows(MalformedReifiedGenericException.class, e);
        Assertions.assertEquals(message, exception.getMessage());
    }

    private void nonNullWasNull(String message, Executable e) {
        IllegalArgumentException exception =
                Assertions.assertThrows(IllegalArgumentException.class, e);
        Assertions.assertEquals(message + " is marked non-null but is null", exception.getMessage());
    }

    private void shouldThrow(String error, Executable e) {
        shouldGetException(error, e);
    }

    @Test
    public void errorWithTypeVariableGeneric() {
        shouldThrow(MalformedReifiedGenericException.TYPE_VARIABLE, this::createsTypeVariable);
    }

    private <X> ReifiedGeneric<X> createsTypeVariable() {
        return new Token<X>() {}.reify();
    }

    @Test
    public void testWithInnerTypeVariableGeneric() {
        createsInnerTypeVariable();
    }

    private <X> ReifiedGeneric<List<X>> createsInnerTypeVariable() {
        return new Token<List<X>>() {}.reify();
    }

    @Test
    public void errorWithForTypeVariableGeneric() throws NoSuchMethodException {
        Type typeVariable = ReifiedGenericTest.class.getDeclaredMethod("foo1").getGenericReturnType();
        shouldThrow(MalformedReifiedGenericException.TYPE_VARIABLE, () -> ReifiedGeneric.of(typeVariable));
    }

    @Test
    public void errorWithGenericArray() {
        shouldThrow(MalformedReifiedGenericException.GENERIC_ARRAY, this::createsGenericArray);
    }

    private <X> ReifiedGeneric<X[]> createsGenericArray() {
        return new Token<X[]>() {}.reify();
    }

    @Test
    public void errorWithForTypeGenericArray() throws NoSuchMethodException {
        Type genericArray = ReifiedGenericTest.class.getDeclaredMethod("foo2").getGenericReturnType();
        shouldThrow(MalformedReifiedGenericException.GENERIC_ARRAY, () -> ReifiedGeneric.of(genericArray));
    }

    @Test
    public void errorWithForTypeWildcard() throws NoSuchMethodException {
        Type parameterized = ReifiedGenericTest.class.getDeclaredMethod("foo3").getGenericReturnType();
        Type wildcard = ((ParameterizedType) parameterized).getActualTypeArguments()[0];
        shouldThrow(MalformedReifiedGenericException.WILDCARD, () -> ReifiedGeneric.of(wildcard));
    }

    @Test
    public void errorWithRawType() {
        shouldThrow(MalformedReifiedGenericException.RAW, this::createsRawType);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private ReifiedGeneric createsRawType() {
        return new Token() {}.reify();
    }

    @Test
    public void errorForClassNull() {
        nonNullWasNull("type", () -> ReifiedGeneric.of(null));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void errorForTypeNull() {
        nonNullWasNull("type", () -> ReifiedGeneric.of((Type) null));
    }

    @Test
    public void testEqualsIsSameOfAndHashCode() {
        ReifiedGeneric<List<String>> s1a = new Token<List<String>>() {}.reify();
        ReifiedGeneric<List<String>> s1b = new Token<List<String>>() {}.reify();
        ReifiedGeneric<List<?>> s2a = new Token<List<?>>() {}.reify();
        ReifiedGeneric<List<?>> s2b = new Token<List<?>>() {}.reify();
        ReifiedGeneric<String> s3a = new Token<String>() {}.reify();
        ReifiedGeneric<String> s3b = new Token<String>() {}.reify();

        Assertions.assertAll("equals",
                () -> Assertions.assertEquals(s1a, s1b),
                () -> Assertions.assertEquals(s1a.hashCode(), s1b.hashCode()),
                () -> Assertions.assertTrue(s1a.isSameOf(s1b)),
                () -> Assertions.assertEquals(s2a, s2b),
                () -> Assertions.assertEquals(s2a.hashCode(), s2b.hashCode()),
                () -> Assertions.assertTrue(s2a.isSameOf(s2b)),
                () -> Assertions.assertEquals(s3a, s3b),
                () -> Assertions.assertEquals(s3a.hashCode(), s3b.hashCode()),
                () -> Assertions.assertTrue(s3a.isSameOf(s3b)),

                () -> Assertions.assertFalse(s1a.equals(s2a)),
                () -> Assertions.assertFalse(s2a.equals(s1a)),
                () -> Assertions.assertFalse(s1a.equals(s3a)),
                () -> Assertions.assertFalse(s3a.equals(s1a)),
                () -> Assertions.assertFalse(s2a.equals(s3a)),
                () -> Assertions.assertFalse(s3a.equals(s2a)),

                () -> Assertions.assertFalse(s1a.isSameOf(s2a)),
                () -> Assertions.assertFalse(s2a.isSameOf(s1a)),
                () -> Assertions.assertFalse(s1a.isSameOf(s3a)),
                () -> Assertions.assertFalse(s3a.isSameOf(s1a)),
                () -> Assertions.assertFalse(s2a.isSameOf(s3a)),
                () -> Assertions.assertFalse(s3a.isSameOf(s2a)),

                () -> Assertions.assertNotEquals(s1a.hashCode(), s2a.hashCode()),
                () -> Assertions.assertNotEquals(s1a.hashCode(), s3a.hashCode()),
                () -> Assertions.assertNotEquals(s2a.hashCode(), s3a.hashCode())
        );
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void testEqualsNull() {
        ReifiedGeneric<List<String>> s1 = new Token<List<String>>() {}.reify();
        ReifiedGeneric<List<?>> s2 = new Token<List<?>>() {}.reify();
        ReifiedGeneric<String> s3 = new Token<String>() {}.reify();
        Assertions.assertAll("equals(null)",
                () -> Assertions.assertFalse(s1.equals(null)),
                () -> Assertions.assertFalse(s2.equals(null)),
                () -> Assertions.assertFalse(s3.equals(null))
        );
    }

    @Test
    @SuppressWarnings("IncompatibleEquals")
    public void testIncompatibleEquals() {
        ReifiedGeneric<List<String>> s1 = new Token<List<String>>() {}.reify();
        ReifiedGeneric<List<?>> s2 = new Token<List<?>>() {}.reify();
        ReifiedGeneric<String> s3 = new Token<String>() {}.reify();
        Assertions.assertAll("equals(String)",
                () -> Assertions.assertFalse(s1.equals("foo")),
                () -> Assertions.assertFalse(s2.equals("foo")),
                () -> Assertions.assertFalse(s3.equals("foo"))
        );
    }
}
