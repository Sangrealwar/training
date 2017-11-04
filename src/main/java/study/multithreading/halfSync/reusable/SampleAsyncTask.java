package study.multithreading.halfSync.reusable;

import study.multithreading.Debug;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Future;

/**
 * 名称：
 * 功能：
 * 条件：
 * Created by wq on 2017/10/9.
 */
public class SampleAsyncTask {


    public static void main(String[] args) {
        XAsyncTask task = new XAsyncTask();

        List<Future<String>> results = new LinkedList<>();

        try {
            results.add(task.doSomeThing("Half-sync/Half-async", 1));
            results.add(task.doSomeThing("Pattern", 2));

            for (Future<String> result : results) {
                Debug.info(result.get());
            }

            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class XAsyncTask extends AsyncTask<String> {
        @Override
        protected String doInBackground(Object... params) {
            String message = (String) params[0];
            int sequence = (Integer) params[1];
            Debug.info("doInBackground" + message);
            return "message " + sequence + "：" + message;
        }

        @Override
        protected void onPreExecute(Object... params) {
            String message = (String) params[0];
            int sequence = (Integer) params[1];
            Debug.info("onPreExecute[" + sequence + "]" + message);
        }

        public Future<String> doSomeThing(String message, int sequence) {
            if (sequence < 0) {
                throw new IllegalArgumentException("非法的次数：" + sequence);
            }
            return this.dispatch(message, sequence);
        }
    }
}
