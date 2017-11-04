package study.multithreading.guardedSuspension;

import java.util.concurrent.Callable;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 名称：基于java条件变量实现的Block
 * 功能：
 * 条件：
 * Created by wq on 2017/8/5.
 */
public class ConditionVarBlocker implements Blocker {
    private final Lock lock;

    //为了避免不必要的嵌套同步，构造器传入lock实例是的调用Condition实例的相关方法可以指定
    // lock实例，而不是用Condition自己的lock实例
    private final Condition condition;

    public ConditionVarBlocker() {
        this.lock = new ReentrantLock();
        this.condition = lock.newCondition();
    }

    public ConditionVarBlocker(Lock lock, Condition condition) {
        this.lock = lock;
        this.condition = condition;
    }

    public <V> V callWIthGuard(GuardedAction<V> guardedAction) throws Exception {
        //针对受保护对象的变量而言，保护方法的判断条件是读线程，而受保护对象的stateChange方法是写线程
        //为了保证变量的正确性，这里加所了
        lock.lockInterruptibly();

        V result;
        try {
            final Predicate guard = guardedAction.getGuard();
            //为了避免线程过早唤醒，即某Condition实例对应多个条件，满足条件A后，唤醒条件A的暂挂线程
            //但其中一个线程T还有条件B，条件B并不满足，此时唤醒线程T就是过早唤醒
            while (!guard.evalute()) {
                condition.await();
            }
            result = guardedAction.call();
            return result;
        } finally {
            //避免锁泄露，即该线程获取了锁，但永远不会释放
            lock.unlock();
        }
    }

    public void signalAfter(Callable<Boolean> stateOperation) throws Exception {
        lock.lockInterruptibly();
        try {
            if (stateOperation.call()) {
                condition.signal();
            }
        } finally {
            lock.unlock();
        }
    }

    public void signal() throws Exception {
        lock.lockInterruptibly();

        try {
            condition.signal();
        } finally {
            lock.unlock();
        }
    }

    public void broadcastAfter(Callable<Boolean> stateOperation) throws Exception {
        lock.lockInterruptibly();

        try {
            if (stateOperation.call()) {
                condition.signalAll();
            }
        } finally {
            lock.unlock();
        }

    }
}
