package study.multithreading.masterSlave.reusable;

/**
 * 名称：
 * 功能：表示子任务处理失败的异常
 * 条件：
 * Created by wq on 2017/9/17.
 */
public class SubTaskFailureException extends Exception {

    public final RetryInfo retryInfo;

    public SubTaskFailureException(RetryInfo retryInfo, Exception cause) {
        super(cause);
        this.retryInfo = retryInfo;
    }
}
