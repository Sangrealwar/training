package study.multithreading.producerConsumer;

/**
 * 名称：
 * 功能：通道抽象
 * 条件：
 * Created by wq on 2017/8/6.
 */
public interface Channel<P> {
    P take() throws InterruptedException;

    void put(P product) throws InterruptedException;
}
