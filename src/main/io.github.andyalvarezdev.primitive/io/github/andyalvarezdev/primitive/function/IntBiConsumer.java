package io.github.andyalvarezdev.primitive.function;

/**
 * Represents an operation that accepts a  {@code int, int}-valued arguments and
 * returns no result.  This is the primitive type specialization of
 * {@link java.util.function.BiConsumer} for {@code int, int'}.
 *
 * @see java.util.function.BiConsumer
 */
@FunctionalInterface
public interface IntBiConsumer {

    void accept(int t, int u);
}
