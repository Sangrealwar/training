package study.multithreading.promise;

import java.util.concurrent.Callable;

/**
 * 名称：小鸡
 * 功能：
 * 条件：
 * Created by wq on 2017/7/29.
 */
public class Chick implements Promisor {

    //承诺运一把圣剑
    Promise<Equipment> delivery() {

        //具体实现承诺的步骤
        Callable<TheSword> callable = new Callable<TheSword>() {
            public TheSword call() throws Exception {
                System.out.println("小鸡：开始拿圣剑");
                Thread.sleep(3000);
                System.out.println("小鸡：拿到");
                return new TheSword();
            }
        };

        Promise<Equipment> promise = new Promise<Equipment>(callable);

        //这里是异步设置承诺凭证，也可以同步
        new Thread(promise).start();
        System.out.println("小鸡：等待执行任务");

        //同步方式
//        new Thread(promise).run();

        return promise;
    }
}
