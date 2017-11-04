package study.multithreading.serialThredConfinement;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/*使用说明：
* 1.定义Serializer提交给WorkerThread的任务类型
* 2.定义AbstractSerializer的子类，并实现父类定义的makeTask方法
* 3.定义TaskProcessor接口的子类
* */

/**
 * 名称：
 * 功能：
 * 条件：
 * Created by wq on 2017/9/16.
 */
public class ReusableCodeExample {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        SomeService ss = new SomeService();
        ss.init();

        Future<String> result = ss.doSome("串行任务", 1);

        Thread.sleep(100);

        System.out.println(result.get());

        ss.shutdown();
    }

    private static class Task {
        private final String message;
        private final int id;

        public Task(String message, int id) {
            this.message = message;
            this.id = id;
        }
    }

    private static class SomeService extends AbstractSerializer<Task, String> {

        public SomeService() {
            super(new ArrayBlockingQueue<Runnable>(100), new TaskProcessor<Task, String>() {
                @Override
                public String doProcess(Task task) throws Exception {
                    System.out.println("[" + task.id + "]" + task.message);
                    return task.message + " 接收";
                }
            });
        }

        @Override
        protected Task makeTask(Object... params) {
            String message = (String) params[0];
            int id = (Integer) params[1];

            return new Task(message, id);
        }

        public Future<String> doSome(String message, int id) throws InterruptedException {
            Future<String> result = null;

            result = service(message, id);

            return result;
        }
    }
}
