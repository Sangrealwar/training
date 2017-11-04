package study.multithreading.masterSlave.reusable;

import java.util.concurrent.Callable;

/**
 * 名称：
 * 功能：对失败的子任务进行重试所需信息
 * 条件：
 * Created by wq on 2017/9/17.
 *
 * @param <T> 子任务类型
 * @param <V> 子任务处理结果类型
 */
public class RetryInfo<T, V> {

    public final T subTask;
    public final Callable<V> redoCommand;

    public RetryInfo(T subTask, Callable<V> redoCommand) {
        this.subTask = subTask;
        this.redoCommand = redoCommand;
    }
}
