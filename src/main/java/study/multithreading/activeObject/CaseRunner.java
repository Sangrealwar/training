package study.multithreading.activeObject;


import study.multithreading.twoPhaseTermination.AbstractTerminatableThread;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 名称：模拟调用AyncRequestPersistence，通过缓存一个图片文件，将耗时的IO操作分发给多个线程
 * 功能：
 * 条件：
 * Created by wq on 2017/9/4.
 */
public class CaseRunner {
    public static void main(String[] args) {
        int numRequestThreads = Runtime.getRuntime().availableProcessors();
        int targetTPS = 200;

        // 每秒中提交的请求数量
        int requestsPerInterval = targetTPS / numRequestThreads;
        final AtomicInteger counter = new AtomicInteger(0);

        final Set<AbstractTerminatableThread> requestThreads = new HashSet<AbstractTerminatableThread>();

        AbstractTerminatableThread requestThread;
        for (int i = 0; i < numRequestThreads; i++) {
            requestThread = new RequestSenderThread(requestsPerInterval, counter);
            requestThreads.add(requestThread);
            requestThread.start();
        }

        Timer timer = new Timer(true);
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                for (AbstractTerminatableThread att : requestThreads) {
                    att.terminate();
                }

            }

        }, 5 * 60 * 1000);
    }

    static class RequestSenderThread extends AbstractTerminatableThread {

        private final int requestsPerInterval;
        private final RequestPersistence persistence;
        private final ThreadPoolExecutor executor;
        private final Attachment attachment;
        private final AtomicInteger counter;

        public RequestSenderThread(int requestsPerInterval, AtomicInteger counter) {

            this.requestsPerInterval = requestsPerInterval;
            this.counter = counter;
            persistence = AsyncRequestPersistence.getInstance();
            executor = new ThreadPoolExecutor(80, 200, 60 * 3600, TimeUnit.SECONDS,
                    new ArrayBlockingQueue<Runnable>(300));
            attachment = new Attachment();
            try {

                // 附件文件，可根据实际情况修改！
                File file = new File("D:\\tmp\\attachment_demo\\attachment.png");
                ByteBuffer contentBuf = ByteBuffer.allocate((int) file.length());
                FileInputStream fin = new FileInputStream(file);
                try {
                    fin.getChannel().read(contentBuf);
                } finally {
                    fin.close();
                }

                attachment.setContentType("image/png");
                attachment.setContent(contentBuf.array());
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void doRun() {
            for (int i = 0; i < requestsPerInterval; i++) {

                executor.execute(() -> {
                    MMSDeliverRequest request = new MMSDeliverRequest();
                    request.setTransactionID(String.valueOf(counter.incrementAndGet()));
                    request.setSenderAddress("13312345678");
                    request.setTimeStamp(new Date());
                    request.setExpiry((new Date().getTime() + 3600000) / 1000);

                    request.setSubject("Hi");
                    request.getRecipient().addTo("776");
                    request.setAttachment(attachment);

                    persistence.store(request);

                });

            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void doCleanup(Exception cause) {
            executor.shutdownNow();
        }

    }
}
