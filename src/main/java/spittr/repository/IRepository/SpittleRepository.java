package spittr.repository.IRepository;

import java.util.List;

import spittr.model.Spittle;

/**
 * 用户更新信息的数据访问库
 *
 * @author wq
 * repository：2016-11-19
 *
 */
public interface SpittleRepository {
	List<Spittle> findSpittles(int page, int pageSize);
	
	List<Spittle> findSpecicalSpittle(String spField);
	
	Spittle findOne(long spittle_id);
	
	Long add(Spittle spittle);
}
