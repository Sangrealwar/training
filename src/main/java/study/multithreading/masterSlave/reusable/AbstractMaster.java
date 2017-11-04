package study.multithreading.masterSlave.reusable;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Future;

/**
 * 名称：
 * 功能： Master参与者的通用实现
 *
 * 1.调用其子类的createSlaves方法创建Slave实例
 * 2.调用newTaskDivideStrategy创建原始任务分解算法对象
 * 3.调用newSubTaskDispatchStrategy创建子任务派发算法，默认是轮询
 * 4.调用子任务派发算法策略对象分发各个任务
 * 5.调用combineResult合并方法
 * 条件：
 * Created by wq on 2017/9/17.
 *
 * @param <T> 子任务类型
 * @param <V> 子任务处理结果类型
 * @param <R> 原始任务处理结果类型
 */
public abstract class AbstractMaster<T, V, R> {
    protected volatile Set<? extends SlaveSpec<T, V>> slaves;

    //子任务派发策略算法
    private volatile SubTaskDispatchStrategy<T, V> dispatchStrategy;

    public AbstractMaster() {
    }

    protected void init() {
        slaves = createSlaves();

        dispatchStrategy = newSubTaskDispatchStrategy();
        for (SlaveSpec<T, V> slave : slaves) {
            slave.init();
        }
    }

    /**
     * 对子类暴露的服务方法，该类的子类需要定义一个比该命名更为具体的方法
     * 由命名具体的服务方法调用该方法
     * 该方法使用了Template模式和Strategy模式
     *
     * @param params 客户端代码传递的参数列表
     * @return
     * @throws Exception
     */
    protected R service(Object... params) throws Exception {
        final TaskDivideStrategy<T> taskDivideStrategy = newTaskDivideStrategy(params);

        /**
         * 对原始任务进行分解，并将分解的来的子任务派发给Slave参与者实例。这里使用Strategy模式
         * 原始任务分解和子任务派发这两个具体计算是通过调用需要的算法策略实现的
         */
        Iterator<Future<V>> subResult = dispatchStrategy.dispatch(slaves, taskDivideStrategy);

        //等待Slave实例处理结束
        for (SlaveSpec<T, V> slave : slaves) {
            slave.shutdown();
        }

        //合并子任务处理结果
        R result = combineResults(subResult);
        return result;
    }

    /**
     * 留给子类实现，用于创建原始任务分解算法策略
     *
     * @param params 客户端传递的参数
     * @return
     */
    protected abstract TaskDivideStrategy<T> newTaskDivideStrategy(Object... params);

    /**
     * 用于创建子任务派发算法策略，默认使用轮询派发算法
     *
     * @return 子任务派发算法策略实例
     */
    protected SubTaskDispatchStrategy<T, V> newSubTaskDispatchStrategy() {
        return new RoundRobinSubTaskDispatchStrategy<T, V>();
    }

    /**
     * 留给子类实现，用于创建Slave参与者实例
     *
     * @return
     */
    protected abstract Set<? extends SlaveSpec<T, V>> createSlaves();

    /**
     * 留给子类实现，用于合并子任务处理结果
     *
     * @param subResults 各个任务处理结果
     * @return 原始任务处理结果
     */
    protected abstract R combineResults(Iterator<Future<V>> subResults);
}
