package io.github.andyalvarezdev.primitive.function;

/**
 * Represents an operation that accepts a  {@code int, double}-valued arguments and
 * returns no result.  This is the primitive type specialization of
 * {@link java.util.function.BiConsumer} for {@code int, double}.
 *
 * @see java.util.function.BiConsumer
 */
@FunctionalInterface
public interface IntDoubleConsumer {

    void accept(int t, double u);
}
