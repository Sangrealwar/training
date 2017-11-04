package study.multithreading.threadSpecificStorage;

import java.text.DecimalFormat;
import java.util.concurrent.*;

/**
 * 名称：
 * 功能：
 * 条件：
 * Created by wq on 2017/9/9.
 */
public class SmsVerficationCodeSender {
    private static final ExecutorService EXECUTOR = new ThreadPoolExecutor(
            1,
            Runtime.getRuntime().availableProcessors(), 60, TimeUnit.SECONDS,
            new SynchronousQueue<Runnable>(), new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, "验证码下发");
            t.setDaemon(true);
            System.out.println();
            return t;
        }
    }, new ThreadPoolExecutor.DiscardOldestPolicy());

    public static void main(String[] args) {
        SmsVerficationCodeSender client = new SmsVerficationCodeSender();

        client.sendVerificationSms("11111111111");
        client.sendVerificationSms("22222222222");
        client.sendVerificationSms("33333333333");

        try {
            //主线程多停留几秒钟，让其他线程打印出东西。。。。
            Thread.sleep(3000);
        } catch (InterruptedException e) {

        }
    }

    public void sendVerificationSms(final String msisn) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                //生成强随机码
                int verificationCode = 0;
                try {
                    verificationCode = ThreadSpecificSecureRandom.INSTANCE.nextInt(9999);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                DecimalFormat df = new DecimalFormat("000000");
                String txtVerCode = df.format(verificationCode);

                sendSms(msisn, txtVerCode);
            }
        };

        EXECUTOR.submit(task);
    }

    private void sendSms(String msisdn, String verificationCode) {
        System.out.println("发送验证码：" + verificationCode + "到" + msisdn);
    }
}
