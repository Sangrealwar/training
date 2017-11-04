package study.multithreading.threadSpecificStorage;

/**
 * 名称：
 * 功能：自定义的线程特有对象，造成内存泄露的测试类
 * 条件：
 * Created by wq on 2017/9/9.
 */
public class Counter {
    private int i = 0;

    public int getAndIncrement() {
        return (i++);
    }
}