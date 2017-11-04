package study.multithreading.immutableObject;


/*使用场景：
1.建模对象变动不频繁，可以将manipulator专门开辟一个线程，用于更新不变对象，此时不可变对象引用用volatile修饰
保证读的一致（多线程的内存可见性）；
2.同时对一组相关数据进行写操作，需要保证原子性
3.使用不可变对象作为HashMap的key，如果HashMap的key的HashCode变化后，会导致get不到对象，不可变对象的hashCode不变

注意问题：
1.变动频繁意味着会加大jvm垃圾回收的负担和cpu的消耗
2.使用等效近似的不可变对象
3.防御性复制：如果不可变本身的字段是可变的，如map，那返回这些对象时需要做防御性复制，避免外部代码改变其内部状态*/

/**
 * 名称：处理彩信中心，路由表变化-Manipulator
 * 功能：对不可变对象进行修改
 * 条件：
 * Created by wq on 2017/8/5.
 */
public class OMCAgent extends Thread {

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //从数据库中读取到更新后，重置MMSCRouter
            MMSCRouter.setInstance(new MMSCRouter());
        }
    }
}
