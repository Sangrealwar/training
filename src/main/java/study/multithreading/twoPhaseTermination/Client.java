package study.multithreading.twoPhaseTermination;

import study.multithreading.guardedSuspension.AlarmType;

/**
 * 名称：
 * 功能：
 * 条件：
 * Created by wq on 2017/8/6.
 */
public class Client {


    public static void main(String[] args) {
        AlarmMgr mgr = AlarmMgr.getInstance();

        //启动告警发送线程
        mgr.init();

        //1.从初始化后，就连接了告警服务器，连接告警服务器要3秒中，但连接的线程是另一个线程
        //如果发送的速度不够快，可忽略连接时间
        mgr.sendAlarm(AlarmType.RESUME, "1", "第一次发送测试");
        mgr.sendAlarm(AlarmType.FAULT, "2", "第二次发送测试");
        mgr.sendAlarm(AlarmType.RESUME, "3", "第三次发送测试");
        mgr.shutdown();
        try {
            //2.发送一条告警信息要3秒钟，加上上述代码的时间，这里需要至少等待9秒钟
            Thread.sleep(9000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mgr.sendAlarm(AlarmType.FAULT, "4", "第四次发送测试");
        mgr.sendAlarm(AlarmType.RESUME, "5", "第五次发送测试");

        System.out.println("调用完成");
    }
}
