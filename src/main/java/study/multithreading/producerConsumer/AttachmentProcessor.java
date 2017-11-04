package study.multithreading.producerConsumer;

import study.multithreading.twoPhaseTermination.AbstractTerminatableThread;

import java.io.*;
import java.text.Normalizer;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * 名称：
 * 功能：
 * 条件：
 * Created by wq on 2017/8/6.
 */
public class AttachmentProcessor {
    private final String ATTACHMENT_STORE_BASE_DIR = "D:\\tmp\\attachments\\";

    private final Channel<File> channel = new BlockingQueueChannel<File>(new ArrayBlockingQueue<File>(200));

    /**
     * 两阶段终止
     */
    private final AbstractTerminatableThread indexingThread = new AbstractTerminatableThread() {
        @Override
        protected void doRun() throws Exception {
            File file = null;

            file = channel.take();

            try {
                indexFile(file);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                terminationToken.reservations.decrementAndGet();
            }

        }

        private void indexFile(File file) throws Exception {
            //模拟生成索引文件的耗时
            Random rnd = new Random();

            try {
                Thread.sleep(rnd.nextInt(100));
            } catch (InterruptedException e) {

            }
        }
    };

    public void init() {
        indexingThread.start();
    }

    public void shutdown() {
        indexingThread.terminate();
    }

    public void saveAttachment(InputStream in, String documentId, String originalFileName) throws IOException {
        File file = saveAsFile(in, documentId, originalFileName);
        try {
            channel.put(file);
        } catch (InterruptedException e) {
        }
        indexingThread.terminationToken.reservations.incrementAndGet();
    }


    private File saveAsFile(InputStream in, String documentId, String originalFileName) throws IOException {
        String dirName = ATTACHMENT_STORE_BASE_DIR + documentId;
        File dir = new File(dirName);

        dir.mkdirs();

        File file = new File(dirName + "/" + Normalizer.normalize(originalFileName, Normalizer.Form.NFC));

        if (!dirName.equals(file.getCanonicalFile().getParent())) {
            throw new SecurityException("非法的原文件名：" + originalFileName);
        }

        BufferedOutputStream bos = null;
        BufferedInputStream bis = new BufferedInputStream(in);
        byte[] buf = new byte[2048];
        int len = -1;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(file));
            while ((len = bis.read(buf)) > 0) {
                bos.write(buf, 0, len);
            }
            bos.flush();
        } finally {
            try {
                bis.close();
            } catch (IOException e) {
            }
            try {
                if (null != bos) {
                    bos.close();
                }
            } catch (IOException e) {
            }
        }

        return file;
    }

}
