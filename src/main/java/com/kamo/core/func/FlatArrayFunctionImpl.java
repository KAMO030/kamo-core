package com.kamo.core.func;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Stream;

public class FlatArrayFunctionImpl<T,R> implements Function<T, Stream<R>> {

    private final Function<T,R[]> arrayGetter;

    public FlatArrayFunctionImpl(Function<T, R[]> arrayGetter) {
        this.arrayGetter = arrayGetter;
    }

    @Override
    public Stream<R> apply(T t) {
        return Arrays.stream(arrayGetter.apply(t));
    }
}
