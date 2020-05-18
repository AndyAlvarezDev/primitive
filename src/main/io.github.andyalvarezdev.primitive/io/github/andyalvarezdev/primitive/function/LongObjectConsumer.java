package io.github.andyalvarezdev.primitive.function;

/**
 * Represents an operation that accepts a  {@code long, Object}-valued arguments and
 * returns no result.  This is the primitive type specialization of
 * {@link java.util.function.BiConsumer} for {@code long, Object}.
 *
 * @see java.util.function.BiConsumer
 */
@FunctionalInterface
public interface LongObjectConsumer<U> {

    void accept(long t, U u);
}
