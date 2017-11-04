package study.multithreading.activeObject;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/*
* ThreadPoolExecutor自身相当于Proxy和Scheduler。ThreadPoolExecutor的submit方法相当于对外暴露的异步方法
*
*
* */

/**
 * 名称：
 * 功能：简化版ActiveObject实例，利用的java的动态代理，同时也是客户端测试调用
 * 条件：
 * Created by wq on 2017/9/4.
 */
public class SampleClientOfReusableActiveObject {
    public static void main(String[] args) throws InterruptedException,
            ExecutionException {

        SampleActiveObject sao = ActiveObjectProxy.newInstance(
                SampleActiveObject.class, new SampleActiveObjectImpl(),
                Executors.newCachedThreadPool());
        Future<String> ft = null;

        System.out.println("调用主动对象前");
        try {
            ft = sao.process("Something", 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //模拟其它操作的时间消耗
        Thread.sleep(40);

        System.out.println(ft.get());
    }
}
