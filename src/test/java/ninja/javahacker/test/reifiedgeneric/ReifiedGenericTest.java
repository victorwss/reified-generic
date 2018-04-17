package ninja.javahacker.test.reifiedgeneric;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import ninja.javahacker.reifiedgeneric.MalformedReifiedGenericException;
import ninja.javahacker.reifiedgeneric.ReifiedGeneric;
import ninja.javahacker.reifiedgeneric.Wrappers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

/**
 * @author Victor Williams Stafusa da Silva
 */
public class ReifiedGenericTest {

    @Test
    public void testClassGeneric1() {
        ReifiedGeneric<String> s1 = new ReifiedGeneric<String>() {};
        Assertions.assertEquals(String.class, s1.getGeneric());
        Assertions.assertEquals(String.class, s1.raw());
        Assertions.assertEquals("ReifiedGeneric<" + String.class.getName() + ">", s1.toString());
    }

    @Test
    public void testClassGeneric2() {
        ReifiedGeneric<String> s2 = ReifiedGeneric.forClass(String.class);
        Assertions.assertEquals(String.class, s2.getGeneric());
        Assertions.assertEquals(String.class, s2.raw());
        Assertions.assertEquals("ReifiedGeneric<" + String.class.getName() + ">", s2.toString());
    }

    @Test
    public void testClassGeneric3() {
        ReifiedGeneric<?> s2 = ReifiedGeneric.forType(String.class);
        Assertions.assertEquals(String.class, s2.getGeneric());
        Assertions.assertEquals(String.class, s2.raw());
        Assertions.assertEquals("ReifiedGeneric<" + String.class.getName() + ">", s2.toString());
    }

    @Test
    public void testParameterizedGeneric() {
        ReifiedGeneric<List<String>> s1 = new ReifiedGeneric<List<String>>() {};
        Assertions.assertEquals("java.util.List<java.lang.String>", s1.getGeneric().getTypeName());
        Assertions.assertEquals("ReifiedGeneric<" + s1.getGeneric().getTypeName() + ">", s1.toString());
        Assertions.assertEquals(List.class, s1.raw());
        ReifiedGeneric<?> s2 = ReifiedGeneric.forType(s1.getGeneric());
        Assertions.assertEquals("java.util.List<java.lang.String>", s2.getGeneric().getTypeName());
        Assertions.assertEquals("ReifiedGeneric<" + s2.getGeneric().getTypeName() + ">", s2.toString());
        Assertions.assertEquals(List.class, s2.raw());
    }

    @Test
    public void testComplexParameterizedGeneric() {
        ReifiedGeneric<List<? extends String>> s1 = new ReifiedGeneric<List<? extends String>>() {};
        Assertions.assertEquals("java.util.List<? extends java.lang.String>", s1.getGeneric().getTypeName());
        Assertions.assertEquals("ReifiedGeneric<" + s1.getGeneric().getTypeName() + ">", s1.toString());
        Assertions.assertEquals(List.class, s1.raw());
        ReifiedGeneric<?> s2 = ReifiedGeneric.forType(s1.getGeneric());
        Assertions.assertEquals("java.util.List<? extends java.lang.String>", s2.getGeneric().getTypeName());
        Assertions.assertEquals("ReifiedGeneric<" + s2.getGeneric().getTypeName() + ">", s2.toString());
        Assertions.assertEquals(List.class, s2.raw());
    }

    private <X> X foo1() {
        return null;
    }

    private <X> X[] foo2() {
        return null;
    }

    private List<?> foo3() {
        return null;
    }

    private void shouldGetException(String message, Executable e) {
        MalformedReifiedGenericException exception =
                Assertions.assertThrows(MalformedReifiedGenericException.class, e);
        Assertions.assertEquals(message, exception.getMessage());
    }

    private void npe(String message, Executable e) {
        NullPointerException exception =
                Assertions.assertThrows(NullPointerException.class, e);
        Assertions.assertEquals(message, exception.getMessage());
    }

    private void shouldBeInstantiable(Executable e) {
        shouldGetException(MalformedReifiedGenericException.SHOULD_BE_INSTANTIABLE, e);
    }

    private void illDefined(Executable e) {
        shouldGetException(MalformedReifiedGenericException.ILL_DEFINED, e);
    }

    @Test
    public <X> void errorWithTypeVariableGeneric() {
        shouldBeInstantiable(() -> new ReifiedGeneric<X>() {});
    }

    @Test
    public <X> void errorWithForTypeVariableGeneric() throws NoSuchMethodException {
        Type typeVariable = ReifiedGenericTest.class.getDeclaredMethod("foo1").getGenericReturnType();
        shouldBeInstantiable(() -> ReifiedGeneric.forType(typeVariable));
    }

    @Test
    public <X> void errorWithGenericArray() {
        shouldBeInstantiable(() -> new ReifiedGeneric<X[]>() {});
    }

