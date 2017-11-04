package study.sync;

/**
 * 功能：
 * 条件：
 * Created by wq on 2017/3/20.
 */
public class Thread3 {

    public static void main(String[] args) {
        final Thread2 myt2 = new Thread2();
        Thread t1 = new Thread(  new Runnable() {  public void run() { myt2.m4t1();   }  }, "t1"  );
        Thread t2 = new Thread(  new Runnable() {  public void run() { myt2.m4t2();   }  }, "t2"  );
        t1.start();
        t2.start();
    }
}
