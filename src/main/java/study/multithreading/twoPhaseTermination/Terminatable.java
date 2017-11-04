package study.multithreading.twoPhaseTermination;

/**
 * 名称：
 * 功能：可停止线程的抽象，提供一个停止线程的方法
 * 条件：
 * Created by wq on 2017/8/6.
 */
public interface Terminatable {
    /**
     * 停止线程
     */
    void terminate();
}
