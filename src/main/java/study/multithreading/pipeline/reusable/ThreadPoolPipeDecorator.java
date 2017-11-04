package study.multithreading.pipeline.reusable;

import java.util.concurrent.*;

/**
 * 名称：基于线程池的Pipe实现类，该类主要实现用线程池中的工作者线程去执行各个输入元素的处理
 * 功能：
 * 条件：
 * Created by wq on 2017/10/7.
 */
public class ThreadPoolPipeDecorator<IN, OUT> implements Pipe<IN, OUT> {
    private final Pipe<IN, OUT> delegate;
    private final ExecutorService executorService;

    private final TerminationToken terminationToken;
    private final CountDownLatch stageProcessDoneLatch = new CountDownLatch(1);

    public ThreadPoolPipeDecorator(Pipe<IN, OUT> delegate, ExecutorService executorService) {
        this.delegate = delegate;
        this.executorService = executorService;
        this.terminationToken = TerminationToken.newInstance(executorService);
    }

    public void init(PipeContext pipeCtx) {
        delegate.init(pipeCtx);
    }

    public void process(final IN input) throws InterruptedException {
        Runnable task = new Runnable() {
            public void run() {
                int remainingReservations = -1;
                try {
                    delegate.process(input);
                } catch (InterruptedException e) {
                } finally {
                    remainingReservations = terminationToken.reservations.decrementAndGet();
                }

                if (terminationToken.isToShutdown() && 0 == remainingReservations) {
                    stageProcessDoneLatch.countDown();
                }
            }
        };

        executorService.submit(task);

        terminationToken.reservations.incrementAndGet();
    }

    public void shutdown(long timeout, TimeUnit unit) {
    }

    public void setNextPipe(Pipe<?, ?> nextPipe) {
        delegate.setNextPipe(nextPipe);
    }

    /**
     * 线程停止对标志
     * 每个ExecutorService实例对那个唯一的一个TerminationToken实例
     */
    private static class TerminationToken
            extends study.multithreading.twoPhaseTermination.TerminationToken {
        private final static ConcurrentMap<ExecutorService, TerminationToken>
                INSTANCES_MAP = new ConcurrentHashMap<ExecutorService, TerminationToken>();

        //私有化构造器
        private TerminationToken() {
        }

        void setIsToShutdown() {
            this.toShutdown = true;
        }

        static TerminationToken newInstance(ExecutorService executorService) {
            TerminationToken token = INSTANCES_MAP.get(executorService);
            if (null == token) {
                token = new TerminationToken();
                TerminationToken existingToken = INSTANCES_MAP.putIfAbsent(executorService, token);
                if (null != existingToken) {
                    token = existingToken;
                }
            }
            return token;
        }
    }

}
