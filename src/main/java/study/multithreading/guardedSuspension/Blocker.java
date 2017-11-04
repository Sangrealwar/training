package study.multithreading.guardedSuspension;

import java.util.concurrent.Callable;

/**
 * 名称：负责对执行guardedMethod方法的线程进行挂起和唤醒，并执行ConcreateGuardedAction所实现的目标操作
 * 功能：
 * 条件：
 * Created by wq on 2017/8/5.
 */
public interface Blocker {

    /**
     * 在保护条件成立时执行目标动作，否则阻塞当前县城，直到保护条件成立
     *
     * @param guardedAction 目标动作
     * @param <V>
     * @return
     * @throws Exception
     */
    <V> V callWIthGuard(GuardedAction<V> guardedAction) throws Exception;

    /**
     * 执行stateOpeartion 所指定的操作后，决定是否唤醒本Blocker所暂挂线程中的一个线程
     *
     * @param stateOperation 更改状态的操作，call的返回值为true时，才唤醒暂挂线程
     * @throws Exception
     */
    void signalAfter(Callable<Boolean> stateOperation) throws Exception;

    void signal() throws Exception;

    /**
     * 执行指定操作后，是否唤醒本Blocker暂挂的所有线程
     *
     * @param stateOperation
     * @throws Exception
     */
    void broadcastAfter(Callable<Boolean> stateOperation) throws Exception;

}
