package spittr.web.util;

/**
 * 字符串工具类
 * Created by ThinkPad on 2017/1/5.
 */
public class StringUtil {
    /**
     * 判断字符串是否为空，为null、长度为0、去前后空格后为''，均为空
     * @param str
     * @return
     */
    public static boolean IsNull(String str)
    {
        if(str != null && str.length()>0 && (!"".equals(str.trim())))
            return false;
        else
            return true;
    }
}
