package study.multithreading.threadSpecificStorage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 名称：
 * 功能：实现SimpleDateFormat线程安全
 * 条件：
 * Created by wq on 2017/9/9.
 */
public class ThreadSpecificDateFormat {
    private static final ThreadLocal<SimpleDateFormat> TS_SDF = new ThreadLocal<SimpleDateFormat>() {

        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat();
        }
    };

    public static Date pares(String timeStamp, String format) throws ParseException {
        final SimpleDateFormat sdf = TS_SDF.get();
        sdf.applyPattern(format);
        Date date = sdf.parse(timeStamp);
        return date;
    }

    public static void main(String[] args) throws ParseException {
        Date date = ThreadSpecificDateFormat.pares("20150501123040", "yyyyMMddHHmmss");
        System.out.println(date);
    }
}
