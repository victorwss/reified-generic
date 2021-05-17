import ninja.javahacker.reifiedgeneric.ReifiedGeneric;
import ninja.javahacker.reifiedgeneric.Token;

/**
 * Defines the {@link Token} and the {@link ReifiedGeneric} classes, which cretes a mechanism for declaring type tokens.
 * <p>For example, you can't say {@code List<String>.class} due to type-erasure.
 * The solution is to use <code>Token&lt;String&gt; {}.getReified()</code> instead.</p>
 * @see ninja.javahacker.reifiedgeneric.ReifiedGeneric
 * @see ninja.javahacker.reifiedgeneric.Token
 */
module ninja.javahacker.reifiedgeneric {
    requires transitive static lombok;
    requires transitive static com.github.spotbugs.annotations;
    exports ninja.javahacker.reifiedgeneric;
}