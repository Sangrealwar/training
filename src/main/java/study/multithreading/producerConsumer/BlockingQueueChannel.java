package study.multithreading.producerConsumer;

import java.util.concurrent.BlockingQueue;

/**
 * 名称：
 * 功能：基于阻塞队列的通道实现，通道之一
 * 当消费者处理能力低于生产者的处理能力是，有界阻塞队列会逐渐挤压到队列满，此时生产者线程会被阻塞
 * 直到相应消费者消费了队列中的产品，使队列非满
 * 条件：
 * Created by wq on 2017/8/6.
 */
public class BlockingQueueChannel<P> implements Channel<P> {

    private final BlockingQueue<P> queue;

    public BlockingQueueChannel(BlockingQueue<P> queue) {
        this.queue = queue;
    }

    public P take() throws InterruptedException {
        return queue.take();
    }

    public void put(P product) throws InterruptedException {
        queue.put(product);
    }
}
