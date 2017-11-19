package study.multithreading.activeObject;




import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.Logger;

/**
 * 名称：
 * 功能：彩信下发请求缓存的入口类，封装多线程的操作，是Proxy
 * 条件：
 * Created by wq on 2017/9/4.
 */
public class AsyncRequestPersistence implements RequestPersistence {
    private static final long ONE_MINUTE_IN_SECONDS = 60;
    private final Logger logger;
    private final AtomicLong taskTimeConsumedPerInterval = new AtomicLong(0);
    private final AtomicInteger requestSubmittedPerIterval = new AtomicInteger(0);

    //模式角色 Servant
    //作用： 负责具体执行Proxy对外暴露的方法，比如说将封装好的MethodRequest进行处理
    private final DiskbasedRequestPersistence delegate = new DiskbasedRequestPersistence();

    //模式角色 Scheduler
    //作用：将MethodRequest对象存入缓存中
    private final ThreadPoolExecutor scheduler;

    //用来保存AsyncRequestPersistence的唯一实例
    private static class InstanceHolder {
        final static RequestPersistence INSTANCE = new AsyncRequestPersistence();
    }

    private AsyncRequestPersistence() {
        //其中ArrayBlockingQueue相当于ActivationQueue参与者，就是个缓冲区
        this.logger = Logger.getLogger(AsyncRequestPersistence.class);
        scheduler = new ThreadPoolExecutor(1, 3, 60 * ONE_MINUTE_IN_SECONDS,
                TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(200), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t;
                t = new Thread(r, "AsyncRequestPersistence");
                return t;
            }
        });

        //缓冲区饱和处理策略，在任务提交方法中运行被拒绝的任务
        //ThreadPoolExecutor.AbortPolicy  直接抛出异常
        //ThreadPoolExecutor.DiscardPolicy 丢弃当前任务，不抛出异常
        //ThreadPoolExecutor.DiscardOldestPolicy 将最老的任务丢弃，重新接纳新拒绝的任务
        scheduler.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        //启动队列监控定时任务
        Timer monitorTime = new Timer(true);
        monitorTime.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (logger.isInfoEnabled()) {
                    logger.info("任务数：" + requestSubmittedPerIterval + "，队列大小："
                            + scheduler.getQueue().size() + "，任务定时消费频率：" + taskTimeConsumedPerInterval.get()
                            + "ms");
                    taskTimeConsumedPerInterval.set(0);
                    requestSubmittedPerIterval.set(0);
                }
            }
        }, 0, ONE_MINUTE_IN_SECONDS * 1000);
    }

    //获得AsyncRequestPersistence的唯一实例
    public static RequestPersistence getInstance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public void store(final MMSDeliverRequest request) {
        /**
         * MethodRequest角色
         * 利用闭包，将对store方法的调用封装成MethodRequest对象，并存入缓冲区
         */
        Callable<Boolean> methodRequest = new Callable<Boolean>() {
            /**
             * 该方法是在工作线程中异步执行，保证了客户端和真正执行方是在不同线程：
             * 服务器工作者线程负责触发请求消息缓存，ThreadPoolExecutor所维护的工作者线程
             * 负责将请求消息序列化到磁盘文件中
             * @return
             * @throws Exception
             */
            @Override
            public Boolean call() throws Exception {
                long start = System.currentTimeMillis();
                try {
                    delegate.store(request);
                } finally {
                    taskTimeConsumedPerInterval.addAndGet(System.currentTimeMillis() - start);
                }
                return Boolean.TRUE;
            }
        };
        //相当于Scheduler的enqueue方法
        //当ThreadPoolExecutor当前使用的线程数量小于其核心线程数时，submit方法会新建线程执行任务，
        //当ThreadPoolExecutor当前使用的线程数量大于其核心线程数时，submit方法所接受的任务才会被存入其
        //维护的阻塞队列中，不过不影响
        scheduler.submit(methodRequest);
        requestSubmittedPerIterval.incrementAndGet();
    }
}
