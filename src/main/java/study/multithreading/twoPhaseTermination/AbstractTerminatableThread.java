package study.multithreading.twoPhaseTermination;

/**
 * 名称：
 * 功能：@see io.github.viscent
 * 条件：
 * Created by wq on 2017/8/6.
 */
public abstract class AbstractTerminatableThread extends Thread implements Terminatable {

    public final TerminationToken terminationToken;

    public AbstractTerminatableThread() {
        this(new TerminationToken());
    }

    public AbstractTerminatableThread(TerminationToken terminationToken) {
        super();
        this.terminationToken = terminationToken;
        terminationToken.register(this);
    }

    /**
     * 具体的处理逻辑
     *
     * @throws Exception
     */
    protected abstract void doRun() throws Exception;

    /**
     * 实现线程停止后的一些清理动作
     *
     * @param cause
     */
    protected void doCleanup(Exception cause) {
    }

    /**
     * 执行线程停止所需的操作
     */
    protected void doTerminiate() {
    }

    @Override
    public void run() {

        Exception ex = null;

        try {
            for (; ; ) {

                //在执行线程处理逻辑前判断线程终止标志
                if (terminationToken.isToShutdown()
                        && terminationToken.reservations.get() <= 0) {
                    break;
                }
                doRun();
            }
        } catch (Exception e) {
            //使得线程能够响应interrupt调用而退出
            ex = e;
        } finally {
            try {
                doCleanup(ex);
            } finally {
                terminationToken.notifyThreadTermination(this);
            }
        }
    }

    @Override
    public void interrupt() {
        terminate();
    }

    public void terminate() {
        terminationToken.setToShutdown(true);

        try {
            //先执行一波待处理的任务
            doTerminiate();
        } finally {

            //如果没有待处理的任务，则试图强行终止该线程
            if (terminationToken.reservations.get() <= 0) {
                super.interrupt();
            }
        }
    }

    public void terminate(boolean waitUtilThreadTerminated) {
        terminate();

        if (waitUtilThreadTerminated) {
            try {
                this.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
