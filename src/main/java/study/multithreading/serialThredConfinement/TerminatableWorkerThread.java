package study.multithreading.serialThredConfinement;

import study.multithreading.twoPhaseTermination.AbstractTerminatableThread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * 名称：
 * 功能：WorkThread参与者
 * 条件：
 * Created by wq on 2017/9/16.
 *
 * @param <T> 向WorkThread提交的任务的类型
 * @param <V> 任务的处理结果
 */

public class TerminatableWorkerThread<T, V> extends AbstractTerminatableThread {
    private final BlockingQueue<Runnable> workQueue;

    //负责真正执行任务的对象
    private final TaskProcessor<T, V> taskProcessor;

    public TerminatableWorkerThread(BlockingQueue<Runnable> workQueue, TaskProcessor<T, V> taskProcessor) {
        this.workQueue = workQueue;
        this.taskProcessor = taskProcessor;
    }

    /**
     * 接受并行任务，并串行话，放入工作队列中
     *
     * @param task 任务
     * @return 借助Promise获得任务处理结果
     * @throws InterruptedException
     */
    public Future<V> submit(final T task) throws InterruptedException {
        Callable<V> callable = new Callable<V>() {
            @Override
            public V call() throws Exception {
                return taskProcessor.doProcess(task);
            }
        };

        FutureTask<V> ft = new FutureTask<V>(callable);
        workQueue.put(ft);

        terminationToken.reservations.incrementAndGet();
        return ft;
    }

    /**
     * 执行任务的处理逻辑
     *
     * @throws Exception
     */
    @Override
    protected void doRun() throws Exception {
        Runnable ft = workQueue.take();

        try {
            ft.run();
        } finally {
            terminationToken.reservations.decrementAndGet();
        }
    }
}
