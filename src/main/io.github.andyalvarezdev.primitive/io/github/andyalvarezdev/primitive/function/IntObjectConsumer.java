package io.github.andyalvarezdev.primitive.function;

/**
 * Represents an operation that accepts a  {@code int, Object}-valued arguments and
 * returns no result.  This is the primitive type specialization of
 * {@link java.util.function.BiConsumer} for {@code int, Object}.
 *
 * @see java.util.function.BiConsumer
 */
@FunctionalInterface
public interface IntObjectConsumer<U> {

    void accept(int t, U u);
}
