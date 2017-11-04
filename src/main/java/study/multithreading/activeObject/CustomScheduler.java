package study.multithreading.activeObject;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 名称：
 * 功能：
 * 条件：
 * Created by wq on 2017/9/4.
 */
public class CustomScheduler implements Runnable {
    private LinkedBlockingQueue<Runnable> activationQueue = new LinkedBlockingQueue<Runnable>();


    @Override
    public void run() {
        dispatch();
    }

    public <T> Future<T> enqueue(Callable<T> methodRequest) {
        final FutureTask<T> task = new FutureTask<T>(methodRequest) {

            @Override
            public void run() {
                try {
                    super.run();
                } catch (Throwable t) {
                    this.setException(t);
                }
            }
        };


        try {
            activationQueue.put(task);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return task;
    }

    public void dispatch() {
        while (true) {
            Runnable methodRequest;

            try {
                methodRequest = activationQueue.take();
                //防止个别任务的执行失败导致线程终止的代码在run方法中
                methodRequest.run();
            } catch (InterruptedException e) {
                //处理异常
            }
        }
    }
}
