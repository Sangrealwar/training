package study.multithreading.guardedSuspension;

import java.util.concurrent.Callable;

/**
 * 名称：抽象的目标动作，并关联了保护条件
 * 功能：
 * 条件：
 * Created by wq on 2017/8/5.
 */
public abstract class GuardedAction<V> implements Callable<V> {

    protected final Predicate guard;

    public Predicate getGuard() {
        return guard;
    }

    protected GuardedAction(Predicate guard) {
        this.guard = guard;
    }
}
