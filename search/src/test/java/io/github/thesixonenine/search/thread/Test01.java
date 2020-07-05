package io.github.thesixonenine.search.thread;

import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Simple
 * @version 1.0
 * @date 2020/07/04 21:44
 * @since 1.0
 */
public class Test01 {
    public static ExecutorService threadPool;
    public static void init(){
        // threadPool = Executors.newFixedThreadPool(10);
        // threadPool = new ThreadPoolExecutor("", "", "", "", "", "", "");
        // 以下是所有参数
        // int corePoolSize,
        // the number of threads to keep in the pool, even if they are idle, unless {@code allowCoreThreadTimeOut} is set
        // 核心线程数, 一直idle的(除非设置了allowCoreThreadTimeOut), new Thread() 一旦有Runnable提交就可以执行

        // int maximumPoolSize,
        // the maximum number of threads to allow in the pool
        // 最大线程数

        // long keepAliveTime,
        // when the number of threads is greater than the core, this is the maximum time that excess idle threads will wait for new tasks before terminating.
        // 存活时间, 指的是maximumPoolSize-corePoolSize这部分线程

        // TimeUnit unit,
        // the time unit for the keepAliveTime argument
        // 存活时间单位

        // BlockingQueue<Runnable> workQueue,
        // the queue to use for holding tasks before they are executed.  This queue will hold only the Runnable tasks submitted by the execute method.
        // 阻塞队列, 如果提交的任务很多, 就会将多的任务放到队列中, 只要有线程空闲 就会从队列中取任务来执行

        // ThreadFactory threadFactory,
        // the factory to use when the executor creates a new thread
        // 线程的创建工厂

        // RejectedExecutionHandler handler
        // the handler to use when execution is blocked because the thread bounds and queue capacities are reached
        // 队列满了后的执行策略(即拒绝的执行器, 可以有多种拒绝执行的方式), 如果workQueue队列满了, 按照指定的拒绝策略来拒绝任务

        // 运行流程
        // 1. 线程池创建, 准备好corePoolSize数量的核心线程, 准备接受任务
        // 2. 新的任务进来, 使用core准备好的空闲线程执行
        //    2.1. core满了, 新进来的任务将放入阻塞队列中, 如果core中的线程有空闲了,则去队列中取任务执行
        //    2.2. 阻塞队列满了, 新进来的任务就直接新建线程执行, 最大的线程数不超过maximumPoolSize
        //    2.3. maximumPoolSize大于corePoolSize的线程执行完后, 等待超过keepAliveTime的时间后自动销毁, 最终保持到corePoolSize大小
        //    2.4. maximumPoolSize满了, 新进来的任务就使用RejectedExecutionHandler的拒绝策略来拒绝任务
        // 3. 所有的线程都是由指定的ThreadFactory来进行创建的

        // 一个线程池 core=7, max=20, queue=50  100个并发进来:
        // 7个会立即执行, 50个进入队列, 再新开13个线程执行, 剩下的30个使用拒绝策略来拒绝
        threadPool = new ThreadPoolExecutor(5, 20, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<>(5), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
    }
    public static void main(String[] args) {
        System.out.println("main_start");

        // new Thread01().start();

        // new Thread(new Runnable01()).start();

        // FutureTask<Integer> futureTask = new FutureTask<>(new Callable01());
        // new Thread(futureTask).start();
        // try {
        //     System.out.println(futureTask.get());// get()会阻塞等待
        // } catch (InterruptedException | ExecutionException ignored) {
        // }

        // 所有的异步任务都应该交给线程池执行
        // 线程池需要在系统启动时初始化
        init();
        // submit可以获得返回值
        // threadPool.submit(Runnable)
        // execute不能获得返回值
        // threadPool.execute(Runnable);

        int COUNT_BITS = Integer.SIZE - 3;
        int RUNNING    = -1 << COUNT_BITS;
        // -536870912
        System.out.println(RUNNING);

        AtomicInteger ctl = new AtomicInteger(ctlOf(RUNNING, 0));
        // -536870912
        System.out.println("main_end");
    }
    public static int ctlOf(int rs, int wc) { return rs | wc; }
    public static class Thread01 extends Thread {
        @Override
        public void run() {
            try {
                Thread.sleep(200);
            } catch (InterruptedException ignored) {
            }
            System.out.println(Thread.currentThread().getId() + " is running....");
        }
    }

    public static class Runnable01 implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(200);
            } catch (InterruptedException ignored) {
            }
            System.out.println(Thread.currentThread().getId() + " is running....");
        }
    }

    public static class Callable01 implements Callable<Integer>{

        @Override
        public Integer call() throws Exception {
            try {
                Thread.sleep(1200);
            } catch (InterruptedException ignored) {
            }
            System.out.println(Thread.currentThread().getId() + " is running....");
            return new Random().nextInt();
        }
    }
}
