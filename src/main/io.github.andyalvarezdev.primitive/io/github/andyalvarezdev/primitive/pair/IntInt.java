package io.github.andyalvarezdev.primitive.pair;

import io.github.andyalvarezdev.primitive.pair.absint.key.IntKey;
import io.github.andyalvarezdev.primitive.pair.absint.value.IntValuePair;

public interface IntInt extends IntKey, IntValuePair {
    int getKey();

    int getValue();

    int setValue(int var1);
}
