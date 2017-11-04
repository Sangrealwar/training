package study.multithreading.threadPool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/*
* 线程池大小视系统资源和任务特性决定
* 1.系统资源主要是：CPU个数和jvm内存大小
* 2.任务特性主要是：CPU密集型、IO密集型、混合型。CPU密集型线程池大小设置为N(cpu)+1，主要考虑CPU密集型任务
* 其执行线程会在某一时刻，如缺页中断而等待；IO密集型可设置的大一些，应为IO等待不会消耗CPU资源，一般设置为N(cpu)*2，
* 一般考虑到IO密集型会引起大量的CPU上下文切换，合兴线程池大小可设置为1
* ！！！归根结底，要看CPU资源的利用程度
*
* 在线程池中，如果出现1/0  并不会抛出异常，需要在工作者线程的执行过程中捕获异常，以便跟踪问题，线程池必须手动调用shutdown
*
* */

/**
 * 名称：
 * 功能：设置IO密集型任务的线程池大小实例
 * 条件：
 * Created by wq on 2017/9/9.
 */
public class ThreadPoolSize4IOIntensiveTask {
    public static void main(String[] args) {
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
                //核心线程大小
                1,
                //最大线程数
                Runtime.getRuntime().availableProcessors() * 2,
                60,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(200)
        );

        threadPool.submit(new IOIntensiveTask());
        threadPool.shutdown();
    }

    private static class IOIntensiveTask implements Runnable {
        @Override
        public void run() {
            //IO操作
            try {
                System.out.println(1 / 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