    @Test
    public <X> void errorWithForTypeGenericArray() throws NoSuchMethodException {
        Type genericArray = ReifiedGenericTest.class.getDeclaredMethod("foo2").getGenericReturnType();
        shouldBeInstantiable(() -> ReifiedGeneric.forType(genericArray));
    }

    @Test
    public <X> void errorWithForTypeWildcard() throws NoSuchMethodException {
        Type parameterized = ReifiedGenericTest.class.getDeclaredMethod("foo3").getGenericReturnType();
        Type wildcard = ((ParameterizedType) parameterized).getActualTypeArguments()[0];
        shouldBeInstantiable(() -> ReifiedGeneric.forType(wildcard));
    }

    @Test
    @SuppressWarnings("rawtypes")
    public void errorWithRawType() {
        illDefined(() -> new ReifiedGeneric() {});
    }

    @Test
    public void errorForClassNull() {
        npe("type", () -> ReifiedGeneric.forClass(null));
    }

    @Test
    public void errorForTypeNull() {
        npe("type", () -> ReifiedGeneric.forType(null));
    }

    @Test
    public void testEqualsIsSameOfAndHashCode() {
        ReifiedGeneric<List<String>> s1a = new ReifiedGeneric<List<String>>() {};
        ReifiedGeneric<List<String>> s1b = new ReifiedGeneric<List<String>>() {};
        Assertions.assertEquals(s1a, s1b);
        Assertions.assertEquals(s1a.hashCode(), s1b.hashCode());
        Assertions.assertTrue(s1a.isSameOf(s1b));
        ReifiedGeneric<List<?>> s2a = new ReifiedGeneric<List<?>>() {};
        ReifiedGeneric<List<?>> s2b = new ReifiedGeneric<List<?>>() {};
        Assertions.assertEquals(s2a, s2b);
        Assertions.assertEquals(s2a.hashCode(), s2b.hashCode());
        Assertions.assertTrue(s2a.isSameOf(s2b));
        ReifiedGeneric<String> s3a = new ReifiedGeneric<String>() {};
        ReifiedGeneric<String> s3b = new ReifiedGeneric<String>() {};
        Assertions.assertEquals(s3a, s3b);
        Assertions.assertEquals(s3a.hashCode(), s3b.hashCode());
        Assertions.assertTrue(s3a.isSameOf(s3b));

        Assertions.assertFalse(s1a.equals(s2a));
        Assertions.assertFalse(s2a.equals(s1a));
        Assertions.assertFalse(s1a.equals(s3a));
        Assertions.assertFalse(s3a.equals(s1a));
        Assertions.assertFalse(s2a.equals(s3a));
        Assertions.assertFalse(s3a.equals(s2a));

        Assertions.assertFalse(s1a.isSameOf(s2a));
        Assertions.assertFalse(s2a.isSameOf(s1a));
        Assertions.assertFalse(s1a.isSameOf(s3a));
        Assertions.assertFalse(s3a.isSameOf(s1a));
        Assertions.assertFalse(s2a.isSameOf(s3a));
        Assertions.assertFalse(s3a.isSameOf(s2a));

        Assertions.assertNotEquals(s1a.hashCode(), s2a.hashCode());
        Assertions.assertNotEquals(s1a.hashCode(), s3a.hashCode());
        Assertions.assertNotEquals(s2a.hashCode(), s3a.hashCode());
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void testEqualsNull() {
        ReifiedGeneric<List<String>> s1 = new ReifiedGeneric<List<String>>() {};
        Assertions.assertFalse(s1.equals(null));
        ReifiedGeneric<List<?>> s2 = new ReifiedGeneric<List<?>>() {};
        Assertions.assertFalse(s2.equals(null));
        ReifiedGeneric<String> s3 = new ReifiedGeneric<String>() {};
        Assertions.assertFalse(s3.equals(null));
    }

    @Test
    @SuppressWarnings("IncompatibleEquals")
    public void testIncompatibleEquals() {
        ReifiedGeneric<List<String>> s1 = new ReifiedGeneric<List<String>>() {};
        Assertions.assertFalse(s1.equals("foo"));
        ReifiedGeneric<List<?>> s2 = new ReifiedGeneric<List<?>>() {};
        Assertions.assertFalse(s2.equals("foo"));
        ReifiedGeneric<String> s3 = new ReifiedGeneric<String>() {};
        Assertions.assertFalse(s3.equals("foo"));
    }

    @Test
    @SuppressWarnings("IncompatibleEquals")
    public void testHashCode() {
        ReifiedGeneric<List<String>> s1 = new ReifiedGeneric<List<String>>() {};
        Assertions.assertFalse(s1.equals("foo"));
        ReifiedGeneric<List<?>> s2 = new ReifiedGeneric<List<?>>() {};
        Assertions.assertFalse(s2.equals("foo"));
        ReifiedGeneric<String> s3 = new ReifiedGeneric<String>() {};
        Assertions.assertFalse(s3.equals("foo"));
    }
}
