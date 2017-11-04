package study.multithreading.twoPhaseTermination;

import study.multithreading.guardedSuspension.AlarmAgent;
import study.multithreading.guardedSuspension.AlarmInfo;
import study.multithreading.guardedSuspension.AlarmType;

import java.lang.reflect.Field;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 名称：
 * 功能：
 * 条件：
 * Created by wq on 2017/8/6.
 */
public class AlarmSendingThread extends AbstractTerminatableThread {

    private AlarmAgent alarmAgent = new AlarmAgent();

    //告警队列
    private final BlockingQueue<AlarmInfo> alarmQueue;
    private final ConcurrentHashMap<String, AtomicInteger> submittedAlarmRegistry;

    public AlarmSendingThread() {
        alarmQueue = new ArrayBlockingQueue<AlarmInfo>(100);

        submittedAlarmRegistry = new ConcurrentHashMap<String, AtomicInteger>();

        alarmAgent.init();
    }

    @Override
    protected void doRun() throws Exception {
        AlarmInfo alarm;
        //发送告警信息
        alarm = alarmQueue.take();
        terminationToken.reservations.decrementAndGet();

        try {
            alarmAgent.sendAlarm(alarm);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /**
         * 处理恢复告警：将相应的故障告警从注册表中删除，使得相应故障恢复后若再次出现相同故障
         * 该故障信息能够上报服务器
         */
        if (AlarmType.RESUME == alarm.type) {
            String key = AlarmType.FAULT.toString() + "：" + alarm.getId() + "@" + alarm.getExtraInfo();
            submittedAlarmRegistry.remove(key);

            key = AlarmType.RESUME.toString() + "：" + alarm.getId() + "@" + alarm.getExtraInfo();
            submittedAlarmRegistry.remove(key);
        }
    }

    @Override
    protected void doCleanup(Exception exp) {
        if (null != exp && !(exp instanceof InterruptedException)) {
            exp.printStackTrace();
        }
        alarmAgent.disconnedt();
    }

    public int sendAlarm(final AlarmInfo alarmInfo) {
        AlarmType type = alarmInfo.type;

        String id = alarmInfo.getId();

        String extraInfo = alarmInfo.getExtraInfo();

        if (terminationToken.isToShutdown()) {
            System.err.println("拒绝的告警记录：" + id + "，" + extraInfo);
            return -1;
        }

        int duplicateSubmissionCount = 0;
        try {
            AtomicInteger prevSubmittedCounter;

            prevSubmittedCounter = submittedAlarmRegistry.putIfAbsent(
                    type.toString() + "：" + id + "@" + extraInfo, new AtomicInteger(0));

            if (null == prevSubmittedCounter) {
                terminationToken.reservations.incrementAndGet();
                alarmQueue.put(alarmInfo);
            } else {

                //如果故障未恢复，不用重复发送告警信息给服务器，仅增加计数
                duplicateSubmissionCount = prevSubmittedCounter.incrementAndGet();
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }

        return duplicateSubmissionCount;
    }
}
