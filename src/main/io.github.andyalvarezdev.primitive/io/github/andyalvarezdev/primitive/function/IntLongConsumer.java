package io.github.andyalvarezdev.primitive.function;

/**
 * Represents an operation that accepts a  {@code int, long}-valued arguments and
 * returns no result.  This is the primitive type specialization of
 * {@link java.util.function.BiConsumer} for {@code int, long}.
 *
 * @see java.util.function.BiConsumer
 */
@FunctionalInterface
public interface IntLongConsumer {

    void accept(int t, long u);
}
