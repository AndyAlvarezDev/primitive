package io.github.andyalvarezdev.primitive.function;

import java.util.function.BiFunction;

/**
 * Represents a function that accepts two arguments and produces a double-valued
 * result.  This is the {@code double}-producing primitive specialization for
 * {@link BiFunction}.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #applyAsDouble(int, Object)}.
 *
 * @param <U> the type of the second argument to the function
 *
 * @see BiFunction
 */
@FunctionalInterface
public interface ToDoubleIntBiFunction<U> {

    /**
     * Applies this function to the given arguments.
     *
     * @param t the first function argument
     * @param u the second function argument
     * @return the function result
     */
    double applyAsDouble(int t, U u);
}