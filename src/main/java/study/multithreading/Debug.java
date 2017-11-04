package study.multithreading;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 名称：
 * 功能：
 * 条件：
 * Created by wq on 2017/9/16.
 */
public class Debug {
    private static ThreadLocal<SimpleDateFormat> sdfWrapper = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        }
    };

    public static void info(String message) {
        SimpleDateFormat sdf = sdfWrapper.get();
        System.out.println('[' + sdf.format(new Date()) + "][INFO][" + Thread.currentThread().getName() + "]:" + message);
    }
}
