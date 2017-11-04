package spittr.repository.IRepository;

import spittr.model.Spitter;
import spittr.model.Spittle;

import java.util.List;

/**
 * 用户信息的数据访问库
 *
 * @author wq
 * repository：2016-11-20
 *
 */
public interface SpitterRepository {
	List<Spitter> findSpitters(int page, int pageSize);

	Spitter save(Spitter before);

	Spitter findByUsername(String username);
	
	int add(Spitter spitter);
}
