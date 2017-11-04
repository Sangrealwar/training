package study.multithreading.producerConsumer;

import java.util.concurrent.BlockingDeque;

/**
 * 名称：
 * 功能：
 * 条件：
 * Created by wq on 2017/8/20.
 */
public interface WorkStealingEnabledChannel<P> extends Channel<P> {

    P take(BlockingDeque<P> preferredQueue) throws InterruptedException;
}
