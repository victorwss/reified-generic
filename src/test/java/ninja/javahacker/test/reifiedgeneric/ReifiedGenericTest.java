package ninja.javahacker.test.reifiedgeneric;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import ninja.javahacker.reifiedgeneric.ReifiedGeneric;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Victor Williams Stafusa da Silva
 */
public class ReifiedGenericTest {

    @Test
    public void testClassGeneric1() {
        ReifiedGeneric<String> s1 = new ReifiedGeneric<>() {};
        Assertions.assertEquals(String.class, s1.getGeneric());
        Assertions.assertEquals(String.class, s1.raw());
        Assertions.assertEquals(String.class.getName(), s1.toString());
    }

    @Test
    public void testClassGeneric2() {
        ReifiedGeneric<String> s2 = ReifiedGeneric.forClass(String.class);
        Assertions.assertEquals(String.class, s2.getGeneric());
        Assertions.assertEquals(String.class, s2.raw());
        Assertions.assertEquals(String.class.getName(), s2.toString());
    }

    @Test
    public void testClassGeneric3() {
        ReifiedGeneric<?> s2 = ReifiedGeneric.forType(String.class);
        Assertions.assertEquals(String.class, s2.getGeneric());
        Assertions.assertEquals(String.class, s2.raw());
        Assertions.assertEquals(String.class.getName(), s2.toString());
    }

    @Test
    public void testParameterizedGeneric() {
        ReifiedGeneric<List<String>> s1 = new ReifiedGeneric<>() {};
        Assertions.assertEquals("java.util.List<java.lang.String>", s1.getGeneric().getTypeName());
        Assertions.assertEquals(s1.getGeneric().getTypeName(), s1.toString());
        Assertions.assertEquals(List.class, s1.raw());
        ReifiedGeneric<?> s2 = ReifiedGeneric.forType(s1.getGeneric());
        Assertions.assertEquals("java.util.List<java.lang.String>", s2.getGeneric().getTypeName());
        Assertions.assertEquals(s2.getGeneric().getTypeName(), s2.toString());
        Assertions.assertEquals(List.class, s2.raw());
    }

    @Test
    public void testComplexParameterizedGeneric() {
        ReifiedGeneric<List<? extends String>> s1 = new ReifiedGeneric<>() {};
        Assertions.assertEquals("java.util.List<? extends java.lang.String>", s1.getGeneric().getTypeName());
        Assertions.assertEquals(s1.getGeneric().getTypeName(), s1.toString());
        Assertions.assertEquals(List.class, s1.raw());
        ReifiedGeneric<?> s2 = ReifiedGeneric.forType(s1.getGeneric());
        Assertions.assertEquals("java.util.List<? extends java.lang.String>", s2.getGeneric().getTypeName());
        Assertions.assertEquals(s2.getGeneric().getTypeName(), s2.toString());
        Assertions.assertEquals(List.class, s2.raw());
    }

