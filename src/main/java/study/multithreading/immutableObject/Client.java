package study.multithreading.immutableObject;

/**
 * 名称：
 * 功能：
 * 条件：
 * Created by wq on 2017/8/5.
 */
public class Client {


    public static void main(String[] args) throws InterruptedException {
        OMCAgent agent = new OMCAgent();
        agent.start();

        for (int i = 0; i < 10; i++) {
            //执行速度慢一点，在读取的时候不可变对象已经由Agent改变了
            Thread.sleep(1000);
            MMSCRouter instance = MMSCRouter.getInstance();
            MMSCInfo info = instance.getMMSC("linlin");
            System.out.println(info.toString());
        }
    }
}
