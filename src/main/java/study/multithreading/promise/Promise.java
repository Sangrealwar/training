package study.multithreading.promise;

import java.util.concurrent.*;

/**
 * 名称： 一个承诺，相当于小票，根据这个小票获取执行的结果，结果是个泛型参数
 *      继承了Runnable，可通过多线程调用给承诺结果赋值，同时返回承诺结果时会等待进程完成
 * 功能：
 * 条件：
 * Created by wq on 2017/7/29.
 */
public class Promise<T extends Equipment> extends FutureTask{

    public Promise(Callable callable) {
        super(callable);
    }
}
