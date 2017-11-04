package study.multithreading.pipeline.reusable;

import java.util.concurrent.TimeUnit;

/**
 * 名称：Pipe的抽象实现类
 * 功能：该类调用其子类实现的doProcess方法对输入元素进行处理，并将相应的输出作为下一个Pipe实例的输入
 * 条件：
 * Created by wq on 2017/10/7.
 *
 * @param <IN>输入类型
 * @param <OUT>输出类型
 */
public abstract class AbstractPipe<IN, OUT> implements Pipe<IN, OUT> {
    protected volatile Pipe<?, ?> nextPipe = null;
    protected volatile PipeContext pipeCtx;

    public void init(PipeContext pipeCtx) {
        this.pipeCtx = pipeCtx;
    }

    public void setNextPipe(Pipe<?, ?> nextPipe) {
        this.nextPipe = nextPipe;
    }

    public void shutdown(long timeout, TimeUnit unit) {
        //停止了
    }

    /**
     * 留给子类实现。用于子类实现其任务处理逻辑
     *
     * @param input 输入元素（任务）
     * @return 任务处理结果
     * @throws PipeException
     */
    protected abstract OUT doProcess(IN input) throws PipeException;

    public void process(IN input) throws InterruptedException {

        try {
            OUT out = doProcess(input);
            if (null != nextPipe) {
                if (null != out) {
                    ((Pipe<OUT, ?>) nextPipe).process(out);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (PipeException pipeException) {
            pipeCtx.handleError(pipeException);
        }
    }
}
