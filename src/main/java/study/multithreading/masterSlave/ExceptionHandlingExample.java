package study.multithreading.masterSlave;

import study.multithreading.Debug;
import study.multithreading.masterSlave.reusable.RetryInfo;
import study.multithreading.masterSlave.reusable.SubTaskFailureException;

import java.math.BigInteger;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 * 名称：
 * 功能：
 * 条件：
 * Created by wq on 2017/9/17.
 */
public class ExceptionHandlingExample {
    public void handle(ExecutionException e, Set<BigInteger> result) {
        Throwable cause = e.getCause();
        if (SubTaskFailureException.class.isInstance(cause)) {

            @SuppressWarnings("rawtypes")
            RetryInfo retryInfo = ((SubTaskFailureException) cause).retryInfo;

            Object subTask = retryInfo.subTask;
            Debug.info("retrying subtask:" + subTask);

            @SuppressWarnings("unchecked")
            Callable<Set<BigInteger>> redoCmd = retryInfo.redoCommand;
            try {
                result.addAll(redoCmd.call());
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}
