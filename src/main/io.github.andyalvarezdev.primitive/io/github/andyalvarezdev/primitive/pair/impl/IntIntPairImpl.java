package io.github.andyalvarezdev.primitive.pair.impl;

import io.github.andyalvarezdev.primitive.pair.abstracts.AbstractIntIntPair;

public class IntIntPairImpl extends AbstractIntIntPair {
    public IntIntPairImpl(int key, int value) {
        super(key, value);
    }

    public int setValue(int value) {
        int old = this._value;
        this._value = value;
        return old;
    }
}
