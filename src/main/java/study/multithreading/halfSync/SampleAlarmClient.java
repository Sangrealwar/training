package study.multithreading.halfSync;


import study.multithreading.Debug;
import study.multithreading.guardedSuspension.AlarmType;
import study.multithreading.twoPhaseTermination.AlarmMgr;

import java.sql.Connection;

/**
 * 名称：
 * 功能：
 * 条件：
 * Created by wq on 2017/10/9.
 */
public class SampleAlarmClient {

    //告警日志抑制阈值
    private static final int ALARM_MSG_SUPRESS_THRESHOLD = 10;

    static {
        //初始化告警模块
        AlarmMgr.getInstance().init();
    }

    public static void main(String[] args) {
        SampleAlarmClient alarmClient = new SampleAlarmClient();
        Connection dbConn = null;

        try {
            dbConn = alarmClient.retrieveDBConnection();
        } catch (Exception e) {
            final AlarmMgr alarmMgr = AlarmMgr.getInstance();
            int duplicateSubmissionCount;
            String alarmId = "00000010000020";
            final String alarmExtraInfo = "operation=GetDconnection;detail=Failed to get DB connection" + e.getMessage();

            duplicateSubmissionCount = alarmMgr.sendAlarm(AlarmType.FAULT, alarmId, alarmExtraInfo);
            if (duplicateSubmissionCount < ALARM_MSG_SUPRESS_THRESHOLD) {
                Debug.info("Alarm[" + alarmId + "] 提交，额外信息：" + alarmExtraInfo);
            } else {
                if (duplicateSubmissionCount == ALARM_MSG_SUPRESS_THRESHOLD) {
                    Debug.info("Alarm[" + alarmId + "] 提交次数超过阈值 "
                            + ALARM_MSG_SUPRESS_THRESHOLD);
                }
            }
        }
    }

    private Connection retrieveDBConnection() throws Exception {
        Connection conn = null;

        return conn;
    }

    private void doSomeThind(Connection connection) {
    }
}