    @Test
    public <X> void errorWithTypeVariableGeneric() {
        try {
            new ReifiedGeneric<X>() {};
            Assertions.fail("Não deveria ser instanciável sem o tipo genérico.");
        } catch (ReifiedGeneric.MalformedReifiedGenericException expected) {
            Assertions.assertEquals("The generic type should be instantiable.", expected.getMessage());
        }
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

    @Test
    public <X> void errorWithForTypeVariableGeneric() throws NoSuchMethodException {
        try {
            ReifiedGeneric.forType(ReifiedGenericTest.class.getDeclaredMethod("foo1").getGenericReturnType());
            Assertions.fail("Shouldn't be instantiable with a type variable.");
        } catch (ReifiedGeneric.MalformedReifiedGenericException expected) {
            Assertions.assertEquals(ReifiedGeneric.MalformedReifiedGenericException.SHOULD_BE_INSTANTIABLE, expected.getMessage());
        }
    }

    @Test
    public <X> void errorWithGenericArray() {
        try {
            new ReifiedGeneric<X[]>() {};
            Assertions.fail("Shouldn't be instantiable with a generic array.");
        } catch (ReifiedGeneric.MalformedReifiedGenericException expected) {
            Assertions.assertEquals(ReifiedGeneric.MalformedReifiedGenericException.SHOULD_BE_INSTANTIABLE, expected.getMessage());
        }
    }

    @Test
    public <X> void errorWithForTypeGenericArray() throws NoSuchMethodException {
        try {
            ReifiedGeneric.forType(ReifiedGenericTest.class.getDeclaredMethod("foo2").getGenericReturnType());
            Assertions.fail("Shouldn't be instantiable with a generic array.");
        } catch (ReifiedGeneric.MalformedReifiedGenericException expected) {
            Assertions.assertEquals(ReifiedGeneric.MalformedReifiedGenericException.SHOULD_BE_INSTANTIABLE, expected.getMessage());
        }
    }

    @Test
    public <X> void errorWithForTypeWildcard() throws NoSuchMethodException {
        try {
            Type wildcard = ReifiedGenericTest.class.getDeclaredMethod("foo3").getGenericReturnType();
            ReifiedGeneric.forType(((ParameterizedType) wildcard).getActualTypeArguments()[0]);
            Assertions.fail("Shouldn't be instantiable with a wildcard.");
        } catch (ReifiedGeneric.MalformedReifiedGenericException expected) {
            Assertions.assertEquals(ReifiedGeneric.MalformedReifiedGenericException.SHOULD_BE_INSTANTIABLE, expected.getMessage());
        }
    }

    @Test
    @SuppressWarnings("rawtypes")
    public void errorWithRawType() {
        try {
            new ReifiedGeneric() {};
            Assertions.fail("Shouldn't be instantiable without the generic type.");
        } catch (ReifiedGeneric.MalformedReifiedGenericException expected) {
            Assertions.assertEquals(ReifiedGeneric.MalformedReifiedGenericException.ILL_DEFINED, expected.getMessage());
        }
    }

    @Test
    public void errorForClassNull() {
        try {
            ReifiedGeneric.forClass(null);
            Assertions.fail("Shouldn't be instantiable with null.");
        } catch (ReifiedGeneric.MalformedReifiedGenericException expected) {
            Assertions.assertEquals(ReifiedGeneric.MalformedReifiedGenericException.SHOULD_BE_INSTANTIABLE, expected.getMessage());
        }
    }

    @Test
    public void errorForTypeNull() {
        try {
            ReifiedGeneric.forType(null);
            Assertions.fail("Shouldn't be instantiable with null.");
        } catch (ReifiedGeneric.MalformedReifiedGenericException expected) {
            Assertions.assertEquals(ReifiedGeneric.MalformedReifiedGenericException.SHOULD_BE_INSTANTIABLE, expected.getMessage());
        }
    }

    @Test
    public void testEqualsIsSameOfAndHashCode() {
        ReifiedGeneric<List<String>> s1a = new ReifiedGeneric<>() {};
        ReifiedGeneric<List<String>> s1b = new ReifiedGeneric<>() {};
        Assertions.assertEquals(s1a, s1b);
        Assertions.assertEquals(s1a.hashCode(), s1b.hashCode());
        Assertions.assertTrue(s1a.isSameOf(s1b));
        ReifiedGeneric<List<?>> s2a = new ReifiedGeneric<>() {};
        ReifiedGeneric<List<?>> s2b = new ReifiedGeneric<>() {};
        Assertions.assertEquals(s2a, s2b);
        Assertions.assertEquals(s2a.hashCode(), s2b.hashCode());
        Assertions.assertTrue(s2a.isSameOf(s2b));
        ReifiedGeneric<String> s3a = new ReifiedGeneric<>() {};
        ReifiedGeneric<String> s3b = new ReifiedGeneric<>() {};
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
        ReifiedGeneric<List<String>> s1 = new ReifiedGeneric<>() {};
        Assertions.assertFalse(s1.equals(null));
        ReifiedGeneric<List<?>> s2 = new ReifiedGeneric<>() {};
        Assertions.assertFalse(s2.equals(null));
        ReifiedGeneric<String> s3 = new ReifiedGeneric<>() {};
        Assertions.assertFalse(s3.equals(null));
    }

    @Test
    @SuppressWarnings("IncompatibleEquals")
    public void testIncompatibleEquals() {
        ReifiedGeneric<List<String>> s1 = new ReifiedGeneric<>() {};
        Assertions.assertFalse(s1.equals("foo"));
        ReifiedGeneric<List<?>> s2 = new ReifiedGeneric<>() {};
        Assertions.assertFalse(s2.equals("foo"));
        ReifiedGeneric<String> s3 = new ReifiedGeneric<>() {};
        Assertions.assertFalse(s3.equals("foo"));
    }

    @Test
    @SuppressWarnings("IncompatibleEquals")
    public void testHashCode() {
        ReifiedGeneric<List<String>> s1 = new ReifiedGeneric<>() {};
        Assertions.assertFalse(s1.equals("foo"));
        ReifiedGeneric<List<?>> s2 = new ReifiedGeneric<>() {};
        Assertions.assertFalse(s2.equals("foo"));
        ReifiedGeneric<String> s3 = new ReifiedGeneric<>() {};
        Assertions.assertFalse(s3.equals("foo"));
    }
}
