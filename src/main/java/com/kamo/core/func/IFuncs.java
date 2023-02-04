package com.kamo.core.func;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface IFuncs {

    static <T,R> WhenFuncs.WhenFunctionImpl<T,R> when(Predicate<T> when, Function<T,R> function){
        return new WhenFuncs.WhenFunctionImpl<T,R>().or(when,function);
    }
    static <T> WhenFuncs.WhenConsumerImpl<T> when(Predicate<T> when, Consumer<T> consumer){
        return new WhenFuncs.WhenConsumerImpl<T>().or(when,consumer);
    }


    static <T,R> Function<T, Stream<R>> flatArray(Function<T,R[] >arrayGetter){
        return new FlatArrayFunctionImpl<T,R>(arrayGetter);
    }
}
