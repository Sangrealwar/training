package study.multithreading.threadPool;

import java.util.concurrent.*;

/**
 * 名称：
 * 功能：线程池中提交的任务又向线程池中提交了任务，会造成依赖死锁，建议提交给线程池的任务相互独立
 *       当任务向同一线程提交taskB，因为设置了线程池大小，会导致线程池已满，因为设置了SynchronousQueue，会导致
 *       taskB插入工作队列失败，taskB就因为线程池饱和而被拒绝，然后被taskA的工作线程补偿执行，两个任务在同一个线程中
 * 条件：
 * Created by wq on 2017/9/9.
 */
public class ThreadPoolDeadLockAvoidance {
    private final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
            1,
            //有限值
            1,
            60,
            TimeUnit.SECONDS,
            //工作队列为SynchronousQueue
            new SynchronousQueue<Runnable>(),
            //线程池饱和处理策略，在客户端执行线程任务
            new ThreadPoolExecutor.CallerRunsPolicy());

    public static void main(String[] args) {
        ThreadPoolDeadLockAvoidance me = new ThreadPoolDeadLockAvoidance();
        me.test("保证不死锁");
    }

    public void test(final String message) {
        Runnable taskA = new Runnable() {
            @Override
            public void run() {
                System.out.println("执行任务A。。。。。");
                Runnable taskB = new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("执行任务B："+message);
                    }
                };

                Future<?> result = threadPool.submit(taskB);

                try {
                    result.get();
                } catch (InterruptedException e) {
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                System.out.println("任务A结束");
            }
        };

        threadPool.submit(taskA);
    }
}
