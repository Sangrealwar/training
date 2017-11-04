package study.multithreading.masterSlave.reusable;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Future;

/**
 * 名称：
 * 功能：对子任务派发算法策略的抽象
 * 其dispatch算法会循环调用TaskDivideStrategy实例的nextChunk获取各个子任务，冰将子任务派发给各个Slave实例
 * 条件：
 * Created by wq on 2017/9/17.
 *
 * @param <T> 子任务类型
 * @param <V> 子任务处理结果类型
 */
public interface SubTaskDispatchStrategy<T, V> {

    /**
     * 根据指定的原始任务分解策略，将分解的来的各个子任务派发给一组Slave参与者实例
     *
     * @param slaves             可接受子任务的一组Slave参与者实例
     * @param taskDivideStrategy 原始任务分解策略
     * @return 遍历该iterator 可得到用于获取子任务处理结果的Promise
     * @throws InterruptedException
     */
    Iterator<Future<V>> dispatch(Set<? extends SlaveSpec<T, V>> slaves,
                                 TaskDivideStrategy<T> taskDivideStrategy) throws InterruptedException;
}
