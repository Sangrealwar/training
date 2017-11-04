package spittr.web.util;

import java.util.Date;

/**
 * 自动生成工具
 * Created by macro_su on 2017/1/5.
 */
public interface GenerateUtil {
    /**
     * 生成主键
     * @return
     */
    public String getID();

    /**
     * 生成时间戳
     * @return
     */
    public String getDate();

    public Date getTime();
}
