package spittr.repository.MysqlRepository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import spittr.repository.IRepository.SpittleRepository;
import spittr.model.Spittle;

/**
 * 更新信息仓库实现类，mysql
 *
 * @author wq
 * repository：2016-11-19
 *
 */
@Repository()
@Transactional()         
public class SpittleRepository4Mysql implements SpittleRepository{

	private SessionFactory sessionFactory;

	@Inject
	public SpittleRepository4Mysql(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}

	/**
	 * 获得当前的Session对象
	 * @return
	 */
	private Session currentSession()
	{
		Session session = null;
	
		try
		{
			//如果开启了AOP拦截Transactional，则会事先开启事务，并绑定到当前线程
			//此时，getCurrentSession会在当前线程中查找，并返回当前事务
			session= sessionFactory.getCurrentSession();
		}
		catch(HibernateException  e)
		{
			throw e;
			//不推荐，为每一个线程创建session，需要手动处理session，并且不绑定当前线程
			//session= sessionFactory.openSession();
		}
		return session;
	}
	
	public long count()
	{
		return currentSession().createCriteria(Spittle.class).list().size();
	}
	
	public Spittle findOne(long spittleId) {
		return (Spittle) currentSession().get(Spittle.class, spittleId);
	}

	public List<Spittle> findSpittles(int page, int pageSize) {
		return currentSession().createCriteria(Spittle.class)
		.setFirstResult((page-1))
		.setMaxResults((page-1)*pageSize).list();
	}

	public List<Spittle> findSpecicalSpittle(String spField) {
		return currentSession().createCriteria(Spittle.class)
		.add(Restrictions.like("message", "xx")).list();
	}

	public Long add(Spittle spittle) {
		Serializable id = currentSession().save(spittle);
		return (Long) id;
	}
}
