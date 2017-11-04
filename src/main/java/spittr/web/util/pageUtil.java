package spittr.web.util;

/**
 * 分页辅助算法
 * Created by ThinkPad on 2017/1/11.
 */
public class pageUtil {
    /**
     * 计算当前索引
     * @param currentPage
     * @param pageSize
     * @return
     */
    public static int calCurrentIndex(int currentPage,int pageSize)
    {
        return currentPage*pageSize;
    }

    /**
     * 计算下一页索引
     * @param currentPage
     * @param pageSize
     * @return
     */
    public static int calNextIndex(int currentPage,int pageSize)
    {
        int skipNum =calCurrentIndex(currentPage,pageSize);
        return skipNum+pageSize;
    }
}
