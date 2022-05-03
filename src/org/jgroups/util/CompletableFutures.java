package org.jgroups.util;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Utility class with {@link CompletableFuture} and {@link CompletionStage} useful methods.
 *
 * @author Pedro Ruivo
 * @since 5.2
 */
public enum CompletableFutures {
    INSTANCE;

    private static final CompletableFuture<?> NULL = CompletableFuture.completedFuture(null);
    private static final Consumer<?> VOID_CONSUMER = o -> {
    };
    private static final Function<?, ?> NULL_FUNCTION = o -> null;

    /**
     * Same as {@link #join(CompletableFuture)} but it receives a {@link CompletionStage} as parameter.
     *
     * @see #join(CompletableFuture)
     */
    public static <T> T join(CompletionStage<T> cs) {
        return join(cs.toCompletableFuture());
    }

    /**
     * Waits for the {@link CompletableFuture} to complete.
     * <p>
     * Any non {@link RuntimeException} thrown is converted to a {@link RuntimeException}.
     *
     * @param cf  The {@link CompletableFuture}
     * @param <T> The value type.
     * @return The value of the completed {@link CompletableFuture}.
     */
    public static <T> T join(CompletableFuture<T> cf) {
        try {
            return cf.join();
        } catch (CompletionException e) {
            if (e.getCause() instanceof RuntimeException) {
                throw (RuntimeException) e.getCause();
            }
            throw new RuntimeException(e.getCause());
        }
    }

    /**
     * A {@code null} completed {@link CompletableFuture}.
     *
     * @param <T> The value type.
     * @return The {@link  CompletableFuture}.
     */
    public static <T> CompletableFuture<T> completedNull() {
        //noinspection unchecked
        return (CompletableFuture<T>) NULL;
    }

    /**
     * Consumes any value and return a {@link Void}.
     *
     * @param <T> The value type.
     * @return The {@link  Consumer}.
     */
    public static <T> Consumer<T> voidConsumer() {
        //noinspection unchecked
        return (Consumer<T>) VOID_CONSUMER;
    }

    public static <T> CompletionStage<T> completeExceptionally(Throwable throwable) {
        CompletableFuture<T> cf = new CompletableFuture<>();
        cf.completeExceptionally(throwable);
        return cf;
    }

    public static CompletionException wrapAsCompletionException(Throwable throwable) {
        return throwable instanceof CompletionException ?
                (CompletionException) throwable :
                new CompletionException(throwable);
    }

    public static <T> Function<T, Void> toVoidFunction() {
        //noinspection unchecked
        return (Function<T, Void>) NULL_FUNCTION;
    }

}
