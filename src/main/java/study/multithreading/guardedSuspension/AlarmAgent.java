package study.multithreading.guardedSuspension;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;

/**
 * 名称：
 * 功能：负责连接告警服务器，发送告警信息至告警服务器，是受保护对象
 * 条件：
 * Created by wq on 2017/8/5.
 */
public class AlarmAgent {

    //用于记录Alarm Agent 是否连接上告警服务器，变量
    private volatile boolean connectedToServer = false;

    /**
     * 保护条件
     */
    private final Predicate agentConnected = new Predicate() {
        public boolean evalute() {
            return connectedToServer;
        }
    };

    private final Blocker blocker = new ConditionVarBlocker();

    //心跳计时器
    private final Timer heartbeatTimer = new Timer(true);

    /**
     * 受保护方法
     *
     * @param alarm
     * @throws Exception
     */
    public void sendAlarm(final AlarmInfo alarm) throws Exception {
        //连接告警服务器，连接需要等待，或者中断后需要重新连接
        GuardedAction<Void> guardedAction = new GuardedAction<Void>(agentConnected) {
            public Void call() throws Exception {
                doSendAlarm(alarm);
                return null;
            }
        };

        blocker.callWIthGuard(guardedAction);
    }

    /**
     * 目标操作，将告警信息发送到告警服务器
     *
     * @param alarmInfo
     */
    private void doSendAlarm(AlarmInfo alarmInfo) {
        System.out.println("发送告警信息：" + alarmInfo.toString());

        //发送告警服务器3秒
        try {
            Thread.sleep(3000);
        } catch (Exception e) {

        }
    }

    public void init() {
        Thread connectingThread = new Thread(new ConnectingTask());

        connectingThread.start();

        heartbeatTimer.schedule(new HeartbeatTask(), 60000, 2000);
    }

    public void disconnedt() {
        System.out.println("中断告警服务器连接");
        connectedToServer = false;
    }

    /**
     * 相当于stateChange，通过调用blocker讲connectedToServer设置为true，并通知blocker唤醒其他线程
     */
    protected void onConnected() {
        try {
            blocker.signalAfter(new Callable<Boolean>() {
                public Boolean call() throws Exception {
                    connectedToServer = true;

                    System.out.println("连接服务器");

                    return Boolean.TRUE;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void onDisconnected() {
        connectedToServer = false;
    }

    /**
     * 负责与告警服务器建立网络连接
     */
    private class ConnectingTask implements Runnable {
        public void run() {
            try {
                System.out.println("与告警服务器进行网络连接中");
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            onConnected();
        }
    }

    /**
     * 心跳定时任务，定时检查与告警服务器连接是否正常，发现连接异常后自动重新连接
     */
    private class HeartbeatTask extends TimerTask {
        public void run() {
            if (!testConnection()) {
                onDisconnected();
                reConnect();
            }
        }

        private boolean testConnection() {
            return true;
        }

        private void reConnect() {
            ConnectingTask connectingTask = new ConnectingTask();

            //直接在心跳定时器线程中执行
            connectingTask.run();
        }
    }
}
