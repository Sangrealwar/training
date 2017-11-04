package study.multithreading.masterSlave.reusable;

/**
 * 名称：
 * 功能：对原始任务分解算法策略的抽象
 * nextChunk会返回子任务，直到没有子任务
 * 条件：
 * Created by wq on 2017/9/17.
 *
 * @param <T> 子任务类型
 */
public interface TaskDivideStrategy<T> {

    /**
     * 返回下一个任务，若返回值为null，则表示无后续任务
     *
     * @return 下一个任务
     */
    T nextChunk();
}
