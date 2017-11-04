package study.multithreading.threadSpecificStorage;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 名称：
 * 功能：隐式参数传递
 * 条件：
 * Created by wq on 2017/9/9.
 */
public class ImplicitParameterPassing {
    public static void main(String[] args) throws InterruptedException {
        ClientThread thread;
        BusinessService bs = new BusinessService();

        for (int i = 0; i < 2; i++) {
            thread = new ClientThread("测试的哦", bs);
            thread.start();
            thread.join();
        }
    }
}

class ClientThread extends Thread {
    private final String message;
    private final BusinessService bs;
    private static final AtomicInteger SEQ = new AtomicInteger(0);

    public ClientThread(String message, BusinessService bs) {
        this.message = message;
        this.bs = bs;
    }

    @Override
    public void run() {
        Context.INSTANCE.setTransactionId(SEQ.getAndIncrement());
        bs.service(message);
    }
}

class Context {
    private static final ThreadLocal<Integer> TS_OBJECT_PROXY = new ThreadLocal<Integer>();

    public static final Context INSTANCE = new Context();

    private Context() {
    }

    public Integer getTransactionId() {
        return TS_OBJECT_PROXY.get();
    }

    public void setTransactionId(Integer transactionId) {
        TS_OBJECT_PROXY.set(transactionId);
    }

    public void reset() {
        TS_OBJECT_PROXY.remove();
    }
}

class BusinessService {
    public void service(String message) {
        int transactionId = Context.INSTANCE.getTransactionId();
        System.out.println("线程事务id" + transactionId + "的消息为：" + message);
    }
}
