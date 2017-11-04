package study.multithreading.halfSync.reusable;


import java.util.concurrent.*;

/**
 * 名称：
 * 功能：
 * 条件：
 * Created by wq on 2017/10/9.
 */
public abstract class AsyncTask<V> {
    //同步层，用于执行异步层提交的任务
    private volatile Executor executor;
    private final static ExecutorService DEFAULT_EXECUTOR;

    static {
        DEFAULT_EXECUTOR = new ThreadPoolExecutor(1, 1, 8, TimeUnit.HOURS,
                new LinkedBlockingDeque<Runnable>(), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r, "同步任务的默认处理工作者");
                //使该线程在JVM关闭时自动停止，设置为守护线程
                thread.setDaemon(true);
                return thread;
            }
        }, new RejectedExecutionHandler() {

            /**
             * 该RejectExecutionHandler支持重试
             * 当任务被ThreadPoolExecutor拒绝时，
             * 该handler重新将任务放入ThreadPoolExecutor的工作队列
             * 意味着，客户端代码需要等待ThreadPoolExecutor的队伍非满
             *
             * @param r
             * @param executor
             */
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                if (!executor.isShutdown()) {
                    try {
                        executor.getQueue().put(r);
                    } catch (InterruptedException e) {

                    }
                }
            }
        });
    }

    public AsyncTask(Executor executor) {
        this.executor = executor;
    }

    public AsyncTask() {
        this(DEFAULT_EXECUTOR);
    }

    /**
     * 留给子类实现耗时较短的任务
     *
     * @param params
     */
    protected void onPreExecute(Object... params) {
    }

    /**
     * 留给子类实现。用于实现同步任务执行结束后所需执行的操作。
     *
     * @param result
     */
    protected void onPostExecute(V result) {
    }

    protected void onExecutionError(Exception e) {
        e.printStackTrace();
    }

    /**
     * 留给子类实现耗时较长的任务（同步任务），由后台线程负责调用
     *
     * @param params
     * @return
     */
    protected abstract V doInBackground(Object... params);

    protected Future<V> dispatch(final Object... params) {
        FutureTask<V> ft = null;

        //进行异步层初步处理
        onPreExecute(params);

        Callable<V> callable = new Callable<V>() {
            @Override
            public V call() throws Exception {
                V result;
                result = doInBackground(params);
                return result;
            }
        };

        ft = new FutureTask<V>(callable) {

            @Override
            protected void done() {
                try {
                    onPostExecute(this.get());
                } catch (InterruptedException e) {
                    onExecutionError(e);
                } catch (ExecutionException e) {
                    onExecutionError(e);
                }
            }
        };

        executor.execute(ft);

        return ft;
    }
}
