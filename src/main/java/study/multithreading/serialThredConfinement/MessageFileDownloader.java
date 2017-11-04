package study.multithreading.serialThredConfinement;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPReply;
import study.multithreading.twoPhaseTermination.AbstractTerminatableThread;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.*;

/**
 * 名称：
 * 功能：模式角色Serializer
 * 条件：
 * Created by wq on 2017/9/16.
 */
public class MessageFileDownloader {

    private final WorkerThread workerThread;

    public MessageFileDownloader(String outputDir, final String ftpServer, final String userName, final String password) {
        workerThread = new WorkerThread(outputDir, ftpServer, userName, password);
    }

    public void init() {
        workerThread.start();
    }

    public void shutdown() {
        workerThread.terminate();
    }

    public void downloadFile(String file) {
        workerThread.download(file);
    }


    private static class WorkerThread extends AbstractTerminatableThread {

        private final BlockingQueue<String> workQueue;
        private final Future<FTPClient> ftpClientPromise;
        private final String outputDir;

        public WorkerThread(String outputDir, final String ftpServer, final String userName, final String password) {
            this.workQueue = new ArrayBlockingQueue<String>(100);
            this.outputDir = outputDir + "/";

            this.ftpClientPromise = new FutureTask<FTPClient>(new Callable<FTPClient>() {
                @Override
                public FTPClient call() throws Exception {
                    FTPClient ftpClient = initFTPClient(ftpServer, userName, password);
                    return ftpClient;
                }
            });

            new Thread((FutureTask<FTPClient>) ftpClientPromise).start();
        }

        public void download(String file) {
            try {
                workQueue.put(file);
            } catch (InterruptedException e) {
            }
            terminationToken.reservations.incrementAndGet();
        }

        private FTPClient initFTPClient(String ftpServer, String userName, String password) throws Exception {
            FTPClient ftpClient = new FTPClient();

            FTPClientConfig config = new FTPClientConfig();
            ftpClient.configure(config);

            int reply;
            ftpClient.connect(ftpServer);

            System.out.println(ftpClient.getReplyString());

            reply = ftpClient.getReplyCode();

            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                throw new RuntimeException("FTP服务拒绝连接");
            }

            boolean isOK = ftpClient.login(userName, password);
            if (isOK) {
                System.out.println(ftpClient.getReplyString());
            } else {
                throw new RuntimeException("登陆失败" + ftpClient.getReplyString());
            }

            reply = ftpClient.cwd("~/messages");
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                throw new RuntimeException("切换工作目录失败" + ftpClient.getReplyString());
            } else {
                System.out.println(ftpClient.getReplyString());
            }

            ftpClient.setFileType(FTP.ASCII_FILE_TYPE);
            return ftpClient;
        }

        @Override
        protected void doRun() throws Exception {
            String file = workQueue.take();

            OutputStream os = null;

            try {
                os = new BufferedOutputStream((new FileOutputStream(outputDir + file)));
                ftpClientPromise.get().retrieveFile(file, os);
            } finally {
                if (null != os) {
                    try {
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                terminationToken.reservations.decrementAndGet();
            }
        }

        @Override
        protected void doCleanup(Exception cause) {
            try {
                ftpClientPromise.get().disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
}
