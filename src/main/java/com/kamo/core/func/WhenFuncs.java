package com.kamo.core.func;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

 abstract class WhenFuncs {
    public static class WhenFunctionImpl<T,R> implements Function<T,R> {
        private Map<Predicate<T>,Function<T,R>> predicateFMap = new HashMap<>();
        @Override
        public R apply(T t) {
            for (Map.Entry<Predicate<T>, Function<T, R>> entry : predicateFMap.entrySet()) {
                if (entry.getKey().test(t)) {
                    return entry.getValue().apply(t);
                }
            }
            return null;
        }
        public WhenFunctionImpl<T,R> or(Predicate<T> when,Function<T,R>function){
            predicateFMap.put(when,function);
            return this;
        }
    }
    public static class WhenConsumerImpl<T> implements Consumer<T> {
        private Map<Predicate<T>,Consumer<T>> predicateFMap = new HashMap<>();
        public WhenConsumerImpl<T> or(Predicate<T> when,Consumer<T>function){
            predicateFMap.put(when,function);
            return this;
        }

        @Override
        public void accept(T t) {
            for (Map.Entry<Predicate<T>,Consumer<T>> entry : predicateFMap.entrySet()) {
                if (entry.getKey().test(t)) {
                    entry.getValue().accept(t);
                    break;
                }
            }
        }
    }
}
