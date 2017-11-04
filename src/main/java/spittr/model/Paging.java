package spittr.model;

/**
 * 功能：分页模型，值对象
 * 条件：
 * Created by wq on 2017/3/1.
 */
public class Paging {
    private int totalCount;     //总项数
    private int current;       //当前页
    private int pageSize;      //每页大小
    private int totalPage;     //总页数

    public int getTotalCount() {
        return totalCount;
    }

    public int getCurrent() {
        return current;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public Paging(int totalCount, int current, int pageSize, int totalPage) {
        this.totalCount = totalCount;
        this.current = current;
        this.pageSize = pageSize;
        this.totalPage = totalPage;
    }

    /**
     * 首页
     * @return
     */
    public boolean isFirst()
    {
        return current==1;
    }

    /**
     * 末页
     * @return
     */
    public boolean isLast()
    {
        return current==totalPage;
    }
}
