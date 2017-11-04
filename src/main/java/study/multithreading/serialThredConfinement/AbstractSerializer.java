package study.multithreading.serialThredConfinement;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;

/**
 * 名称：
 * 功能：Serializer参与者抽象
 * 条件：
 * Created by wq on 2017/9/16.
 */
public abstract class AbstractSerializer<T, V> {

    private final TerminatableWorkerThread<T, V> workerThread;

    public AbstractSerializer(BlockingQueue<Runnable> workQueue, TaskProcessor<T, V> taskProcessor) {
        this.workerThread = new TerminatableWorkerThread<T, V>(workQueue, taskProcessor);
    }

    /**
     * 根据指定参数生成任务实例
     * @param params
     * @return
     */
    protected abstract T makeTask(Object... params);

    /**
     * 对外暴露的服务方法
     * @param params
     * @return
     * @throws InterruptedException
     */
    protected Future<V> service(Object... params) throws InterruptedException {
        T task = makeTask(params);

        Future<V> resultPromise = workerThread.submit(task);
        return resultPromise;
    }

    public void init() {
        workerThread.start();
    }

    public void shutdown() {
        workerThread.terminate();
    }
}
