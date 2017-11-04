package study.multithreading.twoPhaseTermination;

import study.multithreading.guardedSuspension.AlarmInfo;
import study.multithreading.guardedSuspension.AlarmType;

import java.util.IllegalFormatCodePointException;

/**
 * 名称：
 * 功能：
 * 条件：
 * Created by wq on 2017/8/6.
 */
public class AlarmMgr {

    private static final AlarmMgr INSTANCE = new AlarmMgr();

    private volatile boolean shutdownRequested = false;

    //可停止线程
    private final AlarmSendingThread alarmSendingThread;

    private AlarmMgr() {
        //告警发送线程初始化的时候，会启动告警代理，根据保护者模式，发送告警信息
        alarmSendingThread = new AlarmSendingThread();
    }

    public static AlarmMgr getInstance() {
        return INSTANCE;
    }

    public int sendAlarm(AlarmType type, String id, String extraInfo) {
        System.out.println("触发告警：" + type + "，" + id + "，" + extraInfo);

        int duplicateSubmissionCount = 0;

        try {
            AlarmInfo alarmInfo = new AlarmInfo(id, type);
            alarmInfo.setExtraInfo(extraInfo);

            duplicateSubmissionCount = alarmSendingThread.sendAlarm(alarmInfo);
        } catch (Throwable t) {
            t.printStackTrace();
        }

        return duplicateSubmissionCount;
    }

    public void init() {
        alarmSendingThread.start();
    }

    public synchronized void shutdown() {
        if (shutdownRequested) {
            throw new IllegalStateException("已经在停止线程");
        }
        alarmSendingThread.terminate();
        shutdownRequested = true;
    }
}
