package study.multithreading.masterSlave.reusable;

import java.util.concurrent.Future;

/**
 * 名称：
 * 功能：Slave参与者的抽象
 * 条件：
 * Created by wq on 2017/9/17.
 *
 * @param <T> 子任务类型
 * @param <V> 子任务处理结果类型
 */
public interface SlaveSpec<T, V> {

    /**
     * 用于Master向其提交一个子任务
     *
     * @param task 子任务
     * @return 可借此获取子任务处理结果的Promise
     * @throws InterruptedException
     */
    Future<V> submit(final T task) throws InterruptedException;

    /**
     * 初始化Slave实例提交的服务
     */
    void init();

    /**
     * 停止Slave实例对外提供的服务
     */
    void shutdown();
}
