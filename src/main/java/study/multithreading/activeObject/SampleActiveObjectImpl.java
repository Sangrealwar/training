package study.multithreading.activeObject;

/**
 * 名称：
 * 功能：
 * 条件：
 * Created by wq on 2017/9/4.
 */
public class SampleActiveObjectImpl {
    public String doProcess(String arg, int i) {
        System.out.println("do方法执行");
        try {
            // 模拟一个比较耗时的操作
            Thread.sleep(50);
        } catch (InterruptedException e) {
            ;
        }
        return arg + "-" + i;
    }
}
