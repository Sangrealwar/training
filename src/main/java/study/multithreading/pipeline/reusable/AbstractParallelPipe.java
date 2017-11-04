package study.multithreading.pipeline.reusable;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * 名称：并行通道pipe，分发任务给多个pipe
 * 功能：
 * 条件：
 * Created by wq on 2017/10/7.
 */
public abstract class AbstractParallelPipe<IN, OUT, V> extends AbstractPipe<IN, OUT> {

    private final ExecutorService executorService;

    public AbstractParallelPipe(BlockingQueue<IN> queue, ExecutorService executorService) {
        super();
        this.executorService = executorService;
    }


    /**
     * 留给子类实现，用于根据指定的输入元素input构造一组任务
     *
     * @param input 输入元素
     * @return
     * @throws Exception
     */
    protected abstract List<Callable<V>> buildTasks(IN input) throws Exception;

    /**
     * 留给子类实现。对各个子任务的处理结果进行合并，形成相应输入元素的输出结果
     *
     * @param subTaskResults 子任务处理结果列表
     * @return 相应输入元素的处理结果
     */
    protected abstract OUT combineResults(List<Future<V>> subTaskResults) throws Exception;

    protected List<Future<V>> invokeParallel(List<Callable<V>> tasks) throws Exception {
        return executorService.invokeAll(tasks);
    }

    @Override
    protected OUT doProcess(final IN input) throws PipeException {
        OUT out = null;

        try {
            out = combineResults(invokeParallel(buildTasks(input)));
        } catch (Exception e) {
            throw new PipeException(this, input, "任务失败", e);
        }
        return out;
    }
}
