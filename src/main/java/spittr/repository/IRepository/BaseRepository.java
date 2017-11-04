package spittr.repository.IRepository;

import java.util.List;

/**
 * 功能：仓储基类
 * 条件：
 * Created by wq on 2017/3/1.
 */
public interface BaseRepository {
     List find(Class type, int page, int pageSize);
}
