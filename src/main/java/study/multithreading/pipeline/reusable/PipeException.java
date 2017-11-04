package study.multithreading.pipeline.reusable;

/**
 * 名称：
 * 功能：
 * 条件：
 * Created by wq on 2017/10/7.
 */
public class PipeException extends Exception {

    public final Pipe<?, ?> sourcePipe;

    public final Object input;

    public PipeException(Pipe<?, ?> sourcePipe, Object input, String message) {
        super(message);
        this.sourcePipe = sourcePipe;
        this.input = input;
    }

    public PipeException(Pipe<?, ?> sourcePipe, Object input, String message, Throwable cause) {
        super(message, cause);
        this.sourcePipe = sourcePipe;
        this.input = input;
    }
}
