package com.kamo.core.util;

import com.kamo.core.exception.BuilderException;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Builder<T> {
    private final Supplier<T> constructor;

    private Predicate<T> verifyPredicate = (i) -> Boolean.TRUE;
    private Consumer<T> opsConsumer = (i) -> {};

    private Consumer<Throwable> catchConsumer  = (e)->{throw new BuilderException(e);};

    private Builder(Supplier<T> constructor) {
        this.constructor = constructor;
    }

    public <P> Builder<T> and(BiConsumer<T, P> ops, P param) {
        Consumer<T> consumer = instance -> ops.accept(instance, param);
        opsConsumer = opsConsumer.andThen(consumer);
        return this;
    }

    public Builder<T> and(Consumer<T> ops) {
        opsConsumer = opsConsumer.andThen(ops);
        return this;
    }
    public <P> Builder<T> verify(Predicate<T> vp) {
        verifyPredicate = vp.and(verifyPredicate);
        return this;
    }
    public Builder<T> catchThrow(Consumer<Throwable> catchOps) {
        catchConsumer = catchOps;
        return this;
    }

    public T build() {
        T instance = null;
        try {
            instance = constructor.get();
            opsConsumer.accept(instance);
            if (!verifyPredicate.test(instance)) {
                throw new BuilderException(instance.toString());
            }
        } catch (Throwable e) {
            catchConsumer.accept(e);
        }
        return instance;
    }

    public static <B> Builder<B> create(Supplier<B> constructor) {
        return new Builder<>(constructor);
    }


}
