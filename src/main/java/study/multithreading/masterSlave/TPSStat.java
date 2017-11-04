package study.multithreading.masterSlave;

import study.multithreading.Debug;
import study.multithreading.twoPhaseTermination.AbstractTerminatableThread;

import java.io.*;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

/**
 * 名称：
 * 功能：
 * 条件：
 * Created by wq on 2017/9/16.
 */
public class TPSStat {

    public static void main(String[] args) throws IOException {
        //接口日志文件所在目录
        String logBaseDir = args[0];

        //忽略的操作名列表
        String excludedOperationNames = "";

        //指定要统计在内的操作名列表
        String includedOperationNames = "*";

        //指定要统计在内的目标设备名
        String destinationSysName = "*";

        int argc = args.length;

        if (argc > 2) {
            excludedOperationNames = args[1];
        }
        if (argc > 3) {
            excludedOperationNames = args[2];
        }
        if (argc > 4) {
            excludedOperationNames = args[3];
        }

        Master processor = new Master(logBaseDir,
                excludedOperationNames, includedOperationNames, destinationSysName);

        BufferedReader fileNamesReader = new BufferedReader(new InputStreamReader(System.in));

        ConcurrentMap<String, AtomicInteger> result = processor.calcute(fileNamesReader);

        for (String timeRange : result.keySet()) {
            System.out.println(timeRange + "," + result.get(timeRange));
        }

    }

    private static class Master {
        private final String logFileBaseDir;
        private final String excludedOperationNames;
        private final String includedOperationNames;
        private final String destinationSysName;

        //每次派发给某个Slave线程的文件个数
        private static final int NUMBER_OF_FILE_FOR_EACH_DISPATCH = 5;
        //工作线程数=CPU核
        private static final int WORKER_COUNT = Runtime.getRuntime().availableProcessors();

        public Master(String logFileBaseDir, String excludedOperationNames, String includedOperationNames, String destinationSysName) {
            this.logFileBaseDir = logFileBaseDir;
            this.excludedOperationNames = excludedOperationNames;
            this.includedOperationNames = includedOperationNames;
            this.destinationSysName = destinationSysName;
        }

        public ConcurrentMap<String, AtomicInteger> calcute(
                BufferedReader fileNamesReader) throws IOException {
            ConcurrentHashMap<String, AtomicInteger> repository =
                    new ConcurrentHashMap<String, AtomicInteger>();

            //创建工作者线程
            Worker[] workers = createAndStartWorkers(repository);

            //指派任务给工作者线程
            dispatchTask(fileNamesReader, workers);

            for (int i = 0; i < WORKER_COUNT; i++) {
                workers[i].terminate();
            }

            //返回处理结果
            return repository;
        }

        private Worker[] createAndStartWorkers(ConcurrentHashMap<String, AtomicInteger> repository) {
            Worker[] workers = new Worker[WORKER_COUNT];
            Worker worker;

            Thread.UncaughtExceptionHandler eh = new Thread.UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(Thread t, Throwable e) {
                    e.printStackTrace();
                }
            };

            for (int i = 0; i < WORKER_COUNT; i++) {
                worker = new Worker(repository, excludedOperationNames, includedOperationNames, destinationSysName);
                workers[i] = worker;
                worker.start();
            }
            return workers;
        }

        private void dispatchTask(BufferedReader fileNamesReader, Worker[] workers) throws IOException {
            String line;
            Set<String> fileNames = new HashSet<String>();

            int fileCount = 0;
            int workerIndex = -1;
            BufferedReader logFileReader;
            while ((line = fileNamesReader.readLine()) != null) {
                fileNames.add(line);
                fileCount++;
                if (0 == (fileCount % NUMBER_OF_FILE_FOR_EACH_DISPATCH)) {

                    //工作者线程间的负载均衡：采用简单的轮询选择worker
                    workerIndex = (workerIndex + 1) % WORKER_COUNT;
                    logFileReader = makeReaderFrom(fileNames);
                    //TODO debug
                    Debug.info("分发" + NUMBER_OF_FILE_FOR_EACH_DISPATCH + " 文件到：" + workerIndex);
                    workers[workerIndex].submitWorkload(logFileReader);

                    fileNames = new HashSet<String>();
                    fileCount = 0;
                }
            }

            if (fileCount > 0) {
                logFileReader = makeReaderFrom(fileNames);
                workerIndex = (workerIndex + 1) % WORKER_COUNT;
                workers[workerIndex].submitWorkload(logFileReader);
            }
        }

