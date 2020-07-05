package io.github.thesixonenine.search.thread;

import java.util.Objects;
import java.util.concurrent.*;

/**
 * @author Simple
 * @version 1.0
 * @date 2020/07/05 13:56
 * @since 1.0
 */
public class Test02 {
    public static ExecutorService threadPool = new ThreadPoolExecutor(5, 20, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<>(5), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());

    public static void main(String[] args) throws Exception {
        // 异步任务 CompletableFuture
        // 无返回值
        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(200);
            } catch (InterruptedException ignored) {
            }
            System.out.println(Thread.currentThread().getId() + " is running....");
        }, threadPool);
        // 有返回值
        CompletableFuture<Long> longCompletableFuture = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(200);
            } catch (InterruptedException ignored) {
            }
            long id = Thread.currentThread().getId();
            System.out.println(id + " is running....");
            return Thread.currentThread().getId();
        }, threadPool);
        // Long aLong = longCompletableFuture.get();
        longCompletableFuture.whenComplete((result, e) -> {
            // whenComplete 接收结果和异常(不能返回数据)
        }).exceptionally(e -> {
            // 接收异常, 返回默认值
            return 0L;
        });

        // 不仅可以接收结果和异常, 还能返回数据
        longCompletableFuture.handle((result, e)->{
            if (Objects.nonNull(result)) {
                // 正常运行, 无异常
                return result;
            }
            if (Objects.nonNull(e)) {
                // 有异常, 处理异常
                return 0;
            }
            // 默认返回null
            return null;
        });


        // 串行化

        // 不需要上一步的结果(无返回)
        longCompletableFuture.thenRun(()->{});
        longCompletableFuture.thenRunAsync(()->{});
        // 可以对上一步的结果进行处理(无返回)
        longCompletableFuture.thenAccept((result)->{});
        longCompletableFuture.thenAcceptAsync((result)->{});
        // 可以对上一步的结果进行处理(有返回)
        longCompletableFuture.thenApply((result)->{return null;});
        longCompletableFuture.thenApplyAsync((result)->{return null;});


        // 两个任务组合(都要完成), 任务1.method(任务2, 任务1和2都完成后执行的任务3)

        // 不需要两个前置任务的结果(无返回)
        longCompletableFuture.runAfterBoth(new CompletableFuture<>(),()->{});
        longCompletableFuture.runAfterBothAsync(new CompletableFuture<>(),()->{});

        // 可以拿到两个前置任务的结果(无返回)
        longCompletableFuture.thenAcceptBoth(new CompletableFuture<>(),(a, b)->{});
        longCompletableFuture.thenAcceptBothAsync(new CompletableFuture<>(),(a, b)->{});

        // 可以拿到两个前置任务的结果(有返回)
        longCompletableFuture.thenCombine(new CompletableFuture<>(),(a, b)->{return null;});
        longCompletableFuture.thenCombineAsync(new CompletableFuture<>(),(a, b)->{return null;});


        // 两个任务组合(一个完成即可), 任务1.method(任务2, 任务1或2完成后执行的任务3)

        // 不需要两个前置任务的结果(无返回)
        longCompletableFuture.runAfterEither(new CompletableFuture<>(),()->{});
        longCompletableFuture.runAfterEitherAsync(new CompletableFuture<>(),()->{});

        // 可以拿到两个前置任务的结果(无返回)
        longCompletableFuture.acceptEither(new CompletableFuture<>(),(result)->{});
        longCompletableFuture.acceptEitherAsync(new CompletableFuture<>(),(result)->{});

        // 可以拿到两个前置任务的结果(有返回)
        longCompletableFuture.applyToEither(new CompletableFuture<>(),(result)->{return null;});
        longCompletableFuture.applyToEitherAsync(new CompletableFuture<>(),(result)->{return null;});


        // 多任务组合

        // 等所有任务都执行完毕
        CompletableFuture.allOf(new CompletableFuture<>(), new CompletableFuture<>(), new CompletableFuture<>());
        // 等所有任务中任意一个执行完毕
        CompletableFuture.anyOf(new CompletableFuture<>(), new CompletableFuture<>(), new CompletableFuture<>());
    }
}
