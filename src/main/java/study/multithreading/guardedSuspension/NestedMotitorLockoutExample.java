package study.multithreading.guardedSuspension;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;

/**
 * 名称：
 * 功能：嵌套监视器锁死示例
 * 条件：
 * Created by wq on 2017/8/5.
 */
public class NestedMotitorLockoutExample {

    public static void main(String[] args) {
        final Helper helper = new Helper();

        System.out.println("开始调用受保护方法");

        Thread t = new Thread(new Runnable() {
            public void run() {
                String result;
                result = helper.xGuardedMethod("test");
                System.out.println(result);
            }
        });

        t.start();
        final Timer timer = new Timer();

        //延迟50ms
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                helper.xStateChanged();
                timer.cancel();
            }

        }, 50, 10);

        System.out.println("");
    }


    private static class Helper {

        private volatile boolean isStateOk = false;

        private final Predicate stateBeOk = new Predicate() {
            public boolean evalute() {
                return isStateOk;
            }
        };

        private final Blocker blocker = new ConditionVarBlocker();

        // 如果加了synchronized的话：
        // blocker会调用locks.Condition的await方法，await方法会释放其所属Condition实例关联的锁
        // 但该方法本身所获得的锁（NestedMotitorLockoutExample）并没有释放，但是await需要其他线程
        // 调用Condition实例的signal/signlaAll方法，butxStateChanged的锁此时还是被xGuardedMethod获得，然后就死锁了
        public String xGuardedMethod(final String message) {

            GuardedAction<String> ga = new GuardedAction<String>(stateBeOk) {
                public String call() throws Exception {
                    return message + "->接受到了。";
                }
            };

            String result = null;

            try {
                result = blocker.callWIthGuard(ga);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        public void xStateChanged() {
            try {
                blocker.signalAfter(new Callable<Boolean>() {
                    public Boolean call() throws Exception {
                        isStateOk = true;
                        System.out.println("状态ok");
                        return Boolean.TRUE;
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
