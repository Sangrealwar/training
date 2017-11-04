package spittr.web.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 自动生成工具实现类
 * Created by macro_su on 2017/1/5.
 */
public class GenerateUtilImpl implements GenerateUtil {
    Date date=new Date();
    SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    String now = df.format( date );
    public String getID() {
        return now;//暂时以时间戳
    }

    public String getDate() {
        return now;
    }

    public Date getTime() {
        return date;
    }
}
