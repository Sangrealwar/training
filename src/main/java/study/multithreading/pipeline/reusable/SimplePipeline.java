package study.multithreading.pipeline.reusable;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * 名称：
 * 功能：
 * 条件：
 * Created by wq on 2017/10/7.
 */
public class SimplePipeline<T, OUT> extends AbstractPipe<T, OUT> implements Pipeline<T, OUT> {

    private final Queue<Pipe<?, ?>> pipes = new LinkedList<Pipe<?, ?>>();
    private final ExecutorService helperExecutor;

    public SimplePipeline() {
        this(Executors.newSingleThreadExecutor(new ThreadFactory() {
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r, "简易版通道队列-帮助");
                t.setDaemon(true);
                return t;
            }
        }));
    }

    public SimplePipeline(final ExecutorService helperExecutor) {
        super();
        this.helperExecutor = helperExecutor;
    }

    @Override
    public void shutdown(long timeout, TimeUnit unit) {
        Pipe<?, ?> pipe;
        while (null != (pipe = pipes.poll())) {
            pipe.shutdown(timeout, unit);
        }
        helperExecutor.shutdown();
    }

    protected OUT doProcess(T input) throws PipeException {
        return null;
    }

    public void addPipe(Pipe<?, ?> pipe) {
        //Pipe间的关联在init方法中建立
        pipes.add(pipe);
    }

    public <INPUT, OUTPUT> void addAsWorkerThreadBasedPipe(
            Pipe<INPUT, OUTPUT> delegate, int workerCount) {
        addPipe(new WorkerThreadPipeDecorator<INPUT, OUTPUT>(delegate, workerCount));
    }

    public <INPUT, OUTPUT> void addAsThreadPoolBasedPipe(
            Pipe<INPUT, OUTPUT> delegate, ExecutorService executorService) {
        addPipe(new ThreadPoolPipeDecorator<INPUT, OUTPUT>(delegate, executorService));
    }

    @Override
    public void process(T input) throws InterruptedException {

        Pipe<T, ?> firstPipe = (Pipe<T, ?>) pipes.peek();

        firstPipe.process(input);
    }

    public void init(final PipeContext pipeCtx) {
        LinkedList<Pipe<?, ?>> pipesList = (LinkedList<Pipe<?, ?>>) pipes;

        Pipe<?, ?> prevPipe = this;
        for (Pipe<?, ?> pipe : pipesList) {
            prevPipe.setNextPipe(pipe);
            prevPipe = pipe;
        }

        Runnable task = new Runnable() {
            public void run() {
                for (Pipe<?, ?> pipe : pipes) {
                    pipe.init(pipeCtx);
                }
            }
        };

        helperExecutor.submit(task);
    }

    public PipeContext newDefaultPipelineContext() {
        return new PipeContext() {
            public void handleError(final PipeException exp) {
                helperExecutor.submit(new Runnable() {
                    public void run() {
                        exp.printStackTrace();
                    }
                });
            }
        };
    }
}
