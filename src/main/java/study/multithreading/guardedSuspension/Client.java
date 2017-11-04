package study.multithreading.guardedSuspension;

/**
 * 名称：
 * 功能：Guarded Suspension模式
 * 条件：
 * Created by wq on 2017/8/5.
 */
public class Client {


    public static void main(String[] args) {
        AlarmAgent alarmAgent = new AlarmAgent();
        alarmAgent.init();

        try {
            alarmAgent.sendAlarm(new AlarmInfo("电量不足", AlarmType.RESUME));
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("调用完成");
    }
}
