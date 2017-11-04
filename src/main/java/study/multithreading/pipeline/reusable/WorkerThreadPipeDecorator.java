package study.multithreading.pipeline.reusable;

import study.multithreading.twoPhaseTermination.AbstractTerminatableThread;
import study.multithreading.twoPhaseTermination.TerminationToken;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

/**
 * 名称：基于工作者线程的Pipe实现类
 * 功能：提交到该Pipe的任务由指定个数的工作者线程共同处理
 * 条件：
 * Created by wq on 2017/10/7.
 */
public class WorkerThreadPipeDecorator<IN, OUT> implements Pipe<IN, OUT> {

    protected final BlockingQueue<IN> workQueue;
    private final Set<AbstractTerminatableThread> workThreads = new HashSet<AbstractTerminatableThread>();
    private final TerminationToken terminationToken = new TerminationToken();

    private final Pipe<IN, OUT> delegate;

    public WorkerThreadPipeDecorator(Pipe<IN, OUT> delegate, int workerCount) {
        this(new SynchronousQueue<IN>(), delegate, workerCount);
    }

    public WorkerThreadPipeDecorator(BlockingQueue<IN> workQueue, Pipe<IN, OUT> delegate, int workerCount) {
        if (workerCount <= 0) {
            throw new IllegalArgumentException("工作线程数量必须为正数");
        }

        this.workQueue = workQueue;
        this.delegate = delegate;
        for (int i = 0; i < workerCount; i++) {
            workThreads.add(new AbstractTerminatableThread(terminationToken) {
                @Override
                protected void doRun() throws Exception {
                    try {
                        dispatch();
                    } finally {
                        terminationToken.reservations.decrementAndGet();
                    }
                }
            });
        }
    }

    protected void dispatch() throws InterruptedException {
        IN input = workQueue.take();
        delegate.process(input);
    }

    public void init(PipeContext pipeCtx) {
        delegate.init(pipeCtx);
        for (AbstractTerminatableThread thread : workThreads) {
            thread.start();
        }
    }

    public void process(IN input) throws InterruptedException {
        workQueue.put(input);
        terminationToken.reservations.incrementAndGet();
    }

    public void shutdown(long timeout, TimeUnit unit) {
        for (AbstractTerminatableThread thread : workThreads) {
            thread.terminate();

            try {
                thread.join(TimeUnit.MILLISECONDS.convert(timeout, unit));
            } catch (InterruptedException e) {
            }
        }
        delegate.shutdown(timeout, unit);
    }

    public void setNextPipe(Pipe<?, ?> nextPipe) {
        delegate.setNextPipe(nextPipe);
    }
}
