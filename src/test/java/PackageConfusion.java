// No package!

/**
 * @author Victor Williams Stafusa da Silva
 */
public class PackageConfusion<A> {
    //CHECKSTYLE OFF: TypeName
    public class PackageConfusion$xxx<B> {
    }
    //CHECKSTYLE ON: TypeName

    public static PackageConfusion<Integer>.PackageConfusion$xxx<Integer> blah() {
        throw new UnsupportedOperationException();
    }
}
