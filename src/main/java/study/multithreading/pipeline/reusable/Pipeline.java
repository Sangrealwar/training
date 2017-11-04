package study.multithreading.pipeline.reusable;

/**
 * 名称：
 * 功能：对集合Pipe的抽象，一个Pipeline实例可以包含多个Pipe实例
 * 条件：
 * Created by wq on 2017/10/7.
 */
public interface Pipeline<IN, OUT> extends Pipe<IN, OUT> {

    /**
     * 往该PipeLine实例中添加一个Pipe实例
     *
     * @param pipe
     */
    void addPipe(Pipe<?, ?> pipe);
}
