package study.multithreading.pipeline.reusable;

import java.util.concurrent.TimeUnit;

/**
 * 名称：管道
 * 功能：对处理截断的抽象
 * 条件：
 * Created by wq on 2017/10/7.
 *
 * @param <IN>  输入类型
 * @param <OUT> 输出类型
 */
public interface Pipe<IN, OUT> {

    /**
     * 设置当前Pipe实例的下一个实例
     *
     * @param nextPipe 下一个pipe实例
     */
    void setNextPipe(Pipe<?, ?> nextPipe);

    /**
     * 初始化当前Pipe实例对外提供的服务
     *
     * @param pipeCtx
     */
    void init(PipeContext pipeCtx);

    /**
     * 停止当前Pipe实例对外提供的服务
     *
     * @param timeout
     * @param unit
     */
    void shutdown(long timeout, TimeUnit unit);

    /**
     * 对输入元素进行处理，并将处理结果作为下一个Pipe实例的输出
     *
     * @param input
     * @throws InterruptedException
     */
    void process(IN input) throws InterruptedException;
}