        private BufferedReader makeReaderFrom(final Set<String> logFileNames) {
            BufferedReader logFileReader;

            InputStream in = new SequenceInputStream((new Enumeration<InputStream>() {
                private Iterator<String> iterator = logFileNames.iterator();

                @Override
                public boolean hasMoreElements() {
                    return iterator.hasNext();
                }

                @Override
                public InputStream nextElement() {
                    String fileName = iterator.next();
                    InputStream in = null;

                    try {
                        in = new FileInputStream(logFileBaseDir + fileName);
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    return in;
                }
            }));

            logFileReader = new BufferedReader((new InputStreamReader(in)));
            return logFileReader;
        }
    }

    private static class Worker extends AbstractTerminatableThread {
        private static final Pattern SPLIT_PATTERN = Pattern.compile("\\|");
        private final ConcurrentMap<String, AtomicInteger> repository;
        private final BlockingQueue<BufferedReader> workQueue;

        private final String selfDevice = "ESB";
        private final String excludedOperationNames;
        private final String includedOperationNames;
        private final String destinationSysName;

        public Worker(ConcurrentMap<String, AtomicInteger> repository, String excludedOperationNames, String includedOperationNames, String destinationSysName) {
            this.repository = repository;
            this.workQueue = new ArrayBlockingQueue<BufferedReader>(100);
            this.excludedOperationNames = excludedOperationNames;
            this.includedOperationNames = includedOperationNames;
            this.destinationSysName = destinationSysName;
        }

        public void submitWorkload(BufferedReader taskWorkload) {
            try {
                workQueue.put(taskWorkload);
                terminationToken.reservations.incrementAndGet();
            } catch (InterruptedException e) {
            }
        }

        @Override
        protected void doRun() throws Exception {
            BufferedReader logFileReader = workQueue.take();

            String interfaceLogRecord;
            String[] recordParts;
            String timeStamp;
            AtomicInteger reqCounter;
            AtomicInteger existingReqCounter;
            int i = 0;

            try {
                while ((interfaceLogRecord = logFileReader.readLine()) != null) {
                    recordParts = SPLIT_PATTERN.split(interfaceLogRecord, 0);

                    //避免CPU占用过高
                    if (0 == (++i) % 100000) {
                        Thread.sleep(80);
                        i = 0;
                    }

                    //跳过无效记录（如果有的话）
                    if (recordParts.length < 7) {
                        continue;
                    }

                    if (("request".equals(recordParts[2])) &&
                            (recordParts[6].startsWith(selfDevice))) {
                        timeStamp = recordParts[0];

                        timeStamp = new String(timeStamp.substring(0, 19).toCharArray());

                        String operName = recordParts[4];

                        reqCounter = repository.get(timeStamp);
                        if (null == reqCounter) {
                            reqCounter = new AtomicInteger(0);
                            existingReqCounter = repository
                                    .putIfAbsent(timeStamp, reqCounter);
                            if (null != existingReqCounter) {
                                reqCounter = existingReqCounter;
                            }
                        }

                        if (IsSrcDeviceEEligible(recordParts[5])) {
                            if (excludedOperationNames.contains(operName + ",")) {
                                continue;
                            }

                            if ("*".equals(includedOperationNames)) {
                                reqCounter.incrementAndGet();
                            } else {
                                if (includedOperationNames.contains((operName + ","))) {
                                    reqCounter.incrementAndGet();
                                }
                            }
                        }
                    }
                }
            } finally {
                terminationToken.reservations.decrementAndGet();
                logFileReader.close();
            }
        }

        //判断目标设备名是否在待统计之列
        private boolean IsSrcDeviceEEligible(String sourceNE) {
            boolean resule = false;

            if ("*".equals(destinationSysName)) {
                resule = true;
            } else if (destinationSysName.equals(sourceNE)) {
                resule = true;
            }
            return resule;
        }
    }
}
