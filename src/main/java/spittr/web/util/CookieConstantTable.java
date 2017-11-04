package spittr.web.util;

/**
 * cookie的一些常量
 * Created by wq on 2017/1/6.
 */
public class CookieConstantTable {
    /**
     * cookie有效期默认30天
     */
    public final static int COOKIE_MAX_AGE = 60 * 60 * 24 * 30;
    /**
     * 加密时额外的标记
     */
    public final static String FLAG = "rpt";
    /**
     * cookie名
     */
    public final static String COOKIE_DEFAULT_NAME = "userCookie";
}
