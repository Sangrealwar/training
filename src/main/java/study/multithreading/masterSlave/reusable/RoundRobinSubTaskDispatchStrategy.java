package study.multithreading.masterSlave.reusable;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;

/**
 * 名称：
 * 功能：简单的轮询派发算法策略
 * 条件：
 * Created by wq on 2017/9/17.
 *
 * @param <T> 子任务类型
 * @param <V> 子任务处理结果类型
 */
public class RoundRobinSubTaskDispatchStrategy<T, V> implements SubTaskDispatchStrategy<T, V> {

    @Override
    public Iterator<Future<V>> dispatch(Set<? extends SlaveSpec<T, V>> slaves,
                                        TaskDivideStrategy<T> taskDivideStrategy) throws InterruptedException {
        final List<Future<V>> subResults = new LinkedList<Future<V>>();
        T subTask;
        Object[] arrSlaves = slaves.toArray();
        int i = -1;
        final int slaveCount = arrSlaves.length;
        Future<V> subTaskResultPromise;

        while (null != (subTask = taskDivideStrategy.nextChunk())) {
            //轮询
            i = (i + 1) % slaveCount;
            subTaskResultPromise = ((WorkerThreadSlave<T, V>) arrSlaves[i]).submit(subTask);
            subResults.add(subTaskResultPromise);
        }

        return subResults.iterator();
    }
}
