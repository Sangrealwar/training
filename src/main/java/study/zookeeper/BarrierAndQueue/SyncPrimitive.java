package study.zookeeper.BarrierAndQueue;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.Random;

/**
 * 功能：Watcher基类
 * 条件：
 * Created by wq on 2017/4/22.
 */
public class SyncPrimitive implements Watcher {

    static ZooKeeper zk = null;
    static Integer mutex;   //互斥标记

    String root;

    SyncPrimitive(String address) {
        if(zk == null)
        {
            try
            {
                System.out.println("开始zookeeper");
                // Session创建是异步的，构造函数可能在Session完全建立之前返回，产生KeeperErrorCode = ConnectionLoss的异常
                zk = new ZooKeeper(address,30000,this);
                mutex=new Integer(-1);
                System.out.println("zookeeper初始化结束"+zk);
            }
            catch (IOException e){
                System.out.println(e.toString());
                zk=null;
            }
        }
        //else mutex = new Integer(-1);
    }

    public void process(WatchedEvent watchedEvent) {
        synchronized (mutex)
        {
            System.out.println("运行："+watchedEvent.toString());
            mutex.notify();   //唤醒
        }
    }

    public static void main(String args[]) {
        if (args[0].equals("qTest"))
            queueTest(args);
        else
            barrierTest(args);

    }

    public static void queueTest(String args[]) {
        Queue q = new Queue(args[1], "/app1");

        System.out.println("输入: " + args[1]);
        int i;
        Integer max = new Integer(args[2]);

        if (args[3].equals("p") ) {
            System.out.println("生产者");
            for (i = 0; i < max; i++)
                try{
                    q.produce(10 + i);
                } catch (KeeperException e){
                    System.out.println(e.getMessage());
                } catch (InterruptedException e){
                    System.out.println(e.getMessage());
                }
        } else {
            System.out.println("消费者");

            for (i = 0; i < max; i++) {
                try{
                    int r = q.consume();
                    System.out.println("元素: " + r);
                } catch (KeeperException e){
                    System.out.println(e.toString());
                    break;
//                    i--;
                } catch (InterruptedException e){
                    System.out.println(e.toString());

                }
            }
        }
    }

    public static void barrierTest(String args[]) {
        Barrier b = new Barrier(args[1], "/app1", new Integer(args[2]),args[3]);
        try{
            boolean flag = b.enter();
            System.out.println("Entered barrier: " + args[2]);
            if(!flag) System.out.println("Error when entering the barrier");
        } catch (KeeperException e){
            System.out.println(e.toString());
        } catch (InterruptedException e){
          System.out.println(e.toString());
        }

        // Generate random integer
        Random rand = new Random();
        int r = rand.nextInt(100);
        // Loop for rand iterations
        for (int i = 0; i < r; i++) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {

            }
        }
        try{
            b.leave();
        } catch (KeeperException e){
            System.out.println(e.toString());

        } catch (InterruptedException e){
            System.out.println(e.toString());
        }
        System.out.println("Left barrier");
    }
}
